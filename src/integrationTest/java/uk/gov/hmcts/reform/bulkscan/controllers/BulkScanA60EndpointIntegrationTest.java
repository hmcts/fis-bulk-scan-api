package uk.gov.hmcts.reform.bulkscan.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.A60_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.A60_VALIDATION_REQUEST_PATH;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION_VALUE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BulkScanA60EndpointIntegrationTest {
    @Autowired private transient MockMvc mockMvc;

    @DisplayName("should test validate request case type A60")
    @Test
    void shouldTestValidationRequestCaseTypeA60() throws Exception {
        mockMvc.perform(
                        post(A60_CASE_TYPE_VALIDATE_ENDPOINT)
                                .contentType(APPLICATION_JSON)
                                .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                                .content(readFileFrom(A60_VALIDATION_REQUEST_PATH)))
                .andExpect(status().isOk())
                .andReturn();
    }
}
