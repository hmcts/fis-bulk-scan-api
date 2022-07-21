package uk.gov.hmcts.reform.bulkscan.group.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.group.FormIndividualGroup;
import uk.gov.hmcts.reform.bulkscan.group.creation.Group;
import uk.gov.hmcts.reform.bulkscan.group.creation.GroupCreator;
import uk.gov.hmcts.reform.bulkscan.group.fields.CompositeField;
import uk.gov.hmcts.reform.bulkscan.group.fields.Field;
import uk.gov.hmcts.reform.bulkscan.group.selector.Selector;
import uk.gov.hmcts.reform.bulkscan.group.selector.SelectorCreator;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.MessageTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                        traverseFieldTree(compositeField, ocrDataFieldList, errorAndWarningHandler);
                    } else {
                        field.validate(ocrDataFieldList, errorAndWarningHandler);
                    }
                });
        }

        return errorAndWarningHandler.getErrorOrWarning();
    }

    private void traverseFieldTree(CompositeField compositeField, List<OcrDataField> ocrDataFieldList,
                                   ErrorAndWarningHandler errorAndWarningHandler) {
        SelectorCreator selectorCreator = new SelectorCreator();
        Selector selector = selectorCreator.getSelector(compositeField.getSelectorType());
        List<Field> fieldList = selector.apply(compositeField.getFieldList(), ocrDataFieldList,
                                               compositeField.getSelectorType(), errorAndWarningHandler);

        compositeField.setFieldList(fieldList);

        compositeField.getFieldList().stream()
            .forEach(field -> {
                if (field instanceof CompositeField) {
                    CompositeField composite = (CompositeField) field;
                    composite.validate(ocrDataFieldList, errorAndWarningHandler);
                    traverseFieldTree(composite, ocrDataFieldList, errorAndWarningHandler);
                } else {
                    field.validate(ocrDataFieldList, errorAndWarningHandler);
                }
            });
    }
}
