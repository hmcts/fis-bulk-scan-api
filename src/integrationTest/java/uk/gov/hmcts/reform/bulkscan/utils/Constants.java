package uk.gov.hmcts.reform.bulkscan.utils;

public final class Constants {
    public static final String SERVICE_AUTHORIZATION = "serviceauthorization";
    public static final String SERVICE_AUTHORIZATION_VALUE = "SlAV32hkKG";

    public static final String FL401_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/FL401/validate-ocr";
    public static final String FL403_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/FL403/validate-ocr";
    public static final String C51_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/C51/validate-ocr";
    public static final String C63_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/C63/validate-ocr";
    public static final String A58_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/A58/validate-ocr";
    public static final String A59_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/A59/validate-ocr";
    public static final String A60_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/A60/validate-ocr";

    public static final String C100_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-c100-Page1-validation-input.json";
    public static final String A60_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-a60-validation-input.json";
    public static final String C100_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/C100/validate-ocr";
    public static final String C2_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/C2/validate-ocr";
    public static final String EdgeCase_CASE_TYPE_VALIDATE_ENDPOINT = "/forms/EdgeCase/validate-ocr";
    public static final String C100_CASE_TYPE_TRANSFORM_ENDPOINT = "/forms/FL401/transform";
    public static final String FL401_CASE_TYPE_TRANSFORM_ENDPOINT = "/forms/FL403/transform";
    public static final String FL403_CASE_TYPE_TRANSFORM_ENDPOINT = "/forms/FL401/transform";
    public static final String C2_CASE_TYPE_TRANSFORM_ENDPOINT = "/forms/C2/transform";

    public static final String CASE_TYPE_TRANSFORM_ENDPOINT = "/transform-exception-record";

    public static final String WELCOME_BULK_SCAN_API_MESSAGE = "Welcome to fis-bulk-scan-api-latest-24-May";

    private Constants() {
    }
}
