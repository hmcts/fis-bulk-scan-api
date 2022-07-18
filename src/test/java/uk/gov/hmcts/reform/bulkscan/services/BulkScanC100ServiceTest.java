package uk.gov.hmcts.reform.bulkscan.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NUMERIC_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ServiceTest {

    @Autowired
    BulkScanC100Service bulkScanValidationService;

    @Autowired
    BulkScanC100FieldDependencyService bulkScanDependencyService;

    @MockBean
    PostcodeLookupService postcodeLookupService;

    @Test
    void testC100Success() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getData()).build();
        when(postcodeLookupService.isValidPostCode("TW3 1NN", null)).thenReturn(true);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testC100WhenPostCodeNotValid() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getData()).build();
        when(postcodeLookupService.isValidPostCode("TW3 1NN", null)).thenReturn(false);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(POST_CODE_MESSAGE, "appellant_postCode")));
    }

    @Test
    void testC100WhenNotOneFieldPresentOutOfXoRFields() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getErrorData()).build();
        when(postcodeLookupService.isValidPostCode("TW3 1NN", null)).thenReturn(false);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(XOR_CONDITIONAL_FIELDS_MESSAGE,
                                                                "appellant_postCode,appellant_lastName")));
    }

    @Test
    void testC100MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "appellant_lastName")));
    }

    @Test
    void testC100EmergencyProtectionOrderMandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(
            String.format(MANDATORY_ERROR_MESSAGE, "emergency_protection_order")));
    }

    @Test
    void testC100DateErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getDateErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(DATE_FORMAT_MESSAGE, "appellant_dateOfBirth")));
    }

    @Test
    void testC100OtherCourtCaseDateErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getDateErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(DATE_FORMAT_MESSAGE, "other_court_case_date")));
    }

    @Test
    void testC100AuthorisedFamilyMediatorSignedDateErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getDateErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(
            String.format(DATE_FORMAT_MESSAGE, "authorised_family_mediator_signed_date")));
    }

    @Test
    void testC100EmailErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getEmailErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(EMAIL_FORMAT_MESSAGE, "appellant_email")));
    }

    @Test
    void testC100FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getFirstNameData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, "appellant_lastName")));
    }

    @Test
    void testC100CaseNoNumericErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getNumericErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(NUMERIC_MESSAGE, "case_no")));
    }

    @Test
    @DisplayName("Should generate warning on absence of dependency field(s) on exemption_To_Attend_MIAM field")
    void testC100WarningExemptionToAttendMiamWithoughAPage3Checkbox() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getExemptionToAttendWarningData()).build();

        BulkScanValidationResponse res = bulkScanDependencyService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(GROUP_DEPENDENCY_MESSAGE,
                                                                  EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD)));
    }

    @Test
    @DisplayName("Should allow only one mutually exclusive field on section 2 to be checked")
    void testC100SuccessExemptionToAttendMiamWithPage3Checkbox() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getExemptionToAttendGroupDependencySuccessData()).build();

        BulkScanValidationResponse res = bulkScanDependencyService.validate(bulkScanValidationRequest);

        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("Should generate warning on mutually exclusive fields which allows only one field checked")
    void testC100MutuallyExclusiveWarningOnSectionTwoCheckbox() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getMutuallyExclusiveFieldWarningData()).build();

        BulkScanValidationResponse res = bulkScanDependencyService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNotNull(bulkScanTransformationResponse);
    }

}
