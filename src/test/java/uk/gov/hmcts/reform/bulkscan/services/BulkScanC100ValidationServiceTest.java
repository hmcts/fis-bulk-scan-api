package uk.gov.hmcts.reform.bulkscan.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanC100ValidationServiceTest {

    @Autowired
    BulkScanC100ValidationService bulkScanC100ValidationService;

    List<OcrDataField> ocrDataFieldList;

    BulkScanValidationResponse bulkScanValidationResponse;

    @BeforeEach
     void setUp() {
        ocrDataFieldList = getRequestData();
        bulkScanValidationResponse = BulkScanValidationResponse.builder()
            .status(null)
            .warnings(Warnings.builder().items(new ArrayList<>()).build())
            .errors(Errors.builder().items(new ArrayList<>()).build()).build();
    }


    @Test
    void testValidateRequiremntToAttendMiam() {
        bulkScanValidationResponse = bulkScanC100ValidationService
            .validateAttendMiam(ocrDataFieldList,bulkScanValidationResponse);
        assertEquals(Status.ERRORS, bulkScanValidationResponse.status);
    }

    @Test
    void testValidateRequiremntToAttendMiam1() {
        List<OcrDataField> ocrDataFieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("previous_or_ongoingProceeding");
        ocrDataFirstNameField.setValue("Yes");
        ocrDataFieldList.add(ocrDataFirstNameField);
        bulkScanValidationResponse = bulkScanC100ValidationService
            .validateAttendMiam(ocrDataFieldList,bulkScanValidationResponse);
        assertEquals(Status.ERRORS, bulkScanValidationResponse.status);
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

        return fieldList;
    }
}
