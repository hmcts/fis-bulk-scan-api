package uk.gov.hmcts.reform.bulkscan.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.model.Status.ERRORS;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil.buildBulkScanValidationRequest;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil.buildValidationConfig;

@ExtendWith(MockitoExtension.class)
class BulkScanFL403ServiceTest {

    @InjectMocks
    BulkScanFL403Service bulkScanFL403Service;

    @Mock
    BulkScanFormValidationConfigManager bulkScanFormValidationConfigManager;

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanFL403Service.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNull(bulkScanTransformationResponse);
    }

    @Test
    void testValidateForFL403() {
        //Given
        when(bulkScanFormValidationConfigManager.getValidationConfig(FormType.FL403))
                .thenReturn(buildValidationConfig());
        //When
        BulkScanValidationResponse bulkScanValidationResponse =
                bulkScanFL403Service.validate(buildBulkScanValidationRequest());

        //Then
        Assertions.assertNotNull(bulkScanValidationResponse);
        assertThat(bulkScanValidationResponse.getStatus()).isEqualTo(ERRORS);
        Assertions.assertTrue(bulkScanValidationResponse.getErrors().getItems()
                .containsAll(List.of("solicitor_name should not be null or empty",
                        "solicitor_telephone_number is in the wrong format")));
    }
}
