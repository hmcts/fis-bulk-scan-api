package uk.gov.hmcts.reform.bulkscan.group.validation;

import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.CheckBoxFormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.DateFormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.EmailFormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.FaxNumberFormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.FormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.NumericFormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.PhoneNumberFormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.PostcodeFormatValidator;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.TextFormatValidator;

public class FormatValidatorCreator {
    public FormatValidator getValidator(FieldTypeEnum fieldTypeEnum) {
        if (FieldTypeEnum.DATE.equals(fieldTypeEnum)) {
            return new DateFormatValidator();
        } else if (FieldTypeEnum.EMAIL.equals(fieldTypeEnum)) {
            return new EmailFormatValidator();
        } else if (FieldTypeEnum.NUMERIC.equals(fieldTypeEnum)) {
            return new NumericFormatValidator();
        } else if (FieldTypeEnum.POSTCODE.equals(fieldTypeEnum)) {
            return new PostcodeFormatValidator();
        } else if (FieldTypeEnum.PHONE_NUMBER.equals(fieldTypeEnum)) {
            return new PhoneNumberFormatValidator();
        } else if (FieldTypeEnum.CHECKBOX.equals(fieldTypeEnum)) {
            return new CheckBoxFormatValidator();
        } else if (FieldTypeEnum.FAX_NUMBER.equals(fieldTypeEnum)) {
            return new FaxNumberFormatValidator();
        } else {
            return new TextFormatValidator();
        }
    }
}
