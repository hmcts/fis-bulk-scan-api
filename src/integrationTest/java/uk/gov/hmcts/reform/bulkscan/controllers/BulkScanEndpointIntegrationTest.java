package uk.gov.hmcts.reform.bulkscan.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.CASE_TYPE_TRANSFORM_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL401_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL403_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION_VALUE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BulkScanEndpointIntegrationTest {

    @Autowired
    private transient MockMvc mockMvc;

    private static final String FL401_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-validation-input.json";
    private static final String FL403_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-fl403-validation-input.json";
    private static final String C100_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-transform-input.json";
    private static final String FL401_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-transform-input.json";
    private static final String FL403_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl403-transform-input.json";
    private static final String FL403_VALIDATION_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl403-validation-response.json";

    @DisplayName("should test validate request case type FL401")
    @Test
    void shouldTestValidationRequestCaseTypeFL401() throws Exception {
        mockMvc.perform(post(FL401_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(FL401_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test validate request case type FL403")
    @Test
    void shouldTestValidationRequestCaseTypeFL403() throws Exception {
        mockMvc.perform(post(FL403_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(FL403_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk())
                .andExpect(content().json(readFileFrom(FL403_VALIDATION_RESPONSE_PATH)));
    }

    @DisplayName("should test transform request case type C100")
    @Test
    void shouldTestTransformRequestCaseTypeC100() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(C100_TRANSFORM_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test transform request case type FL401")
    @Test
    void shouldTestTransformRequestCaseTypeFL401() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(FL401_TRANSFORM_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test transform request case type FL403")
    @Test
    void shouldTestTransformRequestCaseTypeFL403() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(FL403_TRANSFORM_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }
}
