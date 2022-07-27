package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_CHILDPROTECTIONCONCERNS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_DOMESTICVIOLENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_OTHERREASONS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_PREVIOUSATTENDANCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_URGENCY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_FALSE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_YES;

public final class TestDataC100Util {

    public static final String POST_CODE = "TW3 1NN";

    private TestDataC100Util() {

    }

    public static List<OcrDataField> getData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        fieldList.addAll(getAllNamesRelationSuccessData());
        fieldList.addAll(getExemptionToAttendMiamSuccessData());

        return fieldList;
    }

    public static List<OcrDataField> getAllNamesRelationSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataPreviousOrOngoingProceedingField = new OcrDataField();
        ocrDataPreviousOrOngoingProceedingField.setName("previous_or_ongoingProceeding");
        ocrDataPreviousOrOngoingProceedingField.setValue(TICK_BOX_NO);
        fieldList.add(ocrDataPreviousOrOngoingProceedingField);

        OcrDataField ocrDataExistingCaseEmergencyProtectionField = new OcrDataField();
        ocrDataExistingCaseEmergencyProtectionField
                .setName("existingCase_onEmergencyProtection_Care_or_supervisioNorder");
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

    public static List<OcrDataField> getApplicantSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataApplicant1FirstName = new OcrDataField();
        ocrDataApplicant1FirstName.setName("applicant1_firstName");
        ocrDataApplicant1FirstName.setValue("John");
        fieldList.add(ocrDataApplicant1FirstName);

        OcrDataField ocrDataApplicant1LastName = new OcrDataField();
        ocrDataApplicant1LastName.setName("applicant1_lastName");
        ocrDataApplicant1LastName.setValue("Peters");
        fieldList.add(ocrDataApplicant1LastName);

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
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD);
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
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD);
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
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD);
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
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD);
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
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD);
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
}