package uk.gov.hmcts.reform.bulkscan.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
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
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PREVIOUS_OR_ONGOING_PROCEEDING;

/**
 * This class will hold utility method related to
 * C100 form custom validation
 */
@Service
public class BulkScanC100ValidationService {

    /**
     * This method will validate C100 form, section 2
     * data.
     * @param ocrDataFields
     * @param bulkScanValidationResponse
     * @return
     */
    public static BulkScanValidationResponse validateRequiremntToAttendMiam(List<OcrDataField> ocrDataFields,
                                                      BulkScanValidationResponse bulkScanValidationResponse) {

        Map<String, String> ocrDataFieldsMap = ocrDataFields
            .stream()
            .collect(Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

//        List<String> fieldsToValidate = List.of(EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER,
//                                                 EXEMPTION_TO_ATTEND_MIAM, FAMILY_MEMBER_INTIMATION_ON_NO_MIAM,
//                                                 ATTENDED_MIAM);


        if(null != ocrDataFieldsMap && !ocrDataFieldsMap.isEmpty()) {

            boolean isError = false;

            List<String> items = bulkScanValidationResponse.getErrors()
                .getItems();

            if (ocrDataFieldsMap.containsKey(PREVIOUS_OR_ONGOING_PROCEEDING)
                && StringUtils.hasText(ocrDataFieldsMap.get(PREVIOUS_OR_ONGOING_PROCEEDING))
                && BulkScanConstants.YES.equalsIgnoreCase(ocrDataFieldsMap.get(PREVIOUS_OR_ONGOING_PROCEEDING))) {
                if (!StringUtils.hasText(ocrDataFieldsMap.get(
                    EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER))) {

                    items.add(String.format(
                        MANDATORY_ERROR_MESSAGE,
                        EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER
                    ));

                    isError = true;
                }
            }

            if (ocrDataFieldsMap.containsKey(EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER)
                && StringUtils.hasText(ocrDataFieldsMap.get(
                EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER))
                && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(
                EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER))) {

                if (!StringUtils.hasText(ocrDataFieldsMap.get(
                    EXEMPTION_TO_ATTEND_MIAM))) {

                    items.add(String.format(MANDATORY_ERROR_MESSAGE, EXEMPTION_TO_ATTEND_MIAM));
                    isError = true;
                }
            }

            if (ocrDataFieldsMap.containsKey(EXEMPTION_TO_ATTEND_MIAM)
                && StringUtils.hasText(ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))
                && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))) {

                if (!StringUtils.hasText(ocrDataFieldsMap.get(
                    FAMILY_MEMBER_INTIMATION_ON_NO_MIAM))) {

                    items.add(String.format(MANDATORY_ERROR_MESSAGE, FAMILY_MEMBER_INTIMATION_ON_NO_MIAM));
                    isError = true;
                }
            }

            if (ocrDataFieldsMap.containsKey(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM)
                && StringUtils.hasText(ocrDataFieldsMap.get(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM))
                && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM)
            )) {

                if (!StringUtils.hasText(ocrDataFieldsMap.get(
                    ATTENDED_MIAM))) {

                    items.add(String.format(MANDATORY_ERROR_MESSAGE, ATTENDED_MIAM));
                    isError = true;
                }
            }

            if (isError) {
                bulkScanValidationResponse.setStatus(Status.ERRORS);
            }
        }
        return bulkScanValidationResponse;
    }
}
