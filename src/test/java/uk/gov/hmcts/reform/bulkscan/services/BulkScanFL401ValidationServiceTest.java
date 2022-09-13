package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RESPONDENT_ENGAGED_PROPOSED_CIVIL_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RESPONDENT_MARRIED_CIVIL_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RESPONDENT_PREVIOUS_MARRIED_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RESPONDENT_RELATIONSHIP_END_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RESPONDENT_RELATIONSHIP_START_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFL401ValidationServiceTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String FL401_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-validation-input.json";

    @Autowired BulkScanFL401ValidationService bulkScanFL401ValidationService;

    @Autowired BulkScanFL401Service bulkScanFL401Service;

    @MockBean PostcodeLookupService postcodeLookupService;

    BulkScanValidationResponse bulkScanValidationResponse;

    @BeforeEach
    void setUp() {
        bulkScanValidationResponse =
                BulkScanValidationResponse.builder()
                        .status(Status.SUCCESS)
                        .warnings(Warnings.builder().items(new ArrayList<>()).build())
                        .errors(Errors.builder().items(new ArrayList<>()).build())
                        .build();
    }

    @Test
    @DisplayName("FL401 validation with all relevant information.")
    void testFL401ValidationSuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        Map<String, String> inputFieldsMap =
                getOcrDataFieldsMap(bulkScanValidationRequest.getOcrdatafields());

        bulkScanValidationResponse =
                bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                        inputFieldsMap, bulkScanValidationResponse);
        assertEquals(Status.SUCCESS, bulkScanValidationResponse.status);
    }

    @Test
    @DisplayName(
            "FL401 More than one relationship between Applicant and Respondent should give an"
                    + " Error.")
    void testFL401NoApplicantRespondentRelationshipError() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_MARRIED_CIVIL_RELATIONSHIP_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(NO));

        Map<String, String> inputFieldsMap =
                getOcrDataFieldsMap(bulkScanValidationRequest.getOcrdatafields());

        bulkScanValidationResponse =
                bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                        inputFieldsMap, bulkScanValidationResponse);
        assertEquals(Status.ERRORS, bulkScanValidationResponse.status);
        assertTrue(
                bulkScanValidationResponse
                        .getErrors()
                        .getItems()
                        .contains(
                                "Section 4.1 - Applicant must have a relationship with the"
                                        + " respondent"));
    }

    @Test
    @DisplayName(
            "FL401 More than one relationship between Applicant and Respondent should give"
                    + " warning.")
    void testFL401SelectMoreThanOneApplicantRespondentRelationshipWarning() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_ENGAGED_PROPOSED_CIVIL_RELATIONSHIP_FIELD
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(YES));

        Map<String, String> inputFieldsMap =
                getOcrDataFieldsMap(bulkScanValidationRequest.getOcrdatafields());

        bulkScanValidationResponse =
                bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                        inputFieldsMap, bulkScanValidationResponse);
        assertEquals(Status.WARNINGS, bulkScanValidationResponse.status);
        assertTrue(
                bulkScanValidationResponse
                        .getWarnings()
                        .getItems()
                        .contains(
                                "Section 4.1 - Applicant has more than one relationship with the"
                                        + " respondent"));
    }

    @Test
    @DisplayName(
            "FL401 More than one relationship between Applicant and Respondent should give"
                    + " warning.")
    void
            testFL401SelectingNoneOfTheAboveSection41AndNoSection42ApplicantRespondentRelationshipError()
                    throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(YES));

        Map<String, String> inputFieldsMap =
                getOcrDataFieldsMap(bulkScanValidationRequest.getOcrdatafields());

        bulkScanValidationResponse =
                bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                        inputFieldsMap, bulkScanValidationResponse);
        assertEquals(Status.ERRORS, bulkScanValidationResponse.status);
        assertTrue(
                bulkScanValidationResponse
                        .getErrors()
                        .getItems()
                        .contains(
                                "Section 4.4 - Applicant must have a relationship with the"
                                        + " respondent"));
    }

    @Test
    @DisplayName(
            "FL401 Non-compliant date formats should give StartDate Error and Warnings for End"
                    + " Date.")
    void testFL401NonCompliantDateFormatApplicantRespondentRelationshipStartDateError()
            throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(YES));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_RELATIONSHIP_START_DATE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("2022-de-02"));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_RELATIONSHIP_END_DATE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("2022-December-02"));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_PREVIOUS_MARRIED_DATE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("2022-Dec-02"));

        Map<String, String> inputFieldsMap =
                getOcrDataFieldsMap(bulkScanValidationRequest.getOcrdatafields());

        bulkScanValidationResponse =
                bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                        inputFieldsMap, bulkScanValidationResponse);
        assertEquals(Status.ERRORS, bulkScanValidationResponse.status);
        assertTrue(
                bulkScanValidationResponse
                        .getErrors()
                        .getItems()
                        .contains(
                                "Please enter valid date for Applicant respondent relationship"
                                        + " Start date"));
        assertTrue(
                bulkScanValidationResponse
                        .getWarnings()
                        .getItems()
                        .contains(
                                "Please enter valid date for Applicant respondent relationship End"
                                        + " date"));
    }

    @Test
    @DisplayName("FL401 When 'Non of the above' is chosen then valid dates should generate SUCCESS")
    void testFL401CompliantDateFormatsForApplicantRespondentRelationshipsGiveSuccess()
            throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(FL401_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(NO));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_RELATIONSHIP_START_DATE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("2002-dec-02"));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_RELATIONSHIP_END_DATE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("2022-Dec-02"));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT_PREVIOUS_MARRIED_DATE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("2003-12-02"));

        Map<String, String> inputFieldsMap =
                getOcrDataFieldsMap(bulkScanValidationRequest.getOcrdatafields());

        bulkScanValidationResponse =
                bulkScanFL401ValidationService.validateApplicantRespondentRelationhip(
                        inputFieldsMap, bulkScanValidationResponse);
        assertEquals(Status.SUCCESS, bulkScanValidationResponse.status);
    }

    private Map<String, String> getOcrDataFieldsMap(List<OcrDataField> ocrDataFields) {
        return null != ocrDataFields
                ? ocrDataFields.stream()
                        .collect(
                                Collectors.toMap(OcrDataField::getName, this::getOcrDataFieldValue))
                : null;
    }

    private String getOcrDataFieldValue(OcrDataField ocrDataField) {
        return ocrDataField.getValue() == null
                ? org.apache.commons.lang3.StringUtils.EMPTY
                : ocrDataField.getValue();
    }
}
