package uk.gov.hmcts.reform.bulkscan.group.selector;

import org.apache.commons.lang3.ObjectUtils;
import uk.gov.hmcts.reform.bulkscan.group.fields.CheckboxField;
import uk.gov.hmcts.reform.bulkscan.group.fields.CompositeField;
import uk.gov.hmcts.reform.bulkscan.group.fields.Field;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.CheckboxEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OneOfThemSelector implements Selector {
    @Override
    public List<Field> apply(List<Field> fieldList, List<OcrDataField> ocrDataFields) {
        List<Field> selectedFieldList = new ArrayList<>();
        fieldList.stream()
            .forEach(field -> {
                Optional<OcrDataField> ocrDataFieldOptional =
                    ocrDataFields.stream()
                        .filter(ocrDataField ->
                                    ocrDataField.getName().equals(field.getName())).findAny();
                if (ocrDataFieldOptional.isPresent() && field instanceof CompositeField) {
                    CompositeField compositeField = (CompositeField) field;
                    if (FieldTypeEnum.CHECKBOX.equals(compositeField.getFieldType())
                        && CheckboxEnum.TRUE.getValue().equals(ocrDataFieldOptional.get().getValue())) {
                        selectedFieldList.add(field);
                    } else if (!ObjectUtils.isEmpty(ocrDataFieldOptional.get().getValue())) {
                        selectedFieldList.add(field);
                    }
                } else if (ocrDataFieldOptional.isPresent()
                    && field instanceof CheckboxField
                    && CheckboxEnum.TRUE.getValue().equals(ocrDataFieldOptional.get().getValue())) {
                    selectedFieldList.add(field);
                } else if (ocrDataFieldOptional.isPresent()
                    && !ObjectUtils.isEmpty(ocrDataFieldOptional.get().getValue())) {
                    selectedFieldList.add(field);
                }
            });

        return selectedFieldList;
    }
}
