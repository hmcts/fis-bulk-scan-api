package uk.gov.hmcts.reform.bulkscan.services;

import org.apache.commons.lang3.StringUtils;
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
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_OF_SAME_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME_COLLECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_SOCIAL_AUTHORITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;

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
        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(bulkRequest.getOcrdatafields());

        BulkScanValidationResponse bulkScanValidationResponse =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        bulkRequest.getOcrdatafields(),
                        configManager.getValidationConfig(
                                FormType.C100)
                );

        bulkScanValidationResponse.addErrors(doChildRelatedValidation(inputFieldsMap));

        bulkScanValidationResponse.changeStatus();

        bulkScanValidationResponse.addWarning(dependencyValidationService
                .getDependencyWarnings(inputFieldsMap, FormType.C100));

        bulkScanValidationResponse.changeStatus();

        return bulkScanValidationResponse;
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
                .transformToCaseData(transformConfigManager.getTransformationConfig(FormType.C100)
                        .getCaseDataFields(), inputFieldsMap);


        return BulkScanTransformationResponse.builder().caseCreationDetails(
                CaseCreationDetails.builder().caseData(populatedMap).build()).build();

    }
}
