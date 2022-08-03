package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.NUMERIC_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

import lombok.Data;

@Data
public class NumericFormatValidator implements FormatValidator {
    private static final String pattern = "^[0-9]*$";

    @Override
    public String validateFormat(String fieldName, String value) {
        if (this.isValidFormat(value, pattern)) {
            return VALID_MESSAGE;
        } else {
            return String.format(NUMERIC_FORMAT_MESSAGE, fieldName);
        }
    }
}
