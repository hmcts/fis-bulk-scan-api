package uk.gov.hmcts.reform.bulkscan.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.bulkscan.endpoints.BulkScanEndpoint;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class BulkScanEndpointTest {

    @InjectMocks
    private BulkScanEndpoint bulkScanEndpoint;

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
    assertNotNull(bulkScanEndpoint.validateOcrData(s2sToken,contentType,bulkScanValidationRequest));

}

}
