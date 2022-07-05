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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT_ADVANCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT_AGENCY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_NO_CONSENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_CHILD_WELFARE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_PARENT_LACK_CAPACITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_PARENT_NOT_FOUND;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A58_RELINQUISHED_ADOPTION;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A58_STEP_PARENT;

@Service
public class BulkScanA58Service implements BulkScanService {

    public static final String STEP_PARENT_ADOPTION = "Step Parent";
    public static final String RELINQUISHED_ADOPTION = "Relinquished Adoption";

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Override
    public FormType getCaseType() {
        return FormType.A58;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        return bulkScanValidationHelper.validateMandatoryAndOptionalFields(
            bulkRequest.getOcrdatafields(),
            configManager.getValidationConfig(FormType.A58)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        String formType = null;

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = inputFieldsList.stream().collect(Collectors.toMap(
            OcrDataField::getName,
            OcrDataField::getValue
        ));

        if (STEP_PARENT_ADOPTION.equalsIgnoreCase(inputFieldsMap.get(APPLICANT1_RELATION_TO_CHILD))
            || STEP_PARENT_ADOPTION.equalsIgnoreCase(
            inputFieldsMap.get(APPLICANT2_RELATION_TO_CHILD))
            || TRUE.equalsIgnoreCase(inputFieldsMap.get(
            APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER))
            || FALSE.equalsIgnoreCase(inputFieldsMap.get(
            APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER))) {
            formType = A58_STEP_PARENT.name();
        } else if (nonNull(inputFieldsMap.get(ADOPTION_ORDER_CONSENT))
            || inputFieldsMap.get(ADOPTION_ORDER_CONSENT) == null
            || nonNull(inputFieldsMap.get(ADOPTION_ORDER_CONSENT_ADVANCE))
            || inputFieldsMap.get(ADOPTION_ORDER_CONSENT_ADVANCE) == null
            || nonNull(inputFieldsMap.get(ADOPTION_ORDER_CONSENT_AGENCY))
            || inputFieldsMap.get(ADOPTION_ORDER_CONSENT_AGENCY) == null
            || nonNull(inputFieldsMap.get(ADOPTION_ORDER_NO_CONSENT))
            || inputFieldsMap.get(ADOPTION_ORDER_NO_CONSENT) == null
            || nonNull(inputFieldsMap.get(COURT_CONSENT_PARENT_NOT_FOUND))
            || inputFieldsMap.get(COURT_CONSENT_PARENT_NOT_FOUND) == null
            || nonNull(inputFieldsMap.get(COURT_CONSENT_PARENT_LACK_CAPACITY))
            || inputFieldsMap.get(COURT_CONSENT_PARENT_LACK_CAPACITY) == null
            || nonNull(inputFieldsMap.get(COURT_CONSENT_CHILD_WELFARE))
            || inputFieldsMap.get(COURT_CONSENT_CHILD_WELFARE) == null) {
            formType = A58_RELINQUISHED_ADOPTION.name();
        }

        Map<String, Object> populatedMap = (Map<String, Object>) BulkScanTransformHelper.transformToCaseData(
            transformConfigManager.getTransformationConfig(FormType.valueOf(formType)).getCaseDataFields(),
            inputFieldsMap
        );

        Map<String, String> caseTypeAndEventId = transformConfigManager.getTransformationConfig(FormType.valueOf(
            formType)).getCaseFields();

        return BulkScanTransformationResponse.builder()
            .caseCreationDetails(CaseCreationDetails.builder()
                                     .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                     .eventId(caseTypeAndEventId.get(EVENT_ID))
                                     .caseData(populatedMap).build()).build();
    }
}
