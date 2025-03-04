package uk.gov.hmcts.reform.bulkscan.constants;

import java.util.HashMap;
import java.util.Map;
import uk.gov.hmcts.reform.bulkscan.enums.PartyEnum;

/** All private law form case types constants (Edge cases plus c100). */
public final class BulkScanPrlConstants {

    public static final String MIAM_EXEMPTIONS_CHECKLIST = "miamExemptionsChecklist";
    public static final String NO_MIAM_DOMESTIC_VIOLENCE = "noMIAM_domesticViolance";
    public static final String NO_MIAM_CHILD_PROTECTION_CONCERNS = "noMIAM_childProtectionConcerns";
    public static final String NO_MIAM_URGENCY = "noMIAM_Urgency";
    public static final String NO_MIAM_PREVIOUS_ATTENDENCE = "noMIAM_PreviousAttendence";
    public static final String NO_MIAM_OTHER_REASONS = "noMIAM_otherReasons";
    public static final String PREVIOUS_OR_ONGOING_PROCEEDING = "previous_or_ongoingProceeding";
    public static final String EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER =
            "ExistingCase_onEmergencyProtection_Care_or_supervisionOrder";
    public static final String EXEMPTION_TO_ATTEND_MIAM = "exemption_to_attend_MIAM";
    public static final String FAMILY_MEMBER_INTIMATION_ON_NO_MIAM =
            "familyMember_Intimation_on_No_MIAM";
    public static final String ATTENDED_MIAM = "attended_MIAM";
    public static final String MIAM_DOMESTIC_VIOLENCE_CHECKLIST = "miamDomesticViolenceChecklist";
    public static final String NO_MIAM_DVE_ARRESTED_FOR_SIMILAR_OFFENCE =
            "noMIAM_DVE_arrestedForSimilarOffence";
    public static final String NO_MIAM_DVE_RELEVANT_POLICE_CAUTION =
            "noMIAM_DVE_relevantPoliceCaution";
    public static final String NO_MIAM_DVE_RELEVANT_CRIMINAL_PROCEEDING =
            "noMIAM_DVE_relevantCriminalProceeding";
    public static final String NO_MIAM_DVE_RELEVANT_CONVICTION = "noMIAM_DVE_relevantConviction";
    public static final String NO_MIAM_DVE_COURT_ORDER = "noMIAM_DVE_courtOrder";
    public static final String NO_MIAM_DVE_PROTECTION_NOTICE = "noMIAM_DVE_protectionNotice";
    public static final String NO_MIAM_DVE_PROTECTIVE_INJUNCTION =
            "noMIAM_DVE_protectiveInjunction";
    public static final String NO_MIAM_DVE_NO_CROSS_UNDERTAKING_GIVEN =
            "noMIAM_DVE_noCrossUndertakingGiven";
    public static final String NO_MIAM_DVE_COPY_OF_FACT_FINDING = "noMIAM_DVE_copyOfFactFinding";
    public static final String NO_MIAM_DVE_EXPERT_EVIDENCE_REPORT =
            "noMIAM_DVE_expertEvidenceReport";
    public static final String NO_MIAM_DVE_HEALTH_PROFESSIONAL_REPORT =
            "noMIAM_DVE_healthProfessionalReport";
    public static final String NO_MIAM_DVE_REFERRAL_HEALTH_PROFESSIONAL_REPORT =
            "noMIAM_DVE_ReferralHealthProfessionalReport";
    public static final String
            NO_MIAM_DVE_MEMBER_OF_MULTI_AGENCY_RISK_ASSESSMENT_CONFERRANCE_LETTER =
                    "noMIAM_DVE_memberOf_MultiAgencyRiskAssessmentConferrance_letter";
    public static final String NO_MIAM_DVE_DOMESTIC_VIOLENCE_ADVISOR =
            "noMIAM_DVE_domesticViolenceAdvisor";
    public static final String NO_MIAM_DVE_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR_LETTER =
            "noMIAM_DVE_independentSexualViolenceAdvisor_Letter";
    public static final String NO_MIAM_DVE_OFFICER_EMPLOYED_LOCAL_AUTHORITY_LETTER =
            "noMIAM_DVE_officerEmployed_localAuthority_letter";
    public static final String NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_LETTER =
            "noMIAM_DVE_domesticViolenceSupportCharity_letter";
    public static final String NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER =
            "noMIAM_DVE_domesticViolenceSupportCharity_refuge_letter";
    public static final String NO_MIAM_DVE_PUBLIC_AUTHORITY_CONFIRMATION_LETTER =
            "noMIAM_DVE_publicAuthority_confirmationLetter";
    public static final String NO_MIAM_DVE_SECRETARY_OF_STATE_LETTER =
            "noMIAM_DVE_secretaryOfState_letter";
    public static final String NO_MIAM_DVE_EVIDENCE_FINANCIAL_MATTERS =
            "noMIAM_DVE_evidenceFinancialMatters";
    public static final String NO_MIAM_CHILD_PROTECTION_CONCERNS_CHECKLIST =
            "mpuChildProtectionConcernReason";
    public static final String NO_MIAM_SUBJECT_OF_ENQUIRIES_BY_LOCAL_AUTHORITY =
            "noMIAM_subjectOfEnquiries_byLocalAuthority";
    public static final String NO_MIAM_SUBJECT_OF_CPP_BY_LOCAL_AUTHORITY =
            "noMIAM_subjectOfCPP_byLocalAuthority";
    public static final String MIAM_URGENCY_REASON_CHECKLIST = "mpuUrgencyReason";
    public static final String NO_MIAM_URGENCY_RISK_TO_LIFE_LIBERTY_OR_SAFETY =
            "noMIAM_urgency_risk_to_life_liberty_or_safety";
    public static final String NO_MIAM_URGENCY_RISK_OF_HARM = "noMIAM_urgency_riskOfHarm";
    public static final String NO_MIAM_URGENCY_RISK_TO_UNLAWFUL_REMOVAL =
            "noMIAM_urgency_removalFromUnitedKingdom";
    public static final String NO_MIAM_URGENCY_RISK_TO_MISCARRIAGE_OF_JUSTICE =
            "noMIAM_urgency_miscarriageOfJustice";
    public static final String NO_MIAM_URGENCY_UNREASONABLEHARDSHIP =
            "noMIAM_urgency_financialHardship";
    public static final String NO_MIAM_URGENCY_IRRETRIEVABLE_PROBLEM =
            "noMIAM_urgency_irretrievableProblems";
    public static final String NO_MIAM_URGENCY_CONFLICT_WITH_OTHER_STATE_COURTS =
            "noMIAM_urgency_conflictWithOtherStateCourts";
    public static final String ORDER_APPLIED_FOR = "ordersApplyingFor";
    public static final String CHILD_ARRANGEMENT_ORDER = "childArrangement_order";
    public static final String PROHIBITED_STEPS_ORDER = "prohibitedSteps_order";
    public static final String SPECIAL_ISSUE_ORDER = "specialIssue_order";
    public static final String SPECIFIC_ISSUE_ORDER_DESCRIPTION = "specificIssueOrder";
    public static final String PROHIBITED_STEPS_ORDER_DESCRIPTION = "prohibitedStepsOrder";
    public static final String CHILD_ARRANGEMENTS_ORDER_DESCRIPTION = "childArrangementsOrder";

