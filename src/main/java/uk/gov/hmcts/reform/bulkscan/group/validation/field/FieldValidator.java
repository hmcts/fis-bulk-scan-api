package uk.gov.hmcts.reform.bulkscan.group.validation.field;

import java.util.Optional;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

public interface FieldValidator {
    void validate(
            String fieldName,
            FieldTypeEnum email,
            Optional<OcrDataField> ocrDataFieldOptional,
            ErrorAndWarningHandler errorAndWarningHandler);
}
