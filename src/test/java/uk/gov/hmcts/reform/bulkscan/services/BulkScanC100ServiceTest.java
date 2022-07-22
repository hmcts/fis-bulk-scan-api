package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_OF_SAME_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME_COLLECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NUMERIC_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util.POST_CODE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ServiceTest {
    private static final String EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING
            = "Group Dependency Field (exemption_to_attend_MIAM) has dependency validation warning. "
            + "Must contain at least 1 of the fields [NoMIAM_domesticViolence,NoMIAM_childProtectionConcerns,"
            + "NoMIAM_Urgency,NoMIAM_PreviousAttendence,NoMIAM_otherReasons].";
    private static final String NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING
            = "Group Dependency Field (NoMIAM_domesticViolence) has dependency validation warning. "
            + "Must contain at least 1 of the fields [NoMIAM_DVE_arrestedForSimilarOffence,"
            + "NoMIAM_DVE_relevantPoliceCaution,NoMIAM_DVE_relevantCriminalProceeding,NoMIAM_DVE_relevantConviction,"
            + "NoMIAM_DVE_courtOrder,NoMIAM_DVE_protectionNotice,NoMIAM_DVE_protectiveInjunction,"
            + "NoMIAM_DVE_NoCrossUndertakingGiven,NoMIAM_DVE_copyOfFactFinding,NoMIAM_DVE_expertEvidenceReport,"
            + "NoMIAM_DVE_healthProfessionalReport,NoMIAM_DVE_ReferralHealthProfessionalReport,"
            + "NoMIAM_DVE_memberOf_MultiAgencyRiskAssessmentConferrance_letter,NoMIAM_DVE_domesticViolenceAdvisor,"
            + "NoMIAM_DVE_independentSexualViolenceAdvisor_Letter,NoMIAM_DVE_officerEmployed_localAuthority_letter,"
            + "NoMIAM_DVE_domesticViolenceSupportCharity_letter,"
            + "NoMIAM_DVE_domesticViolenceSupportCharity_refuge_letter,"
            + "NoMIAM_DVE_publicAuthority_confirmationLetter,NoMIAM_DVE_secretaryOfState_letter,"
            + "NoMIAM_DVE_evidenceFinancialMatters].";


    @Autowired
    BulkScanC100Service bulkScanValidationService;

    @MockBean
    PostcodeLookupService postcodeLookupService;


    private final ObjectMapper mapper = new ObjectMapper();

    private static final String C100_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-validate-input.json";

    private static final String C100_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-transform-input.json";

    private static final String C100_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-c100-transform-output.json";

    @Test
    void testC100Success() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataC100Util.getData()).build();
        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testC100WhenPostCodeNotValid() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataC100Util.getData()).build();
        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(false);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(POST_CODE_MESSAGE, "applicant_postCode")));
    }

    @Test
    void testC100WhenNotOneFieldPresentOutOfXoRFields() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataC100Util.getErrorData()).build();
        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(false);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(XOR_CONDITIONAL_FIELDS_MESSAGE,
                "applicant_postCode,applicant_lastName")));
    }

    @Test
    void testC100MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataC100Util.getErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "applicant_lastName")));
    }

    @Test
    void testC100EmergencyProtectionOrderMandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder()
                .ocrdatafields(TestDataC100Util.getErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(
                String.format(MANDATORY_ERROR_MESSAGE, "emergency_protection_order")));
    }

    @Test
    void testC100DateErrorWhileDoingValidation() {
        List<OcrDataField> c100GetDateError = new ArrayList<>();
        c100GetDateError.addAll(TestDataC100Util.getDateErrorData());
        c100GetDateError.addAll(TestDataC100Util.getExemptionToAttendMiamSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDateError).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(DATE_FORMAT_MESSAGE, "applicant_dateOfBirth")));
    }

    @Test
    void testC100OtherCourtCaseDateErrorWhileDoingValidation() {
        List<OcrDataField> c100GetDateError = new ArrayList<>();
        c100GetDateError.addAll(TestDataC100Util.getDateErrorData());
        c100GetDateError.addAll(TestDataC100Util.getExemptionToAttendMiamSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDateError).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(DATE_FORMAT_MESSAGE, "other_court_case_date")));
    }

    @Test
    void testC100AuthorisedFamilyMediatorSignedDateErrorWhileDoingValidation() {
        List<OcrDataField> c100GetDateError = new ArrayList<>();
        c100GetDateError.addAll(TestDataC100Util.getDateErrorData());
        c100GetDateError.addAll(TestDataC100Util.getExemptionToAttendMiamSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest
                = BulkScanValidationRequest.builder().ocrdatafields(c100GetDateError).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(
                String.format(DATE_FORMAT_MESSAGE, "authorised_family_mediator_signed_date")));
    }

    @Test
    void testC100EmailErrorWhileDoingValidation() {
        List<OcrDataField> c100GetEmailError = new ArrayList<>();
        c100GetEmailError.addAll(TestDataC100Util.getEmailErrorData());
        c100GetEmailError.addAll(TestDataC100Util.getExemptionToAttendMiamSuccessData());

        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetEmailError).build();

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(EMAIL_FORMAT_MESSAGE, "applicant_email")));
    }

    @Test
    void testC100FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataC100Util.getFirstNameData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, "applicant_lastName")));
    }

    @Test
    void testC100CaseNoNumericErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataC100Util.getNumericErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(NUMERIC_MESSAGE, "case_no")));
    }

    @Test
    @DisplayName("Should generate warning on absence of dependency field(s) on exemption_To_Attend_MIAM field")
    void testC100WarningExemptionToAttendMiamWithoutAPage3Checkbox() {
        List<OcrDataField> c100GetExemptionWarningData = new ArrayList<>();
        c100GetExemptionWarningData.addAll(TestDataC100Util.getAllNamesSuccessData());
        c100GetExemptionWarningData.addAll(TestDataC100Util.getExemptionToAttendWarningData());
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetExemptionWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(String.format(EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING)));
    }

    @Test
    @DisplayName("Should generate warning on NoMiam_DomesticViolence checked but without dependency Part 3a field(s)")
    void testC100NoMiamDomesticViolenceWarningOnSection3ACheckbox() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        OcrDataField ocrDataPostCodeField = new OcrDataField();
        ocrDataPostCodeField.setName("applicant_postCode");
        ocrDataPostCodeField.setValue(POST_CODE);

        c100GetDomesticViolenceWarningData.add(ocrDataPostCodeField);
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getNoMiamDomesticWarningData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING));
    }

    @DisplayName("C100 validation with empty child live with info.")
    void testC100ValidationError() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(C100_VALIDATION_REQUEST_PATH), BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILD_LIVING_WITH_APPLICANT.equalsIgnoreCase(eachField.getName())
                                || CHILD_LIVING_WITH_RESPONDENT.equalsIgnoreCase(eachField.getName())
                                || CHILD_LIVING_WITH_OTHERS.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(""));
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "one field must be present out of child_living_with_Applicant,child_living_with_Respondent,"
                        + "child_living_with_others", res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty parent name.")
    void testC100ValidationErrorWithParentName() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(C100_VALIDATION_REQUEST_PATH), BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILDREN_PARENTS_NAME.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(""));
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "children_parentsName should not be null or empty", res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty parent collection and child paret name as No.")
    void testC100ValidationErrorWithParentCollName() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(C100_VALIDATION_REQUEST_PATH), BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILDREN_PARENTS_NAME_COLLECTION.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(""));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILDREN_OF_SAME_PARENT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("No"));
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "child_parentsName_collection should not be null or empty", res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty parent collection and child paret name as No.")
    void testC100ValidationErrorWithSocialAuth() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(C100_VALIDATION_REQUEST_PATH), BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILDREN_PARENTS_NAME_COLLECTION.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(""));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILDREN_OF_SAME_PARENT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("No"));
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "child_parentsName_collection should not be null or empty", res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty local authority.")
    void testC100ValidationErrorWithSocialAuthority() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(C100_VALIDATION_REQUEST_PATH), BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(""));
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "child1_localAuthority_or_socialWorker should not be null or empty",
                res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 transform success.")
    void testC100TransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
                readFileFrom(C100_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        JSONAssert.assertEquals(readFileFrom(C100_TRANSFORM_RESPONSE_PATH),
                mapper.writeValueAsString(res), true);
    }

    @Test
    @DisplayName("C100 transform Child live with respondent.")
    void testC100TransformWithRespondent() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
                readFileFrom(C100_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILD_LIVING_WITH_APPLICANT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("false"));
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILD_LIVING_WITH_RESPONDENT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("true"));
        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
                "Respondent",
                res.getCaseCreationDetails().getCaseData().get(CHILD_LIVE_WITH_KEY));
    }

    @Test
    @DisplayName("C100 transform Child live with others.")
    void testC100TransformWithOthers() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
                readFileFrom(C100_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILD_LIVING_WITH_APPLICANT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("false"));
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(eachField ->
                        CHILD_LIVING_WITH_OTHERS.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("true"));
        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
                "OtherPeople",
                res.getCaseCreationDetails().getCaseData().get(CHILD_LIVE_WITH_KEY));
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNotNull(bulkScanTransformationResponse);
    }

}