    public static final String INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD =
            "international_or_factorsAffectingLitigation";

    // C100 form section 8 & 9 validation fields
    public static final String ASSESSMENT_BY_ADULT_LEARNING_TEAM_FIELD =
            "assessmentByAdultLearningTeam";
    public static final String FACTORS_AFFECTING_LITIGATION_CAPACITY_FIELD =
            "factorAffectingLitigationCapacity";
    public static final String FACTORS_AFFECTING_PERSON_IN_COURT_FIELD =
            "factorsAffectingPersonInCourt";
    public static final String INTERNATIONALELEMENT_JURISDICTIONISSUE =
            "internationalElement_jurisdictionIssue";
    public static final String INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH =
            "internationalElement_request_toCentral_or_Consular_authority";
    public static final String INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE =
            "internationalElement_Resident_of_another_state";
    public static final String INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS =
            "internationalElement_Resident_of_another_state_details";
    public static final String INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS =
            "internationalElement_request_toCentral_or_Consular_authority_details";
    public static final String WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS =
            "withoutNotice_jurisdictionIssue_details";
    public static final String INTERNATIONALELEMENT_WARNING_MESSAGE =
            "Some details are missing in the following section - [Cases with an international"
                    + " element]. Details should not be empty with checkbox (Yes).";

