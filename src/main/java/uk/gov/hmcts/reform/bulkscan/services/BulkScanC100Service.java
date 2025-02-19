package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class BulkScanC100Service implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired BulkScanC100ValidationService bulkScanC100ValidationService;

    @Autowired BulkScanDependencyValidationService dependencyValidationService;

    @Autowired BulkScanC100ConditionalTransformerService bulkScanC100ConditionalTransformerService;

    @Autowired BulkScanC100Section6ValidationService bulkScanC100Section6ValidationService;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        Map<String, String> inputFieldMap = getOcrDataFieldAsMap(bulkRequest.getOcrdatafields());
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
            configManager.getValidationConfig(FormType.C100);

        BulkScanValidationResponse response =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        bulkRequest.getOcrdatafields(),
                        validationConfig);
        // Validating the Fields..child related fields
        response.addErrors(bulkScanC100ValidationService.doChildRelatedValidation(inputFieldMap));

        // Validating the Fields..permission related fields
        response.addErrors(bulkScanC100ValidationService.doPermissionRelatedFieldValidation(inputFieldMap));

        // Validating the Fields..other proceeding fields
        response.addErrors(bulkScanC100ValidationService.validateOtherProceedingFields(inputFieldMap, validationConfig));

        //Dependancy warnings
        response.addWarning(
                dependencyValidationService.getDependencyWarnings(inputFieldMap, FormType.C100));

        //Dependancy warnings - straight dependent fields
        response.addWarning(
                dependencyValidationService.validateStraightDependentFields(
                        bulkRequest.getOcrdatafields()));

        // Validating the Fields..Attending Miam
        bulkScanC100ValidationService.validateAttendMiam(bulkRequest.getOcrdatafields(), response);

        // Validating the Fields..Applicant Address
        bulkScanC100ValidationService.validateApplicantAddressFiveYears(
                bulkRequest.getOcrdatafields(), response);

        bulkScanC100Section6ValidationService.validate(bulkRequest, response);

        // Validating the Fields..Attending the Hearing
        response.addErrors(bulkScanC100ValidationService.validateAttendingTheHearing(inputFieldMap));

        response.changeStatus();

        return response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        Map<String, String> inputFieldsMap =
                inputFieldsList.stream()
                        .filter(ocrDataField -> StringUtils.isNotEmpty(ocrDataField.getName())
                            && StringUtils.isNotEmpty(ocrDataField.getValue()))
                        .collect(Collectors.toMap(OcrDataField::getName, this::getValue));

        Map<String, Object> populatedMap =
                (Map<String, Object>)
                        BulkScanTransformHelper.transformToCaseData(
                                new HashMap<>(
                                        transformConfigManager
                                                .getTransformationConfig(FormType.C100)
                                                .getCaseDataFields()),
                                inputFieldsMap);
        populatedMap.remove("otherChildrenNotInTheCaseTable");
        bulkScanC100ConditionalTransformerService.transform(
                populatedMap, inputFieldsMap, bulkScanTransformationRequest);
        log.info("Populated map {}", populatedMap);
        String caseName = buildCaseName(populatedMap);
        populatedMap.put("caseNameHmctsInternal", caseName);
        populatedMap.put("applicantCaseName", caseName);
        populatedMap.put("caseTypeOfApplication", "C100");
        Map<String, String> caseTypeAndEventId =
            transformConfigManager.getTransformationConfig(FormType.C100).getCaseFields();
        return BulkScanTransformationResponse.builder()
                .caseCreationDetails(
                        CaseCreationDetails.builder()
                                .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                .eventId(caseTypeAndEventId.get(EVENT_ID))
                                .caseData(populatedMap)
                                .build())
                .build();
    }

    private String buildCaseName(Map<String, Object> populatedMap) {
        return populatedMap.get("applicantFirstName") + " " + populatedMap.get("applicantLastName")
            + " & " + populatedMap.get("respondentFirstName") + " " + populatedMap.get("respondentLastName");
    }

    private String getValue(OcrDataField ocrDataField) {
        return org.springframework.util.StringUtils.hasText(ocrDataField.getValue())
                ? ocrDataField.getValue()
                : StringUtils.EMPTY;
    }
}
