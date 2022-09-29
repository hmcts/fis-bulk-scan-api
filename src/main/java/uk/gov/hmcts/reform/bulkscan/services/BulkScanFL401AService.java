package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
//import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FL401A_DATE_OF_BIRTH_FIELD;
//import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FL401A_DATE_OF_BIRTH_FORMAT;
//import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FUTURE_DATE_OF_BIRTH_WARNING;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

@Service
public class BulkScanFL401AService implements BulkScanService {

    @Autowired BulkScanFormValidationConfigManager configManager;

    @Autowired BulkScanTransformConfigManager transformConfigManager;

    @Autowired BulkScanValidationHelper bulkScanValidationHelper;

    @Override
    public FormType getCaseType() {
        return FormType.FL401A;
    }

    @Override
    public BulkScanValidationResponse validate(
            BulkScanValidationRequest bulkScanValidationRequest) {
        List<OcrDataField> inputFieldsList = bulkScanValidationRequest.getOcrdatafields();

        Map<String, String> inputFieldsMap =
                inputFieldsList.stream()
                        .filter(ocrDataField -> StringUtils.isNotEmpty(ocrDataField.getName()))
                        .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        BulkScanValidationResponse response =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(
                        bulkScanValidationRequest.getOcrdatafields(),
                        configManager.getValidationConfig(FormType.FL401A));

        response.changeStatus();

        return response;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        // TODO transformation logic
        return null;
    }
}
