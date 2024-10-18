package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_TRANSFORM_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_TRANSFORM_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_VALIDATE_ERROR_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_VALIDATE_ERROR_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_VALIDATE_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_VALIDATE_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_VALIDATE_WARNING_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_VALIDATE_WARNING_RESPONSE_PATH;
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
class BulkScanC63ServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired BulkScanC63Service bulkScanValidationService;

    @Test
    @DisplayName("C63 validating input fields")
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
    @DisplayName("C63 validating with a missing mandatory field")
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
    @DisplayName("C63 Validating mandatory fields with a warning for an unknown field")
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

    @Test
    void testC63TransformNew() {
        BulkScanTransformationRequestNew bulkScanTransformationRequest =
                new BulkScanTransformationRequestNew();

        BulkScanTransformationResponse res =
                bulkScanValidationService.transformNew(bulkScanTransformationRequest);
        Assertions.assertNull(res);
    }
}
