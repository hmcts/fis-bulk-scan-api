package uk.gov.hmcts.reform.bulkscan.util;

public class Constant {
    private Constant() {}

    public static final String AUTH_HEADER = "serviceauthorization";

    public static final String FGM001_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-fgm001-validation-input.json";
    public static final String FGM001_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fgm001-validation-output.json";

    public static final String FGM001_VALIDATION_ERROR_INPUT_PATH =
            "classpath:requests/bulk-scan-fgm001-validation-error-input.json";
    public static final String FGM001_VALIDATION_ERROR_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fgm001-validation-error-output.json";

    public static final String FGM001_VALIDATION_WARNING_INPUT_PATH =
            "classpath:requests/bulk-scan-fgm001-validation-warning-input.json";
    public static final String FGM001_VALIDATION_WARNING_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fgm001-validation-warning-output.json";
    public static final String C63_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-c63-validation-input.json";
    public static final String C63_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c63-validation-output.json";

    public static final String C63_VALIDATION_ERROR_INPUT_PATH =
            "classpath:requests/bulk-scan-c63-validation-error-input.json";
    public static final String C63_VALIDATION_ERROR_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c63-validation-error-output.json";

    public static final String C63_VALIDATION_WARNING_INPUT_PATH =
            "classpath:requests/bulk-scan-c63-validation-warning-input.json";
    public static final String C63_VALIDATION_WARNING_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c63-validation-warning-output.json";

    public static final String C63_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-c63-transform-input.json";
    public static final String C63_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c63-transform-output.json";

    public static final String A60_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-a60-transform-input.json";
    public static final String A60_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a60-transform-output.json";
    public static final String A60_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-a60-validation-input.json";
    public static final String A60_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a60-validation-output.json";
    public static final String FL401_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-fl401-transform-input.json";
    public static final String FL401_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fl401-transform-output.json";
    public static final String FL401_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-fl401-validation-input.json";
    public static final String FL401_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fl401-validation-output.json";
    // e
    public static final String FL401_VALIDATION_ERROR_INPUT_PATH =
            "classpath:requests/bulk-scan-fl401-validation-error-input.json";
    public static final String FL401_VALIDATION_ERROR_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fl401-validation-error-output.json";
    // w
    public static final String FL401_VALIDATION_WARNING_INPUT_PATH =
            "classpath:requests/bulk-scan-fl401-validation-warning-input.json";
    public static final String FL401_VALIDATION_WARNING_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fl401-validation-warning-output.json";
    public static final String BULK_SCAN_TEST_LOCAL_HOST = "http://localhost:8090";
    public static final String BULK_SCAN_TEST_URL = "TEST_URL";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String TRANSFORM_EXCEPTION_URL = "/transform-exception-record";
}
