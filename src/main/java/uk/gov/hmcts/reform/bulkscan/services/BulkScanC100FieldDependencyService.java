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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ATTENDED_MIAM_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.C100_COMPULSORY_SECTION_2_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXISTINGCASE_ONEMERGENCYPROTECTION_CARE_OR_SUPERVISIONORDER_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FAMILYMEMBER_INTIMATION_ON_NO_MIAM_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES_VALUE;

@Service
public class BulkScanC100FieldDependencyService implements BulkScanService {
    public static final int EXEMPTION_TO_ATTEND_MIAM_BITSET = 0x0100;
    public static final int FAMILYMEMBER_INTIMATION_ON_NO_MIAM_BITSET = 0x0010;
    public static final int EXISTINGCASE_ONEMERGENCYPROTECTION_CARE_OR_SUPERVISIONORDER_BITSET = 0x1000;
    public static final int ATTENDED_MIAM_BITSET = 0x0001;
    public static final int REQUIRED_EXEMPTION_DEPENDENCY_FIELD_VALIDATION = 1;

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        BulkScanValidationResponse validateMutuallyExclusiveFieldResponse
            = validateMutuallyExclusiveFields(
            bulkRequest.getOcrdatafields(),
            configManager.getFieldDependenyConfig(
                FieldDependency.C100_FIELD_DEPENDENCY)
        );

        Map<String, Integer> groupDependencyCountMap = new HashMap<>();

        groupDependencyCountMap.put(
            EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD,
            REQUIRED_EXEMPTION_DEPENDENCY_FIELD_VALIDATION
        );

        BulkScanValidationResponse validateGroupDependencyFieldResponse = validateGroupDependencyFields(
            bulkRequest.getOcrdatafields(),
            configManager.getFieldDependenyConfig(
                FieldDependency.C100_FIELD_DEPENDENCY),
            groupDependencyCountMap
        );

        List<String> warningItems = validateMutuallyExclusiveFieldResponse.getWarnings().getItems();
        warningItems.addAll(validateGroupDependencyFieldResponse.getWarnings().getItems());

        Status status = (Status.WARNINGS == validateMutuallyExclusiveFieldResponse.getStatus()) ? Status.WARNINGS :
            (Status.ERRORS == validateMutuallyExclusiveFieldResponse.getStatus()) ? Status.ERRORS :
                (Status.WARNINGS == validateGroupDependencyFieldResponse.getStatus()) ? Status.WARNINGS :
                    (Status.ERRORS == validateGroupDependencyFieldResponse.getStatus()) ? Status.ERRORS :
                        Status.SUCCESS;


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

