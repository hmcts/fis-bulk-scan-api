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
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;

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
    BulkScanC100FieldDependencyService bulkScanGroupDependencyValidation;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        List<String> warningItems = new ArrayList<>();

        BulkScanValidationResponse bulkScanGroupDependencyValidationResponse =
                bulkScanGroupDependencyValidation.validate(bulkRequest);

        BulkScanValidationResponse bulkScanValidationResponse =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        bulkRequest.getOcrdatafields(),
                        configManager.getValidationConfig(
                                FormType.C100)
                );
        if (bulkScanGroupDependencyValidationResponse.getWarnings().getItems() != null) {
            warningItems.addAll(bulkScanGroupDependencyValidationResponse.getWarnings().getItems());
        }
        if (bulkScanValidationResponse.getWarnings().getItems() != null) {
            warningItems.addAll(bulkScanValidationResponse.getWarnings().getItems());
        }

        List<String> errorItems = bulkScanValidationResponse.getErrors().getItems();

        Status status = (Status.WARNINGS == bulkScanValidationResponse.getStatus())
                ? Status.WARNINGS : (Status.ERRORS == bulkScanValidationResponse.getStatus())
                ? Status.ERRORS : (Status.WARNINGS == bulkScanGroupDependencyValidationResponse.getStatus())
                ? Status.WARNINGS : (Status.ERRORS == bulkScanGroupDependencyValidationResponse.getStatus())
                ? Status.ERRORS : Status.SUCCESS;

        return BulkScanValidationResponse.builder()
                .warnings(Warnings.builder().items(warningItems).build())
                .errors(Errors.builder().items(errorItems).build())
                .status(status)
                .build();
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
