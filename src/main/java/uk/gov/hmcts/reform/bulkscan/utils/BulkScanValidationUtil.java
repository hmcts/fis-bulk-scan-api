package uk.gov.hmcts.reform.bulkscan.utils;

import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BulkScanValidationUtil {

    private BulkScanValidationUtil() {

    }

    public static boolean isDateValid(String fieldName, String dateStr, String format) {
        DateFormat sdf = new SimpleDateFormat(format, Locale.UK);
        sdf.setLenient(false);
        try {
            Date parse = sdf.parse(dateStr);
            Date current = new Date();
            if ((fieldName.contains(BulkScanConstants.DOB_HINT)
                || fieldName.contains(BulkScanConstants.DATE_OF_BIRTH_HINT)) && parse.after(current)) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidFormat(String value, String regex) {
        Pattern regexPattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.matches();
    }

    public static boolean isInValidFormat(String value, String regex) {
        Pattern regexPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.find();
    }
}
