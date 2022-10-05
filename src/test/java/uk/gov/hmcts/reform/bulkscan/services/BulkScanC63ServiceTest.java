package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.json.JSONException;
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
class BulkScanC63ServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired BulkScanC63Service bulkScanValidationService;

    private static final String C63_VALIDATE_REQUEST_PATH =
            "classpath:request/bulk-scan-c63-validate-input.json";

    private static final String C63_VALIDATE_RESPONSE_PATH =
            "classpath:response/bulk-scan-c63-validate-output.json";

    private static final String C63_VALIDATE_ERROR_REQUEST_PATH =
            "classpath:request/bulk-scan-c63-validate-error-input.json";

    private static final String C63_VALIDATE_ERROR_RESPONSE_PATH =
            "classpath:response/bulk-scan-c63-validate-error-output.json";

    private static final String C63_VALIDATE_WARNING_REQUEST_PATH =
            "classpath:request/bulk-scan-c63-validate-warning-input.json";

    private static final String C63_VALIDATE_WARNING_RESPONSE_PATH =
            "classpath:response/bulk-scan-c63-validate-warning-output.json";

    private static final String C63_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-c63-transform-input.json";

    private static final String C63_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-c63-transform-output.json";

    @Test
    void testC63ValidationSuccess() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C63_VALIDATE_REQUEST_PATH), BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(C63_VALIDATE_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }

    @Test
    void testC63ErrorWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C63_VALIDATE_ERROR_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(C63_VALIDATE_ERROR_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }

    @Test
    void testC63WarningWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C63_VALIDATE_WARNING_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(C63_VALIDATE_WARNING_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }

    @Test
    @DisplayName("C63 transform success.")
    void testC63TransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C63_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        JSONAssert.assertEquals(
                readFileFrom(C63_TRANSFORM_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }
}
