package uk.gov.hmcts.reform.bulkscan.controllers;

import static uk.gov.hmcts.reform.bulkscan.util.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;

public class BulkScanC51FunctionalTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";
    private static final String C51_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-c51-validation-input.json";
    private static final String C51_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-c51-transform-input.json";
    private static final String C51_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c51-validation-output.json";
    private static final String C51_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c51-transform-output.json";

    private static final String C51_VALIDATION_ERROR_INPUT_PATH =
            "classpath:requests/bulk-scan-c51-validation-error-input.json";

    private static final String C51_VALIDATION_ERROR_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c51-validation-error-output.json";

    private static final String C51_XOR_VALIDATION_XOR_ERROR_INPUT_PATH =
            "classpath:requests/bulk-scan-c51-xor-validation-error-input.json";

    private static final String C51_XOR_VALIDATION_ERROR_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c51-xor-validation-error-output.json";

    private final String targetInstance =
            StringUtils.defaultIfBlank(System.getenv("TEST_URL"), "http://localhost:8090");

    private final RequestSpecification request =
            RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance);

    @Before
    public void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("test case should pass with correct input")
    public void shouldValidateC51BulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C51_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(C51_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C51/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("test case should failed with validation error")
    public void shouldValidateC51BulkScanRequestFailed() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C51_VALIDATION_ERROR_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(C51_VALIDATION_ERROR_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C51/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("test case should failed with validation error")
    public void shouldValidateC51BulkScanXoRRequestFailed() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C51_VALIDATION_ERROR_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(C51_VALIDATION_ERROR_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C51/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("should tranform C51 incoming request to CCD format ")
    public void shouldTransformC51BulkScanRequest() throws Exception {
        String bulkScanTransformRequest = readFileFrom(C51_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse = readFileFrom(C51_TRANSFORM_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanTransformRequest)
                        .when()
                        .contentType("application/json")
                        .post("/transform-exception-record");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanTransformResponse, response.getBody().asString(), true);
    }
}
