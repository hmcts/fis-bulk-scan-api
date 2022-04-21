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
import java.util.Map;
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

        List<String> ocrDataFieldsList = ocrdatafields.stream().map(OcrDataField::getName).collect(Collectors.toList());
        Map<String, String> ocrDataFieldsMap = ocrdatafields
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        List<String> mandatoryErrors = validateFields(mandatoryFields,
                isMandatoryField(ocrDataFieldsList, ocrDataFieldsMap), MANDATORY_MESSAGE_KEY);

        List<String> mandatoryDateFormatErrors = validateFields(
                dateFields.getFieldNames(),
            isValidDate(ocrDataFieldsList,
                    ocrDataFieldsMap,
                        dateFields.getRegex(), false
            ),
            DATE_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryEmailFormatErrors = validateFields(
            emailFields.getFieldNames(),
            isValidEmail(ocrDataFieldsList,
                    ocrDataFieldsMap,
                         emailFields.getRegex(), false
            ),
            EMAIL_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryNumericErrors = validateFields(
            numericFields.getFieldNames(),
            isNumericField(ocrDataFieldsList,
                            ocrDataFieldsMap,
                           numericFields.getRegex(), false
            ),
            NUMERIC_MESSAGE_KEY
        );


        return Stream.of(mandatoryErrors, mandatoryDateFormatErrors, mandatoryEmailFormatErrors,
                        mandatoryNumericErrors)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private static List<String> validateOptionalFields(List<OcrDataField> ocrdatafields,
                                                       BulkScanFormValidationConfigManager
                                                           .ValidationConfig validationConfg) {
        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationFields =
            validationConfg.getRegexValidationFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig dateFields = regexValidationFields.getDateFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig emailFields = regexValidationFields.getEmailFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig numericFields = regexValidationFields.getNumericFields();

        List<String> ocrDataFieldsList = ocrdatafields
                .stream()
                .map(OcrDataField::getName)
                .collect(Collectors.toList());

        Map<String, String> ocrDataFieldsMap = ocrdatafields
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        List<String> mandatoryDateFormatErrors = validateFields(
                dateFields.getFieldNames(),
                isValidDate(ocrDataFieldsList,
                        ocrDataFieldsMap,
                        dateFields.getRegex(), true
                ),
                DATE_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryEmailFormatErrors = validateFields(
                emailFields.getFieldNames(),
                isValidEmail(ocrDataFieldsList,
                        ocrDataFieldsMap,
                        emailFields.getRegex(), true
                ),
                EMAIL_FORMAT_MESSAGE_KEY
        );

        List<String> mandatoryNumericErrors = validateFields(
                numericFields.getFieldNames(),
                isNumericField(ocrDataFieldsList,
                        ocrDataFieldsMap,
                        numericFields.getRegex(), true
                ),
                NUMERIC_MESSAGE_KEY
        );


        return Stream.of(mandatoryDateFormatErrors, mandatoryEmailFormatErrors, mandatoryNumericErrors)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private static List<String> validateFields(List<String> mandatoryFields,
                                               Predicate<String> filterCondition,
                                               String errorMessageKey) {
        return mandatoryFields.stream()
            .filter(filterCondition)
            .map(mandatoryField -> String.format(ERROR_MESSAGE_MAP.get(errorMessageKey), mandatoryField))
            .collect(Collectors.toList());
    }

    private static Predicate<String> isMandatoryField(List<String> ocrDataFieldsList,
                                                      Map<String, String> ocrDataFieldsMap) {
        return mandatoryField -> !ocrDataFieldsList.contains(mandatoryField)
                || ObjectUtils.isEmpty(ocrDataFieldsMap.get(mandatoryField));
    }

    private static Predicate<String> isValidDate(List<String> ocrDataFieldsList, Map<String, String> ocrDataFieldsMap,
                                                       String regex, boolean isOptional) {
        return dateField -> isOptional != ocrDataFieldsList.contains(dateField)
                && ocrDataFieldsList.contains(dateField)
                && !ObjectUtils.isEmpty(ocrDataFieldsMap.get(dateField))
                && !isDateValid(ocrDataFieldsMap.get(dateField), regex);
    }

    private static Predicate<String> isValidEmail(List<String> ocrDataFieldsList, Map<String, String> ocrDataFieldsMap,
                                                  String regex, boolean isOptional) {
        return emailField -> isOptional != ocrDataFieldsList.contains(emailField)
                && ocrDataFieldsList.contains(emailField)
                && !ObjectUtils.isEmpty(ocrDataFieldsMap.get(emailField))
                && !isValidEmailFormat(ocrDataFieldsMap.get(emailField), regex);
    }

    private static Predicate<String> isNumericField(List<String> ocrDataFieldsList,
                                                    Map<String, String> ocrDataFieldsMap,
                                                    String regex, boolean isOptional) {
        return numericField -> isOptional != ocrDataFieldsList.contains(numericField)
                && ocrDataFieldsList.contains(numericField)
                && !ObjectUtils.isEmpty(ocrDataFieldsMap.get(numericField))
                && !isNumeric(ocrDataFieldsMap.get(numericField), regex);
    }


}
