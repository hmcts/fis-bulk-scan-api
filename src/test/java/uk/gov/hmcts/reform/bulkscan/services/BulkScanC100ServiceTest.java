package uk.gov.hmcts.reform.bulkscan.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMAIL_FORMAT_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.GROUP_DEPENDENCY_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOMIAM_DOMESTICVIOLENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NUMERIC_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.POST_CODE_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestDataC100Util.POST_CODE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ServiceTest {

    @Autowired
    BulkScanC100Service bulkScanValidationService;

    @MockBean
    PostcodeLookupService postcodeLookupService;

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
        assertTrue(res.getWarnings().items.contains(String.format(GROUP_DEPENDENCY_MESSAGE,
                EXEMPTION_TO_ATTEND_MIAM_GROUP_FIELD)));
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
        assertTrue(res.getWarnings().items.contains(String.format(GROUP_DEPENDENCY_MESSAGE,
                NOMIAM_DOMESTICVIOLENCE)));
    }

    @Test
    void testTransform() {
        BulkScanTransformationResponse bulkScanTransformationResponse =
                bulkScanValidationService.transform(mock(BulkScanTransformationRequest.class));
        Assertions.assertNotNull(bulkScanTransformationResponse);
    }

}
