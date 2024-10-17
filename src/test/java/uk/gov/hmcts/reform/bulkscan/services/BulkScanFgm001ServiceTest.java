package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_TRANSFORM_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_TRANSFORM_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_VALIDATE_ERROR_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_VALIDATE_ERROR_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_VALIDATE_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_VALIDATE_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_VALIDATE_WARNING_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.FGM001_VALIDATE_WARNING_RESPONSE_PATH;
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
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequestNew;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFgm001ServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired BulkScanFgm001Service bulkScanValidationService;

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

    @DisplayName("FGM001 transform success.")
    @Test
    void testFgm001TransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(FGM001_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FGM001_TRANSFORM_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }

    @Test
    void testFgm001TransformNew() {
        BulkScanTransformationRequestNew bulkScanTransformationRequest =
                new BulkScanTransformationRequestNew();

        BulkScanTransformationResponse res =
                bulkScanValidationService.transformNew(bulkScanTransformationRequest);
        Assertions.assertNull(res);
    }
}