    // END C100 form sections 8 & 9 validation fields

    // C100 Other Proceeding Fields.
    public static final String OTHER_PROCEEDING_AVAILABLE =
            "ExistingCase_onEmergencyProtection_Care_or_supervisionOrder";
    public static final String OTHER_PROCEEDING_NAME_OF_CHILDREN = "other_case_name_of_children";
    public static final String OTHER_PROCEEDING_CASE_NUMBER = "other_case_case_number";
    public static final String OTHER_PROCEEDING_DATE_OF_YEAR = "other_case_date_or_year";
    public static final String OTHER_PROCEEDING_NAME_AND_OFFICE = "other_case_name_and_office";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_1 =
            "other_case_emergency_protection_order";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_2 = "other_case_supervision_order";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_3 = "other_case_care_order";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_4 = "other_case_childAbduction";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_5 =
            "other_case_proceeding_for_NonMolestatioNorder";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_6 =
            "other_case_proceeding_for_contact_or_resident_order";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_7 =
            "other_case_contact_or_residentOrder_withAdoptioNorder";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_8 =
            "other_case_childMaintenanceOrder";
    public static final String OTHER_PROCEEDING_TYPE_OF_ORDER_9 =
            "other_case_childArrangementOrder";
    public static final String TYPE_OF_ORDER = "typeOfOrder";
    public static final String OTHER_PROCEEDINGS_DETAILS_TABLE = "otherProceedingsDetailsTable";
    public static final String URGENT_OR_WITHOUT_HEARING = "urgent_or_withoutHearing";
    public static final String WITHOUT_NOTICE_FRUSTRATE_THE_ORDER =
            "withoutNotice_frustrateTheOrder";
    public static final String WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON =
            "withoutNotice_frustrateTheOrder_reason";
    public static final String WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE =
            "withoutNotice_abridged_or_informalNotice";
    public static final String WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS =
            "withoutNotice_abridged_or_informalNotice_reasons";
    public static final String ORDER_DIRECTION_SOUGHT = "order_direction_sought";
    public static final String URGENCY_REASON = "urgency_reason";
    public static final String APPLICATION_TIMETABLE = "application_timetable";
    public static final String RESPONDENT_EFFORT = "respondent_effort";
    public static final String REASON_FOR_CONSIDERATION = "reason_for_consideration";
    public static final String EITHER_SECTION_6_A_OR_6_B_SHOULD_BE_BE_FILLED_UP_NOT_BOTH =
            "Either Section 6a (Urgent hearing) or 6b (Without notice hearing) should be be filled"
                    + " up, not both.";
    public static final String NEITHER_6A_NOR_6B_HAS_BEEN_FILLED_UP =
            "Neither Section 6a (Urgent hearing) or 6b (Without notice hearing) has been filled"
                    + " up.";
    public static final String HEARING_URGENCY_TABLE = "hearingUrgencyTable";
    public static final String SET_OUT_REASONS_BELOW = "setOutReasonsBelow";
    public static final String SECTION_6_B_WITHOUT_NOTICE_HEARING_DETAILS_IS_MISSING =
            "Section 6b (Without notice hearing) details is missing.";

