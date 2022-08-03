package uk.gov.hmcts.reform.bulkscan.group.fields;

import java.util.List;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

@SuperBuilder
public class PhoneNumberField extends Field {

    @Override
    public void validate(
            List<OcrDataField> ocrDataFieldList, ErrorAndWarningHandler errorAndWarningHandler) {
        this.validateField(FieldTypeEnum.PHONE_NUMBER, ocrDataFieldList, errorAndWarningHandler);
    }
}
