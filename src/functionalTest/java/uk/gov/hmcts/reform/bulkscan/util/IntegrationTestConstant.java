package uk.gov.hmcts.reform.bulkscan.util;

public final class IntegrationTestConstant {
    private IntegrationTestConstant() {
    }

    public static final String AUTH_HEADER = "serviceauthorization";
    public static final String A58_STEP_PARENT_TRANSFORM_INPUT_PATH =
        "classpath:requests/bulk-scan-a58-step-parent-transform-input.json";
    public static final String A58_STEP_PARENT_TRANSFORM_OUTPUT_PATH =
        "classpath:responses/bulk-scan-a58-step-parent-transform-output.json";
    public static final String A58_STEP_PARENT_VALIDATION_INPUT_PATH =
        "classpath:requests/bulk-scan-a58-step-parent-validation-input.json";
    public static final String A58_STEP_PARENT_VALIDATION_OUTPUT_PATH =
        "classpath:responses/bulk-scan-a58-step-parent-validation-output.json";
    public static final String A60_TRANSFORM_INPUT_PATH =
        "classpath:requests/bulk-scan-a60-transform-input.json";
    public static final String A60_TRANSFORM_OUTPUT_PATH =
        "classpath:responses/bulk-scan-a60-transform-output.json";
    public static final String A60_VALIDATION_INPUT_PATH =
        "classpath:requests/bulk-scan-a60-validation-input.json";
    public static final String A60_VALIDATION_OUTPUT_PATH =
        "classpath:responses/bulk-scan-a60-validation-output.json";
    public static final String BULK_SCAN_TEST_URL = "TEST_URL";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String TRANSFORM_EXCEPTION_URL = "/transform-exception-record";
}
