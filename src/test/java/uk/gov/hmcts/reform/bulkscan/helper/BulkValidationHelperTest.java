package uk.gov.hmcts.reform.bulkscan.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;

@ExtendWith(SpringExtension.class)
class BulkValidationHelperTest {

    BulkScanFormValidationConfigManager.ValidationConfig validationConfig;

    @BeforeEach
    void setup() {
        validationConfig = new BulkScanFormValidationConfigManager.ValidationConfig();
        validationConfig.setMandatoryFields(Arrays.asList("appellant_firstName", "appellant_lastName"));

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
    }

    @Test
    void testSuccessScenario() {
        BulkScanValidationResponse res = BulkScanValidationHelper.validateMandatoryAndOptionalFields(
            TestDataUtil.getData(), validationConfig);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testErrorScenario() {
        BulkScanValidationResponse res = BulkScanValidationHelper.validateMandatoryAndOptionalFields(
            TestDataUtil.getErrorData(), validationConfig);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "appellant_lastName")));
    }

    @Test
    void testC100DateErrorWhileDoingValidation() {
        BulkScanValidationResponse res = BulkScanValidationHelper
            .validateMandatoryAndOptionalFields(TestDataUtil.getDateErrorData(), validationConfig);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(DATE_FORMAT_MESSAGE, "appellant_dateOfBirth")));
    }

    @Test
    void testC100EmailErrorWhileDoingValidation() {
        BulkScanValidationResponse res = BulkScanValidationHelper
            .validateMandatoryAndOptionalFields(TestDataUtil.getEmailErrorData(), validationConfig);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(EMAIL_FORMAT_MESSAGE, "appellant_email")));
    }
}
