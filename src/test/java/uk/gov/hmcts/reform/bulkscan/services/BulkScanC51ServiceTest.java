package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_HOME_TELEPHONE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MOBILE_TELEPHONE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.APPLICANT_1_FIRST_NAME;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.APPLICANT_1_LAST_NAME;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.ScanDocument;
import uk.gov.hmcts.reform.bulkscan.model.ScannedDocuments;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC51ServiceTest {

    @Autowired BulkScanC51Service bulkScanValidationService;

    private static final String C51_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-c51-transform-output.json";

    @Test
    void testC51Success() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getC51Data())
                        .build();
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testC51MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getC51ErrorData())
                        .build();
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(
                res.getErrors()
                        .contains(String.format(MANDATORY_ERROR_MESSAGE, APPLICANT_1_FIRST_NAME)));
    }

    @Test
    void testC51FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getC51ErrorData())
                        .build();
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(
                res.getErrors()
                        .contains(String.format(MISSING_FIELD_MESSAGE, APPLICANT_1_LAST_NAME)));
    }

    @Test
    void testC51ConditionalFieldMissingErrorWhileDoingValidation() {
        List<String> conditionalFields =
                List.of(APPLICANT_HOME_TELEPHONE_NUMBER, APPLICANT_MOBILE_TELEPHONE_NUMBER);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getC51ConditionalFieldErrorData())
                        .build();
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(
                res.getErrors()
                        .contains(
                                String.format(
                                        BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE,
                                        conditionalFields)));
    }

    @Test
    void testTransform() throws IOException, JSONException {

        ObjectMapper mapper = new ObjectMapper();
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(
                        BulkScanTransformationRequest.builder()
                                .scannedDocuments(Collections.emptyList())
                                .scannedDocuments(
                                        List.of(
                                                ScannedDocuments.builder()
                                                        .scanDocument(
                                                                ScanDocument.builder()
                                                                        .url("url1")
                                                                        .binaryUrl("binary_url1")
                                                                        .filename("filename1")
                                                                        .build())
                                                        .build()))
                                .ocrdatafields(TestDataUtil.getC51Data())
                                .build());

        JSONAssert.assertEquals(
                readFileFrom(C51_TRANSFORM_RESPONSE_PATH),
                mapper.writeValueAsString(bulkScanTransformationResponse),
                true);
    }
}
