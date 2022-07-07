package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PHONE_NUMBER_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanA58ServiceTest {

    private static final String A58_STEP_PARENT_TRANSFORM_RESPONSE_PATH =
        "classpath:response/bulk-scan-a58-step-parent-transform-output.json";

    @Autowired
    BulkScanA58Service bulkScanValidationService;

    @Test
    void testA58Success() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60OrC63orA58Data()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testA58MandatoryErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60OrC63orA58ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MANDATORY_ERROR_MESSAGE, "applicant1_firstName")));
    }

    @Test
    void testA58FieldMissingErrorWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60OrC63orA58ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertEquals(Status.ERRORS, res.status);
        assertTrue(res.getErrors().items.contains(String.format(MISSING_FIELD_MESSAGE, "applicant1_lastName")));
    }

    @Test
    void testA58OptionalFieldsWarningsWhileDoingValidation() {
        BulkScanValidationRequest bulkScanValidationRequest = BulkScanValidationRequest.builder().ocrdatafields(
            TestDataUtil.getA60OrC63orA58ErrorData()).build();
        BulkScanValidationResponse res = bulkScanValidationService.validate(bulkScanValidationRequest);
        assertTrue(res.getWarnings().items.contains(String.format(PHONE_NUMBER_MESSAGE, "applicant2_telephoneNumber")));
    }

    @Test
    void testA58StepParentAdoptionTransformRequest() throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(TestDataUtil.getA60OrC63orA58Data()).build());

        JSONAssert.assertEquals(readFileFrom(A58_STEP_PARENT_TRANSFORM_RESPONSE_PATH),
                                mapper.writeValueAsString(bulkScanTransformationResponse), true
        );

    }

    @Test
    void testA58RelinquishedAdoptionConsentTransformRequest() throws IOException, JSONException {
        List<OcrDataField> ocrDataFieldList = TestDataUtil.getA58RelinquishedAdoptionConsentData();
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(ocrDataFieldList)
                                                    .build());
        assertEquals(
            bulkScanTransformationResponse
                .getCaseCreationDetails().getCaseData().get("adoptionOrderConsent"),
            "Adoption Order Consent");

    }

    @Test
    void testA58RelinquishedAdoptionConsentAdvanceTransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(
                                                        TestDataUtil.getA58RelinquishedAdoptionConsentAdvanceData())
                                                    .build());

        System.out.println(TestDataUtil.getA58RelinquishedAdoptionConsentAdvanceData());
        System.out.println(bulkScanTransformationResponse.getCaseCreationDetails().getCaseData());

        assertEquals(
            bulkScanTransformationResponse.getCaseCreationDetails()
                .getCaseData().get("adoptionOrderConsentAdvance"),
            "Adoption Order Consent Advance"
        );
    }

    @Test
    void testA58RelinquishedAdoptionConsentAgencyTransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(
                                                        TestDataUtil.getA58RelinquishedAdoptionOrderConsentAgencyData())
                                                    .build());

        assertEquals(bulkScanTransformationResponse.getCaseCreationDetails()
                         .getCaseData().get("adoptionOrderConsentAgency"), "Adoption Order Consent Agency");
    }

    @Test
    void testA58RelinquishedAdoptionNoConsentTransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(
                                                        TestDataUtil.getA58RelinquishedAdoptionOrderNoConsentData())
                                                    .build());

        assertEquals(
            bulkScanTransformationResponse.getCaseCreationDetails()
                .getCaseData().get("adoptionOrderNoConsent"),
            "Adoption Order No Consent"
        );
    }


    @Test
    void testA58RelinquishedAdoptionConsentParentNotFoundTransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(
                                                        TestDataUtil.getA58RelinquishedAdoptionParentNotFoundData())
                                                    .build());

        assertEquals(
            bulkScanTransformationResponse.getCaseCreationDetails()
                .getCaseData().get("courtConsentParentNotFound"),
            "Court Consent Parent Not Found"
        );
    }

    @Test
    void testA58RelinquishedAdoptionConsentParentLackCapacityTransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(
                                                        TestDataUtil
                                                            .getA58RelinquishedAdoptionConsentParentLackCapacityData())
                                                    .build());

        assertEquals(
            bulkScanTransformationResponse.getCaseCreationDetails()
                .getCaseData().get("courtConsentParentLackCapacity"),
            "Court Consent Parent Lack Capacity"
        );
    }

    @Test
    void testA58RelinquishedAdoptionConsentChildWelfareTransformRequest() throws IOException, JSONException {
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService
                .transform(BulkScanTransformationRequest.builder()
                               .ocrdatafields(
                                   TestDataUtil.getA58RelinquishedAdoptionChildWelfareData())
                               .build());

        assertEquals(
            bulkScanTransformationResponse.getCaseCreationDetails()
                .getCaseData().get("courtConsentChildWelfare"),
            "Court Consent Child welfare"
        );
    }

}
