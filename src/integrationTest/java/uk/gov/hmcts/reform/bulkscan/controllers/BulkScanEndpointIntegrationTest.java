package uk.gov.hmcts.reform.bulkscan.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.C100;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.FL401;
import static uk.gov.hmcts.reform.bulkscan.model.CaseType.FL403;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.CASE_TYPE_TRANSFORM_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.EdgeCase_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL401_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.FL403_CASE_TYPE_VALIDATE_ENDPOINT;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.SERVICE_AUTHORIZATION_VALUE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil.buildFL403ValidationRequest;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.expectedResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BulkScanEndpointIntegrationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private transient MockMvc mockMvc;

    private static List<OcrDataField> dataFieldList = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("appellant_firstName");
        ocrDataFirstNameField.setValue("firstName");

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("appellant_lastName");
        ocrDataLastNameField.setValue("LastName");

        OcrDataField ocrDataAddressField = new OcrDataField();
        ocrDataAddressField.setName("appellant_address");
        ocrDataAddressField.setValue("Address1 London");

        dataFieldList = Arrays.asList(ocrDataAddressField, ocrDataFirstNameField, ocrDataLastNameField);
    }

    @DisplayName("should test validate request case type FL401")
    @Test
    void shouldTestCaseTypeFL401() throws Exception {
        mockMvc.perform(post(FL401_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(
                                BulkScanValidationRequest.builder()
                                        .ocrdatafields(dataFieldList)
                                        .build())))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test validate request case type FL403")
    @Test
    void shouldTestCaseTypeFL403() throws Exception {
        mockMvc.perform(post(FL403_CASE_TYPE_VALIDATE_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(
                                BulkScanValidationRequest.builder()
                                        .ocrdatafields(buildFL403ValidationRequest())
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse("classpath:bulk-scan-fl403_validation_response.json")));
    }


    @DisplayName("should test validate request case type EdgeCase")
    @Test
    void shouldTestCaseTypeEdgeCase() throws Exception {
        mockMvc.perform(post(EdgeCase_CASE_TYPE_VALIDATE_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                            .content(OBJECT_MAPPER.writeValueAsString(
                                BulkScanValidationRequest.builder()
                                    .ocrdatafields(dataFieldList)
                                    .build())))
            .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test transform request case type C100")
    @Test
    void shouldTestTransformRequestCaseTypeC100() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(
                                BulkScanTransformationRequest.builder()
                                        .ocrdatafields(dataFieldList)
                                        .caseTypeId(C100.name())
                                        .build())))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test transform request case type FL401")
    @Test
    void shouldTestTransformRequestCaseTypeFL401() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(
                                BulkScanTransformationRequest.builder()
                                        .ocrdatafields(dataFieldList)
                                        .caseTypeId(FL401.name())
                                        .build())))
                .andExpect(status().isOk()).andReturn();
    }

    @DisplayName("should test transform request case type FL403")
    @Test
    void shouldTestTransformRequestCaseTypeFL403() throws Exception {
        mockMvc.perform(post(CASE_TYPE_TRANSFORM_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .header(SERVICE_AUTHORIZATION, SERVICE_AUTHORIZATION_VALUE)
                        .content(OBJECT_MAPPER.writeValueAsString(
                                BulkScanTransformationRequest.builder()
                                        .ocrdatafields(dataFieldList)
                                        .caseTypeId(FL403.name())

                                        .build())))
                .andExpect(status().isOk()).andReturn();
    }
}
