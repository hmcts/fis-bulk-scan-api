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
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.bulkscan.client.S2sClient;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(locations = "classpath:application_e2e.yaml")
public class BulkScanEndpointPostPlacementAdoptionTest {

    @Autowired S2sClient s2sClient;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";
    private static final String A58_POST_PLACEMENT_VALIDATION_ERRORS_INPUT_PATH =
            "classpath:requests/bulk-scan-a58-post-placement-validation-errors-input.json";
    private static final String A58_POST_PLACEMENT_VALIDATION_SUCCESS_INPUT_PATH =
            "classpath:requests/bulk-scan-a58-post-placement-validation-success-input.json";
    private static final String A58_POST_PLACEMENT_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-a58-post-placement-transform-input.json";
    private static final String A58_POST_PLACEMENT_VALIDATION_ERRORS_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a58-post-placement-validation-errors-output.json";
    private static final String A58_POST_PLACEMENT_VALIDATION_SUCCESS_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a58-post-placement-validation-success-output.json";
    private static final String A58_POST_PLACEMENT_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a58-post-placement-transform-output.json";

    private final String targetInstance =
            StringUtils.defaultIfBlank(System.getenv("TEST_URL"), "http://localhost:8090");

    private final RequestSpecification request =
            RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance);

    @Before
    public void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("A58 post placement form validation success scenario")
    public void shouldValidate58PostPlacementBulkScanRequestSuccess() throws Exception {
        String bulkScanValidationRequest =
                readFileFrom(A58_POST_PLACEMENT_VALIDATION_SUCCESS_INPUT_PATH);

        String bulkScanValidationResponse =
                readFileFrom(A58_POST_PLACEMENT_VALIDATION_SUCCESS_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/A58/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("A58 post placement form validation error scenario")
    public void shouldValidate58PostPlacementBulkScanRequestErrors() throws Exception {
        String bulkScanValidationRequest =
                readFileFrom(A58_POST_PLACEMENT_VALIDATION_ERRORS_INPUT_PATH);

        String bulkScanValidationResponse =
                readFileFrom(A58_POST_PLACEMENT_VALIDATION_ERRORS_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType("application/json")
                        .post("forms/A58/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("A58 post placement form transform scenario")
    public void shouldTransformA58PostPlacementBulkScanRequest() throws Exception {
        String bulkScanTransformRequest = readFileFrom(A58_POST_PLACEMENT_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse = readFileFrom(A58_POST_PLACEMENT_TRANSFORM_OUTPUT_PATH);

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
