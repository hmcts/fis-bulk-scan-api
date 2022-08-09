package uk.gov.hmcts.reform.bulkscan.utils;

public final class PrlTestConstants {
    public static final String RESOURCE_CHARSET_UTF8 = "utf-8";
    public static final String S2S_TOKEN = "serviceauthorization";
    public static final String TEST_RESOURCE_NOT_FOUND = "Could not find resource in path ";
    public static final String TEST_URL = "Url";
    public static final String TEST_BINARY_URL = "binary_url";

    public static final String APPLICANT_1_FIRST_NAME = "applicant1_firstName";
    public static final String APPLICANT_1_LAST_NAME = "applicant1_lastName";
    public static final String EMPTY_STRING = "";

    public static final String NOMIAM_CHILDPROTECTIONCONCERNS_FIELD =
            "NoMIAM_childProtectionConcerns";
    public static final String NOMIAM_DOMESTICVIOLENCE_FIELD = "NoMIAM_domesticViolence";
    public static final String NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE_FIELD =
            "NoMIAM_DVE_arrestedForSimilarOffence";
    public static final String NOMIAM_URGENCY_FIELD = "NoMIAM_Urgency";
    public static final String NOMIAM_PREVIOUSATTENDANCE_FIELD = "NoMIAM_PreviousAttendance";
    public static final String NOMIAM_OTHERREASONS_FIELD = "NoMIAM_otherReasons";
    public static final String POST_CODE = "TW3 1NN";
    public static final String TICK_BOX_FALSE = "false";
    public static final String TICK_BOX_NO = "No";
    public static final String TICK_BOX_TRUE = "true";
    public static final String TICK_BOX_YES = "Yes";

    public static final String INTERNATIONAL_FACTORS_AFFECTING_LITIGATION_WARNING =
            "Group Dependency Field (international_or_factorsAffectingLitigation) has dependency"
                + " validation warning. Must contain at least 1 of the fields "
                + "[internationalElement_Resident_of_another_state,internationalElement_jurisdictionIssue,"
                + "internationalElement_request_toCentral_or_Consular_authority,factorAffectingLitigationCapacity,"
                + "assessmentByAdultLearningTeam,factorsAffectingPersonInCourt].";

    public static final String INTERNATIONAL_JURISDICTION_WARNING_MESSAGE =
            "Group Dependency Field (internationalElement_jurisdictionIssue) has dependency"
                    + " validation warning. Must contain at least 1 of the fields"
                    + " [withoutNotice_jurisdictionIssue_details].";

    public static final String INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_WARNING_MESSAGE =
            "Group Dependency Field (internationalElement_Resident_of_another_state) has dependency"
                    + " validation warning. Must contain at least 1 of the fields"
                    + " [internationalElement_Resident_of_another_state_details].";
    public static final String INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_WARNING_MESSAGE =
            "Group Dependency Field (internationalElement_request_toCentral_or_Consular_authority)"
                    + " has dependency validation warning. Must contain at least 1 of the fields"
                    + " [internationalElement_request_toCentral_or_Consular_authority_details].";

    private PrlTestConstants() {}
}
