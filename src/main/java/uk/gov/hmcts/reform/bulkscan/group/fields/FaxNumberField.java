package uk.gov.hmcts.reform.bulkscan.group.fields;

import java.util.List;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

public class FaxNumberField extends Field {

    @Override
    public void validate(
            List<OcrDataField> ocrDataFieldList, ErrorAndWarningHandler errorAndWarningHandler) {
        this.validateField(FieldTypeEnum.FAX_NUMBER, ocrDataFieldList, errorAndWarningHandler);
    }
}
