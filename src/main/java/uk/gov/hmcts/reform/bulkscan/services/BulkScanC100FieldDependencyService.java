package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanDependencyValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FieldDependency;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;

@Service
public class BulkScanC100FieldDependencyService implements BulkScanService {

    @Autowired
    BulkScanDependencyValidationConfigManager configManager;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {

        Map<String, String> ocrDataFieldsMap = getOcrFieldsMap(bulkRequest);

        List<String> warningItems = new ArrayList<>();

        Map<String, BulkScanDependencyValidationConfigManager.GroupDependencyField> groupFieldDependency
                = convertDependecyListToMap(configManager
                .getGroupDependencyValidationConfig(FieldDependency.C100_DEPENDENCY));


        for (Map.Entry<String, BulkScanDependencyValidationConfigManager.GroupDependencyField> dependentField
                : groupFieldDependency.entrySet()) {
            if (ocrDataFieldsMap.get(dependentField.getKey()) != null
                    && ocrDataFieldsMap.get(dependentField.getValue())
                    .equalsIgnoreCase(dependentField.getValue().getGroupValidation())) {
                warningItems.addAll(findDependentFieldWarning(ocrDataFieldsMap, dependentField.getValue()));
            }
        }

        return BulkScanValidationResponse.builder()
                .warnings(Warnings.builder().items(warningItems).build())
                .status(warningItems.isEmpty() ? Status.SUCCESS : Status.WARNINGS)
                .build();
    }

    public Map<String, BulkScanDependencyValidationConfigManager.GroupDependencyField> convertDependecyListToMap(
            List<BulkScanDependencyValidationConfigManager.GroupDependencyField> groupDependentLst) {
        return groupDependentLst.stream()
                .collect(Collectors
                        .toMap(BulkScanDependencyValidationConfigManager.GroupDependencyField::getGroupField,
                                Function.identity()));
    }

    private Map<String, String> getOcrFieldsMap(BulkScanValidationRequest bulkRequest) {
        return bulkRequest.getOcrdatafields()
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));
    }


    private List<String> findDependentFieldWarning(
            Map<String, String> ocrDataFieldsMap,
            BulkScanDependencyValidationConfigManager.GroupDependencyField dependentFields) {

        List<String> dependentWarnings = new ArrayList<>();
        int liValidateFieldCount = 0;
        boolean enoughFieldPresent = false;

        for (Map.Entry<String, String> ocrFieldAndValue : ocrDataFieldsMap.entrySet()) {
            if (dependentFields.getDependentFields().contains(ocrFieldAndValue.getKey())
                    && ocrFieldAndValue.getValue().equalsIgnoreCase(dependentFields.getDependentValue())) {
                ++liValidateFieldCount;
                if (liValidateFieldCount == Integer.parseInt(dependentFields.getValidationCount())) {
                    enoughFieldPresent = true;
                    break;
                }
            }
        }
        if (!enoughFieldPresent) {
            dependentWarnings.add(String.format(GROUP_DEPENDENCY_MESSAGE,
                    dependentFields.getGroupField(),
                    dependentFields.getValidationCount().toString(),
                    dependentFields.getDependentFields().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))));
        }

        return dependentWarnings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        return null;
    }

}
