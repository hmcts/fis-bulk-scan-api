package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

public class TextFormatValidator implements FormatValidator {
    @Override
    public String validateFormat(String fieldName, String value) {
        return VALID_MESSAGE;
    }
}
