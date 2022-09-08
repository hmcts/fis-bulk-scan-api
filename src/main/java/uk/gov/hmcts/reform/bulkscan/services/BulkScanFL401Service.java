package uk.gov.hmcts.reform.bulkscan.services;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.BAIL_CONDITION_END_DATE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RESPONDENT_BAIL_CONDITIONS_ENDDATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.VALID_DATE_WARNING_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
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
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class BulkScanFL401Service implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired BulkScanDependencyValidationService dependencyValidationService;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

    @Autowired BulkScanFL401ValidationService bulkScanFL401ValidationService;

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
                        ocrDataFields, configManager.getValidationConfig(FormType.FL401));

        response.addWarning(
                dependencyValidationService.getDependencyWarnings(inputFieldMap, FormType.FL401));

        response.addWarning(
                validateInputDate(
                        ocrDataFields,
                        RESPONDENT_BAIL_CONDITIONS_ENDDATE,
                        BAIL_CONDITION_END_DATE_MESSAGE));

        bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                inputFieldMap, response);

        response.changeStatus();

        return response;
    }

    @Override
    public FormType getCaseType() {
        return FormType.FL401;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        FormType formType = getCaseType();

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        List<String> unknownFieldsList = null;

        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
                configManager.getValidationConfig(formType);

        Map<String, Object> populatedMap =
                (Map<String, Object>)
                        BulkScanTransformHelper.transformToCaseData(
                                new HashMap<>(
                                        transformConfigManager
                                                .getTransformationConfig(formType)
                                                .getCaseDataFields()),
                                inputFieldsMap);

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
                transformConfigManager.getTransformationConfig(formType).getCaseFields();

        bulkScanFL401ConditionalTransformerService.transform(
                populatedMap, inputFieldsMap, bulkScanTransformationRequest);

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

    private Map<String, String> getOcrDataFieldsMap(List<OcrDataField> ocrDataFields) {
        return null != ocrDataFields
                ? ocrDataFields.stream()
                        .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue))
                : null;
    }

    private List<String> validateInputDate(
            List<OcrDataField> ocrDataFields, String fieldName, String message) {

        final Map<String, String> ocrDataFieldsMap = getOcrDataFieldsMap(ocrDataFields);

        if (null != ocrDataFieldsMap
                && ocrDataFieldsMap.containsKey(fieldName)
                && hasText(ocrDataFieldsMap.get(fieldName))) {
            String date = ocrDataFieldsMap.get(fieldName);

            return validateDate(Objects.requireNonNull(date), message);
        }
        return Collections.emptyList();
    }

    private List<String> validateDate(String date, String fieldName) {

        final boolean validateDate = DateUtil.validateDate(date, TEXT_AND_NUMERIC_MONTH_PATTERN);

        if (!validateDate) {
            return List.of(String.format(VALID_DATE_WARNING_MESSAGE, fieldName));
        }
        return Collections.emptyList();
    }
}
