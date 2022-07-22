package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ATTENDED_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXEMPTION_TO_ATTEND_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FAMILY_MEMBER_INTIMATION_ON_NO_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ATTENDED_MIAM_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PREVIOUS_OR_ONGOING_PROCEEDING;


/**
 * This class will custom validation methods related to.
 * C100 form custom validation
 */
@Service
public class BulkScanC100ValidationService {

    /**
     * This method will validate C100 form, section 2.
     * data.
     * @param ocrDataFields represents request payload
     * @param bulkScanValidationResponse  is used to add error/warnings
     * @return BulkScanValidationResponse object
     */

    public  BulkScanValidationResponse validateAttendMiam(List<OcrDataField> ocrDataFields,
                                                      BulkScanValidationResponse bulkScanValidationResponse) {

        Map<String, String> ocrDataFieldsMap = getOcrDataFieldsMap(ocrDataFields);

        if (null != ocrDataFieldsMap && !ocrDataFieldsMap.isEmpty()) {

            List<String> items = bulkScanValidationResponse.getErrors()
                .getItems();

            if (ocrDataFieldsMap.containsKey(PREVIOUS_OR_ONGOING_PROCEEDING)
                && StringUtils.hasText(ocrDataFieldsMap.get(PREVIOUS_OR_ONGOING_PROCEEDING))
                && BulkScanConstants.YES.equalsIgnoreCase(ocrDataFieldsMap.get(PREVIOUS_OR_ONGOING_PROCEEDING))) {
                setErrorMsg(bulkScanValidationResponse, ocrDataFieldsMap, items,
                            EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER);
            }

            if (ocrDataFieldsMap.containsKey(EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER)
                && StringUtils.hasText(ocrDataFieldsMap.get(
                EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER))
                && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(
                EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER))) {
                setErrorMsg(bulkScanValidationResponse, ocrDataFieldsMap, items,
                            EXEMPTION_TO_ATTEND_MIAM);

            }

            if (ocrDataFieldsMap.containsKey(EXEMPTION_TO_ATTEND_MIAM)
                && StringUtils.hasText(ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))
                && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))) {
                setErrorMsg(bulkScanValidationResponse, ocrDataFieldsMap, items,
                            FAMILY_MEMBER_INTIMATION_ON_NO_MIAM);

            }

            if (ocrDataFieldsMap.containsKey(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM)
                && StringUtils.hasText(ocrDataFieldsMap.get(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM))
                && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM)
            )) {
                setErrorMsg(bulkScanValidationResponse, ocrDataFieldsMap, items,
                            ATTENDED_MIAM);

                isMiamAttended(ocrDataFieldsMap,bulkScanValidationResponse, items);
            }

        }
        return bulkScanValidationResponse;
    }

    private void isMiamAttended(Map<String, String> ocrDataFieldsMap,
                                BulkScanValidationResponse bulkScanValidationResponse,
                                List<String> items) {
        if (ocrDataFieldsMap.containsKey(ATTENDED_MIAM)
            && StringUtils.hasText(ocrDataFieldsMap.get(ATTENDED_MIAM))
            && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))
            && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(ATTENDED_MIAM)
        )) {
            items.add(String.format(
                MANDATORY_ATTENDED_MIAM_MESSAGE,
                ATTENDED_MIAM
            ));
            bulkScanValidationResponse.setStatus(Status.ERRORS);
        }
    }

    private void setErrorMsg(BulkScanValidationResponse bulkScanValidationResponse,
                                 Map<String, String> ocrDataFieldsMap,
                                 List<String> items,String field) {
        if (!StringUtils.hasText(ocrDataFieldsMap.get(
                field))) {

            items.add(String.format(
                    MANDATORY_ERROR_MESSAGE,
                    field
                ));

            bulkScanValidationResponse.setStatus(Status.ERRORS);
        }
    }

    private Map<String, String> getOcrDataFieldsMap(List<OcrDataField> ocrDataFields) {
        return ocrDataFields
                .stream()
                .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));
    }
}
