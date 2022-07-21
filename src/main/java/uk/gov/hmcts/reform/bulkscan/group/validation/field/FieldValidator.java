package uk.gov.hmcts.reform.bulkscan.group.validation.field;

import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Optional;

public interface FieldValidator {
    void validate(String fieldName, FieldTypeEnum email,
                  Optional<OcrDataField> ocrDataFieldOptional, ErrorAndWarningHandler errorAndWarningHandler);
}
