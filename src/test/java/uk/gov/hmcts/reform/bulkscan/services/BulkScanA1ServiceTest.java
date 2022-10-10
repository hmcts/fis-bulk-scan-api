package uk.gov.hmcts.reform.bulkscan.services;

import static org.mockito.Mockito.mock;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.A1_VALIDATE_ERROR_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.A1_VALIDATE_ERROR_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.A1_VALIDATE_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.A1_VALIDATE_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.A1_VALIDATE_WARNING_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.A1_VALIDATE_WARNING_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanA1ServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired BulkScanA1Service bulkScanValidationService;

    @DisplayName("A1 validation.")
    @Test
    void testA1ValidationSuccess() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(A1_VALIDATE_REQUEST_PATH), BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(A1_VALIDATE_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }

    @DisplayName("A1 validation error with warning for unknown fields.")
    @Test
    void testA1ErrorWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(A1_VALIDATE_ERROR_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(A1_VALIDATE_ERROR_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }

    @DisplayName("A1 validation warning for unknown fields.")
    @Test
    void testA1WarningWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(A1_VALIDATE_WARNING_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(A1_VALIDATE_WARNING_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }

    @DisplayName("A1 mock transform.")
    @Test
    void testA1Transform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNull(bulkScanTransformationResponse);
    }
}
