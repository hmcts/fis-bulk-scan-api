package uk.gov.hmcts.reform.bulkscan.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DateUtil {
    private DateUtil() {}

    public static boolean validateDate(String dateStr, String pattern) {

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            log.error("Date {} is in invalid", dateStr);
            return false;
        }
        return true;
    }
}
