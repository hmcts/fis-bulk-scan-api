package uk.gov.hmcts.reform.bulkscan.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BulkScanValidationUtil {

    private BulkScanValidationUtil() {

    }

    public static boolean isDateValid(String dateStr, String format) {
        DateFormat sdf = new SimpleDateFormat(format, Locale.UK);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String value, String regex) {
        Pattern regexPattern = Pattern.compile(regex,
                                                            Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.find();
    }

    public static boolean isValidNumber(String value, String regex) {
        Pattern regexPattern = Pattern.compile(regex,
                                               Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.find();
    }

    public static List<String> validateFields(List<OcrDataField> ocrdatafields,
                                               BulkScanFormValidationConfigManager.ValidationConfig validationConfg) {
        List<String> mandatoryFields = validationConfg.getMandatoryFields();
        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationFields =
            validationConfg.getRegexValidationFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig dateFields = regexValidationFields.getDateFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig emailFields = regexValidationFields.getEmailFields();
        BulkScanFormValidationConfigManager.RegexFieldsConfig numericFields = regexValidationFields.getNumericFields();

        List<String> mandatoryErrors = ocrdatafields.stream().filter(eachData ->
            mandatoryFields.contains(eachData.getName()) && ObjectUtils.isEmpty(eachData.getValue()))
                .map(eachData -> eachData.getName() + BulkScanConstants.MANDATORY_ERROR_MESSAGE)
            .collect(Collectors.toList());

        List<String> dateFormatErrors = ocrdatafields.stream()
            .filter(eachData -> dateFields.getFieldNames().contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && isDateValid(eachData.getValue(),dateFields.getRegex()))
            .map(eachData -> eachData.getName() + BulkScanConstants.DATE_FORMAT_ERROR_MESSAGE)
            .collect(Collectors.toList());

        List<String> emailFormatErrors = ocrdatafields.stream()
            .filter(eachData -> emailFields.getFieldNames().contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && isValidEmail(eachData.getValue(),emailFields.getRegex()))
            .map(eachData -> eachData.getName() + BulkScanConstants.EMAIL_FORMAT_ERROR_MESSAGE)
            .collect(Collectors.toList());

        List<String> numericErrors = ocrdatafields.stream()
            .filter(eachData -> numericFields.getFieldNames().contains(eachData.getName())
                && !ObjectUtils.isEmpty(eachData.getValue())
                && StringUtils.isNumeric(eachData.getValue()))
            .map(eachData -> eachData.getName() + BulkScanConstants.NUMERIC_ERROR_MESSAGE)
            .collect(Collectors.toList());

        return Stream.of(mandatoryErrors, dateFormatErrors, emailFormatErrors, numericErrors)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
