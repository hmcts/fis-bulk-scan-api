package uk.gov.hmcts.reform.bulkscan.constants;

import java.util.Map;

public final class BulkScanConstants {

    public static final String MANDATORY_ERROR_MESSAGE = "%s should not be null or empty";
    public static final String DATE_FORMAT_ERROR_MESSAGE = "%s is invalid date format";
    public static final String EMAIL_FORMAT_ERROR_MESSAGE = "%s is invalid email";
    public static final String NUMERIC_ERROR_MESSAGE = "%s is not a number";

    public static final String MANDATORY_MESSAGE_KEY = "mandatoryMessage";
    public static final String DATE_FORMAT_MESSAGE_KEY = "dateFormatMessage";
    public static final String EMAIL_FORMAT_MESSAGE_KEY = "emailFormatMessage";
    public static final String NUMERIC_MESSAGE_KEY = "numericMessage";

    public static final Map<String, String> ERROR_MESSAGE_MAP = getErrorMessageMap();

    private static Map<String,String> getErrorMessageMap() {
        return Map.of(MANDATORY_MESSAGE_KEY, MANDATORY_ERROR_MESSAGE,
               DATE_FORMAT_MESSAGE_KEY, DATE_FORMAT_ERROR_MESSAGE,
               EMAIL_FORMAT_MESSAGE_KEY, EMAIL_FORMAT_ERROR_MESSAGE,
               NUMERIC_MESSAGE_KEY, NUMERIC_ERROR_MESSAGE);
    }


    private BulkScanConstants() {

    }

}
