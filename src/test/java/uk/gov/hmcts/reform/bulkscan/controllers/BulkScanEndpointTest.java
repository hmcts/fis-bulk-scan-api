package uk.gov.hmcts.reform.bulkscan.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.bulkscan.endpoints.BulkScanEndpoint;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.C100;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.FL401;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.FL403;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.S2S_TOKEN;

@SpringBootTest
class BulkScanEndpointTest {

    @InjectMocks
    private BulkScanEndpoint bulkScanEndpoint;

    @Test
    void testC100ValidationHappyPath() throws Exception {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getC100Data()).build();
        ResponseEntity<?> response =
            bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, C100, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL401ValidationService() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getC100Data()).build();
        ResponseEntity<?> response =
            bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, FL401, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL403ValidationService() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getC100Data()).build();
        ResponseEntity<?> response =
            bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, FL403, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testC100TransformService() {
        //Given
        BulkScanTransformationRequest bulkScanTransformationRequest = mock(BulkScanTransformationRequest.class);

        //When
        ResponseEntity<BulkScanTransformationResponse> response =
                bulkScanEndpoint.transformOcrData(S2S_TOKEN, CONTENT_TYPE, C100, bulkScanTransformationRequest);
        //Then

        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL401TransformService() {
        //Given
        BulkScanTransformationRequest bulkScanTransformationRequest = mock(BulkScanTransformationRequest.class);

        //When
        ResponseEntity<BulkScanTransformationResponse> response =
                bulkScanEndpoint.transformOcrData(S2S_TOKEN, CONTENT_TYPE, FL401, bulkScanTransformationRequest);
        //Then
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL403TransformService() {
        //Given
        BulkScanTransformationRequest bulkScanTransformationRequest = mock(BulkScanTransformationRequest.class);

        //When
        ResponseEntity<BulkScanTransformationResponse> response =
                bulkScanEndpoint.transformOcrData(S2S_TOKEN, CONTENT_TYPE, FL403, bulkScanTransformationRequest);
        //Then
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testTransformationOcrDataWithSuccess() {
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        OcrDataField ocrDataField = new OcrDataField();
        ocrDataField.setName("Name");
        ocrDataField.setValue("Value");
        ocrArray.add(ocrDataField);
        BulkScanTransformationRequest bulkScanTransformationRequest = new BulkScanTransformationRequest();
        bulkScanTransformationRequest.setOcrdatafields(ocrArray);
        assertNotNull(bulkScanTransformationRequest.toString());
        String s2sToken = "serviceauthorization";
        String contentType = "content-type";
        ResponseEntity<BulkScanTransformationResponse> response =
            bulkScanEndpoint.transformationOcrData(s2sToken, contentType, bulkScanTransformationRequest);
        assertSame(response.getStatusCode(), HttpStatus.OK);

    }

}
