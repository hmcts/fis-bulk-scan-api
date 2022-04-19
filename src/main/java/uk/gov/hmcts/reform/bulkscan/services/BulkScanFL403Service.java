package uk.gov.hmcts.reform.bulkscan.services;


import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;


@NoArgsConstructor
@Service
public class BulkScanFL403Service implements BulkScanService {
    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkScanValidationRequest) {
        //TODO validation logic
        return null;
    }

    @Override
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        //TODO transformation logic
        return null;
    }
}
