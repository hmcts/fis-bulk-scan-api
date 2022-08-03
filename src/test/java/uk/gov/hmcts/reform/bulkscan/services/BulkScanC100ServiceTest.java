package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_CHILDPROTECTIONCONCERNS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_DOMESTICVIOLENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_OTHERREASONS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_PREVIOUSATTENDANCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.NOMIAM_URGENCY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.TICK_BOX_TRUE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util.POST_CODE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ServiceTest {
    private static final String EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING
            = "Group Dependency Field (exemption_to_attend_MIAM) has dependency validation warning. "
            + "Must contain at least 1 of the fields [NoMIAM_domesticViolence,NoMIAM_childProtectionConcerns,"
            + "NoMIAM_Urgency,NoMIAM_PreviousAttendance,NoMIAM_otherReasons].";
    private static final String NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING
            = "Group Dependency Field (NoMIAM_childProtectionConcerns) has dependency validation warning. "
            + "Must contain at least 1 of the fields [NoMIAM_subjectOfEnquiries_byLocalAuthority,"
            + "NoMIAM_subjectOfCPP_byLocalAuthority].";
    private static final String NOMIAM_PREVIOUSATTENDANCE_DEPENDENCY_WARNING
            = "Group Dependency Field (NoMIAM_PreviousAttendance) has dependency validation warning. "
            + "Must contain at least 1 of the fields [NoMIAM_PreviousAttendanceReason].";
    private static final String NOMIAM_OTHERREASONS_DEPENDENCY_WARNING
            = "Group Dependency Field (NoMIAM_otherReasons) has dependency validation warning. "
            + "Must contain at least 1 of the fields [NoMIAM_otherExceptions].";
    private static final String NOMIAM_URGENCY_DEPENDENCY_WARNING
            = "Group Dependency Field (NoMIAM_Urgency) has dependency validation warning. "
            + "Must contain at least 1 of the fields [NoMIAM_urgency_risk_to_life_liberty_or_safety,"
            + "NoMIAM_urgency_riskOfHarm,NoMIAM_urgency_risk_to_unlawfulRemoval,"
            + "NoMIAM_urgency_risk_to_miscarriageOfJustice,NoMIAM_urgency_unreasonablehardship,"
            + "NoMIAM_urgency_irretrievableProblem,NoMIAM_urgency_conflictWithOtherStateCourts].";
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

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String C100_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-validate-input.json";

    private static final String C100_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-transform-input.json";

    private static final String C100_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-c100-transform-output.json";


    @Autowired
    BulkScanC100Service bulkScanValidationService;

    @MockBean
    PostcodeLookupService postcodeLookupService;

    @Test
    @DisplayName("C100 validation with child information.")
    void testC100ValidationSuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(C100_VALIDATION_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("Test a sample successful C100 application validation with full details.")
    void testC100Success() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                TestDataC100Util.getData()).build();
        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
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
    @DisplayName("C100 validation with empty parent collection and child parent name as No.")
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
        assertEquals("child_parentsName_collection should not be null or empty", res.getErrors().getItems().get(0));
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
        assertEquals("child1_localAuthority_or_socialWorker should not be null or empty",
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
    @DisplayName("Should generate warning on absence of dependency field(s) on exemption_To_Attend_MIAM field")
    void testC100WarningExemptionToAttendMiamWithoutAnyDependentFields() {
        List<OcrDataField> c100GetExemptionWarningData = new ArrayList<>();
        c100GetExemptionWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetExemptionWarningData.addAll(TestDataC100Util.getExemptionToAttendWarningData());
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetExemptionWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate warning on NoMiam_DomesticViolence checked but without dependency Part 3a field(s)")
    void testC100NoMiamDomesticViolenceWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        OcrDataField ocrDataPostCodeField = new OcrDataField();
        ocrDataPostCodeField.setName("applicant_postCode");
        ocrDataPostCodeField.setValue(POST_CODE);

        c100GetDomesticViolenceWarningData.add(ocrDataPostCodeField);
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate warnings on [NoMiam_ChildProtectionConcerns] checked but without"
            + " dependent Part 3b field(s)")
    void testC100NoMiamChildProtectionConcernsWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate warnings on [NoNiam_Urgency] checked but without dependent Part 3c field(s)")
    void testC100NoMiamUrgencyWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_URGENCY_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate warnings on [NoMIAM_PreviousAttendanceReason] checked but without"
            + " dependent Part 3d field(s)")
    void testC100NoMiamPreviousAttendanceReasonWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_PREVIOUSATTENDANCE_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate warnings on [NoMIAM_otherReasons] checked but without"
            + " dependent Part 3e field(s)")
    void testC100NoMiamOtherReasonsWarning() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionDependentNoMiamFieldWarningsData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().items.contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate SUCCESS status with NoMIAM_domesticViolence field in bulkscan request")
    void testC100NoMiamDomesticViolenceSuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionToAttendMiamDependentSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        Map<String, String> ocrDataFieldMap = getOcrFieldsMap(bulkScanValidationRequest);

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertNotNull(ocrDataFieldMap.get(NOMIAM_DOMESTICVIOLENCE_FIELD));
        assertEquals(TICK_BOX_TRUE, ocrDataFieldMap.get(NOMIAM_DOMESTICVIOLENCE_FIELD));
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate SUCCESS status with NoMIAM_childProtectionConcerns field in bulkscan request")
    void testC100NoMiamChildProtectionConcernsSuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionToAttendMiamDependentSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        Map<String, String> ocrDataFieldMap = getOcrFieldsMap(bulkScanValidationRequest);

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertNotNull(ocrDataFieldMap.get(NOMIAM_CHILDPROTECTIONCONCERNS_FIELD));
        assertEquals(TICK_BOX_TRUE, ocrDataFieldMap.get(NOMIAM_CHILDPROTECTIONCONCERNS_FIELD));
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate SUCCESS status with NoMIAM_Urgency field in bulkscan request")
    void testC100NoMiamUrgencySuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionToAttendMiamDependentSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        Map<String, String> ocrDataFieldMap = getOcrFieldsMap(bulkScanValidationRequest);

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertNotNull(ocrDataFieldMap.get(NOMIAM_URGENCY_FIELD));
        assertEquals(TICK_BOX_TRUE, ocrDataFieldMap.get(NOMIAM_URGENCY_FIELD));
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_URGENCY_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("C100 transform with permission requried option as 'No, permission Not required'.")
    void testC100PermissionRequiredTransform() throws IOException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
            readFileFrom(C100_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
            .filter(eachField ->
                        PERMISSION_REQUIRED.equalsIgnoreCase(eachField.getName()))
            .forEach(field -> field.setValue("No, permission Not required"));
        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
            PermissionRequiredEnum.noNotRequired.getDisplayedValue(),
            res.getCaseCreationDetails().getCaseData().get(APPLICATION_PERMISSION_REQUIRED));
    }

    @Test
    @DisplayName("C100 transform with permission requried option as 'No, permission Now sought'.")
    void testC100PermissionRequiredAsSoughtTransform() throws IOException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
            readFileFrom(C100_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);
        bulkScanTransformationRequest.getOcrdatafields().stream()
            .filter(eachField ->
                        PERMISSION_REQUIRED.equalsIgnoreCase(eachField.getName()))
            .forEach(field -> field.setValue("No, permission Now sought"));
        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
            PermissionRequiredEnum.noNowSought.getDisplayedValue(),
            res.getCaseCreationDetails().getCaseData().get(APPLICATION_PERMISSION_REQUIRED));
    }

    @DisplayName("Should generate SUCCESS status with NoMIAM_PreviousAttendance field in bulkscan request")
    void testC100NoMiamPreviousAttendanceSuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionToAttendMiamDependentSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        Map<String, String> ocrDataFieldMap = getOcrFieldsMap(bulkScanValidationRequest);

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertNotNull(ocrDataFieldMap.get(NOMIAM_PREVIOUSATTENDANCE_FIELD));
        assertEquals(TICK_BOX_TRUE, ocrDataFieldMap.get(NOMIAM_PREVIOUSATTENDANCE_FIELD));
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_PREVIOUSATTENDANCE_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName("Should generate SUCCESS status with NoMIAM_otherReasons field in bulkscan request")
    void testC100NoMiamOtherReasonsSuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getAllNamesRelationSuccessData());
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getExemptionToAttendMiamDependentSuccessData());

        when(postcodeLookupService.isValidPostCode(POST_CODE, null)).thenReturn(true);
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
                c100GetDomesticViolenceWarningData).build();

        Map<String, String> ocrDataFieldMap = getOcrFieldsMap(bulkScanValidationRequest);

        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);

        assertNotNull(ocrDataFieldMap.get(NOMIAM_OTHERREASONS_FIELD));
        assertEquals(TICK_BOX_TRUE, ocrDataFieldMap.get("NoMIAM_otherReasons"));
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().items.contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        assertNotNull(bulkScanTransformationResponse);
    }

    private Map<String, String> getOcrFieldsMap(BulkScanValidationRequest bulkRequest) {
        return bulkRequest.getOcrdatafields()
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));
    }

}
