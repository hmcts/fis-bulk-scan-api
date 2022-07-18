package uk.gov.hmcts.reform.bulkscan.group.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.group.FormIndividualGroup;
import uk.gov.hmcts.reform.bulkscan.group.constants.MessageConstants;
import uk.gov.hmcts.reform.bulkscan.group.creation.Group;
import uk.gov.hmcts.reform.bulkscan.group.creation.GroupCreator;
import uk.gov.hmcts.reform.bulkscan.group.fields.CompositeField;
import uk.gov.hmcts.reform.bulkscan.group.fields.Field;
import uk.gov.hmcts.reform.bulkscan.group.selector.Selector;
import uk.gov.hmcts.reform.bulkscan.group.selector.SelectorCreator;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.ChildRelationEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.MessageTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BulkScanGroupHandler {

    @Autowired
    private ErrorAndWarningHandler errorAndWarningHandler;

    public Map<MessageTypeEnum, List<String>>  handle(FormType formType, List<OcrDataField> ocrDataFieldList) {
        Map<MessageTypeEnum, List<String>> errorOrWarning = errorAndWarningHandler.getErrorOrWarning();
        List<String> errorList = new ArrayList<>();
        List<String> warningList = new ArrayList<>();
        errorOrWarning.put(MessageTypeEnum.WARNING, warningList);
        errorOrWarning.put(MessageTypeEnum.ERROR, errorList);
        errorAndWarningHandler.setErrorOrWarning(errorOrWarning);
        GroupCreator groupCreator = new GroupCreator();
        Optional<Group> groupOptional = Optional.ofNullable(groupCreator.getGroup(formType));
        if (groupOptional.isPresent()) {
            FormIndividualGroup formIndividualGroup = groupOptional.get().create();
            formIndividualGroup.getCompositeList().stream()
                .forEach(field -> {
                    if (field instanceof CompositeField) {
                        CompositeField compositeField = (CompositeField) field;
                        traverseFieldTree(compositeField, ocrDataFieldList);
                    } else {
                        field.validate(ocrDataFieldList, errorAndWarningHandler);
                    }
                });
        }

        return errorAndWarningHandler.getErrorOrWarning();
    }

    private void traverseFieldTree(CompositeField compositeField, List<OcrDataField> ocrDataFieldList) {
        SelectorCreator selectorCreator = new SelectorCreator();
        Selector selector = selectorCreator.getSelector(compositeField.getChildRelation());
        List<Field> fieldList = selector.apply(compositeField.getFieldList(), ocrDataFieldList);
        Optional<String> requiredFieldsSelected = Optional.ofNullable(requiredFieldsSelected(
            compositeField.getFieldList(),
            fieldList,
            compositeField.getChildRelation()
        ));

        if (requiredFieldsSelected.isPresent()) {
            errorAndWarningHandler.updateError(requiredFieldsSelected.get());
        }

        compositeField.setFieldList(fieldList);

        compositeField.getFieldList().stream()
            .forEach(field -> {
                if (field instanceof CompositeField) {
                    CompositeField composite = (CompositeField) field;
                    composite.validate(ocrDataFieldList, errorAndWarningHandler);
                    traverseFieldTree(composite, ocrDataFieldList);
                } else {
                    field.validate(ocrDataFieldList, errorAndWarningHandler);
                }
            });
    }

    private String requiredFieldsSelected(List<Field> parentOriginalChildrenList,
                                          List<Field> parentChildrenSelectedByUserList,
                                          ChildRelationEnum childRelation) {
        if (ChildRelationEnum.ALL_CHILD_REQUIRED.equals(childRelation)
            && parentOriginalChildrenList.size() != parentChildrenSelectedByUserList.size()) {
            return MessageConstants.ALL_OF_THE_FOLLOWING_FIELDS_MUST_BE_SELECTED_FILLED_UP
                + parentOriginalChildrenList.stream()
                .filter(
                    outerField -> !parentChildrenSelectedByUserList.stream()
                        .anyMatch(innerField ->
                                      innerField.getName()
                                          .equalsIgnoreCase(outerField.getName())))
                    .map(field -> field.getName())
                    .collect(Collectors.joining(","));
        }
        if (ChildRelationEnum.ONE_CHILD_REQUIRED.equals(childRelation)) {
            if (parentChildrenSelectedByUserList.size() > 1) {
                return MessageConstants.MULTIPLE_FIELDS_HAVE_BEEN_SELECTED
                    + parentOriginalChildrenList.stream().map(Object::toString).collect(
                    Collectors.joining(","));
            }
            if (parentChildrenSelectedByUserList.isEmpty()) {
                return MessageConstants.AT_LEAST_ONE_OF_THE_CHECKBOX_MUST_BE_SELECTED
                    + parentOriginalChildrenList.stream().map(field -> field.getName()).collect(
                    Collectors.joining(","));
            }
        }
        return null;
    }

}
