package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BulkScanC100Page1ServiceTest {

    @Autowired
    BulkScanC100Service bulkScanValidationService;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String C100_PAGE1_TRANSFORM_REQUEST_PATH =
        "classpath:request/bulk-scan-c100-transform-input.json";
    private static final String C100_PAGE1_TRANSFORM_RESPONSE_PATH =
        "classpath:response/bulk-scan-c100-transform-output.json";

    @Test
    @DisplayName("C100 Page 1 validation success scenario")
    void testC100Page1ApplicationValidationSuccess() {
        List<OcrDataField> ocrDataFieldList = getRequestData();

        OcrDataField ocrChildArrangementOrderField = new OcrDataField();
        ocrChildArrangementOrderField.setName("childArrangement_order");
        ocrChildArrangementOrderField.setValue("childArrangementOrder");
        ocrDataFieldList.add(ocrChildArrangementOrderField);
        BulkScanValidationResponse res = bulkScanValidationService.validate(BulkScanValidationRequest
                                                                                .builder()
                                                                                .ocrdatafields(ocrDataFieldList)
                                                                                .build());
        assertEquals(Status.SUCCESS, res.status);
    }

    @Test
    @DisplayName("C100 page 1 form transform scenario")
    void testC100Page1TransformRequest() throws IOException, JSONException {
        BulkScanTransformationRequest bulkScanTransformationRequest = mapper.readValue(
            readFileFrom(C100_PAGE1_TRANSFORM_REQUEST_PATH), BulkScanTransformationRequest.class);

        BulkScanTransformationResponse res = bulkScanValidationService.transform(bulkScanTransformationRequest);

        JSONAssert.assertEquals(readFileFrom(C100_PAGE1_TRANSFORM_RESPONSE_PATH),
                                mapper.writeValueAsString(res), true);
    }

    public List<OcrDataField> getRequestData() {
        List<OcrDataField> fieldList = new ArrayList<>();
        OcrDataField ocrDataFirstNameField = new OcrDataField();
        ocrDataFirstNameField.setName("applicant1_firstName");
        ocrDataFirstNameField.setValue("John");
        fieldList.add(ocrDataFirstNameField);

        OcrDataField ocrDataLastNameField = new OcrDataField();
        ocrDataLastNameField.setName("applicant1_lastName");
        ocrDataLastNameField.setValue("LastName");
        fieldList.add(ocrDataLastNameField);

        OcrDataField ocrProhibitedStepsOrderField = new OcrDataField();
        ocrProhibitedStepsOrderField.setName("prohibitedSteps_order");
        ocrProhibitedStepsOrderField.setValue("prohibitedStepsOrder");
        fieldList.add(ocrProhibitedStepsOrderField);

        OcrDataField ocrSpecialIssueOrderField = new OcrDataField();
        ocrSpecialIssueOrderField.setName("specialIssue_order");
        ocrSpecialIssueOrderField.setValue("specialIssueOrder");
        fieldList.add(ocrSpecialIssueOrderField);

        return fieldList;
    }
}
