package uk.gov.hmcts.reform.bulkscan.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.enums.ChildLiveWithEnum;
import uk.gov.hmcts.reform.bulkscan.enums.PermissionRequiredEnum;
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

import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

@Service
public class BulkScanC100Service implements BulkScanService {

    public static final String SCAN_DOCUMENTS = "scannedDocuments";

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Autowired
    BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired
    BulkScanC100ValidationService bulkScanC100ValidationService;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(bulkRequest.getOcrdatafields());

        BulkScanValidationResponse response = bulkScanValidationHelper
            .validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(), configManager.getValidationConfig(
                                                                              FormType.C100));
        response.addErrors(bulkScanC100ValidationService.doChildRelatedValidation(inputFieldsMap));
        response.addErrors(bulkScanC100ValidationService.doPermissionRelatedFieldValidation(inputFieldsMap));

        response = bulkScanC100ValidationService
            .validateAttendMiam(bulkRequest.getOcrdatafields(), response);

        response.changeStatus();

        return response;
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
            .transformToCaseData(new HashMap<>(transformConfigManager.getTransformationConfig(FormType.C100)
                    .getCaseDataFields()), inputFieldsMap);

        populatedMap.put(CHILD_LIVE_WITH_KEY, getChildLiveWith(inputFieldsMap));
        populatedMap.put(APPLICATION_PERMISSION_REQUIRED, getApplicationPermissionRequired(inputFieldsMap));

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
            transformConfigManager.getTransformationConfig(FormType.C100).getCaseFields();


        return BulkScanTransformationResponse.builder().caseCreationDetails(
            CaseCreationDetails.builder()
                .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                .eventId(caseTypeAndEventId.get(EVENT_ID))
                .caseData(populatedMap).build()).build();
    }

    private String getChildLiveWith(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_LIVING_WITH_APPLICANT))) {
            return ChildLiveWithEnum.APPLICANT.getName();
        }
        if (TRUE.equals(inputFieldsMap.get(CHILD_LIVING_WITH_RESPONDENT))) {
            return ChildLiveWithEnum.RESPONDENT.getName();
        }
        if (TRUE.equals(inputFieldsMap.get(CHILD_LIVING_WITH_OTHERS))) {
            return ChildLiveWithEnum.OTHERPEOPLE.getName();
        }
        return StringUtils.EMPTY;
    }

    private String getApplicationPermissionRequired(Map<String, String> inputFieldsMap) {
        if (YES.equalsIgnoreCase(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            return PermissionRequiredEnum.yes.getDisplayedValue();
        }
        if ("No, permission Not required".equals(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            return PermissionRequiredEnum.noNotRequired.getDisplayedValue();
        }
        if ("No, permission Now sought".equals(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            return PermissionRequiredEnum.noNowSought.getDisplayedValue();
        }
        return StringUtils.EMPTY;
    }
}
