package uk.gov.hmcts.reform.bulkscan.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.CASE_TYPE_TRANSFORM_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL401A_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL401A_TRANSFORM_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL401A_TRANSFORM_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTH_TOKEN;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@TestPropertySource(locations = "classpath:application_e2e.yaml")
class BulkScanFL401AEndpointIntegrationTest {

    @MockBean protected AuthTokenValidator authTokenValidator;

    @Autowired private transient MockMvc mockMvc;

    private static final String FL401A_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401a-validate-input.json";

    @BeforeEach
    void beforeEach() {
        when(authTokenValidator.getServiceName(SERVICE_AUTH_TOKEN)).thenReturn("fis_cos_api");
    }

    @DisplayName("should test validate request case type FL401A")
    @Test
    void shouldTestValidationRequestCaseTypeFL401A() throws Exception {
        when(authTokenValidator.getServiceName(SERVICE_AUTH_TOKEN)).thenReturn("fis_cos_api");

        mockMvc.perform(
                        post(FL401A_CASE_TYPE_VALIDATE_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .header(SERVICE_AUTHORIZATION, SERVICE_AUTH_TOKEN)
                                .content(readFileFrom(FL401A_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("should test transform request case type FL401A")
    @Test
    void shouldTestTransformRequestCaseTypeFL401A() throws Exception {
        when(authTokenValidator.getServiceName(SERVICE_AUTH_TOKEN)).thenReturn("fis_cos_api");

        mockMvc.perform(
                        post(CASE_TYPE_TRANSFORM_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .header(SERVICE_AUTHORIZATION, SERVICE_AUTH_TOKEN)
                                .content(readFileFrom(FL401A_TRANSFORM_REQUEST_PATH)))
                .andExpect(status().isOk())
                .andExpect(content().json(readFileFrom(FL401A_TRANSFORM_RESPONSE_PATH)));
    }
}
