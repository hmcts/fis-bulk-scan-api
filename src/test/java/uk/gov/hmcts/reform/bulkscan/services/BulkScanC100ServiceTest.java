package uk.gov.hmcts.reform.bulkscan.services;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@Ignore
class BulkScanC100ServiceTest {

    @InjectMocks
    BulkScanC100Service bulkScanValidationService;

    @Mock
    BulkScanFormValidationConfigManager configManager;


    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNull(bulkScanTransformationResponse);
    }

}
