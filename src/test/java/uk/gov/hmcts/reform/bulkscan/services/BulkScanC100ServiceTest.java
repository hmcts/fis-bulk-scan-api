package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ASSESSMENT_BY_ADULT_LEARNING_TEAM_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_OF_SAME_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME_COLLECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FACTORS_AFFECTING_LITIGATION_CAPACITY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FACTORS_AFFECTING_PERSON_IN_COURT_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.INTERNATIONALELEMENT_JURISDICTIONISSUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_ONE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_TWO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.EMPTY_STRING;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_CHILDPROTECTIONCONCERNS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_DOMESTICVIOLENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_OTHERREASONS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_PREVIOUSATTENDANCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_URGENCY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.TICK_BOX_TRUE;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util.POST_CODE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.enums.PermissionRequiredEnum;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ServiceTest {
    private static final String EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING =
            "Group Dependency Field (exemption_to_attend_MIAM) has dependency validation warning."
                    + " Must contain at least 1 of the fields"
                    + " [NoMIAM_domesticViolence,NoMIAM_childProtectionConcerns,"
                    + "NoMIAM_Urgency,NoMIAM_PreviousAttendance,NoMIAM_otherReasons].";
    private static final String NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_childProtectionConcerns) has dependency validation"
                    + " warning. Must contain at least 1 of the fields"
                    + " [NoMIAM_subjectOfEnquiries_byLocalAuthority,"
                    + "NoMIAM_subjectOfCPP_byLocalAuthority].";
    private static final String NOMIAM_PREVIOUSATTENDANCE_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_PreviousAttendance) has dependency validation warning. "
                    + "Must contain at least 1 of the fields [NoMIAM_PreviousAttendanceReason].";
    private static final String NOMIAM_OTHERREASONS_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_otherReasons) has dependency validation warning. "
                    + "Must contain at least 1 of the fields [NoMIAM_otherExceptions].";
    private static final String NOMIAM_URGENCY_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_Urgency) has dependency validation warning. Must"
                + " contain at least 1 of the fields"
                + " [NoMIAM_urgency_risk_to_life_liberty_or_safety,"
                + "NoMIAM_urgency_riskOfHarm,NoMIAM_urgency_risk_to_unlawfulRemoval,"
                + "NoMIAM_urgency_risk_to_miscarriageOfJustice,NoMIAM_urgency_unreasonablehardship,"
                + "NoMIAM_urgency_irretrievableProblem,NoMIAM_urgency_conflictWithOtherStateCourts].";
    private static final String NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING =
            "Group Dependency Field (NoMIAM_domesticViolence) has dependency validation warning."
                + " Must contain at least 1 of the fields [NoMIAM_DVE_arrestedForSimilarOffence,"
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

    private static final String INTERNATIONAL_FACTORS_AFFECTING_LITIGATION_WARNING =
            "Group Dependency Field (international_or_factorsAffectingLitigation) has dependency"
                + " validation warning. Must contain at least 1 of the fields "
                + "[internationalElement_request_toCentral_or_Consular_authority,"
                + "internationalElement_Resident_of_another_state,internationalElement_jurisdictionIssue"
                + " (value should be Yes); "
                + "internationalElement_jurisdictionIssue,internationalElement_jurisdictionIssue,internationalElement_jurisdictionIssue"
                + " (value should be empty)].";

    private static final String RESPONDENT_ONE_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS =
            "("
                    + RESPONDENT_ONE
                    + ") has not lived at the current address "
                    + "for more than 5 years. Previous address(es) field ("
                    + RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS
                    + ") should not be empty or null.";

    private static final String RESPONDENT_TWO_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS =
            "("
                    + RESPONDENT_TWO
                    + ") has not lived at the current address "
                    + "for more than 5 years. Previous address(es) field ("
                    + RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS
                    + ") should not be empty or null.";

    private static final String INTERNATIONAL_JURISDICTION_WARNING_MESSAGE =
            "Group Dependency Field (internationalElement_jurisdictionIssue) has dependency"
                    + " validation warning. Must contain at least 1 of the fields"
                    + " [withoutNotice_jurisdictionIssue_details].";

    private static final String INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_WARNING_MESSAGE =
            "Group Dependency Field (internationalElement_Resident_of_another_state) has dependency"
                    + " validation warning. Must contain at least 1 of the fields"
                    + " [internationalElement_Resident_of_another_state_details].";
    private static final String INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_WARNING_MESSAGE =
            "Group Dependency Field (internationalElement_request_toCentral_or_Consular_authority)"
                    + " has dependency validation warning. Must contain at least 1 of the fields"
                    + " [internationalElement_request_toCentral_or_Consular_authority_details].";

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String C100_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-validate-input.json";

    private static final String C100_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-transform-input.json";

    private static final String C100_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-c100-transform-output.json";

    @Autowired BulkScanC100Service bulkScanValidationService;

    @MockBean PostcodeLookupService postcodeLookupService;

    @Test
    @DisplayName("C100 validation with child information.")
    void testC100ValidationSuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("Test a sample successful C100 application validation with full details.")
    void testC100Success() {
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(TestDataC100Util.getData())
                        .build();
        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("C100 validation with empty child live with info.")
    void testC100ValidationError() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                CHILD_LIVING_WITH_APPLICANT.equalsIgnoreCase(eachField.getName())
                                        || CHILD_LIVING_WITH_RESPONDENT.equalsIgnoreCase(
                                                eachField.getName())
                                        || CHILD_LIVING_WITH_OTHERS.equalsIgnoreCase(
                                                eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "one field must be present out of"
                        + " child_living_with_Applicant,child_living_with_Respondent,"
                        + "child_living_with_others",
                res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty parent name.")
    void testC100ValidationErrorWithParentName() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField -> CHILDREN_PARENTS_NAME.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "children_parentsName should not be null or empty",
                res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty parent collection and child paret name as No.")
    void testC100ValidationErrorWithParentCollName() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                CHILDREN_PARENTS_NAME_COLLECTION.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField -> CHILDREN_OF_SAME_PARENT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("No"));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "child_parentsName_collection should not be null or empty",
                res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty parent collection and child parent name as No.")
    void testC100ValidationErrorWithSocialAuth() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                CHILDREN_PARENTS_NAME_COLLECTION.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField -> CHILDREN_OF_SAME_PARENT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("No"));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "child_parentsName_collection should not be null or empty",
                res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 validation with empty local authority.")
    void testC100ValidationErrorWithSocialAuthority() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertEquals(
                "child1_localAuthority_or_socialWorker should not be null or empty",
                res.getErrors().getItems().get(0));
    }

    @Test
    @DisplayName("C100 transform success.")
    void testC100TransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        JSONAssert.assertEquals(
                readFileFrom(C100_TRANSFORM_RESPONSE_PATH), mapper.writeValueAsString(res), true);
    }

    @Test
    @DisplayName("C100 transform Child live with respondent.")
    void testC100TransformWithRespondent() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                CHILD_LIVING_WITH_APPLICANT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("false"));
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                CHILD_LIVING_WITH_RESPONDENT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("true"));
        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
                "Respondent", res.getCaseCreationDetails().getCaseData().get(CHILD_LIVE_WITH_KEY));
    }

    @Test
    @DisplayName("C100 transform Child live with others.")
    void testC100TransformWithOthers() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                CHILD_LIVING_WITH_APPLICANT.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("false"));
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(eachField -> CHILD_LIVING_WITH_OTHERS.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("true"));
        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
                "OtherPeople", res.getCaseCreationDetails().getCaseData().get(CHILD_LIVE_WITH_KEY));
    }

    @Test
    @DisplayName(
            "Should generate warning on absence of dependency field(s) on exemption_To_Attend_MIAM"
                    + " field")
    void testC100WarningExemptionToAttendMiamWithoutAnyDependentFields() {
        List<OcrDataField> c100GetExemptionWarningData = new ArrayList<>();
        c100GetExemptionWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetExemptionWarningData.addAll(TestDataC100Util.getExemptionToAttendWarningData());
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetExemptionWarningData)
                        .build();

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate warning on NoMiam_DomesticViolence checked but without dependency Part"
                    + " 3a field(s)")
    void testC100NoMiamDomesticViolenceWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        OcrDataField ocrDataPostCodeField = new OcrDataField();
        ocrDataPostCodeField.setName("applicant_postCode");
        ocrDataPostCodeField.setValue(POST_CODE);

        c100GetDomesticViolenceWarningData.add(ocrDataPostCodeField);
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate warnings on [NoMiam_ChildProtectionConcerns] checked but without"
                    + " dependent Part 3b field(s)")
    void testC100NoMiamChildProtectionConcernsWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate warnings on [NoNiam_Urgency] checked but without dependent Part 3c"
                    + " field(s)")
    void testC100NoMiamUrgencyWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_URGENCY_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate warnings on [NoMIAM_PreviousAttendanceReason] checked but without"
                    + " dependent Part 3d field(s)")
    void testC100NoMiamPreviousAttendanceReasonWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_PREVIOUSATTENDANCE_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate warnings on [NoMIAM_otherReasons] checked but without"
                    + " dependent Part 3e field(s)")
    void testC100NoMiamOtherReasonsWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(
                TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate SUCCESS status with NoMIAM_domesticViolence field in bulkscan request")
    void testC100NoMiamDomesticViolenceSuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getData());

        OcrDataField subjectOfEnquiries = new OcrDataField();
        subjectOfEnquiries.setName("NoMIAM_DVE_protectionNotice");
        subjectOfEnquiries.setValue("Yes");

        c100GetDomesticViolenceWarningData.add(subjectOfEnquiries);

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                NOMIAM_DOMESTICVIOLENCE_FIELD.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(TICK_BOX_TRUE));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(
                TICK_BOX_TRUE,
                bulkScanValidationRequest.getOcrdatafields().stream()
                        .filter(
                                eachField ->
                                        NOMIAM_DOMESTICVIOLENCE_FIELD.equalsIgnoreCase(
                                                eachField.getName()))
                        .findAny()
                        .orElse(null)
                        .getValue());

        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate SUCCESS status with NoMIAM_childProtectionConcerns field in bulkscan"
                    + " request")
    void testC100NoMiamChildProtectionConcernsSuccessData() {
        List<OcrDataField> c100GetProtectionConcernsWarningData = new ArrayList<>();
        c100GetProtectionConcernsWarningData.addAll(TestDataC100Util.getData());

        OcrDataField subjectOfEnquiries = new OcrDataField();
        subjectOfEnquiries.setName("NoMIAM_subjectOfEnquiries_byLocalAuthority");
        subjectOfEnquiries.setValue("Yes");

        c100GetProtectionConcernsWarningData.add(subjectOfEnquiries);

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetProtectionConcernsWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                NOMIAM_CHILDPROTECTIONCONCERNS_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(TICK_BOX_TRUE));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(
                TICK_BOX_TRUE,
                bulkScanValidationRequest.getOcrdatafields().stream()
                        .filter(
                                eachField ->
                                        NOMIAM_CHILDPROTECTIONCONCERNS_FIELD.equalsIgnoreCase(
                                                eachField.getName()))
                        .findAny()
                        .orElse(null)
                        .getValue());

        assertEquals(Status.SUCCESS, res.status);
        assertFalse(
                res.getWarnings()
                        .items
                        .contains(NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate SUCCESS status with NoMIAM_Urgency field in bulkscan request")
    void testC100NoMiamUrgencySuccessData() {
        List<OcrDataField> c100GetMiamUrgencyWarningData = new ArrayList<>();
        c100GetMiamUrgencyWarningData.addAll(TestDataC100Util.getData());
        OcrDataField noMiamUrgencyReason = new OcrDataField();
        noMiamUrgencyReason.setName("NoMIAM_urgency_riskOfHarm");
        noMiamUrgencyReason.setValue("Yes");

        c100GetMiamUrgencyWarningData.add(noMiamUrgencyReason);

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetMiamUrgencyWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(eachField -> NOMIAM_URGENCY_FIELD.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(TICK_BOX_TRUE));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(
                TICK_BOX_TRUE,
                bulkScanValidationRequest.getOcrdatafields().stream()
                        .filter(
                                eachField ->
                                        NOMIAM_URGENCY_FIELD.equalsIgnoreCase(eachField.getName()))
                        .findAny()
                        .orElse(null)
                        .getValue());
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_URGENCY_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("C100 transform with permission requried option as 'No, permission Not required'.")
    void testC100PermissionRequiredTransform() throws IOException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(eachField -> PERMISSION_REQUIRED.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("No, permission Not required"));
        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
                PermissionRequiredEnum.noNotRequired.getDisplayedValue(),
                res.getCaseCreationDetails().getCaseData().get(APPLICATION_PERMISSION_REQUIRED));
    }

    @Test
    @DisplayName("C100 transform with permission requried option as 'No, permission Now sought'.")
    void testC100PermissionRequiredAsSoughtTransform() throws IOException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(eachField -> PERMISSION_REQUIRED.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("No, permission Now sought"));
        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
                PermissionRequiredEnum.noNowSought.getDisplayedValue(),
                res.getCaseCreationDetails().getCaseData().get(APPLICATION_PERMISSION_REQUIRED));
    }

    @Test
    @DisplayName(
            "Should generate SUCCESS status with NoMIAM_PreviousAttendance field in bulkscan"
                    + " request")
    void testC100NoMiamPreviousAttendanceSuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getData());
        OcrDataField previousAttendanceReason = new OcrDataField();
        previousAttendanceReason.setName("NoMIAM_PreviousAttendanceReason");
        previousAttendanceReason.setValue("Yes");

        c100GetDomesticViolenceWarningData.add(previousAttendanceReason);

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                NOMIAM_PREVIOUSATTENDANCE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(TICK_BOX_TRUE));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(
                TICK_BOX_TRUE,
                bulkScanValidationRequest.getOcrdatafields().stream()
                        .filter(
                                eachField ->
                                        "NoMIAM_PreviousAttendance"
                                                .equalsIgnoreCase(eachField.getName()))
                        .findAny()
                        .orElse(null)
                        .getValue());
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_PREVIOUSATTENDANCE_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate SUCCESS status with NoMIAM_otherReasons field in bulkscan request")
    void testC100NoMiamOtherReasonsSuccessData() {
        List<OcrDataField> c100GetOtherResonsWarningData = new ArrayList<>();
        c100GetOtherResonsWarningData.addAll(TestDataC100Util.getData());

        OcrDataField subjectOfEnquiries = new OcrDataField();
        subjectOfEnquiries.setName("NoMIAM_otherExceptions");
        subjectOfEnquiries.setValue("Yes");

        c100GetOtherResonsWarningData.add(subjectOfEnquiries);

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetOtherResonsWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                NOMIAM_OTHERREASONS_FIELD.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(TICK_BOX_TRUE));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(
                TICK_BOX_TRUE,
                bulkScanValidationRequest.getOcrdatafields().stream()
                        .filter(
                                eachField ->
                                        NOMIAM_OTHERREASONS_FIELD.equalsIgnoreCase(
                                                eachField.getName()))
                        .findAny()
                        .orElse(null)
                        .getValue());

        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate Warning for respondent_1 not lived for 5 years in current address "
                    + "without previous address details")
    void testC100RespondentOneNotLivedInAddressForFiveYearsWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(NO));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(RESPONDENT_ONE_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS));
    }

    @Test
    @DisplayName(
            "Should generate Warning for respondent_2 not lived for 5 years in current address "
                    + "without previous address details")
    void testC100RespondentTwoNotLivedInAddressForFiveYearsWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(NO));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(RESPONDENT_TWO_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS));
    }

    @Test
    @DisplayName(
            "Should generate success for international_or_factorsAffectingLitigation without any"
                    + " information on sections 8 and 9 of application form.")
    void testC100InternationalOrFactorsAffectingLitigationWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(YES));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_JURISDICTIONISSUE.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                ASSESSMENT_BY_ADULT_LEARNING_TEAM_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                FACTORS_AFFECTING_PERSON_IN_COURT_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                FACTORS_AFFECTING_LITIGATION_CAPACITY_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(INTERNATIONAL_FACTORS_AFFECTING_LITIGATION_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate success for international_or_factorsAffectingLitigation without Yes"
                    + " but No Checkbox information on sections 8 and 9 of application form.")
    void testC100InternationalOrFactorsAffectingLitigationWithoutYesCheckboxAndAllEmptyWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetDomesticViolenceWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(YES));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_JURISDICTIONISSUE.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(NO));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(NO));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(NO));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                ASSESSMENT_BY_ADULT_LEARNING_TEAM_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                FACTORS_AFFECTING_PERSON_IN_COURT_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                FACTORS_AFFECTING_LITIGATION_CAPACITY_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(INTERNATIONAL_FACTORS_AFFECTING_LITIGATION_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate warning for checkbox ticked with 'Yes' when there is any reason to"
                + " believe that any child, parent or potentially significant adult in he child’s"
                + " life may be habitually resident in another state sections 8 of application form"
                + " but no details were given.")
    void testC100InternationalElementResidentOfAnotherStateWithoutDetailsWarning() {
        List<OcrDataField> c100GetResidentOfAnotherStateWarningData = new ArrayList<>();
        c100GetResidentOfAnotherStateWarningData.addAll(TestDataC100Util.getData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetResidentOfAnotherStateWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(YES));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("Yes"));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_WARNING_MESSAGE));
    }

    @Test
    @DisplayName(
            "Should generate warning for checkbox ticked with 'Yes' when there is any reason to"
                    + " believe that there may be an issue as to jurisdiction in this case but no"
                    + " details were given")
    void testC100InternationalElementJurisdictionIssueWithoutDetailsWarning() {
        List<OcrDataField> c100GetJurisdictionIssueWarningData = new ArrayList<>();
        c100GetJurisdictionIssueWarningData.addAll(TestDataC100Util.getData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetJurisdictionIssueWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(YES));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_JURISDICTIONISSUE.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("Yes"));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(INTERNATIONAL_JURISDICTION_WARNING_MESSAGE));
    }

    @Test
    @DisplayName(
            "Should generate warning for checkbox ticked with 'Yes' when a requesthas been made or"
                    + "should a request be made to a Central Authority or other competent "
                    + "authority in a foreign state or a consular authority in England and Wales"
                    + "on sections 8 of application form but no details were given.")
    void testC100InternationalElementRequestCentralConsularWithoutDetailsWarning() {
        List<OcrDataField> c100GetRequestCentralConsularWarningData = new ArrayList<>();
        c100GetRequestCentralConsularWarningData.addAll(TestDataC100Util.getData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetRequestCentralConsularWarningData)
                        .build();

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(YES));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue("Yes"));

        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS
                                        .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(
                res.getWarnings()
                        .items
                        .contains(
                                INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_WARNING_MESSAGE));
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        assertNotNull(bulkScanTransformationResponse);
    }
}
