package uk.gov.hmcts.reform.bulkscan.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BulkScanValidationUtil {

    private BulkScanValidationUtil() {

    }

    public static boolean isDateValid(String dateStr, String format) {
        DateFormat sdf = new SimpleDateFormat(format, Locale.UK);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
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
