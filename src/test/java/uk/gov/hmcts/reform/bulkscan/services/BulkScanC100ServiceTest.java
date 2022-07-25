package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
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
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_OF_SAME_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME_COLLECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String C100_VALIDATION_REQUEST_PATH =
        "classpath:request/bulk-scan-c100-validate-input.json";

    private static final String C100_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-c100-transform-input.json";

    private static final String C100_TRANSFORM_RESPONSE_PATH =
        "classpath:response/bulk-scan-c100-transform-output.json";

    @Spy
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

}
