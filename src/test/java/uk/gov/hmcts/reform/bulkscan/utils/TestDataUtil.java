package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static List<OcrDataField> getData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
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

        OcrDataField ocrDataAppellantChildRelationshipField = new OcrDataField();
        ocrDataAppellantChildRelationshipField.setName("appellant_childRelationship");
        ocrDataAppellantChildRelationshipField.setValue("LastName");
        fieldList.add(ocrDataAppellantChildRelationshipField);

        OcrDataField ocrDataEmergencyProtectionOrder = new OcrDataField();
        ocrDataEmergencyProtectionOrder.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrder.setValue("7777777");
        fieldList.add(ocrDataEmergencyProtectionOrder);

        OcrDataField ocrEmailField = new OcrDataField();
        ocrEmailField.setName("appellant_email");
        ocrEmailField.setValue("test@test.com");
        fieldList.add(ocrEmailField);

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");
        fieldList.add(ocrNumericField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("appellant_dateOfBirth");
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
        ocrPostCodeField.setName("appellant_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("appellant_contactNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("appellant_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);


        return fieldList;
    }

    public static List<OcrDataField> getErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
        ocrDataLastNameField.setValue("");

        OcrDataField ocrDataEmergencyProtectionOrder = new OcrDataField();
        ocrDataEmergencyProtectionOrder.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrder.setValue("");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField,
                             ocrDataEmergencyProtectionOrder, ocrDataLastNameField, ocrNumericField);
    }

    public static List<OcrDataField> getDateErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
        ocrDataLastNameField.setValue("lastName");

        OcrDataField ocrDataChildFirstNameField = new OcrDataField();
        ocrDataChildFirstNameField.setName("child_firstName");
        ocrDataChildFirstNameField.setValue("LastName");

        OcrDataField ocrDataChildLastNameField = new OcrDataField();
        ocrDataChildLastNameField.setName("child_lastName");
        ocrDataChildLastNameField.setValue("LastName");

        OcrDataField ocrDataAppellantChildRelationshipField = new OcrDataField();
        ocrDataAppellantChildRelationshipField.setName("appellant_childRelationship");
        ocrDataAppellantChildRelationshipField.setValue("LastName");

        OcrDataField ocrDataEmergencyProtectionOrderField = new OcrDataField();
        ocrDataEmergencyProtectionOrderField.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrderField.setValue("LastName");

        OcrDataField ocrDataDateField = new OcrDataField();
        ocrDataDateField.setName("appellant_dateOfBirth");
        ocrDataDateField.setValue("testdate");

        OcrDataField ocrDataOtherCourtCaseDateField = new OcrDataField();
        ocrDataOtherCourtCaseDateField.setName("other_court_case_date");
        ocrDataOtherCourtCaseDateField.setValue("testdate");

        OcrDataField ocrDataAuthorisedFamilyMediatorSignedDateField = new OcrDataField();
        ocrDataAuthorisedFamilyMediatorSignedDateField.setName("authorised_family_mediator_signed_date");
        ocrDataAuthorisedFamilyMediatorSignedDateField.setValue("testdate");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");


        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField,
                             ocrDataChildFirstNameField, ocrDataChildLastNameField,
                             ocrDataAppellantChildRelationshipField,
                             ocrDataEmergencyProtectionOrderField,
                             ocrDataDateField, ocrDataOtherCourtCaseDateField,
                             ocrDataAuthorisedFamilyMediatorSignedDateField, ocrNumericField);
    }

    public static List<OcrDataField> getEmailErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
        ocrDataLastNameField.setValue("lastName");

        OcrDataField ocrDataChildFirstNameField = new OcrDataField();
        ocrDataChildFirstNameField.setName("child_firstName");
        ocrDataChildFirstNameField.setValue("LastName");

        OcrDataField ocrDataChildLastNameField = new OcrDataField();
        ocrDataChildLastNameField.setName("child_lastName");
        ocrDataChildLastNameField.setValue("LastName");

        OcrDataField ocrDataAppellantChildRelationshipField = new OcrDataField();
        ocrDataAppellantChildRelationshipField.setName("appellant_childRelationship");
        ocrDataAppellantChildRelationshipField.setValue("LastName");

        OcrDataField ocrDataEmergencyProtectionOrderField = new OcrDataField();
        ocrDataEmergencyProtectionOrderField.setName("emergency_protection_order");
        ocrDataEmergencyProtectionOrderField.setValue("LastName");

        OcrDataField ocrDataEmailField = new OcrDataField();
        ocrDataEmailField.setName("appellant_email");
        ocrDataEmailField.setValue("firstName");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");

        OcrDataField ocrSolicitorFaxNumberField = new OcrDataField();
        ocrSolicitorFaxNumberField.setName("solicitor_fax_number");
        ocrSolicitorFaxNumberField.setValue("131123");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField,
                             ocrDataChildFirstNameField, ocrDataChildLastNameField,
                             ocrDataAppellantChildRelationshipField, ocrDataEmergencyProtectionOrderField,
                             ocrDataEmailField,
                             ocrNumericField, ocrSolicitorFaxNumberField);
    }

    public static List<OcrDataField> getNumericErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
        ocrDataLastNameField.setValue("lastName");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("case_no");
        ocrNumericField.setValue("should_be_numeric");

        return Arrays.asList(ocrDataFirstNameField, ocrDataLastNameField,
                             ocrNumericField);
    }

    public static List<OcrDataField> getFirstNameData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");


        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField,
                             ocrNumericField);
    }

    public static List<OcrDataField> getA60OrC63orA58Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant1_dateOfBirth");
        ocrDateField.setValue("2/02/2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant1_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getFgm001Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_fullName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_dateOfBirth");
        ocrDateField.setValue("2/02/2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getFgm001ErrorDobData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_fullName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_dateOfBirth");
        ocrDateField.setValue("2/02/2023");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getFgm001ErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_dateOfBirth");
        ocrDateField.setValue("2/02/2023");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getFgm001ErrorsData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_fullName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_dateOfBirth");
        ocrDateField.setValue("2/02/2023");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephoneNumber");
        ocrContactNumberField.setValue("+4474058");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }


    public static List<OcrDataField> getA60OrC63orA58ErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant1_dateOfBirth");
        ocrDateField.setValue("2-02-2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant1_postCode");
        ocrPostCodeField.setValue("testCode");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrDateField1 = new OcrDataField();
        ocrDateField1.setName("applicant2_dateOfBirth");
        ocrDateField1.setValue("2-02-2022");
        fieldList.add(ocrDateField1);

        OcrDataField ocrPostCodeField1 = new OcrDataField();
        ocrPostCodeField1.setName("applicant2_postCode");
        ocrPostCodeField1.setValue("testCode");
        fieldList.add(ocrPostCodeField1);

        OcrDataField ocrContactNumberField1 = new OcrDataField();
        ocrContactNumberField1.setName("applicant2_telephoneNumber");
        ocrContactNumberField1.setValue("+447405672");
        fieldList.add(ocrContactNumberField1);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getC51Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant1_dateOfBirth");
        ocrDateField.setValue("2/02/2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicants_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicants_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicants_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getC51ErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant1_dateOfBirth");
        ocrDateField.setValue("2-02-2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrDateField1 = new OcrDataField();
        ocrDateField1.setName("applicant2_dateOfBirth");
        ocrDateField1.setValue("2-02-2022");
        fieldList.add(ocrDateField1);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicants_postCode");
        ocrPostCodeField.setValue("testCode");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicants_telephoneNumber");
        ocrContactNumberField.setValue("+447405672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicants_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }
}
