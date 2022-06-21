package uk.gov.hmcts.reform.bulkscan.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
                readFileFrom("classpath:requests/bulk-scan-a58-step-parent-validation-input.json");

        request.header("serviceauthorization", "serviceauthorization")
                .body(bulkScanValidationRequest)
                .when()
                .contentType("application/json")
                .post("forms/A58/validate-ocr")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void shouldTransformA58StepParentBulkScanRequest() throws Exception {
        String bulkScanTransformRequest =
                readFileFrom("classpath:requests/bulk-scan-a58-step-parent-transform-input.json");

        request.header("serviceauthorization", "serviceauthorization")
                .body(bulkScanTransformRequest)
                .when()
                .contentType("application/json")
                .post("/transform-exception-record")
                .then().assertThat().statusCode(200);
    }
}
