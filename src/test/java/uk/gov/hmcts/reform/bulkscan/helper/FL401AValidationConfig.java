package uk.gov.hmcts.reform.bulkscan.helper;

import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

import java.util.Arrays;

public class FL401AValidationConfig implements ValidationConfig {

    @Override
    public BulkScanFormValidationConfigManager.ValidationConfig getConfig() {
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
            new BulkScanFormValidationConfigManager.ValidationConfig();
        validationConfig.setMandatoryFields(Arrays.asList("applicant_address",
                                                          "applicant_postcode", "applicant_telephone_number"));

        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationConfig =
            new BulkScanFormValidationConfigManager.RegexValidationConfig();

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexPostCodeFieldConfig =
            new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexPostCodeFieldConfig.setFieldNames(Arrays.asList("applicant_postcode"));
        regexPostCodeFieldConfig.setRegex("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$");

        regexValidationConfig.setPostCodeFields(regexPostCodeFieldConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexPhoneNumberFieldConfig =
            new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexPhoneNumberFieldConfig.setFieldNames(Arrays.asList("applicant_telephone_number"));
        regexPhoneNumberFieldConfig.setRegex("^(?:0|\\+?44)(?:\\d\\s?){9,10}$");

        regexValidationConfig.setPhoneNumberFields(regexPhoneNumberFieldConfig);

        validationConfig.setRegexValidationFields(regexValidationConfig);

        return validationConfig;
    }
}

