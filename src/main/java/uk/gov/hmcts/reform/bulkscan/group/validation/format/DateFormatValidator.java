package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.INVALID_DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.Data;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;

@Data
public class DateFormatValidator implements FormatValidator {
    private static final String pattern = "dd/MM/yyyy";

    @Override
    public String validateFormat(String fieldName, String value) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.UK);
        dateFormat.setLenient(false);
        try {
            Date parse = dateFormat.parse(value);
            Date current = new Date();
            if ((fieldName.contains(BulkScanConstants.DOB_HINT)
                            || fieldName.contains(BulkScanConstants.DATE_OF_BIRTH_HINT))
                    && parse.after(current)) {
                return String.format(INVALID_DATE_FORMAT_MESSAGE, fieldName);
            }
        } catch (ParseException e) {
            return String.format(INVALID_DATE_FORMAT_MESSAGE, fieldName);
        }

        return VALID_MESSAGE;
    }
}
