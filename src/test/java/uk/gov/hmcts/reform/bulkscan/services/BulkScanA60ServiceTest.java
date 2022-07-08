package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
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
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PHONE_NUMBER_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanA60ServiceTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String A60_TRANSFORM_RESPONSE_PATH
        = "classpath:response/bulk-scan-a60-transform-output.json";

    @Spy
    @Autowired
    BulkScanA60Service bulkScanValidationService;

    @Test
    void testA60Success() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60Data()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testA60MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60OrC63orA58ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "applicant1_firstName")));
    }

    @Test
    void testA60FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60OrC63orA58ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, "applicant1_lastName")));
    }

    @Test
    void testA60OptionalFieldsWarningsWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60OrC63orA58ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getWarnings().items
                       .contains(String.format(PHONE_NUMBER_MESSAGE, "applicant2_telephoneNumber")));
    }

    @Test
    void testA60UnknownFieldsWarningsWhileDoingValidation() {
        List<OcrDataField> ocrDataFieldLst = TestDataUtil.getA60DataWithUnknownField();
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest
            .builder()
            .ocrdatafields(ocrDataFieldLst)
            .build();

        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(bulkScanTransformationRequest);

        assertTrue(bulkScanTransformationResponse.getWarnings()
                       .contains(String.format(UNKNOWN_FIELDS_MESSAGE, "applicant1_unknownField")));
    }



    @Test
    void testA60Transform() throws IOException, JSONException {
        List<OcrDataField> ocrDataFieldLst = TestDataUtil.getA60Data();
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest
            .builder()
            .ocrdatafields(ocrDataFieldLst)
            .build();

        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(bulkScanTransformationRequest);

        Map<String, Object> caseData = new LinkedHashMap<>(bulkScanTransformationResponse.getCaseCreationDetails()
                                                               .getCaseData());

        assertNotNull(bulkScanTransformationResponse.getCaseCreationDetails()
                                     .getCaseData().get("applicant"));
        assertTrue(caseData.get("applicant").toString().contains("applicant1_firstName"));
        assertTrue(caseData.get("applicant").toString().contains("applicant1_lastName"));
        assertTrue(caseData.get("applicant").toString().contains("123 test street, London"));
    }

    @Test
    void testA60TransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService
                .transform(BulkScanTransformationRequest
                               .builder()
                               .ocrdatafields(TestDataUtil.getA60Data()).build());

        JSONAssert.assertEquals(
            readFileFrom(A60_TRANSFORM_RESPONSE_PATH),
            mapper.writeValueAsString(bulkScanTransformationResponse), true
        );
    }
}
