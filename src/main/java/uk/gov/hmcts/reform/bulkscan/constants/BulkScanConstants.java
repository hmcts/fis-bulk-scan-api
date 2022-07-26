package uk.gov.hmcts.reform.bulkscan.constants;

import org.apache.commons.lang3.tuple.Pair;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BulkScanConstants {

    public static final String YES = "Yes";
    public static final String NO = "No";

    public static final String MANDATORY_ATTENDED_MIAM_MESSAGE = "%s : value is no, "
        + "and you cannot make this application";
    public static final String MANDATORY_ERROR_MESSAGE = "%s should not be null or empty";
    public static final String DATE_FORMAT_MESSAGE = "%s is invalid date or format";
    public static final String EMAIL_FORMAT_MESSAGE = "%s is invalid email";
    public static final String NUMERIC_MESSAGE = "%s is not a number";
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
    public static final String SCAN_DOCUMENTS = "scannedDocuments";

    public static final Map<String, String> MESSAGE_MAP = getErrorMessageMap();

    public static final String BULK_SCAN_CASE_REFERENCE = "bulkScanCaseReference";

    public static final String APPLICANT1_RELATION_TO_CHILD = "applicant1_relationToChild";
    public static final String APPLICANT2_RELATION_TO_CHILD = "applicant2_relationToChild";
    public static final String APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER = "applicant_relationToChild_father_partner";
    public static final String CASE_TYPE_ID = "CASE_TYPE_ID";
    public static final String EVENT_ID = "EVENT_ID";

    public static final String APPLICANT_HOME_TELEPHONE_NUMBER = "applicant_homeTelephoneNumber";
    public static final String APPLICANT_MOBILE_TELEPHONE_NUMBER  = "applicant_mobileTelephoneNumber";

    public static final String DOB_HINT = "dob";
    public static final String DATE_OF_BIRTH_HINT = "dateOfBirth";

    public static final String APPLICANT_MARITAL_STATUS_SINGLE = "applicant_marital_status_single";
    public static final String APPLICANT_MARITAL_STATUS_DIVORCED = "applicant_marital_status_divorced";
    public static final String applicant_marital_status_divorced1 = "applicant_marital_status_divorced";
    public static final String APPLICANT_MARITAL_STATUS_WIDOW = "applicant_marital_status_widow";
    public static final String APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_NOTFOUND =
            "applicant_marital_status_married_spouse_notfound";
    public static final String APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_SEPARATED =
            "applicant_marital_status_married_spouse_separated";
    public static final String APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_INCAPABLE =
            "applicant_marital_status_married_spouse_incapable";
    public static final String APPLICANT_APPLYING_ALONE_NATURAL_PARENT_DIED =
            "applicant_applying_alone_natural_parent_died";
    public static final String APPLICANT_APPLYING_ALONE_NATURAL_PARENT_NOT_FOUND =
            "applicant_applying_alone_natural_parent_not_found";
    public static final String APPLICANT_APPLYING_ALONE_NO_OTHER_PARENT = "applicant_applying_alone_no_other_parent";
    public static final String APPLICANT_APPLYING_ALONE_OTHER_PARENT_EXCLUSION_JUSTIFIED =
            "applicant_applying_alone_other_parent_exclusion_justified";
    public static final String APPLICANT_RELATION_TO_CHILD_MOTHER_PARTNER = "applicant_relationToChild_mother_partner";
    public static final String APPLICANT_RELATION_TO_CHILD_NON_CIVIL_PARTNER =
            "applicant_relationToChild_non_civil_partner";
    public static final String APPLICANTS_DOMICILE_STATUS = "applicants_domicile_status";
    public static final String APPLICANTS_NON_DOMICILE_STATUS = "applicants_non_domicile_status";

    public static final String ADOPTION_ORDER_CONSENT = "adoption_order_consent";

    public static final String ADOPTION_ORDER_CONSENT_ADVANCE = "adoption_order_consent_advance";

    public static final String ADOPTION_ORDER_CONSENT_AGENCY = "adoption_order_consent_agency";

    public static final String ADOPTION_ORDER_NO_CONSENT = "adoption_order_no_consent";

    public static final String COURT_CONSENT_PARENT_NOT_FOUND = "court_consent_parent_not_found";

    public static final String COURT_CONSENT_PARENT_LACK_CAPACITY = "court_consent_parent_lack_capacity";

    public static final String COURT_CONSENT_CHILD_WELFARE = "court_consent_child_welfare";

    public static final String APPLICANT2_FIRSTNAME = "applicant2_firstName";
    public static final String APPLICANT2_SOT = "sole_applicant_or_applicant2_statement_of_truth";
    public static final String APPLICANT2_LEGAL_REP_SOT = "sole_applicant_or_applicant2_legal_rep_statement_of_truth";
    public static final String APPLICANT2_LEGAL_REP_SIGNATURE = "sole_applicant_or_applicant2_or_legal_rep_signature";
    public static final String APPLICANT2_SIGNING = "applicant2_signing";
    public static final String APPLICANT2_LEGAL_REP_SIGNING = "applicant2_legal_rep_signing";
    public static final String APPLICANT2_SOT_DAY = "applicant2_statement_of_truth_date_day";
    public static final String APPLICANT2_SOT_MONTH = "applicant2_statement_of_truth_date_month";
    public static final String APPLICANT2_SOT_YEAR = "applicant2_statement_of_truth_date_year";
    public static final String APPLICANT2_STATEMENT_OF_TRUTH_DATE = "applicant2StatementOfTruthDate";
    public static final String APPLICANT1_SOT_DAY = "statement_of_truth_date_day";
    public static final String APPLICANT1_SOT_MONTH = "statement_of_truth_date_month";
    public static final String APPLICANT1_SOT_YEAR = "statement_of_truth_date_year";
    public static final String APPLICANT1_STATEMENT_OF_TRUTH_DATE = "applicant1StatementOfTruthDate";
    public static final String WELSH_SPOKEN_IN_COURT_REQUIRED = "welsh_spoken_in_court_required";
    public static final String COURT_INTERPRETER_ASSISTANCE_REQUIRED = "court_interpreter_assistance_required";
    public static final String SPECIAL_ASSISTANCE_FACILITIES_REQUIRED = "special_assistance_facilities_required";
    public static final String WELSH_PREFERENCE_PARTY_NAME = "welsh_preference_party_name";
    public static final String WELSH_PREFERENCE_WITNESS_NAME = "welsh_preference_witness_name";
    public static final String WELSH_PREFERENCE_CHILD_NAME = "welsh_preference_child_name";
    public static final String PARTY_WELSH_LANGUAGE_PREFERENCE = "party_welsh_language_preference";
    public static final String WITNESS_WELSH_LANGUAGE_PREFERENCE = "witness_welsh_language_preference";
    public static final String CHILD_WELSH_LANGUAGE_PREFERENCE = "child_welsh_language_preference";
    public static final String APPLICANT_REQUIRE_INTERPRETER = "applicant_require_interpreter";
    public static final String RESPONDENT_REQUIRE_INTERPRETER = "respondent_require_interpreter";
    public static final String OTHER_PARTY_REQUIRE_INTERPRETER = "other_party_require_interpreter";
    public static final String OTHER_PARTY_NAME = "other_party_name";
    public static final String COURT_INTERPRETER_ASSISTANCE_LANGUAGE = "court_interpreter_assistance_language";
    public static final String SPECIAL_ASSISTANCE_FACILITIES = "special_assistance_facilities";

    public static final String WELSH_PREFERENCE_PARTY_NAME_CCD = "welshPreferencePartyName";
    public static final String WELSH_PREFERENCE_WITNESS_NAME_CCD = "welshPreferenceWitnessName";
    public static final String WELSH_PREFERENCE_CHILD_NAME_CCD = "welshPreferenceChildName";
    public static final String PARTY_WELSH_LANGUAGE_PREFERENCE_CCD = "partyWelshLanguagePreference";
    public static final String WITNESS_WELSH_LANGUAGE_PREFERENCE_CCD = "witnessWelshLanguagePreference";
    public static final String CHILD_WELSH_LANGUAGE_PREFERENCE_CCD = "childWelshLanguagePreference";
    public static final String APPLICANT_REQUIRE_INTERPRETER_CCD = "applicantRequireInterpreter";
    public static final String RESPONDENT_REQUIRE_INTERPRETER_CCD = "respondentRequireInterpreter";
    public static final String OTHER_PARTY_REQUIRE_INTERPRETER_CCD = "otherPartyRequireInterpreter";
    public static final String OTHER_PARTY_NAME_CCD = "otherPartyName";
    public static final String COURT_INTERPRETER_ASSISTANCE_LANGUAGE_CCD = "courtInterpreterAssistanceLanguage";
    public static final String SPECIAL_ASSISTANCE_FACILITIES_CCD = "specialAssistanceFacilities";
    public static final String SLASH_DELIMITER = "/";


    // C100 manual validation Fields
    public static final String CHILD_LIVING_WITH_APPLICANT = "child_living_with_Applicant";
    public static final String CHILD_LIVING_WITH_RESPONDENT = "child_living_with_Respondent";
    public static final String CHILD_LIVING_WITH_OTHERS = "child_living_with_others";

    public static final String CHILDREN_OF_SAME_PARENT = "children_of_same_parent";
    public static final String CHILDREN_PARENTS_NAME = "children_parentsName";
    public static final String CHILDREN_PARENTS_NAME_COLLECTION = "child_parentsName_collection";
    public static final String CHILDREN_SOCIAL_AUTHORITY = "childrenServicesAuthority";
    public static final String CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER = "child1_localAuthority_or_socialWorker";
    public static final String CHILD_LIVE_WITH_KEY = "childLiveWith";

    // C100 form section 2 validation fields

    public static final String PREVIOUS_OR_ONGOING_PROCEEDING =  "previous_or_ongoingProceeding";

    public static final String EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER =
        "existingCase_onEmergencyProtection_Care_or_supervisioNorder";

    public static final String EXEMPTION_TO_ATTEND_MIAM =  "exemption_to_attend_MIAM";

    public static final String FAMILY_MEMBER_INTIMATION_ON_NO_MIAM = "familyMember_Intimation_on_No_MIAM";

    public static final String ATTENDED_MIAM = "attended_MIAM";
    //END C100 form section 2 validation fields

    //A58
    public static final String
        CHILD_PLACEMENT_ORDER_BY_ENGLAND_AND_WALES_COURT = "child_placementOrderByEnglandAndWalesCourt";
    public static final String
        CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_COURT_NAME = "child_placmentOrderByEnglandAndWalesCourtName";
    public static final String
        CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_CASE_NUMBER = "child_placmentOrderByEnglandAndWalesCaseNumber";
    public static final String
        CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_TYPE_OF_ORDER = "child_placmentOrderByEnglandAndWalesTypeOfOrder";
    public static final String
        CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_DATE_OF_ORDER = "child_placmentOrderByEnglandAndWalesDateOfOrder";
    public static final String
        CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_COURT = "child_freeingOrderByEnglandAndWalesCourt";
    public static final String
        CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_COURT_NAME = "child_freeingOrderByEnglandAndWalesCourtName";
    public static final String
        CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_CASE_NUMBER = "child_freeingOrderByEnglandAndWalesCaseNumber";
    public static final String
        CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_TYPE_OF_ORDER = "child_freeingOrderByEnglandAndWalesTypeOfOrder";
    public static final String
        CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_DATE_OF_ORDER = "child_freeingOrderByEnglandAndWalesDateOfOrder";
    public static final String
        CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_COURT = "child_freeingOrderByNorthernIrelandCourt";
    public static final String
        CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_COURT_NAME = "child_freeingOrderByNorthernIrelandCourtName";
    public static final String
        CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_CASE_NUMBER = "child_freeingOrderByNorthernIrelandCaseNumber";
    public static final String
        CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_TYPE_OF_ORDER = "child_freeingOrderByNorthernIrelandTypeOfOrder";
    public static final String
        CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_DATE_OF_ORDER = "child_freeingOrderByNorthernIrelandDateOfOrder";
    public static final String
        CHILD_PERMANENCE_ORDER_BY_SCOTLAND_COURT = "child_permanenceOrderByScotlandCourt";
    public static final String
        CHILD_PERMANENCE_ORDER_BY_SCOTLAND_COURT_NAME = "child_permanenceOrderByScotlandCourtName";
    public static final String
        CHILD_PERMANENCE_ORDER_BY_SCOTLAND_CASE_NUMBER = "child_permanenceOrderByScotlandCaseNumber";
    public static final String
        CHILD_PERMANENCE_ORDER_BY_SCOTLAND_TYPE_OF_ORDER = "child_permanenceOrderByScotlandTypeOfOrder";
    public static final String
        CHILD_PERMANENCE_ORDER_BY_SCOTLAND_DATE_OF_ORDER = "child_permanenceOrderByScotlandDateOfOrder";
    public static final String CHILD_NO_ORDER_AVAILABLE = "child_noOrderAvailable";
    public static final String ANY_OTHER_ORDERS_AVAILABLE = "anyOtherOrdersAvailable";
    public static final String PLACEMENT_ORDER_COURT = "placementOrderCourt";
    public static final String PLACEMENT_ORDER_ID = "placementOrderId";
    public static final String PLACEMENT_ORDER_TYPE = "placementOrderType";
    public static final String PLACEMENT_ORDER_DATE = "placementOrderDate";
    public static final String FREEING_ORDER_COURT = "freeingOrderCourt";
    public static final String FREEING_ORDER_ID = "freeingOrderId";
    public static final String FREEING_ORDER_TYPE = "freeingOrderType";
    public static final String FREEING_ORDER_DATE = "freeingOrderDate";
    public static final String CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY = "child_noLaOrParentalResponsibility";
    public static final String LA_OR_PARENTAL_RESPONSIBILITY = "laOrParentalResponsibility";
    public static final String MAINTANENCE_ORDER = "maintanenceOrder";
    public static final String CHILD_COURT_AND_DATE_OF_ORDER = "child_courtAndDateOfOrder";
    public static final String NAME_OF_COURT = "nameOfCourt";
    public static final String DATE_OF_ORDER = "dateOfOrder";
    public static final String CHILD_NO_MAINTANENCE_ORDER = "child_noMaintanenceOrder";
    public static final String CHILD_MAINTANENCE_ORDER = "child_maintanenceOrder";
    public static final String HAS_MAINTANENCE_ORDER = "hasMaintanenceOrder";
    public static final String CHILD_NO_PROCEEDING_DETAILS = "child_noProceedingDetails";
    public static final String HAS_PROCEEDING_DETAILS = "hasProceedingDetails";
    public static final String CHILD_PROCEEDING_DETAILS = "child_proceedingDetails";
    public static final String CHILD_NO_PROCEEDING_DETAILS_WITH_RELATION = "child_noProceedingDetailsWithRelation";
    public static final String HAS_PROCEEDING_DETAILS_WITH_RELATION = "hasProceedingDetailsWithRelation";
    public static final String CHILD_PROCEEDING_DETAILS_WITH_RELATION = "child_proceedingDetailsWithRelation";
    public static final String
        CHILD_DONT_KNOW_PROCEEDING_DETAILS_WITH_RELATION = "child_dontKnowProceedingDetailsWithRelation";
    public static final String DONT_KNOW = "DontKnow";
    //End of A58
    private static Map<String,String> getErrorMessageMap() {
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
