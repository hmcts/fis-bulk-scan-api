package uk.gov.hmcts.reform.bulkscan.services;

import lombok.NoArgsConstructor;
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
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BAIL_CONDITION_END_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.HYPHEN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.INT_THREE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_BAIL_CONDITIONS_ENDDATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TEXT_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.VALID_DATE_WARNING_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.FL401;

@NoArgsConstructor
@Service
public class BulkScanFL401Service implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired BulkScanDependencyValidationService dependencyValidationService;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

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

        response.addWarning(validateInputDate(ocrDataFields, RESPONDENT_BAIL_CONDITIONS_ENDDATE, BAIL_CONDITION_END_DATE));

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

    private Map<String, String> getOcrDataFieldsMap(List<OcrDataField> ocrDataFields) {
        return null != ocrDataFields
                ? ocrDataFields.stream()
                        .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue))
                : null;
    }

    private List<String> validateInputDate(List<OcrDataField> ocrDataFields, String fieldName, String message) {

        final Map<String, String> ocrDataFieldsMap = getOcrDataFieldsMap(ocrDataFields);

        String date = null;

        if (null != ocrDataFieldsMap) {
            if (ocrDataFieldsMap.containsKey(fieldName)) {
                date = ocrDataFieldsMap.get(fieldName);

                return validateDate(
                    Objects.requireNonNull(date), message);
            }

        }
        return Collections.emptyList();
    }

    private List<String> validateDate(String date, String fieldName) {

        String pattern = NUMERIC_MONTH_PATTERN;

        final String[] splitDate = date.split(String.valueOf(HYPHEN));

        if (splitDate.length == INT_THREE) {
            final String month = splitDate[1];

            // IF month holds 3 characters then it could Jan, Feb etc.
            // so check for MMM pattern
            if (month.length() == INT_THREE) {
                pattern = TEXT_MONTH_PATTERN;
            }

            final boolean validateDate = DateUtil.validateDate(date, pattern);

            if (!validateDate) {
                return List.of(String.format(VALID_DATE_WARNING_MESSAGE, fieldName));
            }
        }
        return Collections.emptyList();
    }
}
