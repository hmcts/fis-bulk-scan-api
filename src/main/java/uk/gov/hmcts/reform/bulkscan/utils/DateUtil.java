package uk.gov.hmcts.reform.bulkscan.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

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
}
