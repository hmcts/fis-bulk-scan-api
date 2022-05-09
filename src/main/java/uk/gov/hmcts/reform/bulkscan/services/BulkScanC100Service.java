package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.transformation.BulkScanRequestToOcrDataTransformation;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.lang.reflect.InvocationTargetException;

@Service
public class BulkScanC100Service implements BulkScanService {

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanRequestToOcrDataTransformation bulkScanRequestToOcrDataTransformation;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        return BulkScanValidationHelper.validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(),
                                                                          configManager.getValidationConfig(
                                                                              FormType.C100));
    }

    @Override
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return bulkScanRequestToOcrDataTransformation.transform(bulkScanTransformationRequest);
    }
}
