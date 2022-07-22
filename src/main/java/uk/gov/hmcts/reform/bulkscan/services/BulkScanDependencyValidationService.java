package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanDependencyValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;

@Service
public class BulkScanDependencyValidationService {

    @Autowired
    BulkScanDependencyValidationConfigManager configManager;

    /***
     *
     * @param ocrDataFieldsMap: for all fields and respective values in the bulkscan request
     * @param formType: affected case form - e.g. C100, A58...
     * @return: A list of warnings for all field dependent groups not satisfying applicant's form requirements
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
                                < Integer.valueOf(eachConfig.getMinFieldRequiredCount())).collect(
                                Collectors.toUnmodifiableList());

        if (!errWarningGroupConfig.isEmpty()) {
            return errWarningGroupConfig.stream().map(eachConfig -> String.format(GROUP_DEPENDENCY_MESSAGE,
                            eachConfig.getGroupFieldName(),
                            eachConfig.getMinFieldRequiredCount().toString(),
                            eachConfig.getDependentFields().stream()
                                    .map(String::valueOf)
                                    .collect(Collectors.joining(","))))
                    .collect(Collectors.toUnmodifiableList());
        }

        return Collections.emptyList();
    }

}
