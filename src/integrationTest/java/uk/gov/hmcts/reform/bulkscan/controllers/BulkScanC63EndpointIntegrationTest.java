package uk.gov.hmcts.reform.bulkscan.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.CASE_TYPE_TRANSFORM_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL401_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTH_TOKEN;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_TRANSFORM_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_TRANSFORM_RESPONSE_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.EdgeCaseConstants.C63_VALIDATION_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(locations = "classpath:application_e2e.yaml")
class BulkScanC63EndpointIntegrationTest {

    @MockBean protected AuthTokenValidator authTokenValidator;

    @Autowired private transient MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        when(authTokenValidator.getServiceName(SERVICE_AUTH_TOKEN)).thenReturn("fis_cos_api");
    }

    @DisplayName("should test validate request case type C63")
    @Test
    void shouldTestValidationRequestCaseTypeC63() throws Exception {
        when(authTokenValidator.getServiceName(SERVICE_AUTH_TOKEN)).thenReturn("fis_bulk_scan_api");

        mockMvc.perform(
                        post(FL401_CASE_TYPE_VALIDATE_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .header(SERVICE_AUTHORIZATION, SERVICE_AUTH_TOKEN)
                                .content(readFileFrom(C63_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("should test transform request case type C63")
    @Test
    void shouldTestTransformRequestCaseTypeC63() throws Exception {
        when(authTokenValidator.getServiceName(SERVICE_AUTH_TOKEN)).thenReturn("fis_cos_api");

        mockMvc.perform(
                        post(CASE_TYPE_TRANSFORM_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .header(SERVICE_AUTHORIZATION, SERVICE_AUTH_TOKEN)
                                .content(readFileFrom(C63_TRANSFORM_REQUEST_PATH)))
                .andExpect(status().isOk())
                .andExpect(content().json(readFileFrom(C63_TRANSFORM_RESPONSE_PATH)));
    }
}
