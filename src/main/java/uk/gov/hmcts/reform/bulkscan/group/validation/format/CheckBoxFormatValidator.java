package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.CHECKBOX_FORMAT_MESSAGE;

import lombok.Data;
import uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.CheckboxEnum;

@Data
public class CheckBoxFormatValidator implements FormatValidator {

    @Override
    public String validateFormat(String fieldName, String value) {
        if (!CheckboxEnum.TRUE.getValue().equals(value)
                && !CheckboxEnum.FALSE.getValue().equals(value)) {
            return String.format(CHECKBOX_FORMAT_MESSAGE, fieldName);
        } else {
            return BulkScanGroupConstants.VALID_MESSAGE;
        }
    }
}
