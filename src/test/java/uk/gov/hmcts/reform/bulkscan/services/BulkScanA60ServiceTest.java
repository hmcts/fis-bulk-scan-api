package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import uk.gov.hmcts.reform.bulkscan.model.ScanDocument;
import uk.gov.hmcts.reform.bulkscan.model.ScannedDocuments;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import java.io.IOException;
import java.util.Collections;
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
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BULK_SCAN_APPLICANT_DETAILS_FIELD = "applicant";
    private static final String APPLICANT1_TEST_FIRSTNAME = "applicant1_firstName";
    private static final String APPLICANT1_TEST_LASTNAME = "applicant1_lastName";
    private static final String A60_TRANSFORM_RESPONSE_PATH
        = "classpath:response/bulk-scan-a60-transform-output.json";

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
    void testA60Error() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
    }

    @Test
    void testA60MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, APPLICANT1_TEST_FIRSTNAME)));
    }

    @Test
    void testA60FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, APPLICANT1_TEST_LASTNAME)));
    }

    @Test
    void testA60OptionalFieldsWarningsWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getWarnings().items
                       .contains(String.format(PHONE_NUMBER_MESSAGE, "applicant2_telephoneNumber")));
    }

    @DisplayName("Should record and report unknown field in the test data")
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


    @DisplayName("Should process transformation request and compare the reponse ")
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
                          .getCaseData().get(BULK_SCAN_APPLICANT_DETAILS_FIELD));
        assertTrue(caseData.get(BULK_SCAN_APPLICANT_DETAILS_FIELD).toString().contains(APPLICANT1_TEST_FIRSTNAME));
        assertTrue(caseData.get(BULK_SCAN_APPLICANT_DETAILS_FIELD).toString().contains(APPLICANT1_TEST_LASTNAME));
    }

    @DisplayName("Transformed data processed should be the same as the prepared json output file")
    @Test
    void testA60TransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService
                .transform(BulkScanTransformationRequest
                               .builder()
                               .scannedDocuments(Collections.emptyList())
                               .scannedDocuments(List.of(
                                   ScannedDocuments.builder()
                                       .scanDocument(ScanDocument.builder()
                                                         .url("url")
                                                         .binaryUrl("binary_url")
                                                         .filename("filename")
                                                         .build())
                                       .build(),
                                   ScannedDocuments.builder()
                                       .scanDocument(ScanDocument.builder()
                                                         .url("url1")
                                                         .binaryUrl("binary_url1")
                                                         .filename("filename1")
                                                         .build())
                                       .build()
                               ))
                               .ocrdatafields(TestDataUtil.getA60Data()).build());

        JSONAssert.assertEquals(
            readFileFrom(A60_TRANSFORM_RESPONSE_PATH),
            mapper.writeValueAsString(bulkScanTransformationResponse), true
        );
    }
}