    private BulkScanValidationResponse validateMutuallyExclusiveFields(
        List<OcrDataField> ocrDataFields,
        BulkScanFormValidationConfigManager.GroupDependencyValidationConfig grpFldDepValidationConfig) {
        Map<String, String> ocrDataFieldsMap = ocrDataFields
            .stream()
            .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        List<String> errorOrWarnings = new ArrayList<>();

        int mutualFieldSet = 0;

        if (ocrDataFieldsMap.get(EXISTINGCASE_ONEMERGENCYPROTECTION_CARE_OR_SUPERVISIONORDER_FIELD) != null
            && ocrDataFieldsMap.get(EXISTINGCASE_ONEMERGENCYPROTECTION_CARE_OR_SUPERVISIONORDER_FIELD)
            .equals(YES_VALUE)) {
            mutualFieldSet |= EXISTINGCASE_ONEMERGENCYPROTECTION_CARE_OR_SUPERVISIONORDER_BITSET;
        }
        if (ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD) != null
            && ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD).equals(YES_VALUE)) {
            mutualFieldSet |= EXEMPTION_TO_ATTEND_MIAM_BITSET;
        }
        if (ocrDataFieldsMap.get(FAMILYMEMBER_INTIMATION_ON_NO_MIAM_FIELD) != null
            && ocrDataFieldsMap.get(FAMILYMEMBER_INTIMATION_ON_NO_MIAM_FIELD).equals(YES_VALUE)) {
            mutualFieldSet |= FAMILYMEMBER_INTIMATION_ON_NO_MIAM_BITSET;
        }
        if (ocrDataFieldsMap.get(ATTENDED_MIAM_FIELD) != null
            && ocrDataFieldsMap.get(ATTENDED_MIAM_FIELD).equals(YES_VALUE)) {
            mutualFieldSet |= ATTENDED_MIAM_BITSET;
        }

        switch (mutualFieldSet) {
            case EXISTINGCASE_ONEMERGENCYPROTECTION_CARE_OR_SUPERVISIONORDER_BITSET:
            case EXEMPTION_TO_ATTEND_MIAM_BITSET:
            case FAMILYMEMBER_INTIMATION_ON_NO_MIAM_BITSET:
            case ATTENDED_MIAM_BITSET:
                break;
            default:
                errorOrWarnings.add(String.format(C100_COMPULSORY_SECTION_2_MESSAGE, "2"));
                break;
        }

        Status status = !errorOrWarnings.isEmpty() ? Status.WARNINGS : Status.SUCCESS;

        return BulkScanValidationResponse.builder()
            .status(status)
            .warnings(Warnings.builder().items(errorOrWarnings).build())
            .build();
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

        if (groupDependencyFields.getGroupDependency1() != null
            && groupDependencyFields.getGroupDependency1().getGroupFieldName().equals(groupField)) {
            warnings.addAll(validateDependencyFields(
                groupDependencyFields.getGroupDependency1(),
                ocrDataFields,
                dependencyFieldCount
            ));
        }
        if (groupDependencyFields.getGroupDependency2() != null
            && groupDependencyFields.getGroupDependency2().getGroupFieldName().equals(groupField)) {
            warnings.addAll(validateDependencyFields(
                groupDependencyFields.getGroupDependency2(),
                ocrDataFields,
                dependencyFieldCount
            ));
        }
        if (groupDependencyFields.getGroupDependency3() != null
            && groupDependencyFields.getGroupDependency3().getGroupFieldName().equals(groupField)) {
            warnings.addAll(validateDependencyFields(
                groupDependencyFields.getGroupDependency3(),
                ocrDataFields,
                dependencyFieldCount
            ));
        }
        if (groupDependencyFields.getGroupDependency4() != null
            && groupDependencyFields.getGroupDependency4().getGroupFieldName().equals(groupField)) {
            warnings.addAll(validateDependencyFields(
                groupDependencyFields.getGroupDependency4(),
                ocrDataFields,
                dependencyFieldCount
            ));
        }
        if (groupDependencyFields.getGroupDependency5() != null
            && groupDependencyFields.getGroupDependency5().getGroupFieldName().equals(groupField)) {
            warnings.addAll(validateDependencyFields(
                groupDependencyFields.getGroupDependency5(),
                ocrDataFields,
                dependencyFieldCount
            ));
        }
        if (groupDependencyFields.getGroupDependency6() != null
            && groupDependencyFields.getGroupDependency6().getGroupFieldName().equals(groupField)) {
            warnings.addAll(validateDependencyFields(
                groupDependencyFields.getGroupDependency6(),
                ocrDataFields,
                dependencyFieldCount
            ));
        }
        if (groupDependencyFields.getGroupDependency7() != null
            && groupDependencyFields.getGroupDependency7().getGroupFieldName().equals(groupField)) {
            warnings.addAll(validateDependencyFields(
                groupDependencyFields.getGroupDependency7(),
                ocrDataFields,
                dependencyFieldCount
            ));
        }
        return warnings;
    }

    private List<String> validateDependencyFields(
        BulkScanFormValidationConfigManager.GroupDependencyField groupField,
        List<OcrDataField> ocrDataFields,
        int piFieldPresentCount) {

        List<String> errorOrWarnings = new ArrayList<>();

        if (piFieldPresentCount == 0) {
            return errorOrWarnings;
        }

        int liFieldCnt = 0;
        boolean enoughFieldPresent = false;

        Map<String, String> ocrDataFieldsMap = ocrDataFields
            .stream()
            .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        for (String dependencyField : groupField.getDependentFieldNames()) {
            if (isNotEmpty(ocrDataFieldsMap.get(dependencyField))) {
                liFieldCnt++;
                if (liFieldCnt == piFieldPresentCount) {
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
