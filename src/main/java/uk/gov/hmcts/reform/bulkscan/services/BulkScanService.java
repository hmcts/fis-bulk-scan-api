package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;

@Service
public interface BulkScanService {
    BulkScanValidationResponse validate(BulkScanValidationRequest bulkScanValidationRequest);

    BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest);
}
