package uk.gov.hmcts.reform.bulkscan.controllers;

import static uk.gov.hmcts.reform.bulkscan.util.Constant.A60_TRANSFORM_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.A60_TRANSFORM_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.A60_VALIDATION_INPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.A60_VALIDATION_OUTPUT_PATH;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.AUTH_HEADER;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_LOCAL_HOST;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_URL;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.JSON_CONTENT_TYPE;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.TRANSFORM_EXCEPTION_URL;
import static uk.gov.hmcts.reform.bulkscan.util.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
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
public class BulkScanA60EndpointTest {

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
    public void shouldValidateA60BulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(A60_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(A60_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post("forms/A60/validate-ocr");

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    public void shouldTransformA60BulkScanRequest() throws Exception {
        String bulkScanTransformRequest = readFileFrom(A60_TRANSFORM_INPUT_PATH);

        String bulkScanTransformResponse = readFileFrom(A60_TRANSFORM_OUTPUT_PATH);

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
