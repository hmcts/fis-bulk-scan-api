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
public class BulkScanEndpointTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";
    private static final String A58_STEP_PARENT_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-a58-step-parent-validation-input.json";
    private static final String A58_STEP_PARENT_TRANSFORM_INPUT_PATH =
            "classpath:requests/bulk-scan-a58-step-parent-transform-input.json";
    private static final String A58_STEP_PARENT_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a58-step-parent-validation-output.json";
    private static final String A58_STEP_PARENT_TRANSFORM_OUTPUT_PATH =
            "classpath:responses/bulk-scan-a58-step-parent-transform-output.json";

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
    public void shouldValidate58StepParentBulkScanRequest() throws Exception {
        String bulkScanValidationRequest =
                readFileFrom(A58_STEP_PARENT_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse =
                readFileFrom(A58_STEP_PARENT_VALIDATION_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
                .body(bulkScanValidationRequest)
                .when()
                .contentType("application/json")
                .post("forms/A58/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    public void shouldTransformA58StepParentBulkScanRequest() throws Exception {
        String bulkScanTransformRequest =
                readFileFrom(A58_STEP_PARENT_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse =
                readFileFrom(A58_STEP_PARENT_TRANSFORM_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
                .body(bulkScanTransformRequest)
                .when()
                .contentType("application/json")
                .post("/transform-exception-record");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanTransformResponse, response.getBody().asString(), true);
    }
}
