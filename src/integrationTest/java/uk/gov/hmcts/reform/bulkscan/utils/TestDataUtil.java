package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Arrays;
import java.util.List;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static List<OcrDataField> buildFL403ValidationRequest() {
        OcrDataField ocrDataFirstNameField = OcrDataField.builder()
                .name("solicitor_name")
                .value("solicitor_name_value")
                .build();

        OcrDataField ocrDataLastNameField = OcrDataField.builder()
                .name("solicitor_address")
                .build();

        OcrDataField ocrDataAddressField = OcrDataField.builder()
                .name("solicitor_telephone_number")
                .value("DUMMY")
                .build();

        OcrDataField ocrDataDxNumberField = OcrDataField.builder()
                .name("solicitor_dx_number")
                .value("12345X")
                .build();

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField, ocrDataDxNumberField);
    }
}
