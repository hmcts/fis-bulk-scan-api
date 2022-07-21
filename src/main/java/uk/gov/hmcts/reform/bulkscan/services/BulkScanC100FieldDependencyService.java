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
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_DOMESTICVIOLENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TICK_BOX_YES;

@Service
public class BulkScanC100FieldDependencyService implements BulkScanService {
    public static final int REQUIRED_EXEMPTION_DEPENDENCY_FIELD_COUNT = 1;
    public static final int REQUIRED_NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_FIELD_COUNT = 1;

    @Autowired
    BulkScanDependencyValidationConfigManager configManager;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {

        Map<String, String> ocrDataFieldsMap = bulkRequest.getOcrdatafields()
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        List<String> warningItems = new ArrayList<>();
        BulkScanDependencyValidationConfigManager.GroupDependencyFields dependencyGroupFields
                = configManager.getGroupDependencyFields(FieldDependency.C100_DEPENDENCY);

        if (ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD) != null
                && ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD).equalsIgnoreCase(TICK_BOX_YES)) {
            warningItems.addAll(findDependentFieldWarning(ocrDataFieldsMap, EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD,
                    dependencyGroupFields.getGroupExemptionToAttendMiam(),
                    TICK_BOX_TRUE,
                    REQUIRED_EXEMPTION_DEPENDENCY_FIELD_COUNT));
        }

        if (ocrDataFieldsMap.get(NOMIAM_DOMESTICVIOLENCE) != null
                && ocrDataFieldsMap.get(NOMIAM_DOMESTICVIOLENCE).equalsIgnoreCase(TICK_BOX_TRUE)) {
            warningItems.addAll(findDependentFieldWarning(ocrDataFieldsMap, NOMIAM_DOMESTICVIOLENCE,
                    dependencyGroupFields.getGroupNoMiamDomesticViolence(),
                    TICK_BOX_TRUE,
                    REQUIRED_NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_FIELD_COUNT));
        }

        return BulkScanValidationResponse.builder()
                .warnings(Warnings.builder().items(warningItems).build())
                .status(warningItems.isEmpty() ? Status.WARNINGS : Status.SUCCESS)
                .build();
    }

    private List<String> findDependentFieldWarning(Map<String, String> ocrDataFieldsMap,
                                                   String groupField,
                                                   List<String> dependentFields,
                                                   String dependentFieldRequiredValue,
                                                   int piRequiredFieldCount) {
        List<String> dependentWarnings = new ArrayList<>();
        int liValidateFieldCount = 0;
        boolean enoughFieldPresent = false;

        StringBuilder concatDependentFields = new StringBuilder("");
        for (String dependentField : dependentFields) {
            if (ocrDataFieldsMap.get(dependentField) != null
                    && ocrDataFieldsMap.get(dependentField).equalsIgnoreCase(
                    dependentFieldRequiredValue)) {
                ++liValidateFieldCount;
                if (liValidateFieldCount == piRequiredFieldCount) {
                    enoughFieldPresent = true;
                    break;
                }
            }
            concatDependentFields.append(dependentField).append(", ");
        }
        if (!enoughFieldPresent) {
            dependentWarnings.add(String.format(GROUP_DEPENDENCY_MESSAGE,
                    groupField, piRequiredFieldCount, concatDependentFields.toString()));
        }

        return dependentWarnings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        return null;
    }

}
