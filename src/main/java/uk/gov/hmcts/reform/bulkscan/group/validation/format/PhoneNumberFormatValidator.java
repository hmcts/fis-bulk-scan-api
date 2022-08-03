package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.PHONE_NUMBER_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

import lombok.Data;

@Data
public class PhoneNumberFormatValidator implements FormatValidator {
    private static final String pattern = "^(?:0|\\+?44)(?:\\d\\s?){9,10}$";

    @Override
    public String validateFormat(String fieldName, String value) {
        if (this.isValidFormat(value, pattern)) {
            return VALID_MESSAGE;
        } else {
            return String.format(PHONE_NUMBER_MESSAGE, fieldName);
        }
    }
}
