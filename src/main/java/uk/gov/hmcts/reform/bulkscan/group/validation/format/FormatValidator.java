package uk.gov.hmcts.reform.bulkscan.group.validation.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface FormatValidator {

    String validateFormat(String fieldName, String value);

    default  boolean isValidFormat(String value, String regex) {
        Pattern regexPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.matches();
    }
}
