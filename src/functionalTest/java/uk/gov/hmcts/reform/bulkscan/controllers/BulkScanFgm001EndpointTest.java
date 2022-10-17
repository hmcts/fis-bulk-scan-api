package uk.gov.hmcts.reform.bulkscan.controllers;

import static uk.gov.hmcts.reform.bulkscan.util.Constant.AUTH_HEADER;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_LOCAL_HOST;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_URL;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.CASE_TYPE_TRANSFORM_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_TRANSFORM_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_TRANSFORM_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_VALIDATION_ERROR_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_VALIDATION_ERROR_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_VALIDATION_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_VALIDATION_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_VALIDATION_WARNING_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.FGM001_VALIDATION_WARNING_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.JSON_CONTENT_TYPE;
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
public class BulkScanFgm001EndpointTest {

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
    public void shouldValidateFgm001BulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FGM001_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FGM001_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(FGM001_CASE_TYPE_VALIDATE_ENDPOINT);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating errors for mandatory fields")
    public void shouldValidateFgm001ErrorBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FGM001_VALIDATION_ERROR_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FGM001_VALIDATION_ERROR_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(FGM001_CASE_TYPE_VALIDATE_ENDPOINT);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating warnings for mandatory fields and unknown field for FGM001")
    public void shouldValidateFgm001WarningBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FGM001_VALIDATION_WARNING_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FGM001_VALIDATION_WARNING_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(FGM001_CASE_TYPE_VALIDATE_ENDPOINT);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    public void shouldTransformBulkScanRequest() throws Exception {
        String bulkScanTransformRequest = readFileFrom(FGM001_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse = readFileFrom(FGM001_TRANSFORM_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanTransformRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(CASE_TYPE_TRANSFORM_ENDPOINT);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanTransformResponse, response.getBody().asString(), true);
    }
}
