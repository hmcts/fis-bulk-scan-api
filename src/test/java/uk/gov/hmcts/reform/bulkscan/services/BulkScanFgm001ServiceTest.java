package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PHONE_NUMBER_MESSAGE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFgm001ServiceTest {

    @Spy @Autowired BulkScanFgm001Service bulkScanValidationService;

    @Test
    void testA58Success() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getFgm001Data())
                        .build();
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testA58FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getFgm001ErrorData())
                        .build();
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(
                res.getErrors()
                        .items
                        .contains(String.format(MISSING_FIELD_MESSAGE, "applicant_fullName")));
    }

    @Test
    void testA58OptionalFieldsWarningsWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataUtil.getFgm001ErrorsData())
                        .build();
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(
                res.getErrors()
                        .items
                        .contains(String.format(DATE_FORMAT_MESSAGE, "applicant_dateOfBirth")));
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(
                                String.format(PHONE_NUMBER_MESSAGE, "applicant_telephoneNumber")));
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNull(bulkScanTransformationResponse);
    }
}
