package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TestDataUtil {

    private TestDataUtil() {

    }

    public static List<OcrDataField> getC100Data() {
        OcrDataField ocrDataFirstNameField = OcrDataField.builder()
                .name("appellant_firstName")
                .value("firstName")
                .build();

        OcrDataField ocrDataLastNameField = OcrDataField.builder()
                .name("appellant_lastName")
                .value("LastName")
                .build();

        OcrDataField ocrDataAddressField = OcrDataField.builder()
                .name("appellant_address")
                .value("Address1 London")
                .build();

        return Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField);
    }

    public static BulkScanFormValidationConfigManager.ValidationConfig buildValidationConfig() {
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
                new BulkScanFormValidationConfigManager.ValidationConfig();
        validationConfig.setMandatoryFields(List.of("solicitor_name", "solicitor_address", "solicitor_reference",
                        "solicitor_telephone_number", "solicitor_fax_number", "solicitor_dx_number"));

        BulkScanFormValidationConfigManager.RegexFieldsConfig formatFields =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        formatFields.setFieldNames(List.of("solicitor_telephone_number", "solicitor_fax_number",
                "solicitor_dx_number"));
        formatFields.setRegex("^[0-9]{11}$");

        BulkScanFormValidationConfigManager.RegexFieldsConfig emailFields =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        emailFields.setFieldNames(Collections.emptyList());

        BulkScanFormValidationConfigManager.RegexFieldsConfig dateFields =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        dateFields.setFieldNames(Collections.emptyList());

        BulkScanFormValidationConfigManager.RegexFieldsConfig numericFields =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        numericFields.setFieldNames(Collections.emptyList());

        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationFields =
                new BulkScanFormValidationConfigManager.RegexValidationConfig();
        regexValidationFields.setDateFields(dateFields);
        regexValidationFields.setFormatFields(formatFields);
        regexValidationFields.setEmailFields(emailFields);
        regexValidationFields.setNumericFields(numericFields);

        validationConfig.setRegexValidationFields(regexValidationFields);
        return validationConfig;
    }

    public static BulkScanValidationRequest buildBulkScanValidationRequest() {
        return BulkScanValidationRequest
                .builder()
                .ocrdatafields(List.of(OcrDataField
                        .builder()
                                .name("solicitor_name")
                        .build(), OcrDataField
                        .builder()
                                .name("solicitor_telephone_number")
                                .value("12345")
                        .build(), OcrDataField
                        .builder()
                        .name("solicitor_fax_number")
                        .value("12345678901")
                        .name("solicitor_dx_number")
                        .value("12345678901")
                        .build()))
                .build();

    }
}
