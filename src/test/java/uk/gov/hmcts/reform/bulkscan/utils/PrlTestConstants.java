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
            "noMIAM_childProtectionConcerns";
    public static final String NOMIAM_DOMESTICVIOLENCE_FIELD = "noMIAM_domesticViolance";
    public static final String NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE_FIELD =
            "noMIAM_DVE_arrestedForSimilarOffence";
    public static final String NOMIAM_URGENCY_FIELD = "noMIAM_Urgency";
    public static final String NOMIAM_PREVIOUSATTENDENCE_FIELD = "noMIAM_PreviousAttendence";
    public static final String NOMIAM_OTHERREASONS_FIELD = "noMIAM_otherReasons";
    public static final String POST_CODE = "TW3 1NN";
    public static final String TICK_BOX_NO = "No";
    public static final String TICK_BOX_YES = "Yes";

    public static final String RESPONDENT_ONE = "Respondent 1";
    public static final String RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS =
            "respondentOneAllAddressesForLastFiveYears";
    public static final String RESPONDENT_TWO = "Respondent 2";
    public static final String RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS =
            "respondentTwoAllAddressesForLastFiveYears";

    public static final String INTERNATIONAL_FACTORS_AFFECTING_LITIGATION_WARNING =
            "Some details are missing in the following section: 8 - Cases with an international"
                    + " element; 9 - Factors affecting ability to participate in proceedings.";
    public static final String EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 2b - Applicant claiming exemption "
                    + "from the requirement to attend a MIAM.";
    public static final String NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3b - Child protection concerns.";
    public static final String NOMIAM_PREVIOUSATTENDENCE_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3d - Previous MIAM attendance or"
                    + " MIAM exemption.";
    public static final String NOMIAM_OTHERREASONS_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3e - Other exemptions.";
    public static final String NOMIAM_URGENCY_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3c - Urgency.";
    public static final String NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3a - Domestic violence evidence.";
    public static final String FL401A_APPLICANT_FULL_NAME_FIELD = "applicant_full_name";
    public static final String FL401A_APPLICANT_ADDRESS_FIELD = "applicant_address";
    public static final String FL401A_APPLICANT_POSTCODE_FIELD = "applicant_postcode";
    public static final String FL401A_APPLICANT_DATE_OF_BIRTH_FIELD = "applicant_dateOfBirth";
    public static final String FL401A_TEST_POSTCODE = "N13 4PS";

    public static final String RESPONDENT_ONE_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS =
            "("
                    + RESPONDENT_ONE
                    + ") has not lived at the current address "
                    + "for more than 5 years. Previous address(es) field ("
                    + RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS
                    + ") should not be empty or null.";

    public static final String RESPONDENT_TWO_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS =
            "("
                    + RESPONDENT_TWO
                    + ") has not lived at the current address "
                    + "for more than 5 years. Previous address(es) field ("
                    + RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS
                    + ") should not be empty or null.";

    private PrlTestConstants() {}
}
