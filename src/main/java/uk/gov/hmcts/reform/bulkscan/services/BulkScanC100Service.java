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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_OF_SAME_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME_COLLECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_SOCIAL_AUTHORITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;
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

    @Autowired
    BulkScanDependencyValidationService dependencyValidationService;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        Map<String, String> inputFieldMap = getOcrDataFieldAsMap(bulkRequest.getOcrdatafields());

        BulkScanValidationResponse response = bulkScanValidationHelper
            .validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(), configManager.getValidationConfig(
                                                                              FormType.C100));
        response.addErrors(bulkScanC100ValidationService.doChildRelatedValidation(inputFieldMap));
        response.addErrors(bulkScanC100ValidationService.doPermissionRelatedFieldValidation(inputFieldMap));

        response.addWarning(dependencyValidationService
                .getDependencyWarnings(inputFieldMap, FormType.C100));

        response = dependencyValidationService
                .validateStraightDependentFields(bulkRequest.getOcrdatafields(), response);

        response.addWarning(response.getWarnings().getItems());

        response = bulkScanC100ValidationService
                .validateAttendMiam(bulkRequest.getOcrdatafields(), response);

        response.addErrors(response.getErrors().getItems());

        response.changeStatus();

        return response;
    }

    /**
     * 1. Checking if any one child living with option should be available
     * 2. if Child same parent is yes then parent name should have value
     * 3. if Child same parent is no then parent collection name should have value
     * 4. if social authority is yes then local authority or social worker field should have value
     * */
    List<String> doChildRelatedValidation(Map<String, String> inputFieldsMap) {
        List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(inputFieldsMap.get(CHILD_LIVING_WITH_APPLICANT))
                && StringUtils.isEmpty(inputFieldsMap.get(CHILD_LIVING_WITH_RESPONDENT))
                && StringUtils.isEmpty(inputFieldsMap.get(CHILD_LIVING_WITH_OTHERS))) {
            errors.add(String.format(XOR_CONDITIONAL_FIELDS_MESSAGE, CHILD_LIVING_WITH_APPLICANT
                    .concat(",").concat(CHILD_LIVING_WITH_RESPONDENT).concat(",").concat(CHILD_LIVING_WITH_OTHERS)));
        }

        if (YES.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_OF_SAME_PARENT))
                && StringUtils.isEmpty(inputFieldsMap.get(CHILDREN_PARENTS_NAME))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILDREN_PARENTS_NAME));
        }

        if (NO.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_OF_SAME_PARENT))
                && StringUtils.isEmpty(inputFieldsMap.get(CHILDREN_PARENTS_NAME_COLLECTION))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILDREN_PARENTS_NAME_COLLECTION));
        }

        if (YES.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_SOCIAL_AUTHORITY))
                && StringUtils.isEmpty(inputFieldsMap.get(CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER));
        }
        return errors;
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
