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
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MIAM_ATTEND_EXEMPTION_GROUP_FIELD;

@Service
public class BulkScanC100GroupDependencyValidation implements BulkScanService {
    private static final int REQUIRED_EXEMPTION_DEPENDENCY_FIELD_VALIDATION = 1;
    public static final String C100_COMPULSORY_SECTION_MESSAGE = "All applicants must complete sections "
        + "1, 2 and 5 to 14 before signing this form. Section %s is incomplete";


    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        Map<String, Integer> groupDependencyCountMap = new HashMap<>();

        groupDependencyCountMap.put(MIAM_ATTEND_EXEMPTION_GROUP_FIELD, REQUIRED_EXEMPTION_DEPENDENCY_FIELD_VALIDATION);

        return validateGroupDependencyFields(
            bulkRequest.getOcrdatafields(),
            configManager.getFieldDependenyConfig(
                FieldDependency.C100_FIELD_DEPENDENCY),
            groupDependencyCountMap
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        return null;
    }

    private BulkScanValidationResponse validateMutuallyExclusiveFielkds(
        List<OcrDataField> ocrDataFields,
        BulkScanFormValidationConfigManager.GroupDependencyValidationConfig grpFldDepValidationConfig) {
        Map<String, String> ocrDataFieldsMap = ocrDataFields
            .stream()
            .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        List<String> errorOrWarnings = new ArrayList<>();

        if (ocrDataFieldsMap.get("ExistingCase_onEmergencyProtection_Care_or_supervisioNorder").isEmpty()) {
            if (ocrDataFieldsMap.get("exemption_to_attend_MIAM").isEmpty()) {
                if (ocrDataFieldsMap.get("familyMember_Intimation_on_No_MIAM").isEmpty()) {
                    if (ocrDataFieldsMap.get("attended_MIAM").isEmpty()) {
                        errorOrWarnings.add(String.format(C100_COMPULSORY_SECTION_MESSAGE, "2"));
                    }
                }
            }
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
