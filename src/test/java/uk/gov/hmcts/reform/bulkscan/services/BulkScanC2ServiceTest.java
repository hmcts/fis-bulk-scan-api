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
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC2ServiceTest {

    @Spy
    @Autowired
    BulkScanC2Service bulkScanValidationService;



    @Test
    void testC2SuccesswithBothPhoneNumbers() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC2Data()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }


    @Test
    void testC2MandatoryErrorWhileNoPhoneNumberAvailable() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC2ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(XOR_CONDITIONAL_FIELDS_MESSAGE,
                                               "applicant1_homePhoneNumber,applicant1_mobilePhoneNumber")));
    }

    @Test
    void testC2DateErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC2ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(DATE_FORMAT_MESSAGE, "applicant1_dateOfBirth")));
    }

    @Test
    void testC2FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataUtil.getC2ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, "applicant1_fullName")));
    }


    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNull(bulkScanTransformationResponse);
    }

}
