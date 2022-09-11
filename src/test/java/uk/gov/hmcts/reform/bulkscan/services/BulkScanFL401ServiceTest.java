package uk.gov.hmcts.reform.bulkscan.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.EMPTY_STRING;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import uk.gov.hmcts.reform.bulkscan.exception.PostCodeValidationException;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFL401ServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired BulkScanFL401Service bulkScanValidationService;

    @MockBean PostcodeLookupService postcodeLookupService;

    private static final String FL401_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-transform-input.json";

    private static final String FL401_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl401-transform-output.json";

    private static final String FL401_VALIDATE_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-validate-input.json";

    private static final String FL401_VALIDATE_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl401-validate-output.json";

    private static final String FL401_VALIDATE_ERROR_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-validate-error-input.json";

    private static final String FL401_VALIDATE_ERROR_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl401-validate-error-output.json";

    private static final String FL401_VALIDATE_WARNING_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-validate-warning-input.json";

    private static final String FL401_VALIDATE_WARNING_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl401-validate-warning-output.json";

    public static final String INVALID_POST_CODE = "cv2 A4nz";

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
    void testFL401ValidationSuccess() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATE_REQUEST_PATH), BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FL401_VALIDATE_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }

    @Test
    void testFL401ErrorWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATE_ERROR_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FL401_VALIDATE_ERROR_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }

    @Test
    void testFL401WarningWhileDoingValidation() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATE_WARNING_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        when(postcodeLookupService.isValidPostCode(INVALID_POST_CODE, null)).thenReturn(false);
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FL401_VALIDATE_WARNING_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }

    @Test
    void testFL401WarningWhileDoingValidationInvalidPostCode() throws IOException, JSONException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATE_WARNING_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        when(postcodeLookupService.isValidPostCode(anyString(), any()))
                .thenThrow(
                        new PostCodeValidationException(
                                EMPTY_STRING,
                                new HttpClientErrorException(HttpStatus.BAD_REQUEST)));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FL401_VALIDATE_WARNING_RESPONSE_PATH),
                mapper.writeValueAsString(res),
                true);
    }
}
