package uk.gov.hmcts.reform.bulkscan.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.bulkscan.endpoints.BulkScanEndpoint;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;

import java.util.ArrayList;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class BulkScanEndpointTest {

    @InjectMocks
    private BulkScanEndpoint bulkScanEndpoint;

    @Mock
    private Warnings warnings;

@Test
public void testvalidateOcrDataWithSuccess(){

    String s2sToken="serviceauthorization";
    String contentType="content-type";
    ArrayList<OcrDataField> ocrArray=new ArrayList<>();
    OcrDataField ocrDataField=new OcrDataField();
   ocrDataField.setName("Name");
    ocrDataField.setValue("Value");
    ocrArray.add(ocrDataField);
    BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
    bulkScanValidationRequest.setOcr_data_fields(ocrArray);
    assertNotNull(bulkScanValidationRequest.toString());
    BulkScanValidationResponse bulkScanValidationResponse=new BulkScanValidationResponse();
    ResponseEntity<BulkScanValidationResponse> response=bulkScanEndpoint.validateOcrData(s2sToken, contentType, bulkScanValidationRequest);
   assertEquals(response.getStatusCode(), HttpStatus.OK);

}

    @Test
    public void testvalidateOcrDataWithWarnings(){

        String s2sToken="serviceauthorization";
        String contentType="content-type";
        ArrayList<OcrDataField> ocrArray=new ArrayList<>();
        OcrDataField ocrDataField=new OcrDataField();
        ocrDataField.setName("ocrdatafieldname");
        ocrDataField.setValue("ocrdatafieldvalue");
        ocrArray.add(ocrDataField);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcr_data_fields(ocrArray);
        BulkScanValidationResponse bulkScanValidationResponse=new BulkScanValidationResponse();
        bulkScanValidationResponse.setErrors(null);
        ResponseEntity<BulkScanValidationResponse> response=bulkScanEndpoint.validateOcrData(s2sToken, contentType, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

}
