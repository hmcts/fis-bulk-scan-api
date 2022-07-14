package uk.gov.hmcts.reform.bulkscan.controllers;

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

import static uk.gov.hmcts.reform.bulkscan.util.TestResourceUtil.readFileFrom;

;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("classpath:application.yaml")
public class BulkScanC100FunctionalTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";

    private static final String C100_SECTION2_VALIDATION_INPUT_PATH =
        "classpath:requests/bulk-scan-c100-section2-validation-success-input.json";

    private static final String C100_SECTION2_VALIDATION_OUTPUT_PATH =
        "classpath:responses/bulk-scan-c100-section2-validation-success-output.json";
    private static final String C100_SECTION2_VALIDATION_ERROR_SCENARIO1_INPUT_PATH =
        "classpath:requests/bulk-scan-c100-section2-validation-error-scenario1-input.json";
    private static final String C100_SECTION2_VALIDATION_ERROR_SCENARIO1_OUTPUT_PATH =
        "classpath:responses/bulk-scan-c100-section2-validation-error-scenario1-output.json";


    private final String targetInstance =
        StringUtils.defaultIfBlank(
            System.getenv("TEST_URL"),
            "http://localhost:8090"
        );

    private final RequestSpecification request = RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance);

    @Before
    public void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("test case should pass with correct input")
    public void shouldValidate100Section2BulkScanRequest() throws Exception {
        String bulkScanValidationRequest =
            readFileFrom(C100_SECTION2_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse =
            readFileFrom(C100_SECTION2_VALIDATION_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
            .body(bulkScanValidationRequest)
            .when()
            .contentType("application/json")
            .post("forms/C100/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }
    @Test
    @DisplayName("when previous_or_ongoingProceeding is Yes but existingCase_onEmergencyProtection_Care_or_supervisioNorder is empty")
    public void shouldValidate100Section2ErrorSection1BulkScanRequest() throws Exception {
        String bulkScanValidationRequest =
            readFileFrom(C100_SECTION2_VALIDATION_ERROR_SCENARIO1_INPUT_PATH);

        String bulkScanValidationResponse =
            readFileFrom(C100_SECTION2_VALIDATION_ERROR_SCENARIO1_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
            .body(bulkScanValidationRequest)
            .when()
            .contentType("application/json")
            .post("forms/C100/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

}

