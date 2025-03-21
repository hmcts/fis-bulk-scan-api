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
            "Some details are missing in the following section: Section 8,9 - "
                + "atleast 1 required of internationalElement_Resident_of_another_state,"
                + " internationalElement_jurisdictionIssue, internationalElement_request_toCentral_or_Consular_authority, "
                + "factorAffectingLitigationCapacity,"
                + " assessmentByAdultLearningTeam, factorsAffectingPersonInCourt.";
    public static final String EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 2b - Applicant claiming exemption "
                + "- atleast 1 required - noMIAM_domesticViolance, noMIAM_childProtectionConcerns, noMIAM_Urgency,"
                + " noMIAM_PreviousAttendence, noMIAM_otherReasons.";
    public static final String NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3b - Child protection concerns - atleast 1 required of "
                + "noMIAM_subjectOfEnquiries_byLocalAuthority, noMIAM_subjectOfCPP_byLocalAuthority.";
    public static final String NOMIAM_PREVIOUSATTENDENCE_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3d - Previous MIAM attendance or exemption - "
                + "atleast 1 required of previousMIAM_nonCourtDisputeResolution_4month, "
                + "previousMIAM_existingProceeding_exepmtMIAM,"
                + " otherExemption_NoEvidence_reason.";
    public static final String NOMIAM_OTHERREASONS_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3e - Other exemptions - atleast 1 required of "
                + "otherExemption_withoutNotice, otherExemption_byVirture_of_Rule12_3, "
                + "otherExemption_mediator_notAvailableToConduct, otherExemption_disability_or_inabilityToAttend,"
                + " otherExemption_NotSufficient_Respondent_ContactDetails, otherExemption_applicant_or_respondent_inPrison.";
    public static final String NOMIAM_URGENCY_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3c - Urgency - atleast 1 required of noMIAM_urgency_{*}.";
    public static final String NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING =
            "Some details are missing in the following section: 3a - Domestic violence evidence - "
                + "atleast 1 required of noMIAM_DVE_{*}.";
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
