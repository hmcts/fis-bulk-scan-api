package uk.gov.hmcts.reform.bulkscan.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.ScanDocument;
import uk.gov.hmcts.reform.bulkscan.model.ScannedDocuments;

public final class TestDataUtil {

    public static final String ADDRESS = "123 test street, London";
    public static final String DATE_OF_BIRTH = "2/02/2022";
    public static final String FIRST_NAME = "firstName";
    public static final String PHONE_NUMBER = "+447405878672";
    public static final String POST_CODE = "TW3 1NN";

    private TestDataUtil() {}

    public static List<OcrDataField> getData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);
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
        ocrDateAuthorisedFamilyMediatorSignedDateField.setName(
                "authorised_family_mediator_signed_date");
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

        OcrDataField bailConditionEndDate = new OcrDataField();
        bailConditionEndDate.setName("respondent_BailConditionsEndDate");
        bailConditionEndDate.setValue("2012-feb-02");
        fieldList.add(bailConditionEndDate);

        return fieldList;
    }

    public static List<OcrDataField> getErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
        ocrDataLastNameField.setValue("");

        OcrDataField ocrTelephoneNumberField = new OcrDataField();
        ocrTelephoneNumberField.setName("telephone_number");
        ocrTelephoneNumberField.setValue("+447405878672");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        return Arrays.asList(
                ocrTelephoneNumberField,
                ocrDataAddressField,
                ocrDataFirstNameField,
                ocrDataLastNameField);
    }

    public static List<OcrDataField> getDateErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);

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
        ocrDataAuthorisedFamilyMediatorSignedDateField.setName(
                "authorised_family_mediator_signed_date");
        ocrDataAuthorisedFamilyMediatorSignedDateField.setValue("testdate");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");

        return Arrays.asList(
                ocrDataAddressField,
                ocrDataFirstNameField,
                ocrDataLastNameField,
                ocrDataChildFirstNameField,
                ocrDataChildLastNameField,
                ocrDataAppellantChildRelationshipField,
                ocrDataEmergencyProtectionOrderField,
                ocrDataDateField,
                ocrDataOtherCourtCaseDateField,
                ocrDataAuthorisedFamilyMediatorSignedDateField,
                ocrNumericField);
    }

    public static List<OcrDataField> getEmailErrorData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);

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
        ocrDataEmailField.setValue(FIRST_NAME);

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");

        OcrDataField ocrSolicitorFaxNumberField = new OcrDataField();
        ocrSolicitorFaxNumberField.setName("solicitor_fax_number");
        ocrSolicitorFaxNumberField.setValue("131123");

        return Arrays.asList(
                ocrDataAddressField,
                ocrDataFirstNameField,
                ocrDataLastNameField,
                ocrDataChildFirstNameField,
                ocrDataChildLastNameField,
                ocrDataAppellantChildRelationshipField,
                ocrDataEmergencyProtectionOrderField,
                ocrDataEmailField,
                ocrNumericField,
                ocrSolicitorFaxNumberField);
    }

    public static List<OcrDataField> getFirstNameData() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        OcrDataField ocrNumericField = new OcrDataField();
        ocrNumericField.setName("appellant_case_number");
        ocrNumericField.setValue("1311231231");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrNumericField);
    }

    public static List<OcrDataField> getA60OrC63orA58Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        OcrDataField ocrApplicantRelationToChildField = new OcrDataField();
        ocrApplicantRelationToChildField.setName("applicant_relationToChild_father_partner");
        ocrApplicantRelationToChildField.setValue("false");
        fieldList.add(ocrApplicantRelationToChildField);

        return fieldList;
    }

    public static List<OcrDataField> getC63Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);
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
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        OcrDataField ocrApplicantRelationToChildField = new OcrDataField();
        ocrApplicantRelationToChildField.setName("applicant_relationToChild_father_partner");
        ocrApplicantRelationToChildField.setValue("false");
        fieldList.add(ocrApplicantRelationToChildField);

        return fieldList;
    }

    public static List<OcrDataField> getA60Data() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("applicant1_firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("applicant1_lastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405678972");
        fieldList.add(ocrContactNumberField);

        return fieldList;
    }

    public static List<OcrDataField> getA60DataWithUnknownField() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("applicant1_firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("applicant1_lastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405678972");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrUnknownField = new OcrDataField();
        ocrUnknownField.setName("applicant1_unknownField");
        ocrUnknownField.setValue("UnknownFieldValue");
        fieldList.add(ocrUnknownField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getA59Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataApplicant1FirstNameField = new OcrDataField();
        ocrDataApplicant1FirstNameField.setName("applicant1_firstName");
        ocrDataApplicant1FirstNameField.setValue("murshad");
        fieldList.add(ocrDataApplicant1FirstNameField);

        OcrDataField ocrDataApplicant1LastNameField = new OcrDataField();
        ocrDataApplicant1LastNameField.setName("applicant1_lastName");
        ocrDataApplicant1LastNameField.setValue("haque");
        fieldList.add(ocrDataApplicant1LastNameField);

        OcrDataField ocrApplicant1AddressField = new OcrDataField();
        ocrApplicant1AddressField.setName("applicant1_address");
        ocrApplicant1AddressField.setValue("123 test street, London");
        fieldList.add(ocrApplicant1AddressField);

        OcrDataField ocrApplicant1TelephoneNumberField = new OcrDataField();
        ocrApplicant1TelephoneNumberField.setName("applicant1_telephoneNumber");
        ocrApplicant1TelephoneNumberField.setValue("+447405878672");
        fieldList.add(ocrApplicant1TelephoneNumberField);

        OcrDataField ocrDataApplicant2FirstNameField = new OcrDataField();
        ocrDataApplicant2FirstNameField.setName("applicant2_firstName");
        ocrDataApplicant2FirstNameField.setValue("mohammed");
        fieldList.add(ocrDataApplicant2FirstNameField);

        OcrDataField ocrDataApplicant2LastNameField = new OcrDataField();
        ocrDataApplicant2LastNameField.setName("applicant2_lastName");
        ocrDataApplicant2LastNameField.setValue("haque");
        fieldList.add(ocrDataApplicant2LastNameField);

        OcrDataField ocrApplicant2AddressField = new OcrDataField();
        ocrApplicant2AddressField.setName("applicant2_address");
        ocrApplicant2AddressField.setValue("123 test street, London");
        fieldList.add(ocrApplicant2AddressField);

        OcrDataField ocrApplicant2TelephoneNumberField = new OcrDataField();
        ocrApplicant2TelephoneNumberField.setName("applicant2_telephoneNumber");
        ocrApplicant2TelephoneNumberField.setValue("+447405878672");
        fieldList.add(ocrApplicant2TelephoneNumberField);

        return fieldList;
    }

    public static List<OcrDataField> getA59ErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataApplicant1FirstNameField = new OcrDataField();
        ocrDataApplicant1FirstNameField.setName("applicant1_firstName");
        fieldList.add(ocrDataApplicant1FirstNameField);

        OcrDataField ocrApplicant1AddressField = new OcrDataField();
        ocrApplicant1AddressField.setName("applicant1_address");
        ocrApplicant1AddressField.setValue("123 test street, London");
        fieldList.add(ocrApplicant1AddressField);

        OcrDataField ocrApplicant1TelephoneNumberField = new OcrDataField();
        ocrApplicant1TelephoneNumberField.setName("applicant1_telephoneNumber");
        ocrApplicant1TelephoneNumberField.setValue("+447405");
        fieldList.add(ocrApplicant1TelephoneNumberField);

        OcrDataField ocrDataApplicant2FirstNameField = new OcrDataField();
        ocrDataApplicant2FirstNameField.setName("applicant2_firstName");
        ocrDataApplicant2FirstNameField.setValue("murshad");
        fieldList.add(ocrDataApplicant2FirstNameField);

        OcrDataField ocrDataApplicant2LastNameField = new OcrDataField();
        ocrDataApplicant2LastNameField.setName("applicant2_lastName");
        ocrDataApplicant2LastNameField.setValue("haque");
        fieldList.add(ocrDataApplicant2LastNameField);

        OcrDataField ocrApplicant2AddressField = new OcrDataField();
        ocrApplicant2AddressField.setName("applicant2_address");
        ocrApplicant2AddressField.setValue("123 test street, London");
        fieldList.add(ocrApplicant2AddressField);

        OcrDataField ocrApplicant2TelephoneNumberField = new OcrDataField();
        ocrApplicant2TelephoneNumberField.setName("applicant2_telephoneNumber");
        ocrApplicant2TelephoneNumberField.setValue("9898");
        fieldList.add(ocrApplicant2TelephoneNumberField);

        return fieldList;
    }

    public static List<OcrDataField> getFgm001Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_fullName");
        ocrDataFirstNameField.setValue(FIRST_NAME);
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_dateOfBirth");
        ocrDateField.setValue(DATE_OF_BIRTH);
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postCode");
        ocrPostCodeField.setValue(POST_CODE);
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephoneNumber");
        ocrContactNumberField.setValue(PHONE_NUMBER);
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue(ADDRESS);
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
        ocrPostCodeField.setValue(POST_CODE);
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephoneNumber");
        ocrContactNumberField.setValue(PHONE_NUMBER);
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getFgm001ErrorsData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_fullName");
        ocrDataFirstNameField.setValue(FIRST_NAME);
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_dateOfBirth");
        ocrDateField.setValue("2/02/2023");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postCode");
        ocrPostCodeField.setValue(POST_CODE);
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephoneNumber");
        ocrContactNumberField.setValue("+4474058");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant_address");
        ocrAddressField.setValue(ADDRESS);
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
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getA60ErrorData() {
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

        OcrDataField ocrContactNumberField1 = new OcrDataField();
        ocrContactNumberField1.setName("applicant2_telephoneNumber");
        ocrContactNumberField1.setValue("+447405672");
        fieldList.add(ocrContactNumberField1);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        return fieldList;
    }

    public static List<OcrDataField> getA1Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_name");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_address");
        ocrDataLastNameField.setValue("lastname");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_ref");
        ocrDateField.setValue("ABCDEF1234567890");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postcode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephone_no");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        return fieldList;
    }

    public static List<OcrDataField> getA1ErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_name");
        ocrDataFirstNameField.setValue(FIRST_NAME);
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_address");
        ocrDataLastNameField.setValue("lastname");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant_ref");
        ocrDateField.setValue("ABCDEF1234567890**");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant_postcode");
        ocrPostCodeField.setValue("TW3");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant_telephone_no");
        ocrContactNumberField.setValue("+7777777");
        fieldList.add(ocrContactNumberField);

        return fieldList;
    }

    public static List<OcrDataField> getA1MandatoryErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_name");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        return fieldList;
    }

    public static List<OcrDataField> getC51Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);
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
        ocrContactNumberField.setName("applicant_homeTelephoneNumber");
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

    public static List<OcrDataField> getC2Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_fullName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_address");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant1_dateOfBirth");
        ocrDateField.setValue("2/02/2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant1_placeOfBirth");
        ocrPostCodeField.setValue("Place of Birth");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrHomeNumberField = new OcrDataField();
        ocrHomeNumberField.setName("applicant1_homePhoneNumber");
        ocrHomeNumberField.setValue("+447405878672");
        fieldList.add(ocrHomeNumberField);

        OcrDataField ocrMobileNumberField = new OcrDataField();
        ocrMobileNumberField.setName("applicant1_mobilePhoneNumber");
        ocrMobileNumberField.setValue("+447405878672");
        fieldList.add(ocrMobileNumberField);

        return fieldList;
    }

    public static List<OcrDataField> getC2ErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_address");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant1_dateOfBirth");
        ocrDateField.setValue("2/13/2022");
        fieldList.add(ocrDateField);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicant1_placeOfBirth");
        ocrPostCodeField.setValue("Place of Birth");
        fieldList.add(ocrPostCodeField);

        return fieldList;
    }

    public static List<OcrDataField> getFL401AData() {
        List<OcrDataField> formFieldLst = new ArrayList<>();

        OcrDataField ocrDataFullNameField = new OcrDataField();
        ocrDataFullNameField.setName("applicant_full_name");
        ocrDataFullNameField.setValue("fullName");
        formFieldLst.add(ocrDataFullNameField);

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_address");
        ocrDataAddressField.setValue("Topsfield Parade");
        formFieldLst.add(ocrDataAddressField);

        OcrDataField ocrDataPostCodeField = new OcrDataField();
        ocrDataPostCodeField.setName("applicant_postcode");
        ocrDataPostCodeField.setValue("N8 8PE");
        formFieldLst.add(ocrDataPostCodeField);

        OcrDataField ocrTelephoneNumberField = new OcrDataField();
        ocrTelephoneNumberField.setName("applicant_telephone_number");
        ocrTelephoneNumberField.setValue("+447405878672");
        formFieldLst.add(ocrTelephoneNumberField);

        return formFieldLst;
    }

    public static List<OcrDataField> getFL401AErrorData() {
        List<OcrDataField> formFieldLst = new ArrayList<>();

        OcrDataField ocrDataFullNameField = new OcrDataField();
        ocrDataFullNameField.setName("applicant_full_name");
        ocrDataFullNameField.setValue("");
        formFieldLst.add(ocrDataFullNameField);

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_address");
        ocrDataAddressField.setValue("");
        formFieldLst.add(ocrDataAddressField);

        OcrDataField ocrDataPostCodeField = new OcrDataField();
        ocrDataPostCodeField.setName("applicant_postcode");
        ocrDataPostCodeField.setValue("TW3");
        formFieldLst.add(ocrDataPostCodeField);

        OcrDataField ocrDataDateOfBirthField = new OcrDataField();
        ocrDataDateOfBirthField.setName("applicant_dateOfBirth");
        ocrDataDateOfBirthField.setValue("21/12/01");
        formFieldLst.add(ocrDataDateOfBirthField);

        OcrDataField ocrTelephoneNumberField = new OcrDataField();
        ocrTelephoneNumberField.setName("applicant_telephone_number");
        ocrTelephoneNumberField.setValue("");
        formFieldLst.add(ocrTelephoneNumberField);

        return formFieldLst;
    }

    public static List<OcrDataField> getFL401AMandatoryWarningData() {
        List<OcrDataField> formFieldLst = new ArrayList<>();

        OcrDataField ocrDataTelephoneNumberField = new OcrDataField();
        ocrDataTelephoneNumberField.setName("applicant_telephone_number");
        ocrDataTelephoneNumberField.setValue("4420888");
        formFieldLst.add(ocrDataTelephoneNumberField);

        return formFieldLst;
    }

    public static List<OcrDataField> getA58DataWithAdditonalOptionalFields() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        OcrDataField ocrApplicantRelationToChildField = new OcrDataField();
        ocrApplicantRelationToChildField.setName("applicant_relationToChild_father_partner");
        ocrApplicantRelationToChildField.setValue("false");
        fieldList.add(ocrApplicantRelationToChildField);

        OcrDataField applicant = new OcrDataField();
        applicant.setName("applicant_test_field");
        applicant.setValue("false");
        fieldList.add(applicant);

        return fieldList;
    }

    public static List<OcrDataField> getA58Data() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue(FIRST_NAME);
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue(ADDRESS);
        fieldList.add(ocrAddressField);

        OcrDataField ocrApplicantRelationToChildField = new OcrDataField();
        ocrApplicantRelationToChildField.setName("applicant_relationToChild_father_partner");
        ocrApplicantRelationToChildField.setValue("false");
        fieldList.add(ocrApplicantRelationToChildField);

        return fieldList;
    }

    public static List<OcrDataField> getA58DataWithoutRelation() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        OcrDataField ocrApplicantRelationToChildField = new OcrDataField();
        ocrApplicantRelationToChildField.setName("applicant_relationToChild_father_partner");
        ocrApplicantRelationToChildField.setValue("");
        fieldList.add(ocrApplicantRelationToChildField);

        return fieldList;
    }

    public static List<OcrDataField> getC51ConditionalFieldErrorData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrDataFirstNameField1 = new OcrDataField();
        ocrDataFirstNameField1.setName("applicant2_firstName");
        ocrDataFirstNameField1.setValue("applicant 2 firstName");
        fieldList.add(ocrDataFirstNameField1);

        OcrDataField ocrDataLastNameField1 = new OcrDataField();
        ocrDataLastNameField1.setName("applicant2_lastName");
        ocrDataLastNameField1.setValue("applicant 2 LastName");
        fieldList.add(ocrDataLastNameField1);

        OcrDataField ocrPostCodeField = new OcrDataField();
        ocrPostCodeField.setName("applicants_postCode");
        ocrPostCodeField.setValue("TW3 1NN");
        fieldList.add(ocrPostCodeField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicants_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        OcrDataField ocrDateField = new OcrDataField();
        ocrDateField.setName("applicant1_dateOfBirth");
        ocrDateField.setValue("2/02/2022");
        fieldList.add(ocrDateField);

        return fieldList;
    }

    public static List<ScannedDocuments> getScannedDocumentsList() {
        return List.of(
                ScannedDocuments.builder()
                        .scanDocument(
                                ScanDocument.builder()
                                        .url("url")
                                        .binaryUrl("binary_url")
                                        .filename("filename")
                                        .build())
                        .build(),
                ScannedDocuments.builder()
                        .scanDocument(
                                ScanDocument.builder()
                                        .url("url1")
                                        .binaryUrl("binary_url1")
                                        .filename("filename1")
                                        .build())
                        .build());
    }
}
