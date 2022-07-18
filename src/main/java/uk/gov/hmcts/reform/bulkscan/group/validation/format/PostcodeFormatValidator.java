package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import lombok.Data;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.POST_CODE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

@Data
public class PostcodeFormatValidator implements FormatValidator {
    private static final String pattern = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";

    @Override
    public String validateFormat(String fieldName, String value) {
        if (this.isValidFormat(value, pattern)) {
            return VALID_MESSAGE;
        } else {
            return String.format(POST_CODE_MESSAGE, fieldName);
        }
    }
}
