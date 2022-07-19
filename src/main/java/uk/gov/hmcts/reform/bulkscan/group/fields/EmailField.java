package uk.gov.hmcts.reform.bulkscan.group.fields;

import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldRequiredTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.field.FieldValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.field.FieldValidatorCreator;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.List;
import java.util.Optional;

@SuperBuilder
public class EmailField extends Field {

    @Override
    public void validate(List<OcrDataField> ocrDataFieldList, ErrorAndWarningHandler errorAndWarningHandler) {
        Optional<OcrDataField> ocrDataFieldOptional = Optional.ofNullable(this.getOcrDataFieldByName(ocrDataFieldList));
        FieldValidatorCreator fieldValidatorCreator = new FieldValidatorCreator();
        FieldValidator fieldValidator = null;

        if (FieldRequiredTypeEnum.MANDATORY.equals(this.getFieldRequiredType())) {
            fieldValidator = fieldValidatorCreator.getValidator(FieldRequiredTypeEnum.MANDATORY);
        } else if (FieldRequiredTypeEnum.OPTIONAL.equals(this.getFieldRequiredType())) {
            fieldValidator = fieldValidatorCreator.getValidator(FieldRequiredTypeEnum.OPTIONAL);
        }

        Optional<FieldValidator> fieldValidatorOptional = Optional.ofNullable(fieldValidator);
        if (fieldValidatorOptional.isPresent()) {
            fieldValidator.validate(this.getName(), FieldTypeEnum.EMAIL,
                                    ocrDataFieldOptional, errorAndWarningHandler);
        }
    }
}
