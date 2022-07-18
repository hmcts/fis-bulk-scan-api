package uk.gov.hmcts.reform.bulkscan.constants;

import org.apache.commons.lang3.tuple.Pair;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BulkScanConstants {

    public static final String MANDATORY_ERROR_MESSAGE = "%s should not be null or empty";
    public static final String DATE_FORMAT_MESSAGE = "%s is invalid date or format";
    public static final String EMAIL_FORMAT_MESSAGE = "%s is invalid email";

    public static final String EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD = "exemption_to_attend_MIAM";
    public static final String GROUP_DEPENDENCY_MESSAGE = "Group Dependency Field (%s) has "
        + "incomplete dependency validation";
    public static final String NOMIAM_CHILDPROTECTIONCONCERNS = "NoMIAM_childProtectionConcerns";
    public static final String NOMIAM_DOMESTICVIOLENCE = "NoMIAM_domesticViolence";
    public static final String NOMIAM_URGENCY = "NoMIAM_Urgency";
    public static final String NOMIAM_PREVIOUSATTENDENCE = "NoMIAM_PreviousAttendence";
    public static final String NOMIAM_OTHERREASONS = "NoMIAM_otherReasons";
    public static final String NUMERIC_MESSAGE = "%s is not a number";
    public static final String YES_VALUE = "Yes";

    public static final String MISSING_FIELD_MESSAGE = "%s is missing";
    public static final String POST_CODE_MESSAGE = "%s is not a valid postcode";
    public static final String PHONE_NUMBER_MESSAGE = "%s is not valid phone number";
    public static final String DUPLICATE_FIELDS_MESSAGE = "Invalid OCR data. Duplicate fields exist: %s";
    public static final String FAX_NUMBER_ERROR_MESSAGE = "%s is in the wrong format";
    public static final String ALPHA_NUMERIC_FIELDS_MESSAGE = "%s is not valid alpha numeric";
    public static final String UNKNOWN_FIELDS_MESSAGE = "The following fields are are not configured "
        + "with our system. [%s] ";

    public static final String XOR_CONDITIONAL_FIELDS_MESSAGE = "one field must be present out of %s";

    public static final String MANDATORY_KEY = "mandatoryFields";
    public static final String DATE_FORMAT_FIELDS_KEY = "dateFields";
    public static final String EMAIL_FORMAT_FIELDS_KEY = "emailFields";
    public static final String NUMERIC_FIELDS_KEY = "numericFields";
    public static final String POST_CODE_FIELDS_KEY = "postCodeFields";
    public static final String PHONE_NUMBER_FIELDS_KEY = "phoneNumberFields";
    public static final String FAX_NUMBER_FORMAT_MESSAGE_KEY = "faxNumberFields";
    public static final String XOR_CONDITIONAL_FIELDS_MESSAGE_KEY = "xorConditionalFields";
    public static final String ALPHA_NUMERIC_FIELDS_KEY = "alphaNumericFields";

    public static final Map<String, String> MESSAGE_MAP = getErrorMessageMap();

    public static final String BULK_SCAN_CASE_REFERENCE = "bulkScanCaseReference";

    public static final String APPLICANT1_RELATION_TO_CHILD = "applicant1_relationToChild";
    public static final String APPLICANT2_RELATION_TO_CHILD = "applicant2_relationToChild";
    public static final String APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER = "applicant_relationToChild_father_partner";
    public static final String CASE_TYPE_ID = "CASE_TYPE_ID";
    public static final String EVENT_ID = "EVENT_ID";

    public static final String DOB_HINT = "dob";
    public static final String DATE_OF_BIRTH_HINT = "dateOfBirth";

    public static final String ADOPTION_ORDER_CONSENT = "adoption_order_consent";

    public static final String ADOPTION_ORDER_CONSENT_ADVANCE = "adoption_order_consent_advance";

    public static final String ADOPTION_ORDER_CONSENT_AGENCY = "adoption_order_consent_agency";

    public static final String ADOPTION_ORDER_NO_CONSENT = "adoption_order_no_consent";

    public static final String COURT_CONSENT_PARENT_NOT_FOUND = "court_consent_parent_not_found";

    public static final String COURT_CONSENT_PARENT_LACK_CAPACITY = "court_consent_parent_lack_capacity";

    public static final String COURT_CONSENT_CHILD_WELFARE = "court_consent_child_welfare";

    private static Map<String, String> getErrorMessageMap() {
        return Map.of(MANDATORY_KEY, MANDATORY_ERROR_MESSAGE,
                      DATE_FORMAT_FIELDS_KEY, DATE_FORMAT_MESSAGE,
                      EMAIL_FORMAT_FIELDS_KEY, EMAIL_FORMAT_MESSAGE,
                      NUMERIC_FIELDS_KEY, NUMERIC_MESSAGE, POST_CODE_FIELDS_KEY, POST_CODE_MESSAGE,
                      PHONE_NUMBER_FIELDS_KEY, PHONE_NUMBER_MESSAGE,
                      FAX_NUMBER_FORMAT_MESSAGE_KEY, FAX_NUMBER_ERROR_MESSAGE,
                      XOR_CONDITIONAL_FIELDS_MESSAGE_KEY, XOR_CONDITIONAL_FIELDS_MESSAGE,
                      ALPHA_NUMERIC_FIELDS_KEY, ALPHA_NUMERIC_FIELDS_MESSAGE
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

            BulkScanFormValidationConfigManager.RegexFieldsConfig xorConditionalFields = regexValidationConfig
                .getXorConditionalFields();
            map.put(XOR_CONDITIONAL_FIELDS_MESSAGE_KEY, getPairObject(xorConditionalFields));

            BulkScanFormValidationConfigManager.RegexFieldsConfig alphaNumericFields = regexValidationConfig
                .getAlphaNumericFields();
            map.put(ALPHA_NUMERIC_FIELDS_KEY, getPairObject(alphaNumericFields));
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
