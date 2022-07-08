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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A58_STEP_PARENT_TRANSFORM_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A58_STEP_PARENT_TRANSFORM_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A58_STEP_PARENT_VALIDATION_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A58_STEP_PARENT_VALIDATION_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A60_TRANSFORM_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A60_TRANSFORM_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A60_VALIDATION_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.A60_VALIDATION_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.IntegrationTestConstant.AUTH_HEADER;
import static uk.gov.hmcts.reform.bulkscan.util.TestResourceUtil.readFileFrom;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("classpath:application.yaml")
public class BulkScanEndpointTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${case.orchestration.service.base.uri}")
    private String baseUri;

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
    public void shouldValidateA60BulkScanRequest() throws Exception {
        String bulkScanValidationRequest =
            readFileFrom(A60_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse =
            readFileFrom(A60_VALIDATION_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
            .body(bulkScanValidationRequest)
            .when()
            .contentType("application/json")
            .post("forms/A60/validate-ocr");

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

    @Test
    public void shouldTransformA60BulkScanRequest() throws Exception {
        String bulkScanTransformRequest =
            readFileFrom(A60_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse =
            readFileFrom(A60_TRANSFORM_OUTPUT_PATH);

        Response response = request.header(AUTH_HEADER, AUTH_HEADER)
            .body(bulkScanTransformRequest)
            .when()
            .contentType("application/json")
            .post("/transform-exception-record");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanTransformResponse, response.getBody().asString(), true);
    }
}
