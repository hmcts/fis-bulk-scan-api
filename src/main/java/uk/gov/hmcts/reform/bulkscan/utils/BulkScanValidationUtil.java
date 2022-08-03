package uk.gov.hmcts.reform.bulkscan.utils;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;

public final class BulkScanValidationUtil {

    private BulkScanValidationUtil() {}

    public static boolean isDateValid(String fieldName, String dateStr, String format) {
        DateFormat sdf = new SimpleDateFormat(format, Locale.UK);
        sdf.setLenient(false);
        try {
            Date parse = sdf.parse(dateStr);
            Date current = new Date();
            if ((fieldName.contains(BulkScanConstants.DOB_HINT)
                            || fieldName.contains(BulkScanConstants.DATE_OF_BIRTH_HINT))
                    && parse.after(current)) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean isValidFormat(String value, String regex) {
        Pattern regexPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.matches();
    }

    public static boolean isInValidFormat(String value, String regex) {
        Pattern regexPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.find();
    }

    public static List<String> notNull(String value, String field) {
        return isEmpty(value)
                ? List.of(String.format(MANDATORY_ERROR_MESSAGE, field))
                : emptyList();
    }

    @SafeVarargs
    public static <E> List<E> flattenLists(List<E>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(toList());
    }
}
