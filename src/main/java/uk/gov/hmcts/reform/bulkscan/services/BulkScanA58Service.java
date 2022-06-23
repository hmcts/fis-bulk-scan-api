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

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.*;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.*;

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
            configManager.getValidationConfig(
                FormType.A58)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        String caseTypeId = null;

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = inputFieldsList.stream().collect(
            Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        if (STEP_PARENT_ADOPTION.equalsIgnoreCase(inputFieldsMap.get(APPLICANT1_RELATION_TO_CHILD))
            || STEP_PARENT_ADOPTION.equalsIgnoreCase(inputFieldsMap.get(APPLICANT2_RELATION_TO_CHILD))
            || TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER))
            || FALSE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER))) {
            caseTypeId = A58_STEP_PARENT.name();
        } else if (nonNull(ADOPTION_ORDER_CONSENT) || nonNull(ADOPTION_ORDER_CONSENT_ADVANCE) || nonNull(ADOPTION_ORDER_CONSENT_AGENCY)
            || nonNull(ADOPTION_ORDER_NO_CONSENT) || nonNull(COURT_CONSENT_PARENT_NOT_FOUND) || nonNull(COURT_CONSENT_PARENT_LACK_CAPACITY) || nonNull(COURT_CONSENT_CHILD_WELFARE)) {
            caseTypeId = A58_RELINQUISHED_ADOPTION.name();
        }

        Map<String, Object> populatedMap = (Map<String, Object>) BulkScanTransformHelper
            .transformToCaseData(transformConfigManager
                                     .getSourceAndTargetFields(FormType.valueOf(caseTypeId)), inputFieldsMap);

        return BulkScanTransformationResponse.builder().caseCreationDetails(
            CaseCreationDetails.builder()
                .caseTypeId(caseTypeId)
                .caseData(populatedMap).build()).build();
    }
}
