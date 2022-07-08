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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A60;

@Service
public class BulkScanA60Service implements BulkScanService {
    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Override
    public FormType getCaseType() {
        return A60;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        return bulkScanValidationHelper.validateMandatoryAndOptionalFields(
            bulkRequest.getOcrdatafields(),
            configManager.getValidationConfig(
                A60)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        FormType formType = A60;

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        BulkScanFormValidationConfigManager
            .ValidationConfig validationConfig = configManager
            .getValidationConfig(FormType.valueOf(getCaseType().name()));

        List<String> unknownFieldsList = bulkScanValidationHelper
            .findUnknownFields(
                inputFieldsList,
                validationConfig.getMandatoryFields(),
                validationConfig.getOptionalFields()
            );

        Map<String, Object> populatedMap = (Map) BulkScanTransformHelper
            .transformToCaseData(transformConfigManager
                                     .getTransformationConfig(formType).getCaseDataFields(), inputFieldsMap);

        Map<String, String> caseTypeAndEventId =
            transformConfigManager.getTransformationConfig(formType).getCaseFields();

        BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder = BulkScanTransformationResponse
            .builder().caseCreationDetails(
                CaseCreationDetails.builder()
                    .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                    .eventId(caseTypeAndEventId.get(EVENT_ID))
                    .caseData(populatedMap).build());

        if (null != unknownFieldsList && !unknownFieldsList.isEmpty()) {
            builder.warnings(Arrays.asList(String.format(
                UNKNOWN_FIELDS_MESSAGE,
                String.join(",", unknownFieldsList)
            )));
        }
        return builder.build();
    }
}
