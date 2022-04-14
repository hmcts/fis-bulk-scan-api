package uk.gov.hmcts.reform.bulkscan.controllers;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.bulkscan.endpoints.BulkScanEndpoint;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanC100Service;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanFL401Service;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanFL403Service;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.C100;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.FL401;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.FL403;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.S2S_TOKEN;

@ExtendWith(MockitoExtension.class)
class BulkScanEndpointTest {

    @InjectMocks
    private BulkScanEndpoint bulkScanEndpoint;

    @Mock
    BulkScanC100Service bulkScanValidationService;

    @Mock
    BulkScanC100Service bulkScanC100Service;

    @Mock
    BulkScanFL401Service bulkScanFL401Service;

    @Mock
    BulkScanFL403Service bulkScanFL403Service;

    @Test
    void testvalidateOcrDataWithSuccess() throws Exception {
        OcrDataField ocrDataField = new OcrDataField();
        ocrDataField.setName("appellant_firstName");
        ocrDataField.setValue("firstName");
        ocrDataField.setName("appellant_lastName");
        ocrDataField.setValue("LastName");
        ocrDataField.setName("appellant_address");
        ocrDataField.setValue("Address1 London");
        ArrayList<OcrDataField> ocrArray = new ArrayList<>();
        ocrArray.add(ocrDataField);
        BulkScanValidationRequest bulkScanValidationRequest = new BulkScanValidationRequest();
        bulkScanValidationRequest.setOcrdatafields(ocrArray);
        String s2sToken = "serviceauthorization";
        String contentType = "content-type";
        ResponseEntity<BulkScanValidationResponse> response =
            bulkScanEndpoint.validateOcrData(s2sToken,contentType, C100, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL401ValidationService() {
        //Given
        BulkScanValidationRequest bulkScanValidationRequest = mock(BulkScanValidationRequest.class);

        //When
        ResponseEntity<BulkScanValidationResponse> response =
                bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, FL401, bulkScanValidationRequest);
        //Then
        verify(bulkScanFL401Service).validate(bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL403ValidationService() {
        //Given
        BulkScanValidationRequest bulkScanValidationRequest = mock(BulkScanValidationRequest.class);

        //When
        ResponseEntity<BulkScanValidationResponse> response =
                bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, FL403, bulkScanValidationRequest);
        //Then
        verify(bulkScanFL403Service).validate(bulkScanValidationRequest);
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
        verify(bulkScanC100Service).transform(bulkScanTransformationRequest);
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
        verify(bulkScanFL401Service).transform(bulkScanTransformationRequest);
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
        verify(bulkScanFL403Service).transform(bulkScanTransformationRequest);
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
