package uk.gov.hmcts.reform.bulkscan.helper;

import java.util.Arrays;
import java.util.Collections;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

public class C100ValidationConfig implements ValidationConfig {

    @Override
    public BulkScanFormValidationConfigManager.ValidationConfig getConfig() {
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
                new BulkScanFormValidationConfigManager.ValidationConfig();
        validationConfig.setMandatoryFields(
                Arrays.asList("appellant_firstName", "appellant_lastName"));

        BulkScanFormValidationConfigManager.RegexValidationConfig regexValidationConfig =
                new BulkScanFormValidationConfigManager.RegexValidationConfig();

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexDateFieldsConfig =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexDateFieldsConfig.setFieldNames(Arrays.asList("appellant_dateOfBirth"));
        regexDateFieldsConfig.setRegex("dd/mm/yyyy");

        regexValidationConfig.setDateFields(regexDateFieldsConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexEmailFieldsConfig =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexEmailFieldsConfig.setFieldNames(Arrays.asList("appellant_email"));
        regexEmailFieldsConfig.setRegex("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");

        regexValidationConfig.setEmailFields(regexEmailFieldsConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexNumericFieldConfig =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexNumericFieldConfig.setFieldNames(Arrays.asList("appellant_case_number"));
        regexNumericFieldConfig.setRegex("^[0-9]*$");

        regexValidationConfig.setNumericFields(regexNumericFieldConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexPostCodeFieldConfig =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexPostCodeFieldConfig.setFieldNames(Arrays.asList("appellant_post_code"));
        regexPostCodeFieldConfig.setRegex("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$");

        regexValidationConfig.setPostCodeFields(regexPostCodeFieldConfig);

        BulkScanFormValidationConfigManager.RegexFieldsConfig regexPhoneNumberFieldConfig =
                new BulkScanFormValidationConfigManager.RegexFieldsConfig();
        regexPostCodeFieldConfig.setFieldNames(Arrays.asList("appellant_phone_number"));
        regexPostCodeFieldConfig.setRegex("^(?:0|\\+?44)(?:\\d\\s?){9,10}$");

        regexValidationConfig.setPhoneNumberFields(regexPhoneNumberFieldConfig);

        validationConfig.setRegexValidationFields(regexValidationConfig);

        validationConfig.setOptionalFields(Collections.emptyList());
        return validationConfig;
    }
}
