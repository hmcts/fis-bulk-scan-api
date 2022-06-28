package uk.gov.hmcts.reform.bulkscan.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.bulkscan.endpoints.BulkScanEndpoint;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.C100;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.FL401;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.FL403;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.S2S_TOKEN;

@SpringBootTest
class BulkScanEndpointTest {

    @InjectMocks
    private BulkScanEndpoint bulkScanEndpoint;

    @MockBean
    PostcodeLookupService postcodeLookupService;

    @Test
    void testC100ValidationUnknownFormType() throws Exception {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getData()).build();
        ResponseEntity<?> response =
            bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, null, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testC100ValidationHappyPath() throws Exception {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getData()).build();
        when(postcodeLookupService.isValidPostCode("TW3 1NN", null)).thenReturn(true);
        ResponseEntity<?> response =
            bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, C100, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL401ValidationService() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getData()).build();
        ResponseEntity<?> response =
            bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, FL401, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL403ValidationService() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
            .ocrdatafields(TestDataUtil.getData()).build();
        ResponseEntity<?> response =
            bulkScanEndpoint.validateOcrData(S2S_TOKEN, CONTENT_TYPE, FL403, bulkScanValidationRequest);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testC100TransformService() {
        //Given
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder()
            .ocrdatafields(TestDataUtil.getData()).formType(
            C100.name()).build();

        //When
        ResponseEntity<BulkScanTransformationResponse> response =
                bulkScanEndpoint.transformationOcrData(S2S_TOKEN, CONTENT_TYPE, bulkScanTransformationRequest);
        //Then

        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL401TransformService() {
        //Given
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder()
            .ocrdatafields(TestDataUtil.getData()).formType(
                FL401.name()).build();

        //When
        ResponseEntity<BulkScanTransformationResponse> response =
                bulkScanEndpoint.transformationOcrData(S2S_TOKEN, CONTENT_TYPE, bulkScanTransformationRequest);
        //Then
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void testFL403TransformService() {
        //Given
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder()
            .ocrdatafields(TestDataUtil.getData()).formType(
                FL403.name()).build();

        //When
        ResponseEntity<BulkScanTransformationResponse> response =
                bulkScanEndpoint.transformationOcrData(S2S_TOKEN, CONTENT_TYPE, bulkScanTransformationRequest);
        //Then
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

}
