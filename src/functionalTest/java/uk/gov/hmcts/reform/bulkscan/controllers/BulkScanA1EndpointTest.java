package uk.gov.hmcts.reform.bulkscan.controllers;

import static uk.gov.hmcts.reform.bulkscan.util.Constant.AUTH_HEADER;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_LOCAL_HOST;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_URL;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.JSON_CONTENT_TYPE;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.TRANSFORM_EXCEPTION_URL;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_TRANSFORM_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_TRANSFORM_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_VALIDATION_ERROR_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_VALIDATION_ERROR_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_VALIDATION_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_VALIDATION_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_VALIDATION_WARNING_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.EdgeCaseConstants.A1_VALIDATION_WARNING_OUTPUT_PATH;
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
public class BulkScanA1EndpointTest {

    @Autowired S2sClient s2sClient;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String targetInstance =
            StringUtils.defaultIfBlank(
                    System.getenv(BULK_SCAN_TEST_URL), BULK_SCAN_TEST_LOCAL_HOST);

    private final RequestSpecification request =
            RestAssured.given().relaxedHTTPSValidation().baseUri(targetInstance);

    @Before
    public void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Validating A1")
    public void shouldValidateA1BulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(A1_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(A1_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post("forms/A1/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating A1 errors for mandatory fields")
    public void shouldValidateA1ErrorBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(A1_VALIDATION_ERROR_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(A1_VALIDATION_ERROR_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post("forms/A1/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating warnings for mandatory fields and unknown field for A1")
    public void shouldValidateA1WarningBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(A1_VALIDATION_WARNING_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(A1_VALIDATION_WARNING_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post("forms/A1/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Transform test case for form A1")
    public void shouldTransformA1BulkScanRequest() throws Exception {
        String bulkScanTransformRequest = readFileFrom(A1_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse = readFileFrom(A1_TRANSFORM_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanTransformRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(TRANSFORM_EXCEPTION_URL);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanTransformResponse, response.getBody().asString(), true);
    }
}
