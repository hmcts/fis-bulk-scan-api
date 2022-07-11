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
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.A58_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.A60_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.C2_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.C51_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.C63_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.CASE_TYPE_TRANSFORM_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.EdgeCase_CASE_TYPE_VALIDATE_ENDPOINT;
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
    private static final String EdgeCase_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-fl403-validation-input.json";
    private static final String C2_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-c2-validation-input.json";
    private static final String C2_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-c2-transform-input.json";
    private static final String C100_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-c100-transform-input.json";
    private static final String EdgeCase_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-edgecase-transform-input.json";
    private static final String FL401_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-transform-input.json";
    private static final String FL403_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl403-transform-input.json";
    private static final String A58_STEP_PARENT_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-step-parent-transform-input.json";
    private static final String A58_STEP_PARENT_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-a58-step-parent-transform-output.json";

    private static final String A58_RELINQUISHED_ADOPTION_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-a58-relinquished-adoption-transform-input.json";
    private static final String A58_RELINQUISHED_ADOPTION_TRANSFORM_RESPONSE_PATH =
        "classpath:response/bulk-scan-a58-relinquished-adoption-transform-output.json";
    private static final String FL403_VALIDATION_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl403-validation-response.json";
    private static final String C51_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c51-validation-input.json";
    private static final String C63_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c63-validation-input.json";
    private static final String A60_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-a60-validation-input.json";
    private static final String A58_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-validation-input.json";
    private static final String A58_PART1_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-a58-part1-validation-input.json";

    private static final String A58_PART1_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-a58-part1-transform-input.json";

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

    @DisplayName("should test validate request case type C51")
    @Test
    void shouldTestValidationRequestCaseTypeC51() throws Exception {
        mockMvc.perform(post(C51_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(C51_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test validate request case type C63")
    @Test
    void shouldTestValidationRequestCaseTypeC63() throws Exception {
        mockMvc.perform(post(C63_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(C63_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test validate request case type a58")
    @Test
    void shouldTestValidationRequestCaseTypeA58() throws Exception {
        mockMvc.perform(post(A58_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(A58_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test transform request case type A58 relinquished adoption")
    @Test
    void shouldTestTransformRequestCaseTypeA58RelinquishedAdoption() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(readFileFrom(A58_RELINQUISHED_ADOPTION_TRANSFORM_REQUEST_PATH)))
            .andExpect(status().isOk())
            .andExpect(content().json(readFileFrom(A58_RELINQUISHED_ADOPTION_TRANSFORM_RESPONSE_PATH)));
    }

    @DisplayName("should test validate request case type A60")
    @Test
    void shouldTestValidationRequestCaseTypeA60() throws Exception {
        mockMvc.perform(post(A60_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(A60_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test validate request case type EdgeCase")
    @Test
    void shouldTestCaseTypeEdgeCase() throws Exception {
        mockMvc.perform(post(EdgeCase_CASE_TYPE_VALIDATE_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(readFileFrom(EdgeCase_VALIDATION_REQUEST_PATH)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @DisplayName("should test validate request case type C2")
    @Test
    void shouldTestValidationCaseTypeC2() throws Exception {
        mockMvc.perform(post(C2_CASE_TYPE_VALIDATE_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(readFileFrom(C2_VALIDATION_REQUEST_PATH)))
            .andExpect(status().isOk())
            .andReturn();
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

    @DisplayName("should test transform request case type A58 step parent adoption")
    @Test
    void shouldTestTransformRequestCaseTypeA58StepParentAdoption() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(readFileFrom(A58_STEP_PARENT_TRANSFORM_REQUEST_PATH)))
                .andExpect(status().isOk())
                .andExpect(content().json(readFileFrom(A58_STEP_PARENT_TRANSFORM_RESPONSE_PATH)));
    }

    @DisplayName("should test transform request case type C2")
    @Test
    void shouldTestTransformRequestCaseTypeC2() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(readFileFrom(C2_TRANSFORM_REQUEST_PATH)))
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

    @DisplayName("should test transform request case type EdgeCase")
    @Test
    void shouldTestTransformRequestCaseTypeEdgeCase() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(readFileFrom(EdgeCase_TRANSFORM_REQUEST_PATH)))
            .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test validate request case type A58 for part1")
    @Test
    void shouldTestValidationRequestCaseTypeA58ForPart1() throws Exception {
        mockMvc.perform(post(A58_CASE_TYPE_VALIDATE_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(readFileFrom(A58_PART1_VALIDATION_REQUEST_PATH)))
            .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test transform request of A58 applicant part")
    @Test
    void shouldTestTransformOfA58Applicant() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(readFileFrom(A58_PART1_TRANSFORM_REQUEST_PATH)))
            .andExpect(status().isOk()).andReturn();
    }
}
