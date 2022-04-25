package uk.gov.hmcts.reform.bulkscan.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DUPLICATE_FIELDS_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ERROR_MESSAGE_MAP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PHONE_NUMBER_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_FIELDS_KEY;
import static uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil.isDateValid;
import static uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil.isValidFormat;

@Slf4j
public final class BulkScanValidationHelper {

    private BulkScanValidationHelper() {

    }

    public static BulkScanValidationResponse validateMandatoryAndOptionalFields(List<OcrDataField> ocrdatafields,
                                                                                BulkScanFormValidationConfigManager
                                                                                    .ValidationConfig validationConfg) {
        List<String> duplicateOcrFields = findDuplicateOcrFields(ocrdatafields);
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (!ocrdatafields.isEmpty()) {
            errors = findMissingFields(validationConfg.getMandatoryFields(), ocrdatafields);

            errors.addAll(validateMandatoryAndOptionalFields(ocrdatafields, validationConfg, false));

            warnings = validateMandatoryAndOptionalFields(ocrdatafields, validationConfg, true);
        } else {
            String duplicateFields = String.join(",", duplicateOcrFields);
            log.info("Found duplicate fields in OCR data. {}", duplicateFields);

            String errorMessage = String.format(DUPLICATE_FIELDS_ERROR_MESSAGE, duplicateFields);
            errors.add(errorMessage);
        }

        return BulkScanValidationResponse.builder()
            .status(errors.isEmpty() && warnings.isEmpty() ? Status.SUCCESS : Status.ERRORS)
            .warnings(Warnings.builder().items(warnings).build())
            .errors(Errors.builder().items(errors).build()).build();
    }

    private static List<String> validateMandatoryAndOptionalFields(List<OcrDataField> ocrdatafields,
                                                                   BulkScanFormValidationConfigManager.ValidationConfig
                                                                       validationConfg, boolean isOptional) {
        Map<String, Pair<List<String>, String>> validationKeysMap = BulkScanConstants
            .getValidationFieldsMap(validationConfg);
        List<String> errorOrWarnings = new ArrayList<>();
        List<String> mandatoryFields = validationConfg.getMandatoryFields();
        for (var entry : validationKeysMap.entrySet()) {
            Pair<List<String>, String> pair = entry.getValue();
            switch (entry.getKey()) {
                case MANDATORY_KEY:
                    if (!isOptional) {
                        errorOrWarnings.addAll(validateFields(ocrdatafields, isMandatoryField(pair.getLeft()),
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
                    errorOrWarnings.addAll(validateFormatFields(ocrdatafields, isOptional, mandatoryFields,
                                                                entry.getKey(), pair, false));
                    break;
                default:
                    break;
            }
        }
        return errorOrWarnings;
    }

    private static List<String> validateFormatFields(List<OcrDataField> ocrdatafields, boolean isOptional, List<String>
        mandatoryFields, String key, Pair<List<String>, String> pair, boolean isDateFormat) {
        return validateFields(
            ocrdatafields,
            isDateFormat ? isValidDate(mandatoryFields, pair.getLeft(), pair.getRight(), isOptional) :
                isMatchedWithRegex(mandatoryFields, pair.getLeft(), pair.getRight(), isOptional),
            key
        );
    }

    private static List<String> findMissingFields(List<String> fields, List<OcrDataField> ocrDataFields) {
        return fields.stream().filter(eachField -> !ocrDataFields.stream()
                .anyMatch(inputField -> inputField.getName().equalsIgnoreCase(eachField)))
            .map(eachField -> String.format(MISSING_FIELD_MESSAGE, eachField)).collect(toList());
    }

    private static List<String> validateFields(List<OcrDataField> ocrdatafields,
                                               Predicate<OcrDataField> filterCondition, String errorMessageKey) {
        return ocrdatafields.stream()
            .filter(filterCondition)
            .map(eachData -> String.format(ERROR_MESSAGE_MAP.get(errorMessageKey), eachData.getName()))
            .collect(toList());
    }

    private static Predicate<OcrDataField> isMandatoryField(List<String> fields) {
        return eachData -> fields.contains(eachData.getName())
            && ObjectUtils.isEmpty(null != eachData.getValue() ? eachData.getValue().trim() : eachData.getValue());
    }

    private static Predicate<OcrDataField> isValidDate(List<String> mandatoryFields, List<String> fields,
                                                       String regex, boolean isOptional) {
        return eachData -> (isOptional ? !mandatoryFields.contains(eachData.getName())
            : mandatoryFields.contains(eachData.getName()))
                && fields.contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && !isDateValid(eachData.getValue(), regex);
    }

    private static Predicate<OcrDataField> isMatchedWithRegex(List<String> mandatoryFields, List<String> fields,
                                                              String regex, boolean isOptional) {
        return eachData -> (isOptional ? !mandatoryFields.contains(eachData.getName())
            : mandatoryFields.contains(eachData.getName()))
                && fields.contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && !isValidFormat(eachData.getValue(), regex);
    }

    public static List<String> findDuplicateOcrFields(List<OcrDataField> ocrFields) {
        return ocrFields
            .stream()
            .collect(groupingBy(it -> it.name, counting()))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 1)
            .map(Map.Entry::getKey)
            .collect(toList());
    }
}
