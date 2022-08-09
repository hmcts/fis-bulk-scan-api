package uk.gov.hmcts.reform.bulkscan.utils;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.ASSESSMENT_BY_ADULT_LEARNING_TEAM_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.FACTORS_AFFECTING_LITIGATION_CAPACITY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.FACTORS_AFFECTING_PERSON_IN_COURT_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_JURISDICTIONISSUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_CHILDPROTECTIONCONCERNS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_DOMESTICVIOLENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_OTHERREASONS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_PREVIOUSATTENDANCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_URGENCY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.TICK_BOX_FALSE;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.TICK_BOX_NO;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.TICK_BOX_TRUE;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.TICK_BOX_YES;

import java.util.ArrayList;
import java.util.List;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

public final class TestDataC100Util {
    public static final String POST_CODE = "TW3 1NN";

    private TestDataC100Util() {}

    public static List<OcrDataField> getData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        fieldList.addAll(getAllNamesRelationSuccessData());
        fieldList.addAll(getExemptionToAttendMiamSuccessData());
        fieldList.addAll(getLitigationCapacityGroupSuccessData());

        return fieldList;
    }

    public static List<OcrDataField> getApplicantWarning() {
        List<OcrDataField> fieldList = new ArrayList<>();

        fieldList.addAll(getAllNamesRelationWarningData());
        fieldList.addAll(getExemptionToAttendMiamSuccessData());

        return fieldList;
    }

    public static List<OcrDataField> getAllNamesRelationWarningData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataPreviousOrOngoingProceedingField = new OcrDataField();
        ocrDataPreviousOrOngoingProceedingField.setName("previous_or_ongoingProceeding");
        ocrDataPreviousOrOngoingProceedingField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataPreviousOrOngoingProceedingField);

        OcrDataField ocrDataExistingCaseEmergencyProtectionField = new OcrDataField();
        ocrDataExistingCaseEmergencyProtectionField.setName(
                "existingCase_onEmergencyProtection_Care_or_supervisioNorder");
        ocrDataExistingCaseEmergencyProtectionField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataExistingCaseEmergencyProtectionField);

        OcrDataField ocrDataFamilyMemberIntimationField = new OcrDataField();
        ocrDataFamilyMemberIntimationField.setName("familyMember_Intimation_on_No_MIAM");
        ocrDataFamilyMemberIntimationField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataFamilyMemberIntimationField);

        OcrDataField ocrDataAttendedMiamField = new OcrDataField();
        ocrDataAttendedMiamField.setName("attended_MIAM");
        ocrDataAttendedMiamField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataAttendedMiamField);

        fieldList.addAll(getApplicantSuccessData());

        fieldList.addAll(getChild1NamesRelationSuccessData());

        fieldList.addAll(getChild2NamesRelationSuccessData());

        fieldList.addAll(getChild3NamesRelationSuccessData());

        fieldList.addAll(getChild4NamesRelationSuccessData());

        return fieldList;
    }

    // applicant warning
    public static List<OcrDataField> getAllNamesRelationSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataPreviousOrOngoingProceedingField = new OcrDataField();
        ocrDataPreviousOrOngoingProceedingField.setName("previous_or_ongoingProceeding");
        ocrDataPreviousOrOngoingProceedingField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataPreviousOrOngoingProceedingField);

        OcrDataField ocrDataExistingCaseEmergencyProtectionField = new OcrDataField();
        ocrDataExistingCaseEmergencyProtectionField.setName(
                "existingCase_onEmergencyProtection_Care_or_supervisioNorder");
        ocrDataExistingCaseEmergencyProtectionField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataExistingCaseEmergencyProtectionField);

        OcrDataField ocrDataFamilyMemberIntimationField = new OcrDataField();
        ocrDataFamilyMemberIntimationField.setName("familyMember_Intimation_on_No_MIAM");
        ocrDataFamilyMemberIntimationField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataFamilyMemberIntimationField);

        OcrDataField ocrDataAttendedMiamField = new OcrDataField();
        ocrDataAttendedMiamField.setName("attended_MIAM");
        ocrDataAttendedMiamField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataAttendedMiamField);

        fieldList.addAll(getApplicantSuccessData());

        fieldList.addAll(getChild1NamesRelationSuccessData());

        fieldList.addAll(getChild2NamesRelationSuccessData());

        fieldList.addAll(getChild3NamesRelationSuccessData());

        fieldList.addAll(getChild4NamesRelationSuccessData());

        fieldList.addAll(getRespondent1Details());

        fieldList.addAll(getRespondent2Details());

        return fieldList;
    }

    public static List<OcrDataField> getApplicantSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataApplicant1FirstName = new OcrDataField();
        ocrDataApplicant1FirstName.setName("applicant1FirstName");
        ocrDataApplicant1FirstName.setValue("John");
        fieldList.add(ocrDataApplicant1FirstName);

        OcrDataField ocrDataApplicant1LastName = new OcrDataField();
        ocrDataApplicant1LastName.setName("applicant1LastName");
        ocrDataApplicant1LastName.setValue("Peters");
        fieldList.add(ocrDataApplicant1LastName);

        OcrDataField ocrDataApplicant1DateOfBirth = new OcrDataField();
        ocrDataApplicant1DateOfBirth.setName("applicant1DateOfBirth");
        ocrDataApplicant1DateOfBirth.setValue("09/08/01");
        fieldList.add(ocrDataApplicant1DateOfBirth);

        OcrDataField ocrDataApplicant1PlaceOfBirth = new OcrDataField();
        ocrDataApplicant1PlaceOfBirth.setName("applicant1PlaceOfBirth");
        ocrDataApplicant1PlaceOfBirth.setValue("Petersburg");
        fieldList.add(ocrDataApplicant1PlaceOfBirth);

        OcrDataField ocrDataChildArrangementOrder = new OcrDataField();
        ocrDataChildArrangementOrder.setName("childArrangement_order");
        ocrDataChildArrangementOrder.setValue("childArrangementOrder");
        fieldList.add(ocrDataChildArrangementOrder);

        OcrDataField ocrDataProhibitedStepsOrder = new OcrDataField();
        ocrDataProhibitedStepsOrder.setName("prohibitedSteps_order");
        ocrDataProhibitedStepsOrder.setValue("prohibitedSteps_order");
        fieldList.add(ocrDataProhibitedStepsOrder);

        OcrDataField ocrDataSpecialIssueOrder = new OcrDataField();
        ocrDataSpecialIssueOrder.setName("specialIssue_order");
        ocrDataSpecialIssueOrder.setValue("specialIssueOrder");
        fieldList.add(ocrDataSpecialIssueOrder);

        return fieldList;
    }

    public static List<OcrDataField> getApplicantWarningData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataApplicant1FirstName = new OcrDataField();
        ocrDataApplicant1FirstName.setName("applicant1FirstName");
        ocrDataApplicant1FirstName.setValue("John");
        fieldList.add(ocrDataApplicant1FirstName);

        OcrDataField ocrDataApplicant1LastName = new OcrDataField();
        ocrDataApplicant1LastName.setName("applicant1LastName");
        ocrDataApplicant1LastName.setValue("Peters");
        fieldList.add(ocrDataApplicant1LastName);

        OcrDataField ocrDataApplicant1DateOfBirth = new OcrDataField();
        ocrDataApplicant1DateOfBirth.setName("applicant1DateOfBirth");
        ocrDataApplicant1DateOfBirth.setValue("09/08/01");
        fieldList.add(ocrDataApplicant1DateOfBirth);

        OcrDataField ocrDataApplicant1PlaceOfBirth = new OcrDataField();
        ocrDataApplicant1PlaceOfBirth.setName("applicant1PlaceOfBirth");
        ocrDataApplicant1PlaceOfBirth.setValue("Petersburg");
        fieldList.add(ocrDataApplicant1PlaceOfBirth);

        OcrDataField ocrDataChildArrangementOrder = new OcrDataField();
        ocrDataChildArrangementOrder.setName("childArrangement_order");
        ocrDataChildArrangementOrder.setValue("childArrangementOrder");
        fieldList.add(ocrDataChildArrangementOrder);

        OcrDataField ocrDataProhibitedStepsOrder = new OcrDataField();
        ocrDataProhibitedStepsOrder.setName("prohibitedSteps_order");
        ocrDataProhibitedStepsOrder.setValue("prohibitedSteps_order");
        fieldList.add(ocrDataProhibitedStepsOrder);

        OcrDataField ocrDataSpecialIssueOrder = new OcrDataField();
        ocrDataSpecialIssueOrder.setName("specialIssue_order");
        ocrDataSpecialIssueOrder.setValue("specialIssueOrder");
        fieldList.add(ocrDataSpecialIssueOrder);

        return fieldList;
    }

    public static List<OcrDataField> getChild1NamesRelationSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataChild1FirstName = new OcrDataField();
        ocrDataChild1FirstName.setName("child1_firstName");
        ocrDataChild1FirstName.setValue("firstName");
        fieldList.add(ocrDataChild1FirstName);

        OcrDataField ocrDataChild1LastName = new OcrDataField();
        ocrDataChild1LastName.setName("child1_lastName");
        ocrDataChild1LastName.setValue("LastName");
        fieldList.add(ocrDataChild1LastName);

        OcrDataField ocrDataChildDateOfBirth = new OcrDataField();
        ocrDataChildDateOfBirth.setName("child1_dateOfBirth");
        ocrDataChildDateOfBirth.setValue("02/02/2020");
        fieldList.add(ocrDataChildDateOfBirth);

        OcrDataField ocrDataChild1GenderField = new OcrDataField();
        ocrDataChild1GenderField.setName("child1_gender");
        ocrDataChild1GenderField.setValue("Male");
        fieldList.add(ocrDataChild1GenderField);

        OcrDataField ocrDataChild1OrderAppliedFor = new OcrDataField();
        ocrDataChild1OrderAppliedFor.setName("child1_orderAppliedFor");
        ocrDataChild1OrderAppliedFor.setValue("orderAppliedFor");
        fieldList.add(ocrDataChild1OrderAppliedFor);

        OcrDataField ocrDataChild1Applicant1Relation = new OcrDataField();
        ocrDataChild1Applicant1Relation.setName("child1_applicant1_relationship");
        ocrDataChild1Applicant1Relation.setValue("applicant");
        fieldList.add(ocrDataChild1Applicant1Relation);

        OcrDataField ocrDataChild1Respondent1Relation = new OcrDataField();
        ocrDataChild1Respondent1Relation.setName("child1_respondent1_relationship");
        ocrDataChild1Respondent1Relation.setValue("respondent");
        fieldList.add(ocrDataChild1Respondent1Relation);

        OcrDataField ocrDataLocalAuthoritySocialWorker = new OcrDataField();
        ocrDataLocalAuthoritySocialWorker.setName("child1_localAuthority_or_socialWorker");
        ocrDataLocalAuthoritySocialWorker.setValue("socialWorker");
        fieldList.add(ocrDataLocalAuthoritySocialWorker);

        OcrDataField ocrDataChildrenServicesAuthority = new OcrDataField();
        ocrDataChildrenServicesAuthority.setName("childrenServicesAuthority");
        ocrDataChildrenServicesAuthority.setValue("Yes");
        fieldList.add(ocrDataChildrenServicesAuthority);

        OcrDataField ocrDataChildrenOfSameParent = new OcrDataField();
        ocrDataChildrenOfSameParent.setName("children_of_same_parent");
        ocrDataChildrenOfSameParent.setValue("Yes");
        fieldList.add(ocrDataChildrenOfSameParent);

        OcrDataField ocrDataChildProtectionPlan = new OcrDataField();
        ocrDataChildProtectionPlan.setName("subject_to_childProtectionPlan");
        ocrDataChildProtectionPlan.setValue("protectionplan");
        fieldList.add(ocrDataChildProtectionPlan);

        OcrDataField ocrDataChildrenParentsName = new OcrDataField();
        ocrDataChildrenParentsName.setName("children_parentsName");
        ocrDataChildrenParentsName.setValue("parentsName");
        fieldList.add(ocrDataChildrenParentsName);

        OcrDataField ocrDataChildrenParentsNameColl = new OcrDataField();
        ocrDataChildrenParentsNameColl.setName("child_parentsName_collection");
        ocrDataChildrenParentsNameColl.setValue("parentsNameColl");
        fieldList.add(ocrDataChildrenParentsNameColl);

        OcrDataField ocrDataParentalResponsibilityDetails = new OcrDataField();
        ocrDataParentalResponsibilityDetails.setName("parentalResponsibilityDetails");
        ocrDataParentalResponsibilityDetails.setValue("parentResponsibility");
        fieldList.add(ocrDataParentalResponsibilityDetails);

        OcrDataField ocrDatachildLivingWithOthersDet = new OcrDataField();
        ocrDatachildLivingWithOthersDet.setName("child_living_with_others_details");
        ocrDatachildLivingWithOthersDet.setValue("living other details");
        fieldList.add(ocrDatachildLivingWithOthersDet);

        OcrDataField ocrDatachildLivingWithOthers = new OcrDataField();
        ocrDatachildLivingWithOthers.setName("child_living_with_others");
        ocrDatachildLivingWithOthers.setValue("false");
        fieldList.add(ocrDatachildLivingWithOthers);

        OcrDataField ocrDatachildLivingWithApplicant = new OcrDataField();
        ocrDatachildLivingWithApplicant.setName("child_living_with_Applicant");
        ocrDatachildLivingWithApplicant.setValue("false");
        fieldList.add(ocrDatachildLivingWithApplicant);

        OcrDataField ocrDatachildLivingWithRespondent = new OcrDataField();
        ocrDatachildLivingWithRespondent.setName("child_living_with_Respondent");
        ocrDatachildLivingWithRespondent.setValue("false");
        fieldList.add(ocrDatachildLivingWithRespondent);

        return fieldList;
    }

    public static List<OcrDataField> getChild2NamesRelationSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataChild2FirstName = new OcrDataField();
        ocrDataChild2FirstName.setName("child2_firstName");
        ocrDataChild2FirstName.setValue("child2firstName");
        fieldList.add(ocrDataChild2FirstName);

        OcrDataField ocrDataChild2LastName = new OcrDataField();
        ocrDataChild2LastName.setName("child2_lastName");
        ocrDataChild2LastName.setValue("child2LastName");
        fieldList.add(ocrDataChild2LastName);

        OcrDataField ocrDataChild2DateOfBirth = new OcrDataField();
        ocrDataChild2DateOfBirth.setName("child2_dateOfBirth");
        ocrDataChild2DateOfBirth.setValue("02/02/2020");
        fieldList.add(ocrDataChild2DateOfBirth);

        OcrDataField ocrDataChild2GenderField = new OcrDataField();
        ocrDataChild2GenderField.setName("child2_gender");
        ocrDataChild2GenderField.setValue("Male");
        fieldList.add(ocrDataChild2GenderField);

        OcrDataField ocrDataChild2OrderAppliedFor = new OcrDataField();
        ocrDataChild2OrderAppliedFor.setName("child2_orderAppliedFor");
        ocrDataChild2OrderAppliedFor.setValue("child2orderAppliedFor");
        fieldList.add(ocrDataChild2OrderAppliedFor);

        OcrDataField ocrDataChild2Applicant1Relation = new OcrDataField();
        ocrDataChild2Applicant1Relation.setName("child2_applicant1_relationship");
        ocrDataChild2Applicant1Relation.setValue("applicant");
        fieldList.add(ocrDataChild2Applicant1Relation);

        OcrDataField ocrDataChild2Respondent1Relation = new OcrDataField();
        ocrDataChild2Respondent1Relation.setName("child2_respondent1_relationship");
        ocrDataChild2Respondent1Relation.setValue("respondent");
        fieldList.add(ocrDataChild2Respondent1Relation);

        OcrDataField ocrDataChild2LocalAuthoritySocialWorker = new OcrDataField();
        ocrDataChild2LocalAuthoritySocialWorker.setName("child2_localAuthority_or_socialWorker");
        ocrDataChild2LocalAuthoritySocialWorker.setValue("socialWorker");
        fieldList.add(ocrDataChild2LocalAuthoritySocialWorker);

        return fieldList;
    }

    public static List<OcrDataField> getChild3NamesRelationSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataChild3FirstName = new OcrDataField();
        ocrDataChild3FirstName.setName("child3_firstName");
        ocrDataChild3FirstName.setValue("child3firstName");
        fieldList.add(ocrDataChild3FirstName);

        OcrDataField ocrDataChild3LastName = new OcrDataField();
        ocrDataChild3LastName.setName("child3_lastName");
        ocrDataChild3LastName.setValue("child3LastName");
        fieldList.add(ocrDataChild3LastName);

        OcrDataField ocrDataChild3DateOfBirth = new OcrDataField();
        ocrDataChild3DateOfBirth.setName("child3_dateOfBirth");
        ocrDataChild3DateOfBirth.setValue("02/02/2020");
        fieldList.add(ocrDataChild3DateOfBirth);

        OcrDataField ocrDataChild3GenderField = new OcrDataField();
        ocrDataChild3GenderField.setName("child3_gender");
        ocrDataChild3GenderField.setValue("Male");
        fieldList.add(ocrDataChild3GenderField);

        OcrDataField ocrDataChild3OrderAppliedFor = new OcrDataField();
        ocrDataChild3OrderAppliedFor.setName("child3_orderAppliedFor");
        ocrDataChild3OrderAppliedFor.setValue("child3orderAppliedFor");
        fieldList.add(ocrDataChild3OrderAppliedFor);

        OcrDataField ocrDataChild3Applicant1Relation = new OcrDataField();
        ocrDataChild3Applicant1Relation.setName("child3_applicant1_relationship");
        ocrDataChild3Applicant1Relation.setValue("applicant");
        fieldList.add(ocrDataChild3Applicant1Relation);

        OcrDataField ocrDataChild3Respondent1Relation = new OcrDataField();
        ocrDataChild3Respondent1Relation.setName("child3_respondent1_relationship");
        ocrDataChild3Respondent1Relation.setValue("respondent");
        fieldList.add(ocrDataChild3Respondent1Relation);

        OcrDataField ocrDataChild3LocalAuthoritySocialWorker = new OcrDataField();
        ocrDataChild3LocalAuthoritySocialWorker.setName("child3_localAuthority_or_socialWorker");
        ocrDataChild3LocalAuthoritySocialWorker.setValue("socialWorker");
        fieldList.add(ocrDataChild3LocalAuthoritySocialWorker);

        return fieldList;
    }

    public static List<OcrDataField> getChild4NamesRelationSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataChild4FirstName = new OcrDataField();
        ocrDataChild4FirstName.setName("child4_firstName");
        ocrDataChild4FirstName.setValue("child4firstName");
        fieldList.add(ocrDataChild4FirstName);

        OcrDataField ocrDataChild4LastName = new OcrDataField();
        ocrDataChild4LastName.setName("child4_lastName");
        ocrDataChild4LastName.setValue("child4LastName");
        fieldList.add(ocrDataChild4LastName);

        OcrDataField ocrDataChild4DateOfBirth = new OcrDataField();
        ocrDataChild4DateOfBirth.setName("child4_dateOfBirth");
        ocrDataChild4DateOfBirth.setValue("02/02/2020");
        fieldList.add(ocrDataChild4DateOfBirth);

        OcrDataField ocrDataChild4GenderField = new OcrDataField();
        ocrDataChild4GenderField.setName("child4_gender");
        ocrDataChild4GenderField.setValue("Male");
        fieldList.add(ocrDataChild4GenderField);

        OcrDataField ocrDataChild4OrderAppliedFor = new OcrDataField();
        ocrDataChild4OrderAppliedFor.setName("child4_orderAppliedFor");
        ocrDataChild4OrderAppliedFor.setValue("child4orderAppliedFor");
        fieldList.add(ocrDataChild4OrderAppliedFor);

        OcrDataField ocrDataChild4Applicant1Relation = new OcrDataField();
        ocrDataChild4Applicant1Relation.setName("child4_applicant1_relationship");
        ocrDataChild4Applicant1Relation.setValue("applicant");
        fieldList.add(ocrDataChild4Applicant1Relation);

        OcrDataField ocrDataChild4Respondent1Relation = new OcrDataField();
        ocrDataChild4Respondent1Relation.setName("child4_respondent1_relationship");
        ocrDataChild4Respondent1Relation.setValue("respondent");
        fieldList.add(ocrDataChild4Respondent1Relation);

        OcrDataField ocrDataChild4LocalAuthoritySocialWorker = new OcrDataField();
        ocrDataChild4LocalAuthoritySocialWorker.setName("child4_localAuthority_or_socialWorker");
        ocrDataChild4LocalAuthoritySocialWorker.setValue("socialWorker");
        fieldList.add(ocrDataChild4LocalAuthoritySocialWorker);

        return fieldList;
    }

    public static List<OcrDataField> getExemptionToAttendWarningData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrExemptionToAttendMiamField = new OcrDataField();
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM);
        ocrExemptionToAttendMiamField.setValue(TICK_BOX_YES);
        fieldList.add(ocrExemptionToAttendMiamField);

        OcrDataField ocrNoMiamDomesticViolenceOrderField = new OcrDataField();
        ocrNoMiamDomesticViolenceOrderField.setName(NOMIAM_DOMESTICVIOLENCE_FIELD);
        ocrNoMiamDomesticViolenceOrderField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamDomesticViolenceOrderField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS_FIELD);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY_FIELD);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE_FIELD);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS_FIELD);
        ocrNoMiamotherReasonsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamotherReasonsField);

        return fieldList;
    }

    public static List<OcrDataField> getNoMiamDomesticWarningData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrExemptionToAttendMiamField = new OcrDataField();
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM);
        ocrExemptionToAttendMiamField.setValue(TICK_BOX_YES);
        fieldList.add(ocrExemptionToAttendMiamField);

        OcrDataField ocrNoMiamdomesticViolenceField = new OcrDataField();
        ocrNoMiamdomesticViolenceField.setName(NOMIAM_DOMESTICVIOLENCE_FIELD);
        ocrNoMiamdomesticViolenceField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamdomesticViolenceField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS_FIELD);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY_FIELD);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE_FIELD);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS_FIELD);
        ocrNoMiamotherReasonsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamotherReasonsField);

        return fieldList;
    }

    public static List<OcrDataField> getExemptionDependentNoMiamFieldWarningsData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrExemptionToAttendMiamField = new OcrDataField();
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM);
        ocrExemptionToAttendMiamField.setValue(TICK_BOX_YES);
        fieldList.add(ocrExemptionToAttendMiamField);

        OcrDataField ocrNoMiamdomesticViolenceField = new OcrDataField();
        ocrNoMiamdomesticViolenceField.setName(NOMIAM_DOMESTICVIOLENCE_FIELD);
        ocrNoMiamdomesticViolenceField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamdomesticViolenceField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS_FIELD);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY_FIELD);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE_FIELD);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS_FIELD);
        ocrNoMiamotherReasonsField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamotherReasonsField);

        return fieldList;
    }

    public static List<OcrDataField> getExemptionToAttendMiamSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrExemptionToAttendMiamField = new OcrDataField();
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM);
        ocrExemptionToAttendMiamField.setValue(TICK_BOX_YES);
        fieldList.add(ocrExemptionToAttendMiamField);

        OcrDataField ocrNoMiamDomesticViolenceOrderField = new OcrDataField();
        ocrNoMiamDomesticViolenceOrderField.setName(NOMIAM_DOMESTICVIOLENCE_FIELD);
        ocrNoMiamDomesticViolenceOrderField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamDomesticViolenceOrderField);

        OcrDataField ocrNoMiamArrestSimilaOffenceOrderField = new OcrDataField();
        ocrNoMiamArrestSimilaOffenceOrderField.setName(NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE_FIELD);
        ocrNoMiamArrestSimilaOffenceOrderField.setValue(TICK_BOX_YES);
        fieldList.add(ocrNoMiamArrestSimilaOffenceOrderField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS_FIELD);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY_FIELD);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE_FIELD);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS_FIELD);
        ocrNoMiamotherReasonsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamotherReasonsField);

        return fieldList;
    }

    public static List<OcrDataField> getExemptionToAttendMiamDependentSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrExemptionToAttendMiamField = new OcrDataField();
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM);
        ocrExemptionToAttendMiamField.setValue(TICK_BOX_YES);
        fieldList.add(ocrExemptionToAttendMiamField);

        OcrDataField ocrNoMiamDomesticViolenceOrderField = new OcrDataField();
        ocrNoMiamDomesticViolenceOrderField.setName(NOMIAM_DOMESTICVIOLENCE_FIELD);
        ocrNoMiamDomesticViolenceOrderField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamDomesticViolenceOrderField);

        OcrDataField ocrNoMiamArrestSimilaOffenceOrderField = new OcrDataField();
        ocrNoMiamArrestSimilaOffenceOrderField.setName(NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE_FIELD);
        ocrNoMiamArrestSimilaOffenceOrderField.setValue(TICK_BOX_YES);
        fieldList.add(ocrNoMiamArrestSimilaOffenceOrderField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS_FIELD);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamSubjectOfEnquiriesField = new OcrDataField();
        ocrNoMiamSubjectOfEnquiriesField.setName("NoMIAM_subjectOfEnquiries_byLocalAuthority");
        ocrNoMiamSubjectOfEnquiriesField.setValue(TICK_BOX_YES);
        fieldList.add(ocrNoMiamSubjectOfEnquiriesField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY_FIELD);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamUrgencyRiskOfHarmField = new OcrDataField();
        ocrNoMiamUrgencyRiskOfHarmField.setName("NoMIAM_urgency_riskOfHarm");
        ocrNoMiamUrgencyRiskOfHarmField.setValue(TICK_BOX_YES);
        fieldList.add(ocrNoMiamUrgencyRiskOfHarmField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE_FIELD);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamPreviousAttendanceReasonField = new OcrDataField();
        ocrNoMiamPreviousAttendanceReasonField.setName("NoMIAM_PreviousAttendanceReason");
        ocrNoMiamPreviousAttendanceReasonField.setValue(TICK_BOX_YES);
        fieldList.add(ocrNoMiamPreviousAttendanceReasonField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS_FIELD);
        ocrNoMiamotherReasonsField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamotherReasonsField);

        OcrDataField ocrNoMiamOtherExceptionsField = new OcrDataField();
        ocrNoMiamOtherExceptionsField.setName("NoMIAM_otherExceptions");
        ocrNoMiamOtherExceptionsField.setValue(TICK_BOX_YES);
        fieldList.add(ocrNoMiamOtherExceptionsField);

        return fieldList;
    }

    public static List<OcrDataField> getRespondent1Details() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrrespondent1FirstName = new OcrDataField();
        ocrrespondent1FirstName.setName("respondent1FirstName");
        ocrrespondent1FirstName.setValue("respondent1_FirstName");
        fieldList.add(ocrrespondent1FirstName);

        OcrDataField ocrrespondent1LastName = new OcrDataField();
        ocrrespondent1LastName.setName("respondent1LastName");
        ocrrespondent1LastName.setValue("respondent1_LastName");
        fieldList.add(ocrrespondent1LastName);

        OcrDataField ocrrespondent1PreviousNames = new OcrDataField();
        ocrrespondent1PreviousNames.setName("respondent1PreviousNames");
        ocrrespondent1PreviousNames.setValue("respondent1_PreviousNames");
        fieldList.add(ocrrespondent1PreviousNames);

        OcrDataField ocrrespondent1Gender = new OcrDataField();
        ocrrespondent1Gender.setName("respondent1Gender");
        ocrrespondent1Gender.setValue("Yes");
        fieldList.add(ocrrespondent1Gender);

        OcrDataField ocrrespondent1DateOfBirth = new OcrDataField();
        ocrrespondent1DateOfBirth.setName("respondent1DateOfBirth");
        ocrrespondent1DateOfBirth.setValue("20/02/2002");
        fieldList.add(ocrrespondent1DateOfBirth);

        OcrDataField ocrrespondent1DateOfBirthDontKnow = new OcrDataField();
        ocrrespondent1DateOfBirthDontKnow.setName("respondent1DateOfBirthDontKnow");
        ocrrespondent1DateOfBirthDontKnow.setValue("");
        fieldList.add(ocrrespondent1DateOfBirthDontKnow);

        OcrDataField ocrrespondent1PlaceOfBirth = new OcrDataField();
        ocrrespondent1PlaceOfBirth.setName("respondent1PlaceOfBirth");
        ocrrespondent1PlaceOfBirth.setValue("London");
        fieldList.add(ocrrespondent1PlaceOfBirth);

        OcrDataField ocrrespondent1Address = new OcrDataField();
        ocrrespondent1Address.setName("respondent1Address");
        ocrrespondent1Address.setValue("123, Topsfield Parade");
        fieldList.add(ocrrespondent1Address);

        OcrDataField ocrrespondent1AddressPostCode = new OcrDataField();
        ocrrespondent1AddressPostCode.setName("respondent1AddressPostCode");
        ocrrespondent1AddressPostCode.setValue("N8 8PE");
        fieldList.add(ocrrespondent1AddressPostCode);

        OcrDataField ocrrespondent1AddressDontKnow = new OcrDataField();
        ocrrespondent1AddressDontKnow.setName("respondent1AddressDontKnow");
        ocrrespondent1AddressDontKnow.setValue("");
        fieldList.add(ocrrespondent1AddressDontKnow);

        OcrDataField ocrrespondent1HomePhoneNumber = new OcrDataField();
        ocrrespondent1HomePhoneNumber.setName("respondent1HomePhoneNumber");
        ocrrespondent1HomePhoneNumber.setValue("+442083332222");
        fieldList.add(ocrrespondent1HomePhoneNumber);

        OcrDataField ocrrespondent1MobilePhoneNumber = new OcrDataField();
        ocrrespondent1MobilePhoneNumber.setName("respondent1MobilePhoneNumber");
        ocrrespondent1MobilePhoneNumber.setValue("+447999444444");
        fieldList.add(ocrrespondent1MobilePhoneNumber);

        OcrDataField ocrrespondent1MobilePhoneNumberDontKnow = new OcrDataField();
        ocrrespondent1MobilePhoneNumberDontKnow.setName("respondent1MobilePhoneNumberDontKnow");
        ocrrespondent1MobilePhoneNumberDontKnow.setValue("");
        fieldList.add(ocrrespondent1MobilePhoneNumberDontKnow);

        OcrDataField ocrrespondent1EmailAddress = new OcrDataField();
        ocrrespondent1EmailAddress.setName("respondent1EmailAddress");
        ocrrespondent1EmailAddress.setValue("test@test.com");
        fieldList.add(ocrrespondent1EmailAddress);

        OcrDataField ocrrespondent1EmailAddressDontKnow = new OcrDataField();
        ocrrespondent1EmailAddressDontKnow.setName("respondent1EmailAddressDontKnow");
        ocrrespondent1EmailAddressDontKnow.setValue("");
        fieldList.add(ocrrespondent1EmailAddressDontKnow);

        OcrDataField ocrRespondent1LivedAtThisAddressForOverFiveYears = new OcrDataField();
        ocrRespondent1LivedAtThisAddressForOverFiveYears.setName(
                "respondent1LivedAtThisAddressForOverFiveYears");
        ocrRespondent1LivedAtThisAddressForOverFiveYears.setValue("No");
        fieldList.add(ocrRespondent1LivedAtThisAddressForOverFiveYears);

        OcrDataField ocrrespondent1AllAddressesForLastFiveYears = new OcrDataField();
        ocrrespondent1AllAddressesForLastFiveYears.setName(
                "respondent1AllAddressesForLastFiveYears");
        ocrrespondent1AllAddressesForLastFiveYears.setValue("225, Topsfield Parade, N8 8PE");
        fieldList.add(ocrrespondent1AllAddressesForLastFiveYears);

        return fieldList;
    }

    public static List<OcrDataField> getRespondent2Details() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrrespondent2FirstName = new OcrDataField();
        ocrrespondent2FirstName.setName("respondent2FirstName");
        ocrrespondent2FirstName.setValue("respondent2_FirstName");
        fieldList.add(ocrrespondent2FirstName);

        OcrDataField ocrrespondent2LastName = new OcrDataField();
        ocrrespondent2LastName.setName("respondent2LastName");
        ocrrespondent2LastName.setValue("respondent2_LastName");
        fieldList.add(ocrrespondent2LastName);

        OcrDataField ocrrespondent2PreviousNames = new OcrDataField();
        ocrrespondent2PreviousNames.setName("respondent2PreviousNames");
        ocrrespondent2PreviousNames.setValue("respondent2_PreviousNames");
        fieldList.add(ocrrespondent2PreviousNames);

        OcrDataField ocrrespondent2Gender = new OcrDataField();
        ocrrespondent2Gender.setName("respondent2Gender");
        ocrrespondent2Gender.setValue("Yes");
        fieldList.add(ocrrespondent2Gender);

        OcrDataField ocrrespondent2DateOfBirth = new OcrDataField();
        ocrrespondent2DateOfBirth.setName("respondent2DateOfBirth");
        ocrrespondent2DateOfBirth.setValue("21/02/2002");
        fieldList.add(ocrrespondent2DateOfBirth);

        OcrDataField ocrrespondent2DateOfBirthDontKnow = new OcrDataField();
        ocrrespondent2DateOfBirthDontKnow.setName("respondent2DateOfBirthDontKnow");
        ocrrespondent2DateOfBirthDontKnow.setValue("");
        fieldList.add(ocrrespondent2DateOfBirthDontKnow);

        OcrDataField ocrrespondent2PlaceOfBirth = new OcrDataField();
        ocrrespondent2PlaceOfBirth.setName("respondent2PlaceOfBirth");
        ocrrespondent2PlaceOfBirth.setValue("London");
        fieldList.add(ocrrespondent2PlaceOfBirth);

        OcrDataField ocrrespondent2Address = new OcrDataField();
        ocrrespondent2Address.setName("respondent2Address");
        ocrrespondent2Address.setValue("124, Topsfield Parade");
        fieldList.add(ocrrespondent2Address);

        OcrDataField ocrrespondent2AddressPostCode = new OcrDataField();
        ocrrespondent2AddressPostCode.setName("respondent2AddressPostCode");
        ocrrespondent2AddressPostCode.setValue("N8 8PE");
        fieldList.add(ocrrespondent2AddressPostCode);

        OcrDataField ocrrespondent2AddressDontKnow = new OcrDataField();
        ocrrespondent2AddressDontKnow.setName("respondent2AddressDontKnow");
        ocrrespondent2AddressDontKnow.setValue("");
        fieldList.add(ocrrespondent2AddressDontKnow);

        OcrDataField ocrrespondent2HomePhoneNumber = new OcrDataField();
        ocrrespondent2HomePhoneNumber.setName("respondent2HomePhoneNumber");
        ocrrespondent2HomePhoneNumber.setValue("+442081112222");
        fieldList.add(ocrrespondent2HomePhoneNumber);

        OcrDataField ocrrespondent2MobilePhoneNumber = new OcrDataField();
        ocrrespondent2MobilePhoneNumber.setName("respondent2MobilePhoneNumber");
        ocrrespondent2MobilePhoneNumber.setValue("+447999666666");
        fieldList.add(ocrrespondent2MobilePhoneNumber);

        OcrDataField ocrrespondent2MobilePhoneNumberDontKnow = new OcrDataField();
        ocrrespondent2MobilePhoneNumberDontKnow.setName("respondent2MobilePhoneNumberDontKnow");
        ocrrespondent2MobilePhoneNumberDontKnow.setValue("");
        fieldList.add(ocrrespondent2MobilePhoneNumberDontKnow);

        OcrDataField ocrrespondent2EmailAddress = new OcrDataField();
        ocrrespondent2EmailAddress.setName("respondent2EmailAddress");
        ocrrespondent2EmailAddress.setValue("test@test.com");
        fieldList.add(ocrrespondent2EmailAddress);

        OcrDataField ocrrespondent2EmailAddressDontKnow = new OcrDataField();
        ocrrespondent2EmailAddressDontKnow.setName("respondent2EmailAddressDontKnow");
        ocrrespondent2EmailAddressDontKnow.setValue("");
        fieldList.add(ocrrespondent2EmailAddressDontKnow);

        OcrDataField ocrRespondent2LivedAtThisAddressForOverFiveYears = new OcrDataField();
        ocrRespondent2LivedAtThisAddressForOverFiveYears.setName(
                "respondent2LivedAtThisAddressForOverFiveYears");
        ocrRespondent2LivedAtThisAddressForOverFiveYears.setValue("No");
        fieldList.add(ocrRespondent2LivedAtThisAddressForOverFiveYears);

        OcrDataField ocrrespondent2AllAddressesForLastFiveYears = new OcrDataField();
        ocrrespondent2AllAddressesForLastFiveYears.setName(
                "respondent2AllAddressesForLastFiveYears");
        ocrrespondent2AllAddressesForLastFiveYears.setValue("227, Topsfield Parade, N8 8PE");
        fieldList.add(ocrrespondent2AllAddressesForLastFiveYears);

        return fieldList;
    }

    public static List<OcrDataField> getLitigationCapacityGroupSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrFactorsAffectingLitigationField = new OcrDataField();
        ocrFactorsAffectingLitigationField.setName(
                INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD);
        ocrFactorsAffectingLitigationField.setValue(TICK_BOX_YES);
        fieldList.add(ocrFactorsAffectingLitigationField);

        OcrDataField ocrInternationalJurisdictionField = new OcrDataField();
        ocrInternationalJurisdictionField.setName(INTERNATIONALELEMENT_JURISDICTIONISSUE);
        ocrInternationalJurisdictionField.setValue(TICK_BOX_YES);
        fieldList.add(ocrInternationalJurisdictionField);

        OcrDataField ocrInternationalJurisdictionDetails = new OcrDataField();
        ocrInternationalJurisdictionDetails.setName(WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS);
        ocrInternationalJurisdictionDetails.setValue("International jurisdiction big issue");
        fieldList.add(ocrInternationalJurisdictionDetails);

        OcrDataField ocrResidentOfAnotherStateField = new OcrDataField();
        ocrResidentOfAnotherStateField.setName(INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE);
        ocrResidentOfAnotherStateField.setValue(TICK_BOX_NO);
        fieldList.add(ocrResidentOfAnotherStateField);

        OcrDataField ocrResidentOfAnotherStateDetails = new OcrDataField();
        ocrResidentOfAnotherStateDetails.setName(
                INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS);
        ocrResidentOfAnotherStateDetails.setValue("");
        fieldList.add(ocrResidentOfAnotherStateDetails);

        OcrDataField ocrRequestCentralConsularAuthField = new OcrDataField();
        ocrRequestCentralConsularAuthField.setName(
                INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH);
        ocrRequestCentralConsularAuthField.setValue("");
        fieldList.add(ocrRequestCentralConsularAuthField);

        OcrDataField ocrRequestCentralConsularAuthDetails = new OcrDataField();
        ocrRequestCentralConsularAuthDetails.setName(
                INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS);
        ocrRequestCentralConsularAuthDetails.setValue("");
        fieldList.add(ocrRequestCentralConsularAuthDetails);

        OcrDataField ocrFactorsAffectingLitigationCapacityField = new OcrDataField();
        ocrFactorsAffectingLitigationCapacityField.setName(
                FACTORS_AFFECTING_LITIGATION_CAPACITY_FIELD);
        ocrFactorsAffectingLitigationCapacityField.setValue(
                "Factors affecting litigation capacity");
        fieldList.add(ocrFactorsAffectingLitigationCapacityField);

        OcrDataField ocrAssessmentByAdultLearningTeamField = new OcrDataField();
        ocrAssessmentByAdultLearningTeamField.setName(ASSESSMENT_BY_ADULT_LEARNING_TEAM_FIELD);
        ocrAssessmentByAdultLearningTeamField.setValue("Assessment by adult learning team");
        fieldList.add(ocrAssessmentByAdultLearningTeamField);

        OcrDataField ocrFfactorsAffectingPersonInCourtField = new OcrDataField();
        ocrFfactorsAffectingPersonInCourtField.setName(FACTORS_AFFECTING_PERSON_IN_COURT_FIELD);
        ocrFfactorsAffectingPersonInCourtField.setValue("Factors affecting person in court");
        fieldList.add(ocrFfactorsAffectingPersonInCourtField);

        return fieldList;
    }
}
