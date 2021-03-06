package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanDependencyValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_ONE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_TWO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS;


@Service
public class BulkScanDependencyValidationService {

    @Autowired
    BulkScanDependencyValidationConfigManager configManager;

    @SuppressWarnings("Summary")
    /**
     * Records dependency validation warnings for group fields
     *
     * @param ocrDataFieldsMap HashMap of all fields and respective values in the bulkscan request
     * @param formType type of forms e.g. C100, A58...
     * @return list of warnings for all group fields violating form requirements
     */
    public List<String> getDependencyWarnings(Map<String, String> ocrDataFieldsMap, FormType formType) {
        List<BulkScanDependencyValidationConfigManager.GroupDependencyConfig> groupDepConfig = configManager
                .getGroupDependencyValidationConfig(formType);

        List<BulkScanDependencyValidationConfigManager.GroupDependencyConfig> errWarningGroupConfig =
                groupDepConfig.stream().filter(eachConfig -> eachConfig.getGroupValidationValue()
                                .equalsIgnoreCase(ocrDataFieldsMap.get(eachConfig.getGroupFieldName())))
                        .filter(eachConfig -> eachConfig.getDependentFields().stream()
                                .filter(eachDepField -> eachConfig.getDependentFieldValue()
                                        .equalsIgnoreCase(ocrDataFieldsMap.get(eachDepField))).count()
                                < Integer.valueOf(eachConfig.getRequiredFieldCount())).collect(
                                Collectors.toUnmodifiableList());

        if (!errWarningGroupConfig.isEmpty()) {
            return errWarningGroupConfig.stream().map(eachConfig -> String.format(GROUP_DEPENDENCY_MESSAGE,
                            eachConfig.getGroupFieldName(),
                            eachConfig.getRequiredFieldCount(),
                            eachConfig.getDependentFields().stream()
                                    .map(String::valueOf)
                                    .collect(Collectors.joining(","))))
                    .collect(Collectors.toUnmodifiableList());
        }

        return Collections.emptyList();
    }

    /**
     * This method will validate C100 form, section 12.
     * data.
     *
     * @param ocrDataFields              represents request payload
     * @return BulkScanValidationResponse object
     */

    public List<String> validateStraightDependentFields(List<OcrDataField> ocrDataFields) {

        List<String> items = new ArrayList<>();

        Map<String, String> ocrDataFieldsMap = getOcrDataFieldsMap(ocrDataFields);

        if (null != ocrDataFieldsMap && !ocrDataFieldsMap.isEmpty()) {


            if (ocrDataFieldsMap.containsKey(RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS)
                    && ocrDataFieldsMap.get(RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS).equalsIgnoreCase(NO)
                    && ocrDataFieldsMap.containsKey(RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS)
                    && !StringUtils.hasText(ocrDataFieldsMap.get(RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS))) {
                items.add(String.format(ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE,
                        RESPONDENT_ONE, RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS));
            }

            if (ocrDataFieldsMap.containsKey(RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS)
                    && ocrDataFieldsMap.get(RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS).equalsIgnoreCase(NO)
                    && ocrDataFieldsMap.containsKey(RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS)
                    && !StringUtils.hasText(ocrDataFieldsMap.get(RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS))) {
                items.add(String.format(ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE,
                        RESPONDENT_TWO, RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS));
            }

        }

        return items;
    }

    private Map<String, String> getOcrDataFieldsMap(List<OcrDataField> ocrDataFields) {
        return null != ocrDataFields ? ocrDataFields
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue)) : null;
    }
}
