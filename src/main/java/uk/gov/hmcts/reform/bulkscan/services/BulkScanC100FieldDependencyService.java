package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
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
import java.util.HashMap;
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
    public static final int REQUIRED_EXEMPTION_DEPENDENCY_FIELD_VALIDATION = 1;
    public static final int REQUIRED_NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_FIELD_VALIDATION = 1;

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {

        Map<String, Integer> groupDependencyCountMap = new HashMap<>();

        groupDependencyCountMap.put(
            EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD,
            REQUIRED_EXEMPTION_DEPENDENCY_FIELD_VALIDATION
        );
        groupDependencyCountMap.put(
            NOMIAM_DOMESTICVIOLENCE,
            REQUIRED_NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_FIELD_VALIDATION
        );

        Map<String, String> ocrDataFieldsMap = bulkRequest.getOcrdatafields()
            .stream()
            .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        List<String> warningItems = new ArrayList<>();

        Status status = Status.SUCCESS;

        if (ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD) != null
            && ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD).equals(TICK_BOX_YES)
            ) {
            BulkScanValidationResponse validateGroupDependency3aFieldResponse = validateGroupDependencyFields(
                bulkRequest.getOcrdatafields(),
                configManager.getFieldDependenyConfig(
                    FieldDependency.C100_FIELD_DEPENDENCY),
                groupDependencyCountMap
            );
            warningItems.addAll(validateGroupDependency3aFieldResponse.getWarnings().getItems());

            status = (Status.WARNINGS == validateGroupDependency3aFieldResponse.getStatus()) ? Status.WARNINGS :
                Status.SUCCESS;
        }

        return BulkScanValidationResponse.builder()
            .warnings(Warnings.builder().items(warningItems).build())
            .status(status)
            .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        return null;
    }

    private BulkScanValidationResponse validateGroupDependencyFields(
        List<OcrDataField> ocrDatafields,
        BulkScanFormValidationConfigManager
            .GroupDependencyValidationConfig grpFldDepalidationConfg,
        Map<String, Integer> groupFieldCountMap) {
        List<String> warnings = new ArrayList<>();

        if (!ocrDatafields.isEmpty()) {
            for (var entry : groupFieldCountMap.entrySet()) {
                warnings.addAll(validateDependencyFields(ocrDatafields, grpFldDepalidationConfg,
                                                         entry.getKey(), entry.getValue()
                ));
            }
        }

        Status status = !warnings.isEmpty() ? Status.WARNINGS : Status.SUCCESS;

        return BulkScanValidationResponse.builder()
            .status(status)
            .warnings(Warnings.builder().items(warnings).build())
            .build();
    }

    private List<String> validateDependencyFields(
        List<OcrDataField> ocrDataFields,
        BulkScanFormValidationConfigManager.GroupDependencyValidationConfig grpFldDepValidationConfig,
        String groupField, Integer dependencyFieldCount) {
        List<String> warnings = new ArrayList<>();

        BulkScanFormValidationConfigManager.GroupDependencyFields groupDependencyFields
            = grpFldDepValidationConfig.getGroupFieldDependencyDetail();

        List<BulkScanFormValidationConfigManager.GroupDependencyField> dependencyFields
            = new ArrayList<>();

        if (groupDependencyFields.getGroupDependency1() != null) {
            dependencyFields.add(groupDependencyFields.getGroupDependency1());
        }
        if (groupDependencyFields.getGroupDependency2() != null) {
            dependencyFields.add(groupDependencyFields.getGroupDependency2());
        }
        if (groupDependencyFields.getGroupDependency3() != null) {
            dependencyFields.add(groupDependencyFields.getGroupDependency3());
        }
        if (groupDependencyFields.getGroupDependency4() != null) {
            dependencyFields.add(groupDependencyFields.getGroupDependency4());
        }
        if (groupDependencyFields.getGroupDependency5() != null) {
            dependencyFields.add(groupDependencyFields.getGroupDependency5());
        }
        if (groupDependencyFields.getGroupDependency6() != null) {
            dependencyFields.add(groupDependencyFields.getGroupDependency6());
        }
        if (groupDependencyFields.getGroupDependency7() != null) {
            dependencyFields.add(groupDependencyFields.getGroupDependency7());
        }

        for (BulkScanFormValidationConfigManager.GroupDependencyField dependencyField : dependencyFields) {
            if (dependencyField.getGroupFieldName().equals(groupField)) {
                warnings.addAll(validateDependencyFields(
                    dependencyField,
                    ocrDataFields,
                    dependencyFieldCount,
                    TICK_BOX_TRUE
                ));
            }
        }
        return warnings;
    }

    private List<String> validateDependencyFields(
        BulkScanFormValidationConfigManager.GroupDependencyField groupField,
        List<OcrDataField> ocrDataFields,
        int piFieldPresentCount, String fieldValidationValue) {

        List<String> errorOrWarnings = new ArrayList<>();

        if (piFieldPresentCount == 0) {
            return errorOrWarnings;
        }

        int liRequiredFieldCount = 0;
        boolean enoughFieldPresent = false;

        Map<String, String> ocrDataFieldsMap = ocrDataFields
            .stream()
            .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        for (String dependencyField : groupField.getDependentFieldNames()) {
            if (ocrDataFieldsMap.get(dependencyField) != null
                && ocrDataFieldsMap.get(dependencyField).equals(
                fieldValidationValue)) {
                liRequiredFieldCount++;
                if (liRequiredFieldCount == piFieldPresentCount) {
                    enoughFieldPresent = true;
                    break;
                }
            }
        }

        if (!enoughFieldPresent) {
            errorOrWarnings.add(String.format(GROUP_DEPENDENCY_MESSAGE, groupField.getGroupFieldName()));
        }
        return errorOrWarnings;
    }
}
