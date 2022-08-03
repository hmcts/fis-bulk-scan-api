package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Service
public class BulkScanA59Service implements BulkScanService {

    public static final String SCAN_DOCUMENTS = "scannedDocuments";

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

    @Override
    public FormType getCaseType() {
        return FormType.A59;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        return bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                bulkRequest.getOcrdatafields(), configManager.getValidationConfig(FormType.A59));
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
                configManager.getValidationConfig(FormType.valueOf(getCaseType().name()));

        List<String> unknownFieldsList =
                bulkScanValidationHelper.findUnknownFields(
                        inputFieldsList,
                        validationConfig.getMandatoryFields(),
                        validationConfig.getOptionalFields());

        Map<String, Object> populatedMap =
                (Map<String, Object>)
                        BulkScanTransformHelper.transformToCaseData(
                                new HashMap<>(
                                        transformConfigManager
                                                .getTransformationConfig(FormType.A59)
                                                .getCaseDataFields()),
                                inputFieldsMap);

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
                transformConfigManager
                        .getTransformationConfig(FormType.valueOf(getCaseType().name()))
                        .getCaseFields();

        BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder =
                BulkScanTransformationResponse.builder()
                        .caseCreationDetails(
                                CaseCreationDetails.builder()
                                        .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                        .eventId(caseTypeAndEventId.get(EVENT_ID))
                                        .caseData(populatedMap)
                                        .build());

        if (null != unknownFieldsList && !unknownFieldsList.isEmpty()) {
            builder.warnings(
                    Arrays.asList(
                            String.format(
                                    UNKNOWN_FIELDS_MESSAGE, String.join(",", unknownFieldsList))));
        }

        return builder.build();
    }
}
