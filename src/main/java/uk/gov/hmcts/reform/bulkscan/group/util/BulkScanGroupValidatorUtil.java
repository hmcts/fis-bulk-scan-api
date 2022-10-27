package uk.gov.hmcts.reform.bulkscan.group.util;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.model.Status.ERRORS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import uk.gov.hmcts.reform.bulkscan.group.FormIndividualGroup;
import uk.gov.hmcts.reform.bulkscan.group.creation.Group;
import uk.gov.hmcts.reform.bulkscan.group.creation.GroupCreator;
import uk.gov.hmcts.reform.bulkscan.group.fields.CompositeField;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.MessageTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.Status;

public final class BulkScanGroupValidatorUtil {
    private BulkScanGroupValidatorUtil() {}

    public static List<String> getAllConfiguredGroupFields(Group group) {
        List<String> fieldNameList = new ArrayList<>();
        if (Optional.ofNullable(group).isPresent()) {
            FormIndividualGroup formIndividualGroup = group.create();
            formIndividualGroup.getCompositeList().stream()
                    .forEach(
                            field -> {
                                if (field instanceof CompositeField) {
                                    CompositeField compositeField = (CompositeField) field;
                                    fieldNameList.add(compositeField.getName());
                                    traverseFieldTree(compositeField, fieldNameList);
                                } else {
                                    fieldNameList.add(field.getName());
                                }
                            });
        }
        return fieldNameList;
    }

    private static void traverseFieldTree(
            CompositeField compositeField, List<String> fieldNameList) {
        compositeField.getFieldList().stream()
                .forEach(
                        field -> {
                            if (field instanceof CompositeField) {
                                CompositeField composite = (CompositeField) field;
                                fieldNameList.add(composite.getName());
                                traverseFieldTree(composite, fieldNameList);
                            } else {
                                fieldNameList.add(field.getName());
                            }
                        });
    }

    public static void updateGroupMissingFields(
            BulkScanValidationResponse bulkScanValidationResponse, FormType formType) {
        GroupCreator groupCreator = new GroupCreator();
        Group group = groupCreator.getGroup(formType);
        List<String> allConfiguredGroupFields =
                BulkScanGroupValidatorUtil.getAllConfiguredGroupFields(group);
        List<String> updateWarningList =
                bulkScanValidationResponse.getWarnings().stream()
                        .map(item -> updateMissingField(item, allConfiguredGroupFields))
                        .filter(s -> !StringUtils.isEmpty(s))
                        .collect(Collectors.toList());
        if (!updateWarningList.isEmpty()) {
            bulkScanValidationResponse.setWarnings(updateWarningList);
            bulkScanValidationResponse.setStatus(Status.WARNINGS);
        } else {
            bulkScanValidationResponse.setWarnings(updateWarningList);
        }
        if (!bulkScanValidationResponse.getErrors().isEmpty()) {
            bulkScanValidationResponse.setStatus(ERRORS);
        }
        if (updateWarningList.isEmpty() && bulkScanValidationResponse.getErrors().isEmpty()) {
            bulkScanValidationResponse.setStatus(Status.SUCCESS);
        }
    }

    private static String updateMissingField(String item, List<String> allConfiguredGroupFields) {
        if (item.contains(UNKNOWN_FIELDS_MESSAGE.split(":")[0])) {
            List<String> missingFieldList =
                    Arrays.asList(item.split("\\[")[1].split("\\]")[0].split(","));
            String warnings =
                    missingFieldList.stream()
                            .filter(s -> !allConfiguredGroupFields.contains(s))
                            .collect(Collectors.joining(","));
            if (StringUtils.isEmpty(warnings)) {
                return null;
            } else {
                return UNKNOWN_FIELDS_MESSAGE.split(":")[0] + ": [" + warnings + "]";
            }
        }
        return item;
    }

    /**
     * The yaml based framework validation throws warning for missing fields which are configured in
     * the group validation framework.so this function removes the missing fields from warning
     * message, which are configured in group framework.
     */
    public static void updateGroupErrorsAndWarnings(
            BulkScanValidationResponse bulkScanValidationResponse,
            Map<MessageTypeEnum, List<String>> groupErrorsAndWarningsHashMap) {

        List<String> errorsItems = bulkScanValidationResponse.getErrors();
        List<String> warningsItems = bulkScanValidationResponse.getWarnings();

        errorsItems.addAll(groupErrorsAndWarningsHashMap.get(MessageTypeEnum.ERROR));
        warningsItems.addAll(groupErrorsAndWarningsHashMap.get(MessageTypeEnum.WARNING));
    }

    public static void updateTransformationUnknownFieldsByGroupFields(
            FormType formType,
            List<String> fieldsNotKnownByYamlBasedImplementation,
            BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder) {
        if (null != fieldsNotKnownByYamlBasedImplementation
                && !fieldsNotKnownByYamlBasedImplementation.isEmpty()) {
            GroupCreator groupCreator = new GroupCreator();
            Optional<Group> groupOptional = Optional.ofNullable(groupCreator.getGroup(formType));
            if (groupOptional.isPresent()) {
                List<String> fieldsKnownByGroupBasedImplementation =
                        BulkScanGroupValidatorUtil.getAllConfiguredGroupFields(groupOptional.get());
                List<String> warningMessages =
                        fieldsNotKnownByYamlBasedImplementation.stream()
                                .filter(
                                        warning ->
                                                !fieldsKnownByGroupBasedImplementation.contains(
                                                        warning))
                                .collect(Collectors.toList());
                if (!warningMessages.isEmpty()) {
                    builder.warnings(
                            Arrays.asList(
                                    String.format(
                                            UNKNOWN_FIELDS_MESSAGE,
                                            String.join(",", warningMessages))));
                }
            } else {
                builder.warnings(
                        Arrays.asList(
                                String.format(
                                        UNKNOWN_FIELDS_MESSAGE,
                                        String.join(
                                                ",", fieldsNotKnownByYamlBasedImplementation))));
            }
        }
    }
}
