package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_ONE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_TWO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WILDCARD_DEPENDENT_INPUT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_JURISDICTIONISSUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_WARNING_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanDependencyValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

@Service
public class BulkScanDependencyValidationService {
    @Autowired BulkScanDependencyValidationConfigManager configManager;

    /**
     * This method records dependency validation warnings for group fields.
     *
     * @param ocrDataFieldsMap HashMap of all fields and respective values in the bulkscan request
     * @param formType type of forms e.g. C100, A58...
     * @return list of warnings for all dependent fields violating group validation on form
     *     requirements
     */
    public List<String> getDependencyWarnings(
            Map<String, String> ocrDataFieldsMap, FormType formType) {
        List<BulkScanDependencyValidationConfigManager.GroupDependencyConfig> groupDepConfig =
                configManager.getGroupDependencyValidationConfig(formType);

        List<BulkScanDependencyValidationConfigManager.GroupDependencyConfig>
                errWarningGroupConfig =
                        groupDepConfig.stream()
                                .filter(
                                        eachConfig ->
                                                eachConfig
                                                        .getGroupValidationValue()
                                                        .equalsIgnoreCase(
                                                                ocrDataFieldsMap.get(
                                                                        eachConfig
                                                                                .getGroupFieldName())))
                                .filter(
                                        eachConfig ->
                                                filterDependentFields(
                                                                eachConfig.getDependentFields(),
                                                                isDependentFieldValid(
                                                                        eachConfig,
                                                                        ocrDataFieldsMap))
                                                        < Integer.valueOf(
                                                                eachConfig.getRequiredFieldCount()))
                                .collect(Collectors.toUnmodifiableList());

        if (!errWarningGroupConfig.isEmpty()) {
            return errWarningGroupConfig.stream()
                    .map(
                            eachConfig ->
                                    String.format(
                                            GROUP_DEPENDENCY_MESSAGE, eachConfig.getSectionName()))
                    .collect(Collectors.toUnmodifiableList());
        }

        return Collections.emptyList();
    }

    public static long filterDependentFields(
            List<String> dependentFields, Predicate<String> predicate) {
        return dependentFields.stream().filter(predicate).count();
    }

    private Predicate<String> isDependentFieldValid(
            BulkScanDependencyValidationConfigManager.GroupDependencyConfig eachConfig,
            Map<String, String> ocrDataFieldsMap) {
        return eachDepField ->
                eachConfig
                                .getDependentFieldValue()
                                .equalsIgnoreCase(ocrDataFieldsMap.get(eachDepField))
                        || WILDCARD_DEPENDENT_INPUT.equalsIgnoreCase(
                                        eachConfig.getDependentFieldValue())
                                && StringUtils.hasText(ocrDataFieldsMap.get(eachDepField));
    }

    /**
     * This method will validate C100 form, section 12. data.
     *
     * @param ocrDataFields represents request payload
     * @return BulkScanValidationResponse object
     */
    public List<String> validateStraightDependentFields(List<OcrDataField> ocrDataFields) {

        List<String> items = new ArrayList<>();

        Map<String, String> ocrDataFieldsMap = getOcrDataFieldsMap(ocrDataFields);

        if (null != ocrDataFieldsMap && !ocrDataFieldsMap.isEmpty()) {

            if (ocrDataFieldsMap.containsKey(RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS)
                    && ocrDataFieldsMap
                            .get(RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS)
                            .equalsIgnoreCase(NO)
                    && ocrDataFieldsMap.containsKey(RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS)
                    && !StringUtils.hasText(
                            ocrDataFieldsMap.get(RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS))) {
                items.add(
                        String.format(
                                ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE,
                                RESPONDENT_ONE,
                                RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS));
            }

            if (ocrDataFieldsMap.containsKey(RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS)
                    && ocrDataFieldsMap
                            .get(RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS)
                            .equalsIgnoreCase(NO)
                    && ocrDataFieldsMap.containsKey(RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS)
                    && !StringUtils.hasText(
                            ocrDataFieldsMap.get(RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS))) {
                items.add(
                        String.format(
                                ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE,
                                RESPONDENT_TWO,
                                RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS));
            }
        }
        if (ocrDataFieldsMap != null) {
            items.addAll(validateInternationalFactors(ocrDataFieldsMap));
        }

        return items;
    }

    private List<String> validateInternationalFactors(Map<String, String> ocrDataFieldsMap) {

        List<String> items = new ArrayList<>();

        // section 8
        if (ocrDataFieldsMap.containsKey(INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD)
                        && ocrDataFieldsMap
                                .get(INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD)
                                .equalsIgnoreCase(YES)
                        && !isFieldDetailValid(
                                INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE,
                                INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS,
                                ocrDataFieldsMap)
                || !isFieldDetailValid(
                        INTERNATIONALELEMENT_JURISDICTIONISSUE,
                        WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS,
                        ocrDataFieldsMap)
                || !isFieldDetailValid(
                        INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH,
                        INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS,
                        ocrDataFieldsMap)) {
            items.add(INTERNATIONALELEMENT_WARNING_MESSAGE);
        }

        return items;
    }

    private boolean isFieldDetailValid(
            String fieldCheckBox, String fieldDetails, Map<String, String> ocrDataFieldMap) {
        boolean lbFieldValid = true;

        if (ocrDataFieldMap.containsKey(fieldCheckBox)
                && ocrDataFieldMap.get(fieldCheckBox).equalsIgnoreCase(YES)
                && !StringUtils.hasText(ocrDataFieldMap.get(fieldDetails))) {
            lbFieldValid = false;
        }

        return lbFieldValid;
    }

    private Map<String, String> getOcrDataFieldsMap(List<OcrDataField> ocrDataFields) {
        return null != ocrDataFields
                ? ocrDataFields.stream()
                        .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue))
                : null;
    }
}
