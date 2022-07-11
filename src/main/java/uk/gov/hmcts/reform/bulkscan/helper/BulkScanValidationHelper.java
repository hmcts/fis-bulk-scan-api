package uk.gov.hmcts.reform.bulkscan.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanGroupValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.model.*;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ALPHA_NUMERIC_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DUPLICATE_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FAX_NUMBER_FORMAT_MESSAGE_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MESSAGE_MAP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NUMERIC_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PHONE_NUMBER_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE_KEY;
import static uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil.isDateValid;
import static uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil.isValidFormat;

@Slf4j
@Service
public class BulkScanValidationHelper {

    @Autowired
    PostcodeLookupService postcodeLookupService;

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanGroupValidatorHelper bulkScanGroupValidatorHelper;

    @Autowired
    BulkScanGroupValidationConfigManager bulkScanGroupValidationConfigManager;

    public BulkScanValidationResponse validateMandatoryAndOptionalFields(List<OcrDataField> ocrDatafields,
                                                                         FormType formType) {
        BulkScanFormValidationConfigManager.ValidationConfig validationConfg = configManager.getValidationConfig(
            formType);
        List<String> duplicateOcrFields = findDuplicateOcrFields(ocrDatafields);
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        List<String> differences = findUnknownFields(ocrDatafields, formType);

        if (!differences.isEmpty()) {
            warnings.add(String.format(UNKNOWN_FIELDS_MESSAGE, String.join(",", differences)));
        }

        if (duplicateOcrFields.isEmpty() && !ocrDatafields.isEmpty()) {
            List<String> mandatoryFields = new ArrayList<>();
            mandatoryFields.addAll(validationConfg.getMandatoryFields());
            mandatoryFields.addAll(bulkScanGroupValidatorHelper.validate(ocrDatafields, formType));
            errors.addAll(findMissingFields(mandatoryFields, ocrDatafields));

            errors.addAll(validateMandatoryAndOptionalFields(ocrDatafields, validationConfg, formType, false));

            warnings.addAll(validateMandatoryAndOptionalFields(ocrDatafields, validationConfg, formType, true));
        } else {
            String duplicateFields = String.join(",", duplicateOcrFields);
            log.info("Found duplicate fields in OCR data. {}", duplicateFields);

            String errorMessage = String.format(DUPLICATE_FIELDS_MESSAGE, duplicateFields);
            errors.add(errorMessage);
        }

        Status status = !errors.isEmpty() ? Status.ERRORS : !warnings.isEmpty() ? Status.WARNINGS : Status.SUCCESS;

        return BulkScanValidationResponse.builder()
            .status(status)
            .warnings(Warnings.builder().items(warnings).build())
            .errors(Errors.builder().items(errors).build()).build();
    }

    private List<String> validateMandatoryAndOptionalFields(List<OcrDataField> ocrdatafields,
                                                                   BulkScanFormValidationConfigManager.ValidationConfig
                                                                       validationConfg, FormType formType,
                                                            boolean isOptional) {
        Map<String, Pair<List<String>, String>> validationKeysMap = BulkScanConstants
            .getValidationFieldsMap(validationConfg);
        List<String> errorOrWarnings = new ArrayList<>();
        List<String> mandatoryFields = validationConfg.getMandatoryFields();
        for (var entry : validationKeysMap.entrySet()) {
            Pair<List<String>, String> pair = entry.getValue();
            switch (entry.getKey()) {
                case MANDATORY_KEY:
                    if (!isOptional) {
                        List<String> fields = new ArrayList<>();
                        fields.addAll(pair.getLeft());
                        fields.addAll(bulkScanGroupValidatorHelper.validate(ocrdatafields, formType));
                        errorOrWarnings.addAll(validateFields(ocrdatafields, isMandatoryField(fields),
                                                              MANDATORY_KEY));
                    }
                    break;
                case DATE_FORMAT_FIELDS_KEY:
                    errorOrWarnings.addAll(validateFormatFields(ocrdatafields, isOptional, mandatoryFields,
                                                                entry.getKey(), pair, true));
                    break;
                case EMAIL_FORMAT_FIELDS_KEY:
                case POST_CODE_FIELDS_KEY:
                case PHONE_NUMBER_FIELDS_KEY:
                case FAX_NUMBER_FORMAT_MESSAGE_KEY:
                case NUMERIC_FIELDS_KEY:
                case ALPHA_NUMERIC_FIELDS_KEY:
                    errorOrWarnings.addAll(validateFormatFields(ocrdatafields, isOptional, mandatoryFields,
                                                                entry.getKey(), pair, false));
                    break;
                case XOR_CONDITIONAL_FIELDS_MESSAGE_KEY:
                    errorOrWarnings.addAll(validateXorFields(ocrdatafields, isOptional, pair));
                    break;
                default:
                    break;
            }
        }
        return errorOrWarnings;
    }

