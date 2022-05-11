package uk.gov.hmcts.reform.bulkscan.helper;

import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

import java.util.Arrays;

public class A1ValidationConfig implements ValidationConfig {

    @Override
    public BulkScanFormValidationConfigManager.ValidationConfig getConfig() {
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
            new BulkScanFormValidationConfigManager.ValidationConfig();
        validationConfig.setMandatoryFields(Arrays.asList("applicant_name", "applicant_address", "applicant_ref",
                                                          "applicant_postcode", "applicant_telephone_no"));

        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationConfig =
            new BulkScanFormValidationConfigManager.RegexValidationConfig();

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexEmailFieldsConfig =
            new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexEmailFieldsConfig.setFieldNames(Arrays.asList("applicant_email"));
        regexEmailFieldsConfig.setRegex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");

        regexValidationConfig.setEmailFields(regexEmailFieldsConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexAlphaNumericFieldsConfig =
            new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexAlphaNumericFieldsConfig.setFieldNames(Arrays.asList("applicant_ref"));
        regexAlphaNumericFieldsConfig.setRegex("^[a-zA-Z0-9]+$");

        regexValidationConfig.setAlphaNumericFields(regexAlphaNumericFieldsConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexPostCodeFieldConfig =
            new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexPostCodeFieldConfig.setFieldNames(Arrays.asList("applicant_postcode"));
        regexPostCodeFieldConfig.setRegex("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$");

        regexValidationConfig.setPostCodeFields(regexPostCodeFieldConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexPhoneNumberFieldConfig =
            new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexPhoneNumberFieldConfig.setFieldNames(Arrays.asList("applicant_telephone_no"));
        regexPhoneNumberFieldConfig.setRegex("^(?:0|\\+?44)(?:\\d\\s?){9,10}$");

        regexValidationConfig.setPhoneNumberFields(regexPhoneNumberFieldConfig);

        validationConfig.setRegexValidationFields(regexValidationConfig);
        return validationConfig;
    }
}
