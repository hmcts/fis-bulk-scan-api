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

import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A58;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A58_STEP_PARENT;

@Service
public class BulkScanA58Service implements BulkScanService {

    public static final String STEP_PARENT_ADOPTION = "Step Parent";
    public static final String RELINQUISHED_ADOPTION = "Relinquished Adoption";
    public static final String SCAN_DOCUMENTS = "scannedDocuments";

    @Autowired
    BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Override
    public FormType getCaseType() {
        return A58;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        FormType formType = A58;
        if (isA58ParentFormType(getOcrDataFieldAsMap(bulkRequest.getOcrdatafields()))) {
            formType = A58_STEP_PARENT;
        }
        // Validating the Fields..
        return bulkScanValidationHelper.validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(),
                        formType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        FormType formType = A58;

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        if (isA58ParentFormType(inputFieldsMap)) {
            formType = A58_STEP_PARENT;
        }

        // Validating if any unknown fields present or not. if exist then it should go as warnings.
        List<String> unknownFieldsList = bulkScanValidationHelper.findUnknownFields(inputFieldsList,
                formType);

        //TODO RELINQUISHED_ADOPTION condition to be added as part of ISDB-269

        Map<String, Object> populatedMap = (Map<String, Object>) BulkScanTransformHelper
                .transformToCaseData(transformConfigManager
                        .getTransformationConfig(formType).getCaseDataFields(), inputFieldsMap);

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
                transformConfigManager.getTransformationConfig(formType).getCaseFields();

        BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder = BulkScanTransformationResponse
                .builder().caseCreationDetails(
                        CaseCreationDetails.builder()
                                .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                .eventId(caseTypeAndEventId.get(EVENT_ID))
                                .caseData(populatedMap).build());
        if (null != unknownFieldsList && !unknownFieldsList.isEmpty()) {
            builder.warnings(Arrays.asList(String.format(UNKNOWN_FIELDS_MESSAGE,
                    String.join(",", unknownFieldsList))));
        }
        return builder.build();
    }

    private boolean isA58ParentFormType(Map<String, String> inputFieldsMap) {
        return STEP_PARENT_ADOPTION.equalsIgnoreCase(inputFieldsMap.get(APPLICANT1_RELATION_TO_CHILD))
                || STEP_PARENT_ADOPTION.equalsIgnoreCase(inputFieldsMap.get(APPLICANT2_RELATION_TO_CHILD))
                || TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER))
                || FALSE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER));
    }
}
