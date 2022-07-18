package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanA58GroupFieldsTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String A58_GROUP_FIELD_REQUEST_PATH =
        "classpath:request/bulk-scan-a58-group-input.json";
    private static final String A58_GROUP_FIELD_REQUEST_WARNING_PATH =
        "classpath:request/bulk-scan-a58-group-warning-input.json";
    private static final String A58_GROUP_FIELD_REQUEST_ERROR_PATH =
        "classpath:request/bulk-scan-a58-group-error-input.json";

    @Spy
    @Autowired
    BulkScanA58Service bulkScanValidationService;

    @Test
    @DisplayName("A58 Group Fields form validation success scenario")
    void testA58PostPlacementApplicationValidationSuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
            .readValue(readFileFrom(A58_GROUP_FIELD_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("A58 Group Fields form validation success scenario")
    void testA58PostPlacementApplicationValidationWarnings() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
            .readValue(readFileFrom(A58_GROUP_FIELD_REQUEST_WARNING_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
    }

    @Test
    @DisplayName("A58 Group Fields form validation success scenario")
    void testA58PostPlacementApplicationValidationErrors() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
            .readValue(readFileFrom(A58_GROUP_FIELD_REQUEST_ERROR_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
    }
}
