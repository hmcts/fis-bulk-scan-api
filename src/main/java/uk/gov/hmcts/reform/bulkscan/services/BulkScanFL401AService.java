package uk.gov.hmcts.reform.bulkscan.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

@NoArgsConstructor
@Service
public class BulkScanFL401AService implements BulkScanService {

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Override
    public FormType getCaseType() {
        return FormType.FL401A;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        return BulkScanValidationHelper.validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(),
                                                                           configManager.getValidationConfig(
                                                                               FormType.FL401A));
    }

    @Override
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        //TODO transformation logic
        return null;
    }
}
