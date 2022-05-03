package uk.gov.hmcts.reform.bulkscan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.*;
import uk.gov.hmcts.reform.bulkscan.utils.BulkScanValidationUtil;

import java.util.*;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;

@Service
public class BulkScanC100Service implements BulkScanService {

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        return BulkScanValidationHelper.validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(),
                                                                          configManager.getValidationConfig(
                                                                              FormType.C100));
    }

    public Map<String, String> convertWithStream(String mapAsString) {
        Map<String, String> map = Arrays.stream(mapAsString.split(","))
            .map(entry -> entry.split("="))
            .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();
        // Put generic values from the input
        // Need to store the Exception Record id as part of the CCD data
        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

       // caseData.put("scannedDocuments", transformScannedDocuments(transformationInput));

        // Converting input list into a map
        Map<String, String> inputFieldsMap = inputFieldsList.stream().collect(
            Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        Map<String, Object> populatedMap = (Map<String, Object>) BulkScanTransformHelper
            .getMapObjectAndValue(transformConfigManager.getSourceAndTargetFields(FormType.C100), inputFieldsMap);

        try {
            String json = new ObjectMapper().writeValueAsString(populatedMap);
            System.out.println(json);
        } catch(Exception e) {

        }


        return BulkScanTransformationResponse.builder().caseCreationDetails(
            CaseCreationDetails.builder().caseData(populatedMap).build()).build();

    }
}
