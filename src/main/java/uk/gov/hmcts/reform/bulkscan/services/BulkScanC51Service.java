package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_HOME_TELEPHONE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MOBILE_TELEPHONE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;

/** service class for C51 form It provides methods to validate and transform requesst. */
@Service
public class BulkScanC51Service implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

    @Override
    public FormType getCaseType() {
        return FormType.C51;
    }

    /**
     * This method will be used to validate incoming request. It can return SUCCESS, ERROR or
     * WARNING response.
     *
     * @param bulkRequest BulkScanValidationRequest Object
     * @return BulkScanValidationResponse Object
     */
    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        List<OcrDataField> ocrDataFields = bulkRequest.getOcrdatafields();

        BulkScanValidationResponse bulkScanValidationResponse =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        ocrDataFields, configManager.getValidationConfig(FormType.C51));

        // validate conditional fields

        if (bulkScanValidationResponse.getErrors().isEmpty()) {
            validateConditionalFields(ocrDataFields, bulkScanValidationResponse);
        }

        return bulkScanValidationResponse;
    }

    /**
     * This method will validate if at least one of the input (either telephone number or mobile
     * number) is present in request.
     *
     * @param ocrDataFields OcrDataField list
     * @param bulkScanValidationResponse BulkScanValidationResponse
     */
    private void validateConditionalFields(
            List<OcrDataField> ocrDataFields,
            BulkScanValidationResponse bulkScanValidationResponse) {
        Map<String, String> ocrDataFieldsMap =
                ocrDataFields.stream()
                        .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        List<String> conditionalFields =
                List.of(APPLICANT_HOME_TELEPHONE_NUMBER, APPLICANT_MOBILE_TELEPHONE_NUMBER);
        boolean validField = false;

        for (String conditionalField : conditionalFields) {
            if (ocrDataFieldsMap.containsKey(conditionalField)
                    && StringUtils.hasText(ocrDataFieldsMap.get(conditionalField))) {
                validField = true;
                break;
            }
        }

        if (!validField) {

            bulkScanValidationResponse.setStatus(Status.ERRORS);

            List<String> items = bulkScanValidationResponse.getErrors();

            items.add(
                    String.format(
                            BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE, conditionalFields));
        }
    }

    /**
     * This method will tranfrom incoming requet to CCD object.
     *
     * @param bulkScanTransformationRequest BulkScanTransformationRequest
     * @return BulkScanTransformationResponse
     */
    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {

        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        FormType formType = getCaseType();

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        // Validating if any unknown fields present or not. if exist then it should go as warnings.
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
                configManager.getValidationConfig(formType);

        List<String> unknownFieldsList =
                bulkScanValidationHelper.findUnknownFields(
                        inputFieldsList,
                        validationConfig.getMandatoryFields(),
                        validationConfig.getOptionalFields());

        Map<String, Object> populatedMap =
                (Map<String, Object>)
                        BulkScanTransformHelper.transformToCaseData(
                                new HashMap<>(
                                        transformConfigManager
                                                .getTransformationConfig(formType)
                                                .getCaseDataFields()),
                                inputFieldsMap);

        populatedMap.put(
                BulkScanConstants.SCAN_DOCUMENTS,
                transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
                transformConfigManager.getTransformationConfig(formType).getCaseFields();

        BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder =
                BulkScanTransformationResponse.builder()
                        .caseCreationDetails(
                                CaseCreationDetails.builder()
                                        .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                        .eventId(caseTypeAndEventId.get(EVENT_ID))
                                        .caseData(populatedMap)
                                        .build());
        if (null != unknownFieldsList && !unknownFieldsList.isEmpty()) {
            builder.warnings(
                    Arrays.asList(
                            String.format(
                                    UNKNOWN_FIELDS_MESSAGE, String.join(",", unknownFieldsList))));
        }
        return builder.build();
    }
}
