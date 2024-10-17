package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.FL401A;

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
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequestNew;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

@Service
public class BulkScanFL401AService implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Override
    public FormType getCaseType() {
        return FL401A;
    }

    /**
     * This method will validate FL401A form data.
     *
     * @param bulkScanValidationRequest is used to add error/warnings
     * @return BulkScanValidationResponse object
     */
    @Override
    public BulkScanValidationResponse validate(
            BulkScanValidationRequest bulkScanValidationRequest) {
        BulkScanValidationResponse response =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        bulkScanValidationRequest.getOcrdatafields(),
                        configManager.getValidationConfig(FL401A));

        response.changeStatus();

        return response;
    }

    /**
     * This method will transform FL401A form data to CCD fields.
     *
     * @param bulkScanTransformationRequest is used to store input fields to be transformed
     * @return BulkScanValidationResponse object used to store transformed ccd field and data
     */
    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        FormType formType = FL401A;

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        Map<String, Object> populatedMap =
                (Map)
                        BulkScanTransformHelper.transformToCaseData(
                                new HashMap<>(
                                        transformConfigManager
                                                .getTransformationConfig(formType)
                                                .getCaseDataFields()),
                                inputFieldsMap);

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
                transformConfigManager.getTransformationConfig(formType).getCaseFields();

        BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder =
                BulkScanTransformationResponse.builder()
                        .caseCreationDetails(
                                CaseCreationDetails.builder()
                                        .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                        .eventId(caseTypeAndEventId.get(EVENT_ID))
                                        .caseData(populatedMap)
                                        .build());

        return builder.build();
    }

    @Override
    public BulkScanTransformationResponse transformNew(
            BulkScanTransformationRequestNew bulkScanTransformationRequest) {
        return null;
    }
}
