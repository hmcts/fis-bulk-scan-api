package uk.gov.hmcts.reform.bulkscan.controllers;

import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_LOCAL_HOST;
import static uk.gov.hmcts.reform.bulkscan.util.Constant.BULK_SCAN_TEST_URL;
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
public class BulkScanEndpointFL401AFunctionalTest {

    @Autowired S2sClient s2sClient;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String AUTH_HEADER = "serviceauthorization";
    private static final String FL401A_VALIDATION_TEST_PATH = "forms/FL401A/validate-ocr";
    private static final String FL401A_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-fl401a-validate-input.json";
    private static final String FL401A_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fl401a-validate-output.json";

    private static final String FL401A_WARNING_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-fl401a-validate-warning-input.json";

    private static final String FL401A_WARNING_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fl401a-validate-warning-output.json";

    private static final String FL401A_ERROR_VALIDATION_INPUT_PATH =
            "classpath:requests/bulk-scan-fl401a-validate-error-input.json";

    private static final String FL401A_ERROR_VALIDATION_OUTPUT_PATH =
            "classpath:responses/bulk-scan-fl401a-validate-error-output.json";

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
    public void shouldValidateFL401ABulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FL401A_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FL401A_VALIDATION_OUTPUT_PATH);
        System.out.println();

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(FL401A_VALIDATION_TEST_PATH);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating errors for mandatory fields and unknown field for FL401A")
    public void shouldValidateFL401AErrorBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FL401A_ERROR_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FL401A_ERROR_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(FL401A_VALIDATION_TEST_PATH);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }

    @Test
    @DisplayName("Validating warnings for mandatory fields and unknown field for FL401A")
    public void shouldValidateFL401AWarningBulkScanRequest() throws Exception {
        String bulkScanValidationRequest = readFileFrom(FL401A_WARNING_VALIDATION_INPUT_PATH);

        String bulkScanValidationResponse = readFileFrom(FL401A_WARNING_VALIDATION_OUTPUT_PATH);

        Response response =
                request.header(AUTH_HEADER, s2sClient.serviceAuthTokenGenerator())
                        .body(bulkScanValidationRequest)
                        .when()
                        .contentType(JSON_CONTENT_TYPE)
                        .post(FL401A_VALIDATION_TEST_PATH);

        response.then().assertThat().statusCode(HttpStatus.OK.value());

        JSONAssert.assertEquals(bulkScanValidationResponse, response.getBody().asString(), true);
    }
}
