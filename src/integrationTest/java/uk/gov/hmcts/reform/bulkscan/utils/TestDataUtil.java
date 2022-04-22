package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Arrays;
import java.util.List;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static List<OcrDataField> buildFL403ValidationRequest() {

        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("solicitor_name");
        ocrDataFirstNameField.setValue("solicitor_name_value");

        OcrDataField ocrDataSolicitorAddressField = new OcrDataField();
        ocrDataSolicitorAddressField.setName("solicitor_address");

        OcrDataField ocrDataSolicitorTelephoneField = new OcrDataField();
        ocrDataSolicitorTelephoneField.setName("solicitor_telephone_number");
        ocrDataSolicitorTelephoneField.setValue("DUMMY");

        OcrDataField ocrDataSolicitorDxField = new OcrDataField();
        ocrDataSolicitorDxField.setName("solicitor_dx_number");
        ocrDataSolicitorDxField.setValue("12345X");

        return Arrays.asList(ocrDataSolicitorAddressField, ocrDataFirstNameField, ocrDataSolicitorTelephoneField,
                ocrDataSolicitorDxField);
    }
}
