package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100Section6ValidationServiceTest {
    private static final String C100_SECTION6B_ERROR_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-section6b-error-validate-input.json";

    private static final String C100_SECTION_6B_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-section6b-validate-input.json";
    @Autowired BulkScanC100Service bulkScanValidationService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("C100 section 6b validation success.")
    void testC100Section6bValidationSuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_SECTION_6B_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName(
            "Test a sample successful C100 section 6b application validation error with full"
                    + " details.")
    void testC100Section6bError() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_SECTION6B_ERROR_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
    }
}
