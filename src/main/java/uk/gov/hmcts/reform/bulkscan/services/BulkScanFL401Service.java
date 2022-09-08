package uk.gov.hmcts.reform.bulkscan.services;

import static java.util.Objects.nonNull;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.FL401;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.group.util.BulkScanGroupValidatorUtil;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

@NoArgsConstructor
@Service
public class BulkScanFL401Service implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired BulkScanDependencyValidationService dependencyValidationService;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

    @Autowired
    BulkScanFL401ConditionalTransformerService bulkScanFL401ConditionalTransformerService;

    @Override
    public BulkScanValidationResponse validate(
            BulkScanValidationRequest bulkScanValidationRequest) {
        // Validating the Fields..

        final List<OcrDataField> ocrDataFields = bulkScanValidationRequest.getOcrdatafields();

        Map<String, String> inputFieldMap = getOcrDataFieldAsMap(ocrDataFields);

        BulkScanValidationResponse response =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        ocrDataFields, configManager.getValidationConfig(FL401));

        response.addWarning(
                dependencyValidationService.getDependencyWarnings(inputFieldMap, FL401));

        return response;
    }

    @Override
    public FormType getCaseType() {
        return FL401;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        // TODO transformation logic

        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        FormType formType = FL401;

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        List<String> unknownFieldsList = null;

        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
                configManager.getValidationConfig(formType);

        Map<String, String> caseTypeAndEventId =
                transformConfigManager.getTransformationConfig(formType).getCaseFields();

        Map<String, Object> populatedMap =
                (Map<String, Object>)
                        BulkScanTransformHelper.transformToCaseData(
                                new HashMap<>(
                                        transformConfigManager
                                                .getTransformationConfig(formType)
                                                .getCaseDataFields()),
                                inputFieldsMap);

        bulkScanFL401ConditionalTransformerService.transform(
                inputFieldsMap, populatedMap, bulkScanTransformationRequest);

        BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder =
                BulkScanTransformationResponse.builder()
                        .caseCreationDetails(
                                CaseCreationDetails.builder()
                                        .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                        .eventId(caseTypeAndEventId.get(EVENT_ID))
                                        .caseData(populatedMap)
                                        .build());

        if (nonNull(validationConfig)) {
            unknownFieldsList =
                    bulkScanValidationHelper.findUnknownFields(
                            inputFieldsList,
                            validationConfig.getMandatoryFields(),
                            validationConfig.getOptionalFields());
        }
        BulkScanGroupValidatorUtil.updateTransformationUnknownFieldsByGroupFields(
                formType, unknownFieldsList, builder);

        return builder.build();
    }
}
