package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_CHILDPROTECTIONCONCERNS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_DOMESTICVIOLENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_OTHERREASONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_PREVIOUSATTENDANCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_URGENCY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_FALSE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_YES;

public final class TestDataC100Util {

    public static final String FIRST_NAME = "firstName";
    public static final String DATE_OF_BIRTH = "2/02/2022";
    public static final String POST_CODE = "TW3 1NN";
    public static final String ADDRESS = "123 test street, London";
    public static final String PHONE_NUMBER = "+447405878672";

    private TestDataC100Util() {

    }

    public static List<OcrDataField> getData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrEmailField = new OcrDataField();
        ocrEmailField.setName("applicant_email");
        ocrEmailField.setValue("test@test.com");
        fieldList.add(ocrEmailField);

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("applicant_case_number");
        ocrNumericField.setValue("1311231231");
        fieldList.add(ocrNumericField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_dateOfBirth");
        ocrDateField.setValue("2/02/2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrDateOtherCourtCaseDateField = new OcrDataField();
        ocrDateOtherCourtCaseDateField.setName("other_court_case_date");
        ocrDateOtherCourtCaseDateField.setValue("2/02/2022");
        fieldList.add(ocrDateOtherCourtCaseDateField);

        OcrDataField ocrDateAuthorisedFamilyMediatorSignedDateField = new OcrDataField();
        ocrDateAuthorisedFamilyMediatorSignedDateField.setName("authorised_family_mediator_signed_date");
        ocrDateAuthorisedFamilyMediatorSignedDateField.setValue("2/02/2022");
        fieldList.add(ocrDateAuthorisedFamilyMediatorSignedDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postCode");
        ocrPostCodeField.setValue(POST_CODE);
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_contactNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        fieldList.addAll(getAllNamesRelationSuccessData());
        fieldList.addAll(getExemptionToAttendMiamSuccessData());

        return fieldList;
    }

    public static List<OcrDataField> getAllNamesRelationSuccessData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDataChildFirstNameField = new OcrDataField();
        ocrDataChildFirstNameField.setName("child_firstName");
        ocrDataChildFirstNameField.setValue("LastName");
        fieldList.add(ocrDataChildFirstNameField);

        OcrDataField ocrDataChildLastNameField = new OcrDataField();
        ocrDataChildLastNameField.setName("child_lastName");
        ocrDataChildLastNameField.setValue("LastName");
        fieldList.add(ocrDataChildLastNameField);

        OcrDataField ocrDataApplicantChildRelationshipField = new OcrDataField();
        ocrDataApplicantChildRelationshipField.setName("applicant_childRelationship");
        ocrDataApplicantChildRelationshipField.setValue("LastName");
        fieldList.add(ocrDataApplicantChildRelationshipField);

        OcrDataField ocrDataEmergencyProtectionOrder = new OcrDataField();
        ocrDataEmergencyProtectionOrder.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrder.setValue("7777777");
        fieldList.add(ocrDataEmergencyProtectionOrder);

        OcrDataField ocrApplicantRelationToChildField = new OcrDataField();
        ocrApplicantRelationToChildField.setName("applicant_relationToChild");
        ocrApplicantRelationToChildField.setValue("Step Father");
        fieldList.add(ocrApplicantRelationToChildField);

        return fieldList;
    }

    public static List<OcrDataField> getErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_lastName");
        ocrDataLastNameField.setValue("");

        OcrDataField ocrDataEmergencyProtectionOrder = new OcrDataField();
        ocrDataEmergencyProtectionOrder.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrder.setValue("");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("applicant_case_number");
        ocrNumericField.setValue("1311231231");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField,
                ocrDataEmergencyProtectionOrder, ocrDataLastNameField, ocrNumericField
        );
    }

    public static List<OcrDataField> getDateErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_lastName");
        ocrDataLastNameField.setValue("lastName");

        OcrDataField ocrDataChildFirstNameField = new OcrDataField();
        ocrDataChildFirstNameField.setName("child_firstName");
        ocrDataChildFirstNameField.setValue("LastName");

        OcrDataField ocrDataChildLastNameField = new OcrDataField();
        ocrDataChildLastNameField.setName("child_lastName");
        ocrDataChildLastNameField.setValue("LastName");

        OcrDataField ocrDataApplicantChildRelationshipField = new OcrDataField();
        ocrDataApplicantChildRelationshipField.setName("applicant_childRelationship");
        ocrDataApplicantChildRelationshipField.setValue("LastName");

        OcrDataField ocrDataEmergencyProtectionOrderField = new OcrDataField();
        ocrDataEmergencyProtectionOrderField.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrderField.setValue("LastName");

        OcrDataField ocrDataDateField = new OcrDataField();
        ocrDataDateField.setName("applicant_dateOfBirth");
        ocrDataDateField.setValue("testdate");

        OcrDataField ocrDataOtherCourtCaseDateField = new OcrDataField();
        ocrDataOtherCourtCaseDateField.setName("other_court_case_date");
        ocrDataOtherCourtCaseDateField.setValue("testdate");

        OcrDataField ocrDataAuthorisedFamilyMediatorSignedDateField = new OcrDataField();
        ocrDataAuthorisedFamilyMediatorSignedDateField.setName("authorised_family_mediator_signed_date");
        ocrDataAuthorisedFamilyMediatorSignedDateField.setValue("testdate");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrDataPostCodeField = new OcrDataField();
        ocrDataPostCodeField.setName("applicant_postCode");
        ocrDataPostCodeField.setValue(POST_CODE);

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("applicant_case_number");
        ocrNumericField.setValue("1311231231");


        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField,
                ocrDataChildFirstNameField, ocrDataChildLastNameField,
                ocrDataApplicantChildRelationshipField,
                ocrDataEmergencyProtectionOrderField, ocrDataPostCodeField,
                ocrDataDateField, ocrDataOtherCourtCaseDateField,
                ocrDataAuthorisedFamilyMediatorSignedDateField, ocrNumericField
        );
    }

    public static List<OcrDataField> getEmailErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_lastName");
        ocrDataLastNameField.setValue("lastName");

        OcrDataField ocrDataChildFirstNameField = new OcrDataField();
        ocrDataChildFirstNameField.setName("child_firstName");
        ocrDataChildFirstNameField.setValue("LastName");

        OcrDataField ocrDataChildLastNameField = new OcrDataField();
        ocrDataChildLastNameField.setName("child_lastName");
        ocrDataChildLastNameField.setValue("LastName");

        OcrDataField ocrDataApplicantChildRelationshipField = new OcrDataField();
        ocrDataApplicantChildRelationshipField.setName("applicant_childRelationship");
        ocrDataApplicantChildRelationshipField.setValue("LastName");

        OcrDataField ocrDataEmergencyProtectionOrderField = new OcrDataField();
        ocrDataEmergencyProtectionOrderField.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrderField.setValue("LastName");

        OcrDataField ocrDataPostCodeField = new OcrDataField();
        ocrDataPostCodeField.setName("applicant_postCode");
        ocrDataPostCodeField.setValue(POST_CODE);

        OcrDataField ocrDataEmailField = new OcrDataField();
        ocrDataEmailField.setName("applicant_email");
        ocrDataEmailField.setValue("firstName");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("applicant_case_number");
        ocrNumericField.setValue("1311231231");

        OcrDataField ocrSolicitorFaxNumberField = new OcrDataField();
        ocrSolicitorFaxNumberField.setName("solicitor_fax_number");
        ocrSolicitorFaxNumberField.setValue("131123");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField,
                ocrDataChildFirstNameField, ocrDataChildLastNameField,
                ocrDataApplicantChildRelationshipField, ocrDataEmergencyProtectionOrderField,
                ocrDataEmailField, ocrDataPostCodeField, ocrNumericField, ocrSolicitorFaxNumberField
        );
    }

    public static List<OcrDataField> getNumericErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_lastName");
        ocrDataLastNameField.setValue("lastName");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("case_no");
        ocrNumericField.setValue("should_be_numeric");

        return Arrays.asList(ocrDataFirstNameField, ocrDataLastNameField,
                ocrNumericField
        );
    }

    public static List<OcrDataField> getFirstNameData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_firstName");
        ocrDataFirstNameField.setValue("firstName");


        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("applicant_case_number");
        ocrNumericField.setValue("1311231231");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField,
                ocrNumericField
        );
    }


    public static List<OcrDataField> getExemptionToAttendWarningData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrExemptionToAttendMiamField = new OcrDataField();
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD);
        ocrExemptionToAttendMiamField.setValue(TICK_BOX_YES);
        fieldList.add(ocrExemptionToAttendMiamField);

        OcrDataField ocrNoMiamDomesticViolenceOrderField = new OcrDataField();
        ocrNoMiamDomesticViolenceOrderField.setName(NOMIAM_DOMESTICVIOLENCE);
        ocrNoMiamDomesticViolenceOrderField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamDomesticViolenceOrderField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS);
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
        ocrNoMiamdomesticViolenceField.setName(NOMIAM_DOMESTICVIOLENCE);
        ocrNoMiamdomesticViolenceField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamdomesticViolenceField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS);
        ocrNoMiamotherReasonsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamotherReasonsField);

        return fieldList;
    }

    public static List<OcrDataField> getExemptionDependentNoMiamFieldWarningData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrExemptionToAttendMiamField = new OcrDataField();
        ocrExemptionToAttendMiamField.setName(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD);
        ocrExemptionToAttendMiamField.setValue(TICK_BOX_YES);
        fieldList.add(ocrExemptionToAttendMiamField);

        OcrDataField ocrNoMiamdomesticViolenceField = new OcrDataField();
        ocrNoMiamdomesticViolenceField.setName(NOMIAM_DOMESTICVIOLENCE);
        ocrNoMiamdomesticViolenceField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamdomesticViolenceField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS);
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
        ocrNoMiamDomesticViolenceOrderField.setName(NOMIAM_DOMESTICVIOLENCE);
        ocrNoMiamDomesticViolenceOrderField.setValue(TICK_BOX_TRUE);
        fieldList.add(ocrNoMiamDomesticViolenceOrderField);

        OcrDataField ocrNoMiamArrestSimilaOffenceOrderField = new OcrDataField();
        ocrNoMiamArrestSimilaOffenceOrderField.setName(NOMIAM_DVE_ARRESTEDFORSIMILAROFFENCE);
        ocrNoMiamArrestSimilaOffenceOrderField.setValue(TICK_BOX_YES);
        fieldList.add(ocrNoMiamArrestSimilaOffenceOrderField);

        OcrDataField ocrNoMiamchildProtectionConcernsField = new OcrDataField();
        ocrNoMiamchildProtectionConcernsField.setName(NOMIAM_CHILDPROTECTIONCONCERNS);
        ocrNoMiamchildProtectionConcernsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamchildProtectionConcernsField);

        OcrDataField ocrNoMiamUrgencyField = new OcrDataField();
        ocrNoMiamUrgencyField.setName(NOMIAM_URGENCY);
        ocrNoMiamUrgencyField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamUrgencyField);

        OcrDataField ocrNoMiamPreviousAttendanceField = new OcrDataField();
        ocrNoMiamPreviousAttendanceField.setName(NOMIAM_PREVIOUSATTENDANCE);
        ocrNoMiamPreviousAttendanceField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamPreviousAttendanceField);

        OcrDataField ocrNoMiamotherReasonsField = new OcrDataField();
        ocrNoMiamotherReasonsField.setName(NOMIAM_OTHERREASONS);
        ocrNoMiamotherReasonsField.setValue(TICK_BOX_FALSE);
        fieldList.add(ocrNoMiamotherReasonsField);

        return fieldList;
    }
}