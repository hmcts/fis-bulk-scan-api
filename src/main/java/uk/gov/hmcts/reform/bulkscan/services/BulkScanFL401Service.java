package uk.gov.hmcts.reform.bulkscan.services;

import java.util.Map;
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
public class BulkScanFL401Service implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired BulkScanFL401ValidationService bulkScanFL401ValidationService;

    @Override
    public BulkScanValidationResponse validate(
            BulkScanValidationRequest bulkScanValidationRequest) {
        // Validating the Fields..
        Map<String, String> inputFieldsMap =
                getOcrDataFieldAsMap(bulkScanValidationRequest.getOcrdatafields());

        BulkScanValidationResponse response =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        bulkScanValidationRequest.getOcrdatafields(),
                        configManager.getValidationConfig(FormType.FL401));

        BulkScanValidationResponse applicantRespondentRelationshipRespons =
                bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                        inputFieldsMap, response);
        response.addWarning(applicantRespondentRelationshipRespons.getWarnings().getItems());
        response.addErrors(applicantRespondentRelationshipRespons.getErrors().getItems());
        response.changeStatus();

        return bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                bulkScanValidationRequest.getOcrdatafields(),
                configManager.getValidationConfig(FormType.FL401));
    }

    @Override
    public FormType getCaseType() {
        return FormType.FL401;
    }

    @Override
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        // TODO transformation logic
        return null;
    }
}
