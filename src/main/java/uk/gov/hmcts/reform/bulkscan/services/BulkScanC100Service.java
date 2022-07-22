package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
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
    BulkScanDependencyValidationService dependencyValidationService;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        List<String> warningItems = new ArrayList<>();
        Map<String, String> inputFieldMap = getOcrDataFieldAsMap(bulkRequest.getOcrdatafields());

        BulkScanValidationResponse bulkScanValidationResponse =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        bulkRequest.getOcrdatafields(),
                        configManager.getValidationConfig(
                                FormType.C100)
                );

        bulkScanValidationResponse.addWarning(dependencyValidationService
                                                  .getDependencyWarnings(inputFieldMap, FormType.C100));

       bulkScanValidationResponse.changeStatus();

       return bulkScanValidationResponse;
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