    // C100 Attending the hearing
    public static final String APPLICANT_REQUIRES_INTERPRETER = "applicantRequiresInterpreter";
    public static final String APPLICANT_REQUIRES_INTERPRETER_APPLICANT =
            "applicantRequiresInterpreter_applicant";
    public static final String APPLICANT_REQUIRES_INTERPRETER_RESPONDENT =
            "applicantRequiresInterpreter_respondent";
    public static final String APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY =
            "applicantRequiresInterpreter_otherParty";
    public static final String WELSH_NEEDS = "nameOfPartyWhoNeedsWelsh";
    public static final String WELSH_NEEDS_CCD = "welshNeeds";
    public static final String WHO_WELSH_NEEDS = "whoNeedsWelsh";
    public static final String SPOKEN_WRITTEN_BOTH = "spokenOrWritten";
    public static final String INTERPRETER_NEEDS = "interpreterNeeds";
    public static final String PARTY_ENUM = "party";

    // C100 - section 4 starts
    public static final String MEDIATOR_CERTIFIES_MIAM_EXEMPTION = "mediatorCertifiesMiamExemption";
    public static final String MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_WILLING_TO_ATTEND_MIAM =
            "mediationNotSuitable_NoneOfTheRespondentsWillingToAttendMIAM";
    public static final String
            MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_FAILED_TO_ATTEND_MIAM_WITHOUT_GOOD_REASON =
                    "mediationNotSuitable_NoneOfTheRespondentsFailedToAttendMIAMWithoutGoodReason";
    public static final String MEDIATION_NOT_SUITABLE_FOR_RESOLVING_THE_DISPUTE =
            "mediationNotSuitable_forResolvingTheDispute";
    public static final String MEDIATOR_CERTIFIES_APPLICANT_ATTEND_MIAM =
            "mediatorCertifiesApplicantAttendMIAM";
    public static final String APPLICANT_ONLY_ATTENDED_MIAM = "applicantOnly_AttendedMIAM";
    public static final String APPLICANT_ONLY_ATTENDED_MIAM_TOGETHER =
            "applicantOnly_AttendedMIAMTogether";
    public static final String APPLICANT_AND_RESPONDENT_PARTY_ATTENDED_MIAM_SEPARATELY =
            "applicantAndRespondentParty_AttendedMIAMSeparately";
    public static final String RESPONDENT_PARTY_ARRANGED_TO_ATTEND_MIAM_SEPARATELY =
            "respondentPartyArranged_ToAttendMIAMSeparately";
    public static final String MEDIATOR_CERTIFIES_DISPUTE_RESOLUTION_NOT_PROCEEDING =
            "mediatorCertifiesDisputeResolutionNotProceeding";
    public static final String MEDIATION_NOT_PROCEEDING_APPLICANT_ATTENDED_MIAM_ALONE =
            "mediationNotProceeding_applicationAttendedMIAMAlone";
    public static final String MEDIATION_NOT_PROCEEDING_APPLICANTS_AND_RESPONDENTS_ATTENDED_MIAM =
            "mediationNotProceeding_applicantsAndRespondentsAttendedMIAM";
    public static final String MEDIATION_NOT_PROCEEDING_HASSTARTED_BUT_BROKEN_WITH_SOMEISSUE =
            "mediationNotProceeding_hasStartedButBrokenWithSomeIssue";

    public static final String SEARCH_RESULTS_POSTCODE_POSTCODE_SERVICE_AREA = "search/results?postcode={postcode}&serviceArea=";
    public static final String DOMESTIC_ABUSE_POSTCODE_URL = SEARCH_RESULTS_POSTCODE_POSTCODE_SERVICE_AREA + "domestic-abuse";
    public static final String COURT_DETAILS_URL = "courts/{court-slug}";

    // // C100 - section 4 ENDS

    public static Map<String, String> getInterpreterEnum() {
        Map<String, String> map = new HashMap<>();
        map.put(APPLICANT_REQUIRES_INTERPRETER_APPLICANT, PartyEnum.applicant.getDisplayedValue());
        map.put(
                APPLICANT_REQUIRES_INTERPRETER_RESPONDENT,
                PartyEnum.respondent.getDisplayedValue());
        map.put(APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY, PartyEnum.other.getDisplayedValue());

        return map;
    }

    private BulkScanPrlConstants() {}
}
