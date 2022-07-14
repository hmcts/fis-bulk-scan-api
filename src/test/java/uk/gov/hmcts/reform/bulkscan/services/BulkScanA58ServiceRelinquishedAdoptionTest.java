package uk.gov.hmcts.reform.bulkscan.services;

import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BulkScanA58ServiceRelinquishedAdoptionTest {

    @Autowired
    BulkScanA58Service bulkScanValidationService;

    List<OcrDataField> ocrDataFieldList;

    @BeforeEach
    void initEach() {
        ocrDataFieldList = getRequestData();
    }

    @Test
    @DisplayName("A58 relinquished adoption form validation success scenario")
    void testA58RelinquishedAdoptionApplicationValidationSuccess() {
        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("adoption_order_consent");
        ocrOrderConsentAdvanceField.setValue("Adoption Order Consent");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanValidationResponse res = bulkScanValidationService.validate(BulkScanValidationRequest
                .builder().ocrdatafields(ocrDataFieldList).build());
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    void testA58RelinquishedAdoptionConsentTransformRequest() throws InterruptedException {
        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("adoption_order_consent");
        ocrOrderConsentAdvanceField.setValue("Adoption Order Consent");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(
                                                        ocrDataFieldList)
                                                    .build());

        assertEquals(
            bulkScanTransformationResponse
                .getCaseCreationDetails().getCaseData().get("adoptionOrderConsent"),
            "Adoption Order Consent");

    }

    @Test
    void testA58RelinquishedAdoptionConsentAdvanceTransformRequest() {

        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("adoption_order_consent_advance");
        ocrOrderConsentAdvanceField.setValue("Adoption Order Consent Advance");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(
                                                        ocrDataFieldList)
                                                    .build());



        assertEquals(
            bulkScanTransformationResponse.getCaseCreationDetails()
                .getCaseData().get("adoptionOrderConsentAdvance"),
            "Adoption Order Consent Advance"
        );
    }

    @Test
    void testA58RelinquishedAdoptionConsentAgencyTransformRequest()  {
        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("adoption_order_consent_agency");
        ocrOrderConsentAdvanceField.setValue("Adoption Order Consent Agency");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(ocrDataFieldList
                                                        )
                                                    .build());

        assertEquals(bulkScanTransformationResponse.getCaseCreationDetails()
                         .getCaseData().get("adoptionOrderConsentAgency"), "Adoption Order Consent Agency");
    }




    @Test
    void testA58RelinquishedAdoptionNoConsentTransformRequest() throws IOException, JSONException {
        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("adoption_order_no_consent");
        ocrOrderConsentAdvanceField.setValue("adoption order no consent");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(ocrDataFieldList
                                                    )
                                                    .build());

        assertEquals(bulkScanTransformationResponse.getCaseCreationDetails()
                         .getCaseData().get("adoptionOrderNoConsent"), "adoption order no consent");
    }


    @Test
    void testA58RelinquishedAdoptionConsentParentNotFoundTransformRequest() throws IOException, JSONException {
        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("court_consent_parent_not_found");
        ocrOrderConsentAdvanceField.setValue("court consent parent not found");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(ocrDataFieldList
                                                    )
                                                    .build());

        assertEquals(bulkScanTransformationResponse.getCaseCreationDetails()
                         .getCaseData().get("courtConsentParentNotFound"), "court consent parent not found");
    }

    @Test
    void testA58RelinquishedAdoptionConsentParentLackCapacityTransformRequest() throws IOException, JSONException {
        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("court_consent_parent_lack_capacity");
        ocrOrderConsentAdvanceField.setValue("court consent parent lack capacity");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(ocrDataFieldList
                                                    )
                                                    .build());

        assertEquals(bulkScanTransformationResponse.getCaseCreationDetails()
                         .getCaseData().get("courtConsentParentLackCapacity"), "court consent parent lack capacity");
    }

    @Test
    void testA58RelinquishedAdoptionConsentChildWelfareTransformRequest() throws IOException, JSONException {
        OcrDataField ocrOrderConsentAdvanceField = new OcrDataField();
        ocrOrderConsentAdvanceField.setName("court_consent_child_welfare");
        ocrOrderConsentAdvanceField.setValue("court consent child welfare");
        ocrDataFieldList.add(ocrOrderConsentAdvanceField);
        BulkScanTransformationResponse bulkScanTransformationResponse =
            bulkScanValidationService.transform(BulkScanTransformationRequest.builder()
                                                    .ocrdatafields(ocrDataFieldList
                                                    )
                                                    .build());

        assertEquals(bulkScanTransformationResponse.getCaseCreationDetails()
                         .getCaseData().get("courtConsentChildWelfare"), "court consent child welfare");
    }

    public  List<OcrDataField> getRequestData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("firstName");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrContactNumberField = new OcrDataField();
        ocrContactNumberField.setName("applicant1_telephoneNumber");
        ocrContactNumberField.setValue("+447405878672");
        fieldList.add(ocrContactNumberField);

        OcrDataField ocrAddressField = new OcrDataField();
        ocrAddressField.setName("applicant1_address");
        ocrAddressField.setValue("123 test street, London");
        fieldList.add(ocrAddressField);

        return fieldList;
    }

}
