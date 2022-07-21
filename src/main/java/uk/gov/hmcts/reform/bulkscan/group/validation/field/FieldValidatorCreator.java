package uk.gov.hmcts.reform.bulkscan.group.validation.field;

import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldRequiredTypeEnum;

public class FieldValidatorCreator {
    public FieldValidator getValidator(FieldRequiredTypeEnum fieldRequiredType) {
        if (FieldRequiredTypeEnum.MANDATORY.equals(fieldRequiredType)) {
            return new MandatoryFieldValidator();
        } else {
            return new OptionalFieldValidator();
        }
    }
}
