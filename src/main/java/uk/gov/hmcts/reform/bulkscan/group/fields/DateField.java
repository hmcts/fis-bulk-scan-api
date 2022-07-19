package uk.gov.hmcts.reform.bulkscan.group.fields;

import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.List;

@SuperBuilder
public class DateField extends Field {

    @Override
    public void validate(List<OcrDataField> ocrDataFieldList, ErrorAndWarningHandler errorAndWarningHandler) {
        this.validateField(FieldTypeEnum.DATE, ocrDataFieldList, errorAndWarningHandler);
    }
}
