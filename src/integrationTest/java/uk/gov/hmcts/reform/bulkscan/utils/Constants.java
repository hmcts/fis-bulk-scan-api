package uk.gov.hmcts.reform.bulkscan.utils;

public final class Constants {
    public static final String SERVICE_AUTHORIZATION = "serviceauthorization";
    public static final String SERVICE_AUTHORIZATION_VALUE = "SlAV32hkKG";

    public static final String FL401_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/FL401/validate-ocr";
    public static final String FL403_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/FL403/validate-ocr";
    public static final String C51_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/C51/validate-ocr";
    public static final String C63_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/C63/validate-ocr";
    public static final String A58_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/A58/validate-ocr";
    public static final String A60_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/A60/validate-ocr";
    public static final String C2_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/C2/validate-ocr";
    public static final String EdgeCase_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/EdgeCase/validate-ocr";
    public static final String FL401_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-fl401-validation-input.json";
    public static final String FL403_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-fl403-validation-input.json";
    public static final String EdgeCase_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-fl403-validation-input.json";
    public static final String C2_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-c2-validation-input.json";
    public static final String C2_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-c2-transform-input.json";
    public static final String C100_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-c100-transform-input.json";
    public static final String EdgeCase_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-edgecase-transform-input.json";
    public static final String FL401_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-fl401-transform-input.json";
    public static final String FL403_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-fl403-transform-input.json";
    public static final String A58_STEP_PARENT_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-a58-step-parent-transform-input.json";
    public static final String A58_STEP_PARENT_TRANSFORM_RESPONSE_PATH =
        "classpath:response/bulk-scan-a58-step-parent-transform-output.json";
    public static final String FL403_VALIDATION_RESPONSE_PATH =
        "classpath:response/bulk-scan-fl403-validation-response.json";
    public static final String C51_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-c51-validation-input.json";
    public static final String C63_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-c63-validation-input.json";
    public static final String A60_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-a60-validation-input.json";
    public static final String A58_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-a58-validation-input.json";

    public static final String CASE_TYPE_TRANSFORM_ENDPOINT = "/transform-exception-record";

    public static final String WELCOME_BULK_SCAN_API_MESSAGE = "Welcome to fis-bulk-scan-api-latest-24-May";

    private Constants() {
    }
}
