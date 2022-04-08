package uk.gov.hmcts.reform.bulkscan.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BulkScanValidationServiceTest {

    @InjectMocks
    BulkScanValidationService bulkScanValidationService;

    @Test
     void testValidationServiceWithSuccessStatus() throws Exception {
        OcrDataField ocrDataField1 = new OcrDataField();
        OcrDataField ocrDataField2 = new OcrDataField();
        ocrDataField1.setName("appellant_firstName");
        ocrDataField1.setValue("firstName");
        ocrDataField2.setName("appellant_lastName");
        ocrDataField2.setValue("LastName");
        OcrDataField ocrDataField3 = new OcrDataField();
        ocrDataField3.setName("appellant_address");
        ocrDataField3.setValue("Address1 London");
        OcrDataField ocrDataField4 = new OcrDataField();
        ocrDataField4.setName("appellant_contactNumber");
        ocrDataField4.setValue("9876545782");
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        ocrArray.add(ocrDataField1);
        ocrArray.add(ocrDataField2);
        ocrArray.add(ocrDataField3);
        ocrArray.add(ocrDataField4);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcrdatafields(ocrArray);
        BulkScanValidationResponse bulkScanResponse =
            bulkScanValidationService.validateBulkService(bulkScanValidationRequest);
        assertEquals(bulkScanResponse.status, Status.SUCCESS);
    }

    @Test
     void testValidationServiceWithErrorStatusAndEmptyValue() throws Exception {
        OcrDataField ocrDataField1 = new OcrDataField();
        OcrDataField ocrDataField2 = new OcrDataField();
        ocrDataField1.setName("appellant_firstName");
        ocrDataField1.setValue("firstName");
        ocrDataField2.setName("appellant_lastName");
        ocrDataField2.setValue("LastName");
        OcrDataField ocrDataField3 = new OcrDataField();
        ocrDataField3.setName("appellant_address");
        ocrDataField3.setValue(" ");
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        ocrArray.add(ocrDataField1);
        ocrArray.add(ocrDataField2);
        ocrArray.add(ocrDataField3);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcrdatafields(ocrArray);
        BulkScanValidationResponse bulkScanResponse =
            bulkScanValidationService.validateBulkService(bulkScanValidationRequest);
        assertEquals(bulkScanResponse.status, Status.ERRORS);
    }

    @Test
     void testValidationServiceWithErrorStatusAndMissingField() throws Exception {
        OcrDataField ocrDataField1 = new OcrDataField();
        OcrDataField ocrDataField2 = new OcrDataField();
        ocrDataField1.setName("appellant_firstName");
        ocrDataField1.setValue("firstName");
        ocrDataField2.setName("appellant_lastName");
        ocrDataField2.setValue("LastName");
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        ocrArray.add(ocrDataField1);
        ocrArray.add(ocrDataField2);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcrdatafields(ocrArray);
        BulkScanValidationResponse bulkScanResponse =
            bulkScanValidationService.validateBulkService(bulkScanValidationRequest);
        assertNotNull(bulkScanResponse.getErrors());
        assertEquals(bulkScanResponse.status, Status.ERRORS);
    }

    @Test
    void testValidationServiceWithErrorStatusNullValue() throws Exception {
        OcrDataField ocrDataField1 = new OcrDataField();
        OcrDataField ocrDataField2 = new OcrDataField();
        ocrDataField1.setName("appellant_firstName");
        ocrDataField1.setValue("firstName");
        ocrDataField2.setName("appellant_lastName");
        ocrDataField2.setValue("LastName");
        OcrDataField ocrDataField3 = new OcrDataField();
        ocrDataField3.setName("appellant_address");
        ocrDataField3.setValue("Address1 London");
        OcrDataField ocrDataField4 = new OcrDataField();
        ocrDataField4.setName("appellant_contactNumber");
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        ocrArray.add(ocrDataField1);
        ocrArray.add(ocrDataField2);
        ocrArray.add(ocrDataField3);
        ocrArray.add(ocrDataField4);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcrdatafields(ocrArray);
        BulkScanValidationResponse bulkScanResponse =
            bulkScanValidationService.validateBulkService(bulkScanValidationRequest);
        assertNotNull(bulkScanResponse.getErrors());
        assertEquals(bulkScanResponse.status, Status.ERRORS);
    }

}
