package uk.gov.hmcts.reform.bulkscan.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class DateUtil {
    private DateUtil() {}

    public static boolean validateDate(String dateStr, String pattern) {

        DateTimeFormatterBuilder dateTimeFormatterBuilder =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .append(
                                DateTimeFormatter.ofPattern(pattern)
                                        .withResolverStyle(ResolverStyle.STRICT));

        try {
            LocalDate.parse(dateStr, dateTimeFormatterBuilder.toFormatter());
        } catch (DateTimeParseException e) {
            log.error("Date {} is in invalid", dateStr);
            return false;
        }
        return true;
    }

    public static String transformDate(String dateStr, String pattern, String formatPattern) {

        String formattedDate = null;

        DateTimeFormatterBuilder dateTimeFormatterBuilder =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .append(
                                DateTimeFormatter.ofPattern(pattern)
                                        .withResolverStyle(ResolverStyle.STRICT));

        try {
            final LocalDate localDate =
                    LocalDate.parse(dateStr, dateTimeFormatterBuilder.toFormatter());
            formattedDate = localDate.format(DateTimeFormatter.ofPattern(formatPattern));
        } catch (DateTimeParseException e) {
            log.error("Date {} is in invalid", dateStr);
        }
        return formattedDate;
    }

    public static String buildDate(String dd, String mm, String yyyy) {
        if (StringUtils.isBlank(dd) || StringUtils.isBlank(mm) || StringUtils.isBlank(yyyy)) {
            return null;
        }
        return yyyy + "-" + mm + "-" + dd;
    }
}
