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

    public static final String RESPONDENT_ONE = "Respondent 1";
    public static final String RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS =
            "respondent1AllAddressesForLastFiveYears";
    public static final String RESPONDENT_TWO = "Respondent 2";
    public static final String RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS =
            "respondent2AllAddressesForLastFiveYears";

    public static final String INTERNATIONAL_FACTORS_AFFECTING_LITIGATION_WARNING =
            "Group Dependency Field (international_or_factorsAffectingLitigation) has dependency"
                + " validation warning. Must contain at least 1 of the fields "
                + "[internationalElement_Resident_of_another_state,internationalElement_jurisdictionIssue,"
                + "internationalElement_request_toCentral_or_Consular_authority,factorAffectingLitigationCapacity,"
                + "assessmentByAdultLearningTeam,factorsAffectingPersonInCourt].";

    public static final String EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING =
            "Group Dependency Field (exemption_to_attend_MIAM) has dependency validation warning."
                    + " Must contain at least 1 of the fields"
                    + " [NoMIAM_domesticViolence,NoMIAM_childProtectionConcerns,"
                    + "NoMIAM_Urgency,NoMIAM_PreviousAttendance,NoMIAM_otherReasons].";
    public static final String NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_childProtectionConcerns) has dependency validation"
                    + " warning. Must contain at least 1 of the fields"
                    + " [NoMIAM_subjectOfEnquiries_byLocalAuthority,"
                    + "NoMIAM_subjectOfCPP_byLocalAuthority].";
    public static final String NOMIAM_PREVIOUSATTENDANCE_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_PreviousAttendance) has dependency validation warning. "
                    + "Must contain at least 1 of the fields [NoMIAM_PreviousAttendanceReason].";
    public static final String NOMIAM_OTHERREASONS_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_otherReasons) has dependency validation warning. "
                    + "Must contain at least 1 of the fields [NoMIAM_otherExceptions].";
    public static final String NOMIAM_URGENCY_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_Urgency) has dependency validation warning. Must"
                + " contain at least 1 of the fields"
                + " [NoMIAM_urgency_risk_to_life_liberty_or_safety,"
                + "NoMIAM_urgency_riskOfHarm,NoMIAM_urgency_risk_to_unlawfulRemoval,"
                + "NoMIAM_urgency_risk_to_miscarriageOfJustice,NoMIAM_urgency_unreasonablehardship,"
                + "NoMIAM_urgency_irretrievableProblem,NoMIAM_urgency_conflictWithOtherStateCourts].";
    public static final String NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_domesticViolence) has dependency validation warning."
                + " Must contain at least 1 of the fields [NoMIAM_DVE_arrestedForSimilarOffence,"
                + "NoMIAM_DVE_relevantPoliceCaution,NoMIAM_DVE_relevantCriminalProceeding,NoMIAM_DVE_relevantConviction,"
                + "NoMIAM_DVE_courtOrder,NoMIAM_DVE_protectionNotice,NoMIAM_DVE_protectiveInjunction,"
                + "NoMIAM_DVE_NoCrossUndertakingGiven,NoMIAM_DVE_copyOfFactFinding,NoMIAM_DVE_expertEvidenceReport,"
                + "NoMIAM_DVE_healthProfessionalReport,NoMIAM_DVE_ReferralHealthProfessionalReport,"
                + "NoMIAM_DVE_memberOf_MultiAgencyRiskAssessmentConferrance_letter,NoMIAM_DVE_domesticViolenceAdvisor,"
                + "NoMIAM_DVE_independentSexualViolenceAdvisor_Letter,NoMIAM_DVE_officerEmployed_localAuthority_letter,"
                + "NoMIAM_DVE_domesticViolenceSupportCharity_letter,"
                + "NoMIAM_DVE_domesticViolenceSupportCharity_refuge_letter,"
                + "NoMIAM_DVE_publicAuthority_confirmationLetter,NoMIAM_DVE_secretaryOfState_letter,"
                + "NoMIAM_DVE_evidenceFinancialMatters].";

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
