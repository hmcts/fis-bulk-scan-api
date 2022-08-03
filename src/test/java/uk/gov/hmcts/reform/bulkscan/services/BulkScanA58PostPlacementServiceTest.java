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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.enums.MaritalStatusEnum;
import uk.gov.hmcts.reform.bulkscan.enums.RelationToChildEnum;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanA58PostPlacementServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String A58_POST_PLACEMENT_TRANSFORM_RESPONSE_PATH =
            "classpath:response/bulk-scan-a58-post-placement-transform-output.json";

    private static final String A58_POST_PLACEMENT_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-input.json";
    private static final String A58_POST_PLACEMENT_WARNING_REQUEST_PATH =
        "classpath:request/bulk-scan-a58-post-placement-warning-input.json";
    private static final String A58_POST_PLACEMENT_UNKNOWN_FIELD_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-unknown-field-input.json";
    private static final String A58_POST_PLACEMENT_ERROR_FIELD_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-error-field-input.json";
    private static final String A58_POST_PLACEMENT_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-a58-post-placement-transform-input.json";

    @Spy
    @Autowired
    BulkScanA58Service bulkScanValidationService;

    @Test
    @DisplayName("A58 post placement form validation success scenario")
    void testA58PostPlacementApplicationValidationSuccess() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(A58_POST_PLACEMENT_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("A58 post placement form validation success scenario")
    void testA58PostPlacementApplicationValidationWithWarning() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
            .readValue(readFileFrom(A58_POST_PLACEMENT_WARNING_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
    }

    @Test
    @DisplayName("A58 post placement form validation unknown field warning scenario")
    void testA58PostPlacementApplicationValidationUnknownFieldWarning() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper.readValue(readFileFrom(
                A58_POST_PLACEMENT_UNKNOWN_FIELD_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.WARNINGS, res.status);
    }

    @Test
    @DisplayName("A58 post placement form validation error scenario")
    void testA58PostPlacementApplicationMandatoryErrorWhileDoingValidation() throws IOException {
        BulkScanValidationRequest bulkScanValidationRequest = mapper
                .readValue(readFileFrom(A58_POST_PLACEMENT_ERROR_FIELD_REQUEST_PATH), BulkScanValidationRequest.class);
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "applicant1_firstName")));
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, APPLICANT2_SOT)));
    }

    @Test
    @DisplayName("A58 post placement form transform scenario")
    void testA58PostPlacementApplicationTransformSuccess() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
                readFileFrom(A58_POST_PLACEMENT_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);

        JSONAssert.assertEquals(readFileFrom(A58_POST_PLACEMENT_TRANSFORM_RESPONSE_PATH),
                mapper.writeValueAsString(res), true);
    }

    @Test
    @DisplayName("A58 post placement with other parent exclusion justified marital status")
    void testA58PostPlacementApplicationTransformApplicantSuccess() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
            FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_applying_alone_other_parent_exclusion_justified", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.OTHER_PARENT_EXCLUSION_JUSTIFIED.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }


    @Test
    @DisplayName("A58 post placement with other parent exclusion justified marital status")
    void testA58PostPlacementApplicationTransformWithNoOtherParent() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
            FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_applying_alone_no_other_parent", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.NO_OTHER_PARENT.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with other parent Natural parentnot found marital status")
    void testA58PostPlacementApplicationTransformWithNaturalOtherParent() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
            FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_applying_alone_natural_parent_not_found", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.NATURAL_PARENT_NOT_FOUND.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with other parent Natural parent Died marital status")
    void testA58PostPlacementApplicationTransformWithNaturalParentDied() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
            FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_applying_alone_natural_parent_died", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.NATURAL_PARAENT_DIED.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with Spouse Incapable marital status")
    void testA58PostPlacementApplicationTransformWithSpouseIncapable() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_marital_status_married_spouse_incapable", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.SPOUSE_INCAPABLE.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with Spouse Separated marital status")
    void testA58PostPlacementApplicationTransformWithSpouseSeparated() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_marital_status_married_spouse_separated", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.SPOUSE_SEPARATED.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }


    @Test
    @DisplayName("A58 post placement with Spouse not found marital status")
    void testA58PostPlacementApplicationTransformWithSpouseNotFound() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_marital_status_married_spouse_notfound", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.SPOUSE_NOT_FOUND.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with Widow marital status")
    void testA58PostPlacementApplicationTransformWithWidow() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_marital_status_widow", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.WIDOW.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with Divorced marital status")
    void testA58PostPlacementApplicationTransformWithDivorced() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_marital_status_divorced", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.DIVORCED.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with Divorced Single status")
    void testA58PostPlacementApplicationTransformWithSingle() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_marital_status_single", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(MaritalStatusEnum.SINGLE.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantMaritalStatus"));
    }

    @Test
    @DisplayName("A58 post placement with mother relationship")
    void testA58PostPlacementApplicationRelationToChild() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_relationToChild_mother_partner", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(RelationToChildEnum.MOTHER.getName(),
            res.getCaseCreationDetails().getCaseData().get("applicantRelationToChild"));
    }

    @Test
    @DisplayName("A58 post placement with Civil relationship")
    void testA58PostPlacementApplicationRelationToChildAsCivil() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicant_relationToChild_non_civil_partner", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals(RelationToChildEnum.CIVIL.getName(),
                                res.getCaseCreationDetails().getCaseData().get("applicantRelationToChild"));
    }

    @Test
    @DisplayName("A58 post placement with Domicile status")
    void testA58PostPlacementApplicationDomicilleStatus() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicants_domicile_status", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals("true",
                                res.getCaseCreationDetails().getCaseData().get("applicantsDomicileStatus"));
    }

    @Test
    @DisplayName("A58 post placement with non Domicile status")
    void testA58PostPlacementApplicationNonDomicilleStatus() {
        BulkScanTransformationRequest bulkScanTransformationRequest = BulkScanTransformationRequest.builder().formType(
                FormType.A58.name()).caseTypeId("A58")
            .ocrdatafields(getOcrDataFields("applicants_non_domicile_status", "true"))
            .build();

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);
        assertEquals("false",
                                res.getCaseCreationDetails().getCaseData().get("applicantsDomicileStatus"));
    }


    private List<OcrDataField> getOcrDataFields(String key, String value) {
        List<OcrDataField> list = TestDataUtil.getA58DataWithoutRelation();
        OcrDataField ocrDataField = new OcrDataField();
        ocrDataField.setName(key);
        ocrDataField.setValue(value);
        list.add(ocrDataField);
        return list;
    }
}
