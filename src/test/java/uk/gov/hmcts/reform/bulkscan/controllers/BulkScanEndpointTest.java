package uk.gov.hmcts.reform.bulkscan.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.bulkscan.endpoints.BulkScanEndpoint;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;



@ExtendWith(MockitoExtension.class)
class BulkScanEndpointTest {

    @InjectMocks
    private BulkScanEndpoint bulkScanEndpoint;

    @Test
    void testvalidateOcrDataWithSuccess() {
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        OcrDataField ocrDataField = new OcrDataField();
        ocrDataField.setName("Name");
        ocrDataField.setValue("Value");
        ocrArray.add(ocrDataField);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcrdatafields(ocrArray);
        assertNotNull(bulkScanValidationRequest.toString());
        //BulkScanValidationResponse bulkScanValidationResponse = new BulkScanValidationResponse();
        String s2sToken = "serviceauthorization";
        String contentType = "content-type";
        ResponseEntity<BulkScanValidationResponse> response =
            bulkScanEndpoint.validateOcrData(s2sToken, contentType, bulkScanValidationRequest);
        assertSame(response.getStatusCode(), HttpStatus.OK);
        //assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void testvalidateOcrDataWithWarnings() {
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        OcrDataField ocrDataField = new OcrDataField();
        ocrDataField.setName("ocrdatafieldname");
        ocrDataField.setValue("ocrdatafieldvalue");
        ocrArray.add(ocrDataField);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcrdatafields(ocrArray);
        BulkScanValidationResponse bulkScanValidationResponse = new BulkScanValidationResponse();
        bulkScanValidationResponse.setErrors(null);
        String s2sToken = "serviceauthorization";
        String contentType = "content-type";
        ResponseEntity<BulkScanValidationResponse> response =
            bulkScanEndpoint.validateOcrData(s2sToken, contentType, bulkScanValidationRequest);
        assertSame(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        //assertEquals(response.getStatusCode(), HttpStatus.OK);

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
        //BulkScanTransformationResponse bulkScanTransformationResponse = new BulkScanTransformationResponse();
        String s2sToken = "serviceauthorization";
        String contentType = "content-type";
        ResponseEntity<BulkScanTransformationResponse> response =
            bulkScanEndpoint.transformationOcrData(s2sToken, contentType, bulkScanTransformationRequest);
        assertSame(response.getStatusCode(), HttpStatus.OK);
        //assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

}
