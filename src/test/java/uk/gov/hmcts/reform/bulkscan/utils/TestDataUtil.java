package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Arrays;
import java.util.List;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static List<OcrDataField> getC100Data() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
        ocrDataLastNameField.setValue("LastName");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField);
    }

    public static List<OcrDataField> getFL403Data() {
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant_lastName");
        ocrDataLastNameField.setValue("lastName");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_address");
        ocrDataAddressField.setValue("78 Bruce Castle Road, London");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_postcode");
        ocrDataAddressField.setValue("N17 8NJ");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_dateOfBirth");
        ocrDataAddressField.setValue("20 02 04");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("applicant_telephone");
        ocrDataAddressField.setValue("02082099999");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("solicitor_name");
        ocrDataAddressField.setValue("Ross Pendleton");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("solicitor_address");
        ocrDataAddressField.setValue("45 Topsfield Parade, London, N8 8PT");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("solicitor_reference");
        ocrDataAddressField.setValue("TSX098766");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("solicitor_telephone");
        ocrDataAddressField.setValue("02082088888");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("solicitor_faxNumber");
        ocrDataAddressField.setValue("02082088889");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("solicitor_dxNumber");
        ocrDataAddressField.setValue("02082088887");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("solicitor_feeAccountNumber");
        ocrDataAddressField.setValue("20757401238889");

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField);
    }
}
