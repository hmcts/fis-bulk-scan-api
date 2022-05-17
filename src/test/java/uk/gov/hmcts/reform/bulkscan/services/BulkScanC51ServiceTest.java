package uk.gov.hmcts.reform.bulkscan.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC51ServiceTest {

    @Spy
    @Autowired
    BulkScanC51Service bulkScanValidationService;

    @Test
    void testC51Success() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC51Data()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testC51MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC51ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "applicant1_firstName")));
    }

    @Test
    void testC51DateErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC51ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(DATE_FORMAT_MESSAGE, "applicant1_dateOfBirth")));
    }

    @Test
    void testC51FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC51ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, "applicant1_lastName")));
    }

    @Test
    void testC51OptionalFieldsWarningsWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC51ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getWarnings().items.contains(String.format(DATE_FORMAT_MESSAGE, "applicant2_dateOfBirth")));
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNull(bulkScanTransformationResponse);
    }

}
