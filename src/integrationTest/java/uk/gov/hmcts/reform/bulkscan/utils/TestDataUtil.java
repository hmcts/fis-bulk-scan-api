package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Arrays;
import java.util.List;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static List<OcrDataField> buildFL403ValidationRequest() {

        OcrDataField ocrDataApplicantFirstName = new OcrDataField();
        ocrDataApplicantFirstName.setName("applicant_firstName");

        OcrDataField ocrDataApplicantLastName = new OcrDataField();
        ocrDataApplicantLastName.setName("applicant_lastName");

        OcrDataField ocrDataApplicantAddress = new OcrDataField();
        ocrDataApplicantAddress.setName("applicant_address");

        OcrDataField ocrDataApplicantPostcode = new OcrDataField();
        ocrDataApplicantPostcode.setName("applicant_postcode");

        OcrDataField ocrDataApplicantTelephoneField = new OcrDataField();
        ocrDataApplicantTelephoneField.setName("applicant_telephone_number");

        OcrDataField ocrDataApplicantEmailField = new OcrDataField();
        ocrDataApplicantEmailField.setName("applicant_email");

        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("solicitor_name");
        ocrDataFirstNameField.setValue("solicitor_name_value");

        OcrDataField ocrDataSolicitorAddressField = new OcrDataField();
        ocrDataSolicitorAddressField.setName("solicitor_address");

        OcrDataField ocrDataSolicitorTelephoneField = new OcrDataField();
        ocrDataSolicitorTelephoneField.setName("solicitor_telephone_number");
        ocrDataSolicitorTelephoneField.setValue("DUMMY");

        OcrDataField ocrDataSolicitorDxField = new OcrDataField();
        ocrDataSolicitorDxField.setName("solicitor_fax_number");
        ocrDataSolicitorDxField.setValue("123455");

        return Arrays.asList(ocrDataApplicantFirstName,
                             ocrDataApplicantLastName,
                             ocrDataApplicantAddress,
                             ocrDataApplicantPostcode,
                             ocrDataApplicantTelephoneField,
                             ocrDataApplicantEmailField,
                             ocrDataSolicitorAddressField,
                             ocrDataFirstNameField,
                             ocrDataSolicitorTelephoneField,
                             ocrDataSolicitorDxField
        );
    }
}
