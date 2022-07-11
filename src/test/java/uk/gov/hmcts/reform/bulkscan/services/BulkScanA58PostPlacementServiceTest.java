package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanA58PostPlacementServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String A58_POST_PLACEMENT_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-a58-post-placement-transform-output.json";

    private static final String A58_POST_PLACEMENT_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-input.json";
    private static final String A58_POST_PLACEMENT_UNKNOWN_FIELD_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-unknown-field-input.json";
    private static final String A58_POST_PLACEMENT_ERROR_FIELD_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-error-field-input.json";
    private static final String A58_POST_PLACEMENT_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-transform-input.json";

    @Spy
    @Autowired
    BulkScanA58Service bulkScanValidationService;

    @Test
    @DisplayName("A58 post placement form validation success scenario")
    void testA58PostPlacementApplicationValidationSuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(A58_POST_PLACEMENT_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("A58 post placement form validation unknown field warning scenario")
    void testA58PostPlacementApplicationValidationUnknownFieldWarning() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper.readValue(readFileFrom(
                A58_POST_PLACEMENT_UNKNOWN_FIELD_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
    }

    @Test
    @DisplayName("A58 post placement form validation error scenario")
    void testA58PostPlacementApplicationMandatoryErrorWhileDoingValidation() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(A58_POST_PLACEMENT_ERROR_FIELD_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "applicant1_firstName")));
    }

    @Test
    @DisplayName("A58 post placement form transform scenario")
    void testA58PostPlacementApplicationTransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
                readFileFrom(A58_POST_PLACEMENT_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        JSONAssert.assertEquals(readFileFrom(A58_POST_PLACEMENT_TRANSFORM_RESPONSE_PATH),
                mapper.writeValueAsString(res), true);
    }

}
