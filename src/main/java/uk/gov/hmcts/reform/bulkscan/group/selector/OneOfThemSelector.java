package uk.gov.hmcts.reform.bulkscan.group.selector;

import java.util.List;
import java.util.stream.Collectors;
import uk.gov.hmcts.reform.bulkscan.group.constants.MessageConstants;
import uk.gov.hmcts.reform.bulkscan.group.fields.Field;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.SelectorEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

public class OneOfThemSelector implements Selector {
    @Override
    public List<Field> apply(
            List<Field> fieldList,
            List<OcrDataField> ocrDataFields,
            SelectorEnum selectorType,
            ErrorAndWarningHandler errorAndWarningHandler) {
        List<Field> selectedFieldList = getSelectedFieldList(fieldList, ocrDataFields);

        if (SelectorEnum.ONE_CHILD_REQUIRED.equals(selectorType)) {
            if (selectedFieldList.size() > 1) {
                errorAndWarningHandler.updateError(
                        MessageConstants.MULTIPLE_FIELDS_HAVE_BEEN_SELECTED
                                + fieldList.stream()
                                        .map(field -> field.getName())
                                        .collect(Collectors.joining(",")));
            }
            if (selectedFieldList.isEmpty()) {
                errorAndWarningHandler.updateError(
                        MessageConstants.AT_LEAST_ONE_OF_THE_CHECKBOX_MUST_BE_SELECTED
                                + fieldList.stream()
                                        .map(field -> field.getName())
                                        .collect(Collectors.joining(",")));
            }
        }

        return selectedFieldList;
    }
}
