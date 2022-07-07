package uk.gov.hmcts.reform.bulkscan.services;

import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.*;
import uk.gov.hmcts.reform.bulkscan.utils.TestDataUtil;

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

    List<OcrDataField> ocrDataFieldList ;

    @BeforeEach
    void initEach(){
        ocrDataFieldList = null;
        ocrDataFieldList = getRequestData();
    }



    @Test
    void testA58RelinquishedAdoptionConsentTransformRequest() throws InterruptedException {
        System.out.println("case1");


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
    void testA58RelinquishedAdoptionConsentAdvanceTransformRequest() throws IOException, JSONException, InterruptedException {
      System.out.println("case2");

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
    void testA58RelinquishedAdoptionConsentAgencyTransformRequest() throws IOException, JSONException, InterruptedException {
         System.out.println("case3");
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
