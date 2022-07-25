package uk.gov.hmcts.reform.bulkscan.group.selector;

import uk.gov.hmcts.reform.bulkscan.group.constants.MessageConstants;
import uk.gov.hmcts.reform.bulkscan.group.fields.Field;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.SelectorEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.List;
import java.util.stream.Collectors;

public class AllOfThemSelector implements Selector {
    @Override
    public List<Field> apply(List<Field> fieldList, List<OcrDataField> ocrDataFields, SelectorEnum selectorType,
                             ErrorAndWarningHandler errorAndWarningHandler) {
        List<Field> selectedFieldList = getSelectedFieldList(fieldList, ocrDataFields);

        if (SelectorEnum.ALL_CHILD_REQUIRED.equals(selectorType)
            && fieldList.size() != selectedFieldList.size()) {
            String fieldsNotConfigured = fieldList.stream()
                .filter(
                    outerField -> !selectedFieldList.stream()
                        .anyMatch(innerField ->
                                      innerField.getName()
                                          .equalsIgnoreCase(outerField.getName())))
                .map(field -> field.getName())
                .collect(Collectors.joining(","));
            errorAndWarningHandler.updateError(MessageConstants.ALL_OF_THE_FOLLOWING_FIELDS_MUST_BE_SELECTED_FILLED_UP
                + fieldsNotConfigured);
        }

        return selectedFieldList;
    }
}


