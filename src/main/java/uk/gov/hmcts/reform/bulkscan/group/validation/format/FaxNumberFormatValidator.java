package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import lombok.Data;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.EMAIL_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

@Data
public class FaxNumberFormatValidator implements FormatValidator {
    private static final String pattern = "^[0-9]{11}$";

    @Override
    public String validateFormat(String fieldName, String value) {
        if (this.isValidFormat(value, pattern)) {
            return VALID_MESSAGE;
        } else {
            return String.format(EMAIL_FORMAT_MESSAGE, fieldName);
        }
    }
}
