package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFgm001ServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired BulkScanFgm001Service bulkScanValidationService;

    private static final String FGM001_VALIDATE_REQUEST_PATH =
            "classpath:request/bulk-scan-fgm001-validate-input.json";

    private static final String FGM001_VALIDATE_RESPONSE_PATH =
            "classpath:response/bulk-scan-fgm001-validate-output.json";

    private static final String FGM001_VALIDATE_ERROR_REQUEST_PATH =
            "classpath:request/bulk-scan-fgm001-validate-error-input.json";

    private static final String FGM001_VALIDATE_ERROR_RESPONSE_PATH =
            "classpath:response/bulk-scan-fgm001-validate-error-output.json";

    private static final String FGM001_VALIDATE_WARNING_REQUEST_PATH =
            "classpath:request/bulk-scan-fgm001-validate-warning-input.json";

    private static final String FGM001_VALIDATE_WARNING_RESPONSE_PATH =
            "classpath:response/bulk-scan-fgm001-validate-warning-output.json";

    @Test
    void testFL401ValidationSuccess() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FGM001_VALIDATE_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FGM001_VALIDATE_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }

    @Test
    void testFL401ErrorWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FGM001_VALIDATE_ERROR_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FGM001_VALIDATE_ERROR_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }

    @Test
    void testFL401WarningWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FGM001_VALIDATE_WARNING_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FGM001_VALIDATE_WARNING_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }
}
