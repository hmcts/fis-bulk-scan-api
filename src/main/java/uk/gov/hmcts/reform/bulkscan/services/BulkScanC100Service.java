package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseType;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil;

import java.util.List;

@Service
public class BulkScanC100Service implements BulkScanService {

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Override
    public CaseType getCaseType() {
        return CaseType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        List<String> errors = BulkScanValidationUtil.validateFields(bulkRequest.getOcrdatafields(),
                                                       configManager.getValidationConfig(CaseType.C100));
        if (!errors.isEmpty()) {
            return BulkScanValidationResponse.builder().status(Status.ERRORS)
                .errors(Errors.builder().items(errors).build()).build();
        }
        return BulkScanValidationResponse.builder().status(Status.SUCCESS).build();
    }

    @Override
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        //TODO transformation logic
        return null;
    }
}
