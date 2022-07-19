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

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("classpath:application.yaml")
public class BulkScanEndpointA58GroupFunctionalTestTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";
    private static final String A58_POST_GROUP_VALIDATION_ERRORS_INPUT_PATH =
            "classpath:requests/bulk-scan-a58-group-validation-errors-input.json";
    private static final String A58_POST_GROUP_VALIDATION_SUCCESS_INPUT_PATH =
            "classpath:requests/bulk-scan-a58-group-validation-success-input.json";
    private static final String A58_POST_GROUP_VALIDATION_ERRORS_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a58-group-validation-errors-output.json";
    private static final String A58_POST_GROUP_VALIDATION_SUCCESS_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a58-group-validation-success-output.json";

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
    @DisplayName("A58 post group validation success scenario")
    public void shouldValidate58GroupBulkScanRequestSuccess() throws Exception {
        String bulkScanValidationRequest =
                readFileFrom(A58_POST_GROUP_VALIDATION_SUCCESS_INPUT_PATH);

        String bulkScanValidationResponse =
                readFileFrom(A58_POST_GROUP_VALIDATION_SUCCESS_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
                .body(bulkScanValidationRequest)
                .when()
                .contentType("application/json")
                .post("forms/A58/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("A58 post group validation error scenario")
    public void shouldValidate58PostPlacementBulkScanRequestErrors() throws Exception {
        String bulkScanValidationRequest =
                readFileFrom(A58_POST_GROUP_VALIDATION_ERRORS_INPUT_PATH );

        String bulkScanValidationResponse =
                readFileFrom(A58_POST_GROUP_VALIDATION_ERRORS_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
                .body(bulkScanValidationRequest)
                .when()
                .contentType("application/json")
                .post("forms/A58/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }
}
