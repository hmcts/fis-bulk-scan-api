package uk.gov.hmcts.reform.bulkscan.services;

import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

public interface BulkScanService {

    FormType getCaseType();

    BulkScanValidationResponse validate(BulkScanValidationRequest bulkScanValidationRequest);

    BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest);
}