    private List<String> validateFormatFields(List<OcrDataField> ocrdatafields, boolean isOptional, List<String>
        mandatoryFields, String key, Pair<List<String>, String> pair, boolean isDateFormat) {
        return validateFields(
            ocrdatafields,
            isDateFormat ? isValidDate(mandatoryFields, pair.getLeft(), pair.getRight(), isOptional) :
                isMatchedWithRegex(mandatoryFields, pair.getLeft(), pair.getRight(), isOptional),
            key
        );
    }

    private List<String> validateXorFields(List<OcrDataField> ocrDataFields,  boolean isOptional,
                                           Pair<List<String>, String> pair) {
        List<String> postCodeErrorMessages = new ArrayList<>(Collections.emptyList());
        Map<String, String> ocrDataFieldsMap = ocrDataFields
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));
        List<String> pairs = pair.getKey();
        pairs.forEach(eachPair -> {
            postCodeErrorMessages.addAll(validateXorField(eachPair, ocrDataFieldsMap, isOptional));
        });
        return postCodeErrorMessages;
    }

    private List<String> validateXorField(String eachPair, Map<String, String> ocrDataFieldsMap, boolean isOptional) {
        List<String> validationMessages = new ArrayList<>();
        if (isOptional) {
            return validationMessages;
        }
        List<String> fieldsToCheck = List.of(eachPair.split(","));
        boolean singleFieldPresent = false;
        for (String eachField : fieldsToCheck) {
            if (eachField.contains("postCode") && ocrDataFieldsMap.containsKey(eachField)) {
                boolean isValidPostcode = postcodeLookupService.isValidPostCode(ocrDataFieldsMap.get(eachField), null);
                if (!isValidPostcode) {
                    validationMessages.add(String.format(POST_CODE_MESSAGE, eachField));
                }
                singleFieldPresent = true;
                break;
            } else if (ocrDataFieldsMap.containsKey(eachField) && isNotEmpty(ocrDataFieldsMap.get(eachField))) {
                singleFieldPresent = true;
                break;
            }
        }
        if (!singleFieldPresent) {
            validationMessages.add(String.format(XOR_CONDITIONAL_FIELDS_MESSAGE, eachPair));
        }
        return validationMessages;
    }

    private List<String> findMissingFields(List<String> fields, List<OcrDataField> ocrDataFields) {
        return fields.stream().filter(eachField -> !ocrDataFields.stream()
                .anyMatch(inputField -> inputField.getName().equalsIgnoreCase(eachField)))
            .map(eachField -> String.format(MISSING_FIELD_MESSAGE, eachField)).collect(toList());
    }

    private List<String> validateFields(List<OcrDataField> ocrdatafields,
                                               Predicate<OcrDataField> filterCondition, String errorMessageKey) {
        return ocrdatafields.stream()
            .filter(filterCondition)
            .map(eachData -> String.format(MESSAGE_MAP.get(errorMessageKey), eachData.getName()))
            .collect(toList());
    }

    private Predicate<OcrDataField> isMandatoryField(List<String> fields) {
        return eachData -> fields.contains(eachData.getName())
            && ObjectUtils.isEmpty(null != eachData.getValue() ? eachData.getValue().trim() : eachData.getValue());
    }

    private Predicate<OcrDataField> isValidDate(List<String> mandatoryFields, List<String> fields,
                                                       String regex, boolean isOptional) {
        return eachData -> isOptional != mandatoryFields.contains(eachData.getName())
                && fields.contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && !isDateValid(eachData.getName(), eachData.getValue(), regex);
    }

    private Predicate<OcrDataField> isMatchedWithRegex(List<String> mandatoryFields, List<String> fields,
                                                              String regex, boolean isOptional) {
        return eachData -> isOptional != mandatoryFields.contains(eachData.getName())
            && fields.contains(eachData.getName())
            && !ObjectUtils.isEmpty(eachData.getValue())
            && !isValidFormat(eachData.getValue(), regex);
    }

    public List<String> findDuplicateOcrFields(List<OcrDataField> ocrFields) {
        return ocrFields
            .stream()
            .collect(groupingBy(it -> it.name, counting()))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(toList());
    }

    public List<String> findUnknownFields(List<OcrDataField> ocrDatafields, FormType formType) {
        BulkScanFormValidationConfigManager
            .ValidationConfig validationConfig = configManager.getValidationConfig(formType);
        List<String> mandatoryFields = validationConfig.getMandatoryFields();
        List<String> optionalFields = validationConfig.getOptionalFields();
        optionalFields.removeIf(String::isEmpty);
        if (optionalFields.isEmpty()) {
            //TODO: Optional fields not yet configured for all forms. For now will allow perform operation.
            return Collections.emptyList();
        }
        List<String> configuredFieldsList = Stream.concat(mandatoryFields.stream(), optionalFields.stream())
            .collect(Collectors.toUnmodifiableList());
        List<String> ocrList = ocrDatafields.stream().map(eachKey -> eachKey.getName()).collect(toList());
        return ocrList.stream()
            .filter(element -> !configuredFieldsList.contains(element))
            .collect(toList());
    }
}
