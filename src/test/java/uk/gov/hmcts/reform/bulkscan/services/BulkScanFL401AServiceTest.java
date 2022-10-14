package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.FL401A_APPLICANT_ADDRESS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.FL401A_APPLICANT_DATE_OF_BIRTH_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.FL401A_APPLICANT_FULL_NAME_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.FL401A_APPLICANT_POSTCODE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.FL401A_TEST_POSTCODE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFL401AServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String FL401A_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401a-validate-input.json";

    private static final String FL401A_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401a-transform-input.json";

    private static final String FL401A_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-fl401a-transform-output.json";

    private static final String FL401A_VALIDATION_ERROR_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401a-validate-error-input.json";

    private static final String FL401A_VALIDATION_WARNING_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401a-validate-warning-input.json";

    @Autowired BulkScanFL401AService bulkScanFL401AValidationService;

    @MockBean PostcodeLookupService postcodeLookupService;

    @Test
    void testFL401ASuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401A_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        when(postcodeLookupService.isValidPostCode(FL401A_TEST_POSTCODE, null)).thenReturn(true);

        BulkScanValidationResponse res =
                bulkScanFL401AValidationService.validate(bulkScanValidationRequest);

        Assertions.assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testFL401AMandatoryFieldMissingErrorWhileDoingValidation() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401A_VALIDATION_ERROR_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        when(postcodeLookupService.isValidPostCode(FL401A_TEST_POSTCODE, null)).thenReturn(true);

        BulkScanValidationResponse res =
                bulkScanFL401AValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(
                res.getErrors()
                        .items
                        .contains(
                                String.format(
                                        MANDATORY_ERROR_MESSAGE,
                                        FL401A_APPLICANT_FULL_NAME_FIELD)));
        assertTrue(
                res.getErrors()
                        .items
                        .contains(
                                String.format(
                                        MANDATORY_ERROR_MESSAGE, FL401A_APPLICANT_ADDRESS_FIELD)));
        assertTrue(
                res.getErrors()
                        .items
                        .contains(
                                String.format(
                                        MANDATORY_ERROR_MESSAGE, FL401A_APPLICANT_POSTCODE_FIELD)));
    }

    @Test
    void testFL401AFutureDateOfBirthValidationWarning() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401A_VALIDATION_WARNING_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        when(postcodeLookupService.isValidPostCode(FL401A_TEST_POSTCODE, null)).thenReturn(true);

        BulkScanValidationResponse res =
                bulkScanFL401AValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(
                                String.format(
                                        DATE_FORMAT_MESSAGE,
                                        FL401A_APPLICANT_DATE_OF_BIRTH_FIELD)));
    }

    @Test
    @DisplayName("FL401A transform success.")
    void testFL401ATransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(FL401A_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res =
                bulkScanFL401AValidationService.transform(bulkScanTransformationRequest);
        JSONAssert.assertEquals(
                readFileFrom(FL401A_TRANSFORM_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }
}
