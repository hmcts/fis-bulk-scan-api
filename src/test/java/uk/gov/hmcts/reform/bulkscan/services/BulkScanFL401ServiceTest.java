package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFL401ServiceTest {

    @Spy @Autowired BulkScanFL401Service bulkScanService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired BulkScanFL401Service bulkScanValidationService;

    private static final String FL401_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-transform-input.json";

    private static final String FL401_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl401-transform-output.json";

    @Test
    @DisplayName("FL401 transform success.")
    void testFL401TransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(FL401_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FL401_TRANSFORM_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }

    @Test
    void testFL401Success() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getFL401Data())
                        .build();
        BulkScanValidationResponse res = bulkScanService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testFL401MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getErrorData())
                        .build();
        BulkScanValidationResponse res = bulkScanService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(
                res.getErrors()
                        .items
                        .contains(String.format(MANDATORY_ERROR_MESSAGE, "appellant_lastName")));
    }

    @Test
    void testFL401FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getFirstNameData())
                        .build();
        BulkScanValidationResponse res = bulkScanService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(
                res.getErrors()
                        .items
                        .contains(String.format(MISSING_FIELD_MESSAGE, "appellant_lastName")));
    }
}
