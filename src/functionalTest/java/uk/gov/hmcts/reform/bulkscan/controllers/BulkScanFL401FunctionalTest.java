package uk.gov.hmcts.reform.bulkscan.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.bulkscan.client.S2sClient;

import static uk.gov.hmcts.reform.bulkscan.util.TestResourceUtil.readFileFrom;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(locations = "classpath:application_e2e.yaml")
public class BulkScanFL401FunctionalTest {

    @Autowired S2sClient s2sClient;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";
    private static final String FL401_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-FL401-validation-input.json";
    private static final String FL401_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-FL401-transform-input.json";
    private static final String FL401_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-FL401-validation-output.json";
    private static final String FL401_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-FL401-transform-output.json";

    private static final String FL401_VALIDATION_ERROR_INPUT_PATH =
            "classpath:requests/bulk-scan-FL401-validation-error-input.json";

    private static final String FL401_VALIDATION_ERROR_OUTPUT_PATH =
            "classpath:responses/bulk-scan-FL401-validation-error-output.json";

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
    public void shouldValidateFL401BulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FL401_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FL401_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/FL401/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("test case should failed with validation error")
    public void shouldValidateFL401BulkScanRequestFailed() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FL401_VALIDATION_ERROR_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FL401_VALIDATION_ERROR_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/FL401/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("should tranform FL401 incoming request to CCD format ")
    public void shouldTransformFL401BulkScanRequest() throws Exception {
        String bulkScanTransformRequest = readFileFrom(FL401_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse = readFileFrom(FL401_TRANSFORM_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanTransformRequest)
                        .when()
                        .contentType("application/json")
                        .post("/transform-exception-record");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanTransformResponse, response.getBody().asString(), true);
    }
}
