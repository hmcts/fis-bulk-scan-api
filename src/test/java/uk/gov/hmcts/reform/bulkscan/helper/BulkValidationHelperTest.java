package uk.gov.hmcts.reform.bulkscan.helper;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ALPHA_NUMERIC_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PHONE_NUMBER_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_MESSAGE;

@ExtendWith(SpringExtension.class)
class BulkValidationHelperTest {
    ValidationConfigFactory validationConfigFactory;

    @InjectMocks
    BulkScanValidationHelper bulkScanValidationHelper;

    @BeforeEach
    void setup() {
        validationConfigFactory = new ValidationConfigFactory();
    }

    @Test
    void testSuccessScenario() {
        ValidationConfig validationConfig = validationConfigFactory.getValidationConfig(FormType.C100.toString());
        BulkScanValidationResponse res = bulkScanValidationHelper.validateMandatoryAndOptionalFields(
            TestDataUtil.getData(), validationConfig.getConfig());
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testErrorScenario() {
        ValidationConfig validationConfig = validationConfigFactory.getValidationConfig(FormType.C100.toString());
        BulkScanValidationResponse res = bulkScanValidationHelper.validateMandatoryAndOptionalFields(
            TestDataUtil.getErrorData(), validationConfig.getConfig());
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "appellant_lastName")));
    }

    @Test
    void testC100DateErrorWhileDoingValidation() {
        ValidationConfig validationConfig = validationConfigFactory.getValidationConfig(FormType.C100.toString());
        BulkScanValidationResponse res = bulkScanValidationHelper
            .validateMandatoryAndOptionalFields(TestDataUtil.getDateErrorData(), validationConfig.getConfig());
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(DATE_FORMAT_MESSAGE, "appellant_dateOfBirth")));
    }

    @Test
    void testC100EmailErrorWhileDoingValidation() {
        ValidationConfig validationConfig = validationConfigFactory.getValidationConfig(FormType.C100.toString());
        BulkScanValidationResponse res = bulkScanValidationHelper
            .validateMandatoryAndOptionalFields(TestDataUtil.getEmailErrorData(), validationConfig.getConfig());
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(EMAIL_FORMAT_MESSAGE, "appellant_email")));
    }

    @Test
    void testA1FieldValidationErrorWhileDoingValidation() {
        ValidationConfig validationConfig = validationConfigFactory.getValidationConfig(FormType.A1.toString());
        BulkScanValidationResponse res = bulkScanValidationHelper.validateMandatoryAndOptionalFields(
            TestDataUtil.getA1ErrorData(), validationConfig.getConfig());
        assertEquals(Status.ERRORS, res.status);
        Assert.assertTrue(res.getErrors().items.contains(String.format(ALPHA_NUMERIC_FIELDS_MESSAGE,
                                                                       "applicant_ref")));
        Assert.assertTrue(res.getErrors().items.contains(String.format(POST_CODE_MESSAGE,
                                                                       "applicant_postcode")));
        Assert.assertTrue(res.getErrors().items.contains(String.format(PHONE_NUMBER_MESSAGE,
                                                                       "applicant_telephone_no")));
    }

    @Test
    void testFL401AFieldValidationErrorWhileDoingValidation() {
        ValidationConfig validationConfig = validationConfigFactory.getValidationConfig(FormType.FL401A.toString());
        BulkScanValidationResponse res = bulkScanValidationHelper.validateMandatoryAndOptionalFields(
            TestDataUtil.getFL401AErrorData(), validationConfig.getConfig());
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(POST_CODE_MESSAGE, "applicant_postcode")));
    }
}
