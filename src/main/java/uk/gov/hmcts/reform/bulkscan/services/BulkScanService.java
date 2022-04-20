package uk.gov.hmcts.reform.bulkscan.services;

import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseType;

public interface BulkScanService {

    CaseType getCaseType();

    BulkScanValidationResponse validate(BulkScanValidationRequest bulkScanValidationRequest);

    BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest);
}
