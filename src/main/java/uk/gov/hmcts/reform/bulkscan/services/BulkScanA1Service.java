package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

@Service
public class BulkScanA1Service implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Override
    public FormType getCaseType() {
        return FormType.A1;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        return bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                bulkRequest.getOcrdatafields(), configManager.getValidationConfig(FormType.A1));
    }

    @Override
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        // TODO transformation logic
        return null;
    }
}
