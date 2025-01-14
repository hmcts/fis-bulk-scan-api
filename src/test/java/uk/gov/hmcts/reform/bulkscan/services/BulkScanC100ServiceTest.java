package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_OF_SAME_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME_COLLECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1ALLADDRESSESFORLASTFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT1LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2ALLADDRESSESFORLASTFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT2LIVEDATTHISADDRESSFOROVERFIVEYEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.OTHER_PROCEEDINGS_DETAILS_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.OTHER_PROCEEDING_CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.OTHER_PROCEEDING_TYPE_OF_ORDER_1;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.EMPTY_STRING;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_CHILDPROTECTIONCONCERNS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_DOMESTICVIOLENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_OTHERREASONS_DEPENDENCY_WARNING;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_OTHERREASONS_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_PREVIOUSATTENDENCE_DEPENDENCY_WARNING;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_PREVIOUSATTENDENCE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_URGENCY_DEPENDENCY_WARNING;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.NOMIAM_URGENCY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.RESPONDENT_ONE_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.RESPONDENT_TWO_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.TICK_BOX_TRUE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util.POST_CODE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String C100_VALIDATION_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-validate-input.json";

    private static final String C100_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-transform-input.json";

    private static final String C100_TRANSFORM_SECTION4_SCENARIO1_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-section4-secenario-1-transform-input.json";

    private static final String C100_TRANSFORM_SECTION4_SCENARIO2_REQUEST_PATH =
            "classpath:request/bulk-scan-c100-section4-secenario-2-transform-input.json";

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

    @Ignore
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
        assertEquals(Status.SUCCESS, res.status);
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
        assertEquals(Status.SUCCESS, res.status);
    }

    @Ignore
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
        assertEquals(Status.SUCCESS, res.status);
    }

    @Ignore
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
        assertEquals(Status.SUCCESS, res.status);
    }

    @Ignore
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
        assertEquals(Status.SUCCESS, res.status);
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
        assertNotNull(res);
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
        c100GetExemptionWarningData.addAll(TestDataC100Util.getC100AttendTheHearingData());
        BulkScanValidationRequest bulkScanValidationRequest =
                BulkScanValidationRequest.builder()
                        .ocrdatafields(c100GetExemptionWarningData)
                        .build();

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(Status.WARNINGS, res.status);
        assertTrue(res.getWarnings().contains(EXEMPTION_TO_ATTEND_MIAM_DEPENDENCY_WARNING));
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
        assertTrue(res.getWarnings().contains(NOMIAM_DOMESTICVIOLENCE_DEPENDENCY_WARNING));
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
        assertTrue(res.getWarnings().contains(NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING));
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
        assertTrue(res.getWarnings().contains(NOMIAM_URGENCY_DEPENDENCY_WARNING));
    }

    @Test
    @DisplayName(
            "Should generate warnings on [NoMIAM_PreviousAttendenceReason] checked but without"
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
        assertTrue(res.getWarnings().contains(NOMIAM_PREVIOUSATTENDENCE_DEPENDENCY_WARNING));
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
        assertTrue(res.getWarnings().contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
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
        assertFalse(res.getWarnings().contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
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
        assertFalse(res.getWarnings().contains(NOMIAM_CHILDPROTECTIONCONCERNS_DEPENDENCY_WARNING));
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
        assertFalse(res.getWarnings().contains(NOMIAM_URGENCY_DEPENDENCY_WARNING));
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
            "Should generate SUCCESS status with NoMIAM_PreviousAttendence field in bulkscan"
                    + " request")
    void testC100NoMiamPreviousAttendanceSuccessData() {
        List<OcrDataField> c100GetDomesticViolenceWarningData = new ArrayList<>();
        c100GetDomesticViolenceWarningData.addAll(TestDataC100Util.getData());
        OcrDataField previousAttendanceReason = new OcrDataField();
        previousAttendanceReason.setName("NoMIAM_PreviousAttendenceReason");
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
                                NOMIAM_PREVIOUSATTENDENCE_FIELD.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(TICK_BOX_TRUE));

        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);

        assertEquals(
                TICK_BOX_TRUE,
                bulkScanValidationRequest.getOcrdatafields().stream()
                        .filter(
                                eachField ->
                                        "NoMIAM_PreviousAttendence"
                                                .equalsIgnoreCase(eachField.getName()))
                        .findAny()
                        .orElse(null)
                        .getValue());
        assertEquals(Status.SUCCESS, res.status);
        assertFalse(res.getWarnings().contains(NOMIAM_PREVIOUSATTENDENCE_DEPENDENCY_WARNING));
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
        assertFalse(res.getWarnings().contains(NOMIAM_OTHERREASONS_DEPENDENCY_WARNING));
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
        assertTrue(res.getWarnings().contains(RESPONDENT_ONE_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS));
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
        assertTrue(res.getWarnings().contains(RESPONDENT_TWO_NOT_LIVED_IN_ADDRESS_FOR_FIVE_YEARS));
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        assertNotNull(bulkScanTransformationResponse);
    }

    @Test
    @DisplayName("C100 validation with type of order.")
    void testC100ValidationErrorWithOtherProceedings() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                OTHER_PROCEEDING_TYPE_OF_ORDER_1.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(""));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("C100 validation with mandatory fields.")
    void testC100ValidationErrorWithOtherProceedingsMandatoryFields() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                OTHER_PROCEEDING_CASE_NUMBER.equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue("1234"));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("C100 transform with other proceedings.")
    void testC100TransformWithOtherProceedings() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        bulkScanTransformationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                OTHER_PROCEEDING_TYPE_OF_ORDER_1.equalsIgnoreCase(
                                        eachField.getName()))
                .forEach(field -> field.setValue(""));

        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(
                ((List)
                                res.getCaseCreationDetails()
                                        .getCaseData()
                                        .get(OTHER_PROCEEDINGS_DETAILS_TABLE))
                        .size(),
                1);
    }

    @Test
    @DisplayName("C100 validation with Attend the hearing.")
    void testC100ValidationErrorWithAttendTheHearing() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest =
                mapper.readValue(
                        readFileFrom(C100_VALIDATION_REQUEST_PATH),
                        BulkScanValidationRequest.class);
        bulkScanValidationRequest.getOcrdatafields().stream()
                .filter(
                        eachField ->
                                APPLICANT_REQUIRES_INTERPRETER_APPLICANT.equalsIgnoreCase(
                                                eachField.getName())
                                        || APPLICANT_REQUIRES_INTERPRETER_RESPONDENT
                                                .equalsIgnoreCase(eachField.getName())
                                        || APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY
                                                .equalsIgnoreCase(eachField.getName()))
                .forEach(field -> field.setValue(EMPTY_STRING));
        BulkScanValidationResponse res =
                bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("C100 transform success section 4 - scenario 1.")
    void testC100TransformSuccessSection4Scenario1() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_SECTION4_SCENARIO1_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertNotNull(res);
    }

    @Test
    @DisplayName("C100 transform success section 4 - scenario 2.")
    void testC100TransformSuccessSection4Scenario2() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(C100_TRANSFORM_SECTION4_SCENARIO2_REQUEST_PATH),
                        BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res =
                bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertNotNull(res);
    }
}
