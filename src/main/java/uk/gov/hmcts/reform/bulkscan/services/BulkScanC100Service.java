package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;

@Service
public class BulkScanC100Service implements BulkScanService {

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Autowired
    BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired
    BulkScanC100GroupDependencyValidation bulkScanGroupDependencyValidation;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        BulkScanValidationResponse bulkScanGroupDependencyValidationResponse =
            bulkScanGroupDependencyValidation.validate(bulkRequest);
        // Validating the Fields..
        BulkScanValidationResponse bulkScanValidationResponse = bulkScanValidationHelper.validateMandatoryAndOptionalFields(
            bulkRequest.getOcrdatafields(),
            configManager.getValidationConfig(
                FormType.C100)
        );

        List<String> warningItems = bulkScanGroupDependencyValidationResponse.getWarnings().getItems();
        warningItems.addAll(bulkScanGroupDependencyValidationResponse.getWarnings().getItems());

        List<String> errorItems = bulkScanGroupDependencyValidationResponse.getErrors().getItems();
        errorItems.addAll(bulkScanGroupDependencyValidationResponse.getErrors().getItems());

        Status status = Status.SUCCESS;

        if (Status.SUCCESS != bulkScanValidationResponse.getStatus()
            || Status.SUCCESS != bulkScanGroupDependencyValidationResponse.getStatus()) {
            status = Status.ERRORS;
        }

        BulkScanValidationResponse validationResponse = BulkScanValidationResponse.builder()
            .warnings(Warnings.builder().items(warningItems).build())
            .errors(Errors.builder().items(errorItems).build())
            .status(status)
            .build();

        return validationResponse;
    }


    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = inputFieldsList.stream().collect(
            Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        Map<String, Object> populatedMap = (Map<String, Object>) BulkScanTransformHelper
            .transformToCaseData(transformConfigManager.getTransformationConfig(FormType.C100)
                                     .getCaseDataFields(), inputFieldsMap);


        return BulkScanTransformationResponse.builder().caseCreationDetails(
            CaseCreationDetails.builder().caseData(populatedMap).build()).build();

    }
}
