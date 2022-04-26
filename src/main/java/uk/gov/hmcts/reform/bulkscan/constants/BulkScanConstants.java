package uk.gov.hmcts.reform.bulkscan.constants;

import org.apache.commons.lang3.tuple.Pair;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BulkScanConstants {

    public static final String MANDATORY_ERROR_MESSAGE = "%s should not be null or empty";
    public static final String DATE_FORMAT_ERROR_MESSAGE = "%s is invalid date format";
    public static final String EMAIL_FORMAT_ERROR_MESSAGE = "%s is invalid email";
    public static final String NUMERIC_ERROR_MESSAGE = "%s is not a number";
    public static final String MISSING_FIELD_ERROR_MESSAGE = "%s is missing";
    public static final String POST_CODE_ERROR_MESSAGE = "%s is not a valid postcode";
    public static final String PHONE_NUMBER_ERROR_MESSAGE = "%s is not valid phone number";
    public static final String DUPLICATE_FIELDS_ERROR_MESSAGE = "Invalid OCR data. Duplicate fields exist: %s";
    public static final String FAX_NUMBER_ERROR_MESSAGE = "%s is in the wrong format";


    public static final String MANDATORY_KEY = "mandatoryFields";
    public static final String DATE_FORMAT_FIELDS_KEY = "dateFields";
    public static final String EMAIL_FORMAT_FIELDS_KEY = "emailFields";
    public static final String NUMERIC_FIELDS_KEY = "numericFields";
    public static final String POST_CODE_FIELDS_KEY = "postCodeFields";
    public static final String PHONE_NUMBER_FIELDS_KEY = "phoneNumberFields";
    public static final String FAX_NUMBER_FORMAT_MESSAGE_KEY = "faxNumberFields";

    public static final Map<String, String> ERROR_MESSAGE_MAP = getErrorMessageMap();

    private static Map<String,String> getErrorMessageMap() {
        return Map.of(MANDATORY_KEY, MANDATORY_ERROR_MESSAGE,
                      DATE_FORMAT_FIELDS_KEY, DATE_FORMAT_ERROR_MESSAGE,
                      EMAIL_FORMAT_FIELDS_KEY, EMAIL_FORMAT_ERROR_MESSAGE,
                      NUMERIC_FIELDS_KEY, NUMERIC_ERROR_MESSAGE, POST_CODE_FIELDS_KEY, POST_CODE_ERROR_MESSAGE,
                      PHONE_NUMBER_FIELDS_KEY, PHONE_NUMBER_ERROR_MESSAGE,
                      FAX_NUMBER_FORMAT_MESSAGE_KEY, FAX_NUMBER_ERROR_MESSAGE
        );
    }

    public static Map<String, Pair<List<String>, String>> getValidationFieldsMap(
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig) {
        Map<String, Pair<List<String>, String>> map = new HashMap<>();

        map.put(MANDATORY_KEY, Pair.of(validationConfig.getMandatoryFields(), null));

        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationConfig =
            validationConfig.getRegexValidationFields();

        if (regexValidationConfig != null) {
            BulkScanFormValidationConfigManager.RegexFieldsConfig dateFields = regexValidationConfig.getDateFields();
            BulkScanFormValidationConfigManager.RegexFieldsConfig emailFields = regexValidationConfig.getEmailFields();
            BulkScanFormValidationConfigManager.RegexFieldsConfig numericFields = regexValidationConfig
                .getNumericFields();
            BulkScanFormValidationConfigManager.RegexFieldsConfig faxNumberFieldsFields = regexValidationConfig
                    .getFaxNumberFields();

            map.put(DATE_FORMAT_FIELDS_KEY, getPairObject(dateFields));
            map.put(EMAIL_FORMAT_FIELDS_KEY, getPairObject(emailFields));
            map.put(NUMERIC_FIELDS_KEY, getPairObject(numericFields));
            map.put(FAX_NUMBER_FORMAT_MESSAGE_KEY, getPairObject(faxNumberFieldsFields));


            BulkScanFormValidationConfigManager.RegexFieldsConfig postCodeFields = regexValidationConfig
                .getPostCodeFields();
            map.put(POST_CODE_FIELDS_KEY, getPairObject(postCodeFields));

            BulkScanFormValidationConfigManager.RegexFieldsConfig phoneNumberFields = regexValidationConfig
                .getPhoneNumberFields();
            map.put(PHONE_NUMBER_FIELDS_KEY, getPairObject(phoneNumberFields));
        }

        // Remove null value from map
        map.values().removeIf(Objects::isNull);

        return map;
    }

    private static Pair<List<String>, String> getPairObject(BulkScanFormValidationConfigManager
                                                                .RegexFieldsConfig fields) {
        if (fields != null && fields.getFieldNames() != null) {
            return Pair.of(fields.getFieldNames(), fields.getRegex());
        }

        return null;
    }

    private BulkScanConstants() {

    }

}
