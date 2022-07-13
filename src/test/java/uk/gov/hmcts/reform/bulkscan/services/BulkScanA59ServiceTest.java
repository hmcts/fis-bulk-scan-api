package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.skyscreamer.jsonassert.JSONAssert;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PHONE_NUMBER_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanA59ServiceTest {

    private static final String A59_TRANSFORM_RESPONSE_PATH =
        "classpath:response/bulk-scan-a59-transform-output.json";

    @Spy
    @Autowired
    BulkScanA59Service bulkScanValidationService;

    @Test
    void testA59Success() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA59Data()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testApplicant1FirstnameMandatoryFieldErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA59ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "applicant1_firstName")));
    }

    @Test
    void testApplicant1LastnameMandatoryFieldErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA59ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, "applicant1_lastName")));
    }

    @Test
    void testApplicant1TelephoneNumberMandatoryFieldErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA59ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getErrors().items.contains(String.format(PHONE_NUMBER_MESSAGE, "applicant1_telephoneNumber")));
    }

    @Test
    void testApplicant2TelephoneNumberMandatoryFieldErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA59ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getWarnings().items.contains(String.format(PHONE_NUMBER_MESSAGE, "applicant2_telephoneNumber")));
    }

    @Test
    void testTransformRequest() throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .scannedDocuments(TestDataUtil.getScannedDocumentsList())
                                                    .ocrdatafields(TestDataUtil.getA59Data()).build());

        JSONAssert.assertEquals(readFileFrom(A59_TRANSFORM_RESPONSE_PATH),
                                mapper.writeValueAsString(bulkScanTransformationResponse), true);

    }
}
