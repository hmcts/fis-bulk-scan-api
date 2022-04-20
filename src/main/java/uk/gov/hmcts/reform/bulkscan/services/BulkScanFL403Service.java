package uk.gov.hmcts.reform.bulkscan.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.exception.BulkScanValidationException;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseType;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil;

import java.util.List;

@NoArgsConstructor
@Service
public class BulkScanFL403Service implements BulkScanService {

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkScanValidationRequest) {
        // Validating the Fields..
        List<String> errors = BulkScanValidationUtil.validateFields(bulkScanValidationRequest.getOcrdatafields(),
                                                                    configManager.getValidationConfig(CaseType.C100));
        if (!errors.isEmpty()) {
            throw new BulkScanValidationException(Errors.builder().items(errors).build());
        }
        return BulkScanValidationResponse.builder().status(Status.SUCCESS).build();
    }

    @Override
    public FormType getCaseType() {
        return FormType.FL403;
    }

    @Override
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        //TODO transformation logic
        return null;
    }
}
