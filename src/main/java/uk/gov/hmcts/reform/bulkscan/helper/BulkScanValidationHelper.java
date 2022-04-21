package uk.gov.hmcts.reform.bulkscan.helper;

import org.apache.commons.lang3.ObjectUtils;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_MESSAGE_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ERROR_MESSAGE_MAP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_MESSAGE_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NUMERIC_MESSAGE_KEY;
import static uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil.isDateValid;
import static uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil.isNumeric;
import static uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil.isValidEmailFormat;

public final class BulkScanValidationHelper {

    private BulkScanValidationHelper() {

    }

    public static BulkScanValidationResponse validateMandatoryAndOptionalFields(List<OcrDataField> ocrdatafields,
                                                                                BulkScanFormValidationConfigManager
                                                                                    .ValidationConfig validationConfg) {
        List<String> mandatoryFieldErrors = validateMandatoryFields(ocrdatafields, validationConfg);
        List<String> optionalFieldWarning = validateOptionalFields(ocrdatafields, validationConfg);

        return BulkScanValidationResponse.builder()
            .status(mandatoryFieldErrors.isEmpty() && optionalFieldWarning.isEmpty() ? Status.SUCCESS : Status.ERRORS)
            .warnings(Warnings.builder().items(optionalFieldWarning).build())
            .errors(Errors.builder().items(mandatoryFieldErrors).build()).build();
    }

    private static List<String> validateMandatoryFields(List<OcrDataField> ocrdatafields,
                                                        BulkScanFormValidationConfigManager
                                                            .ValidationConfig validationConfg) {
        List<String> mandatoryFields = validationConfg.getMandatoryFields();
        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationFields =
            validationConfg.getRegexValidationFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig dateFields = regexValidationFields.getDateFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig emailFields = regexValidationFields.getEmailFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig numericFields = regexValidationFields.getNumericFields();

        List<String> mandatoryErrors = validateFields(ocrdatafields, isMandatoryField(mandatoryFields),
                                                      MANDATORY_MESSAGE_KEY
        );

        List<String> mandatoryDateFormatErrors = validateFields(
            ocrdatafields,
            isValidDate(mandatoryFields, dateFields.getFieldNames(),
                        dateFields.getRegex(), false
            ),
            DATE_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryEmailFormatErrors = validateFields(
            ocrdatafields,
            isValidEmail(mandatoryFields, emailFields.getFieldNames(),
                         emailFields.getRegex(), false
            ),
            EMAIL_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryNumericErrors = validateFields(
            ocrdatafields,
            isNumericField(mandatoryFields,
                           numericFields.getFieldNames(),
                           numericFields.getRegex(), false
            ),
            NUMERIC_MESSAGE_KEY
        );


        return Stream.of(mandatoryErrors, mandatoryDateFormatErrors, mandatoryEmailFormatErrors, mandatoryNumericErrors)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private static List<String> validateOptionalFields(List<OcrDataField> ocrdatafields,
                                                       BulkScanFormValidationConfigManager
                                                           .ValidationConfig validationConfg) {
        List<String> mandatoryFields = validationConfg.getMandatoryFields();
        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationFields =
            validationConfg.getRegexValidationFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig dateFields = regexValidationFields.getDateFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig emailFields = regexValidationFields.getEmailFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig numericFields = regexValidationFields.getNumericFields();

        List<String> mandatoryDateFormatErrors = validateFields(
            ocrdatafields,
            isValidDate(mandatoryFields, dateFields.getFieldNames(),
                        dateFields.getRegex(), true
            ),
            DATE_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryEmailFormatErrors = validateFields(
            ocrdatafields,
            isValidEmail(mandatoryFields, emailFields.getFieldNames(),
                         emailFields.getRegex(), true
            ),
            EMAIL_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryNumericErrors = validateFields(
            ocrdatafields,
            isNumericField(mandatoryFields, numericFields.getFieldNames(),
                           numericFields.getRegex(), true
            ),
            NUMERIC_MESSAGE_KEY
        );


        return Stream.of(mandatoryDateFormatErrors, mandatoryEmailFormatErrors, mandatoryNumericErrors)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private static List<String> validateFields(List<OcrDataField> ocrdatafields,
                                               Predicate<OcrDataField> filterCondition,
                                               String errorMessageKey) {
        return ocrdatafields.stream()
            .filter(filterCondition)
            .map(eachData -> String.format(ERROR_MESSAGE_MAP.get(errorMessageKey), eachData.getName()))
            .collect(Collectors.toList());
    }

    private static Predicate<OcrDataField> isMandatoryField(List<String> fields) {
        return eachData -> fields.contains(eachData.getName())
            && ObjectUtils.isEmpty(eachData.getValue());
    }

    private static Predicate<OcrDataField> isValidDate(List<String> mandatoryFields, List<String> fields,
                                                       String regex, boolean isOptional) {
        return eachData -> (isOptional ? !mandatoryFields.contains(eachData.getName())
            : mandatoryFields.contains(eachData.getName()))
                && fields.contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && !isDateValid(eachData.getValue(), regex);
    }

    private static Predicate<OcrDataField> isValidEmail(List<String> mandatoryFields, List<String> fields, String regex,
                                                        boolean isOptional) {
        return eachData -> (isOptional ? !mandatoryFields.contains(eachData.getName())
            : mandatoryFields.contains(eachData.getName()))
                && fields.contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && !isValidEmailFormat(eachData.getValue(), regex);
    }

    private static Predicate<OcrDataField> isNumericField(List<String> mandatoryFields, List<String> fields,
                                                          String regex, boolean isOptional) {
        return eachData -> (isOptional ? !mandatoryFields.contains(eachData.getName())
            : mandatoryFields.contains(eachData.getName()))
                && fields.contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && !isNumeric(eachData.getValue(), regex);
    }


}
