package uk.gov.hmcts.reform.bulkscan.services;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_FOR_YOUR_FAMILY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_FOR_YOU_ONLY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NEED_FOR_PARENTIAL_RESPONSIBILITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_ADDRESS_POSTCODE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_DATE_OF_BIRTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_DATE_OF_BIRTH_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.BAIL_CONDITION_END_DATE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RESPONDENT_BAIL_CONDITIONS_ENDDATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.VALID_DATE_WARNING_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants;
import uk.gov.hmcts.reform.bulkscan.enums.FL401StopRespondentBehaviourChildEnum;
import uk.gov.hmcts.reform.bulkscan.enums.FL401StopRespondentEnum;
import uk.gov.hmcts.reform.bulkscan.exception.PostCodeValidationException;
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
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;
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

    @Autowired PostcodeLookupService postcodeLookupService;

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

        response.addWarning(
                validateInputDate(
                        ocrDataFields, APPLICANT_DATE_OF_BIRTH, APPLICANT_DATE_OF_BIRTH_MESSAGE));

        response.addWarning(isValidPostCode(inputFieldMap, APPLICANT_ADDRESS_POSTCODE));

        response.addWarning(validateRespondentBehaviour(inputFieldMap));

        response.addWarning(validateJustYouOrYouAndFamilySectionFive(inputFieldMap));

        response.changeStatus();

        return response;
    }

    private List<String> validateJustYouOrYouAndFamilySectionFive(
            Map<String, String> inputFieldMap) {

        String needForParentalResponsiblity = inputFieldMap.get(NEED_FOR_PARENTIAL_RESPONSIBILITY);
        String justYou = inputFieldMap.get(APPLICATION_FOR_YOU_ONLY);
        String youAndFamily = inputFieldMap.get(APPLICATION_FOR_YOUR_FAMILY);
        List<String> warningLst = new ArrayList<>();

        if (hasText(needForParentalResponsiblity)
                && needForParentalResponsiblity.equalsIgnoreCase(YES)) {
            if (hasText(justYou)
                    && !justYou.equalsIgnoreCase(YES)
                    && hasText(youAndFamily)
                    && !youAndFamily.equalsIgnoreCase(YES)) {
                warningLst.add(
                        String.format(MISSING_FIELD_MESSAGE, "5.1 - Who is this application for?"));

            } else if (hasText(justYou)
                    && justYou.equalsIgnoreCase(YES)
                    && hasText(youAndFamily)
                    && youAndFamily.equalsIgnoreCase(YES)) {
                warningLst.add(
                        String.format(MISSING_FIELD_MESSAGE, "5.1 - Who is this application for?"));
            }
        }
        return warningLst;
    }

    /**
     * This method will validate given post code field and will return warning if post code is
     * invalid.
     *
     * @param inputFieldMap request input map
     * @param fieldName field contain postcode
     * @return warning
     */
    private List<String> isValidPostCode(Map<String, String> inputFieldMap, Object fieldName) {
        if (null != inputFieldMap
                && inputFieldMap.containsKey(fieldName)
                && hasText(inputFieldMap.get(fieldName))) {

            try {
                boolean isValidPostcode =
                        postcodeLookupService.isValidPostCode(
                                inputFieldMap.get(APPLICANT_ADDRESS_POSTCODE), null);

                if (!isValidPostcode) {
                    return List.of(String.format(POST_CODE_MESSAGE, APPLICANT_ADDRESS_POSTCODE));
                }
            } catch (PostCodeValidationException e) {
                // if postocde is wrong then postcode lookup service will throw exception
                if (e.getCause() instanceof HttpClientErrorException
                        && ((HttpClientErrorException) e.getCause())
                                .getStatusCode()
                                .equals(HttpStatus.BAD_REQUEST)) {
                    return List.of(String.format(POST_CODE_MESSAGE, APPLICANT_ADDRESS_POSTCODE));
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * validate the 6. Respondent’s behaviour section in FL401 form
     *
     * @param inputFieldMap .
     * @return validation of warning list.
     */
    private List<String> validateRespondentBehaviour(Map<String, String> inputFieldMap) {
        List<String> warningLst = new ArrayList<>();
        String applyingOrder =
                inputFieldMap.get(BulkScanFl401Constants.APPLYING_FOR_NON_MOLES_STATION_ORDER);
        if (hasText(applyingOrder) && applyingOrder.equalsIgnoreCase(YES)) {

            validateStopRespondedDoing(inputFieldMap, warningLst);
        }

        return warningLst;
    }

    private void validateStopRespondedDoing(
            Map<String, String> inputFieldMap, List<String> warningLst) {
        boolean stopRespondentFromDoing = false;
        for (FL401StopRespondentEnum l : EnumSet.allOf(FL401StopRespondentEnum.class)) {
            String key = l.getKey();
            if (hasText(inputFieldMap.get(key)) && inputFieldMap.get(key).equalsIgnoreCase(YES)) {
                stopRespondentFromDoing = true;
            }
        }
        if (!stopRespondentFromDoing) {
            warningLst.add(
                    String.format(MANDATORY_ERROR_MESSAGE, "Respondent's behaviour options 6.2 "));
        }
        boolean stopRespondentFromDoingToChild = false;
        for (FL401StopRespondentBehaviourChildEnum l :
                EnumSet.allOf(FL401StopRespondentBehaviourChildEnum.class)) {
            String key = l.getKey();
            if (hasText(inputFieldMap.get(key)) && inputFieldMap.get(key).equalsIgnoreCase(YES)) {
                stopRespondentFromDoingToChild = true;
            }
        }
        if (!stopRespondentFromDoingToChild) {
            warningLst.add(
                    String.format(MANDATORY_ERROR_MESSAGE, "Respondent's behaviour options 6.3"));
        }
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

        bulkScanFL401ConditionalTransformerService.transform(populatedMap, inputFieldsMap);

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

    private List<String> validateInputDate(
            List<OcrDataField> ocrDataFields, String fieldName, String message) {

        final Map<String, String> ocrDataFieldsMap = getOcrDataFieldAsMap(ocrDataFields);

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
