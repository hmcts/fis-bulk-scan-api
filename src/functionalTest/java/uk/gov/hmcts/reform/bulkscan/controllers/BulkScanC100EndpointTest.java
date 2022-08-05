package uk.gov.hmcts.reform.bulkscan.controllers;

import static uk.gov.hmcts.reform.bulkscan.util.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("classpath:application.yaml")
public class BulkScanC100EndpointTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";
    private static final String C100_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-c100-validate-input.json";
    private static final String C100_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c100-validation-output.json";
    private static final String C100_VALIDATION_SECTION6B_INPUT_PATH =
            "classpath:requests/bulk-scan-c100-section6b-validate-input.json";
    private static final String C100_VALIDATION_SECTION6B_ERROR_INPUT_PATH =
            "classpath:requests/bulk-scan-c100-section6b-error-validate-input.json";
    private static final String C100_VALIDATION_SECTION6B_ERROR_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c100-section6b-error-validation-output.json";
    private static final String C100_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-c100-transform-input.json";
    private static final String C100_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c100-transform-output.json";

    private static final String C100_PAGE_1_WARNING_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-c100-warning-validation-input.json";

    private static final String C100_PAGE_1_WARNING_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c100-warning-validation-output.json";

    private static final String C100_PAGE_1_ERROR_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-c100-error-validation-input.json";

    private static final String C100_PAGE_1_ERROR_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-c100-error-validation-output.json";

    private final String targetInstance =
            StringUtils.defaultIfBlank(System.getenv("TEST_URL"), "http://localhost:8090");

    private final RequestSpecification request =
            RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance);

    @Before
    public void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldValidateC100BulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C100_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(C100_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C100/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    public void shouldValidateC100Section6bBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C100_VALIDATION_SECTION6B_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(C100_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C100/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    public void shouldValidateC100Section6bErrorBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C100_VALIDATION_SECTION6B_ERROR_INPUT_PATH);

        String bulkScanValidationResponse =
                readFileFrom(C100_VALIDATION_SECTION6B_ERROR_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C100/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    public void shouldTransformBulkScanRequest() throws Exception {
        String bulkScanTransformRequest = readFileFrom(C100_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse = readFileFrom(C100_TRANSFORM_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanTransformRequest)
                        .when()
                        .contentType("application/json")
                        .post("/transform-exception-record");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanTransformResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating errors for mandatory fields and unknown field for c100")
    public void shouldValidateC100ErrorBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C100_PAGE_1_ERROR_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(C100_PAGE_1_ERROR_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C100/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating warnings for mandatory fields and unknown field for c100")
    public void shouldValidateC100WarningBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(C100_PAGE_1_WARNING_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse =
                readFileFrom(C100_PAGE_1_WARNING_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, AUTH_HEADER)
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/C100/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }
}
