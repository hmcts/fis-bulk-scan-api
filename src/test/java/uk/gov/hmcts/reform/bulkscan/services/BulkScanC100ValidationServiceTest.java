package uk.gov.hmcts.reform.bulkscan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.ASSESSMENT_BY_ADULT_LEARNING_TEAM_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.FACTORS_AFFECTING_LITIGATION_CAPACITY_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.FACTORS_AFFECTING_PERSON_IN_COURT_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_JURISDICTIONISSUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_REQUEST_CENTRAL_CONSULAR_AUTH_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONALELEMENT_RESIDENT_ANOTHER_STATE_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERNATIONAL_OR_FACTORS_AFFECTING_LITIGATION_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUTNOTICE_JURISDICTIONISSUE_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.utils.Constants.EMPTY_STRING;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util.POST_CODE;

import java.util.ArrayList;
import java.util.List;
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
import uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ValidationServiceTest {
    private static final String INTERNATIONAL_FACTORS_AFFECTING_LITIGATION_WARNING =
            "Group Dependency Field (international_or_factorsAffectingLitigation) has dependency"
                + " validation warning. Must contain at least 1 of the fields "
                + "[internationalElement_Resident_of_another_state,internationalElement_jurisdictionIssue,"
                + "internationalElement_request_toCentral_or_Consular_authority,factorAffectingLitigationCapacity,"
                + "assessmentByAdultLearningTeam,factorsAffectingPersonInCourt].";

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

    @Autowired BulkScanC100ValidationService bulkScanC100ValidationService;

    @Autowired BulkScanC100Service bulkScanValidationService;

    @MockBean PostcodeLookupService postcodeLookupService;

    List<OcrDataField> ocrDataFieldList;

    BulkScanValidationResponse bulkScanValidationResponse;

    @BeforeEach
    void setUp() {
        ocrDataFieldList = getRequestData();
        bulkScanValidationResponse =
                BulkScanValidationResponse.builder()
                        .status(null)
                        .warnings(Warnings.builder().items(new ArrayList<>()).build())
                        .errors(Errors.builder().items(new ArrayList<>()).build())
                        .build();
    }

    @Test
    void testValidateRequiremntToAttendMiam() {
        bulkScanValidationResponse =
                bulkScanC100ValidationService.validateAttendMiam(
                        ocrDataFieldList, bulkScanValidationResponse);
        assertEquals(Status.ERRORS, bulkScanValidationResponse.status);
    }

    @Test
    void testValidateRequiremntToAttendMiam1() {
        List<OcrDataField> ocrDataFieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("previous_or_ongoingProceeding");
        ocrDataFirstNameField.setValue("Yes");
        ocrDataFieldList.add(ocrDataFirstNameField);
        bulkScanValidationResponse =
                bulkScanC100ValidationService.validateAttendMiam(
                        ocrDataFieldList, bulkScanValidationResponse);
        assertEquals(Status.ERRORS, bulkScanValidationResponse.status);
    }

    @Test
    void testOcrDataListEmptyOrNull() {
        List<OcrDataField> ocrDataFieldList = new ArrayList<>();
        List<OcrDataField> ocrDataFieldListNull = null;

        bulkScanValidationResponse =
                bulkScanC100ValidationService.validateAttendMiam(
                        ocrDataFieldList, bulkScanValidationResponse);
        assertNotEquals(Status.ERRORS, bulkScanValidationResponse.status);

        bulkScanValidationResponse =
                bulkScanC100ValidationService.validateAttendMiam(
                        ocrDataFieldListNull, bulkScanValidationResponse);
        assertNotEquals(Status.ERRORS, bulkScanValidationResponse.status);
    }

    @Test
    void testOcrDataListForApplicantAddress() {
        List<OcrDataField> ocrDataFieldList = new ArrayList<>();
        OcrDataField applicantOneLivedAtThisAddressForOverFiveYears = new OcrDataField();
        applicantOneLivedAtThisAddressForOverFiveYears.setName(
                "applicantOneLivedAtThisAddressForOverFiveYears");
        applicantOneLivedAtThisAddressForOverFiveYears.setValue("No");
        ocrDataFieldList.add(applicantOneLivedAtThisAddressForOverFiveYears);
        OcrDataField applicantOneAllAddressesForLastFiveYears = new OcrDataField();
        applicantOneAllAddressesForLastFiveYears.setName(
                "applicantOneAllAddressesForLastFiveYears");
        ocrDataFieldList.add(applicantOneAllAddressesForLastFiveYears);
        OcrDataField applicantTwoLivedAtThisAddressForOverFiveYears = new OcrDataField();
        applicantTwoLivedAtThisAddressForOverFiveYears.setName(
                "applicantTwoLivedAtThisAddressForOverFiveYears");
        applicantTwoLivedAtThisAddressForOverFiveYears.setValue("No");
        ocrDataFieldList.add(applicantTwoLivedAtThisAddressForOverFiveYears);
        OcrDataField applicantTwoAllAddressesForLastFiveYears = new OcrDataField();
        applicantTwoAllAddressesForLastFiveYears.setName(
                "applicantTwoAllAddressesForLastFiveYears");
        ocrDataFieldList.add(applicantTwoAllAddressesForLastFiveYears);

        bulkScanValidationResponse =
                bulkScanC100ValidationService.validateApplicantAddressFiveYears(
                        ocrDataFieldList, bulkScanValidationResponse);
        assertNotEquals(Status.ERRORS, bulkScanValidationResponse.status);
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

        assertEquals(Status.SUCCESS, res.status);
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
                .forEach(field -> field.setValue(YES));

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
                .forEach(field -> field.setValue(YES));

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
            "Should generate warning for checkbox ticked with 'Yes' when a request has been made or"
                + " should a request be made to a Central Authority or other competent authority in"
                + " a foreign state or a consular authority in England and Waleson sections 8 of"
                + " application form but no details were given.")
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
                .forEach(field -> field.setValue(YES));

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

    public List<OcrDataField> getRequestData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("previous_or_ongoingProceeding");
        ocrDataFirstNameField.setValue("Yes");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("existingCase_onEmergencyProtection_Care_or_supervisioNorder");
        ocrDataLastNameField.setValue("No");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrProhibitedStepsOrderField = new OcrDataField();
        ocrProhibitedStepsOrderField.setName("exemption_to_attend_MIAM");
        ocrProhibitedStepsOrderField.setValue("No");
        fieldList.add(ocrProhibitedStepsOrderField);

        OcrDataField ocrSpecialIssueOrderField = new OcrDataField();
        ocrSpecialIssueOrderField.setName("familyMember_Intimation_on_No_MIAM");
        ocrSpecialIssueOrderField.setValue("No");
        fieldList.add(ocrSpecialIssueOrderField);

        OcrDataField ocrAttendedMiamField = new OcrDataField();
        ocrAttendedMiamField.setName("attended_MIAM");
        ocrAttendedMiamField.setValue("No");
        fieldList.add(ocrAttendedMiamField);

        return fieldList;
    }
}
