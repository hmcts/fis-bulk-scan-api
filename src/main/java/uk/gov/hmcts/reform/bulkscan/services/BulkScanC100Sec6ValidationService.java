package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICATION_TIMETABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.EITHER_SECTION_6_A_OR_6_B_SHOULD_BE_BE_FILLED_UP_NOT_BOTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NEITHER_6A_NOR_6B_HAS_BEEN_FILLED_UP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.ORDER_DIRECTION_SOUGHT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.REASON_FOR_CONSIDERATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.RESPONDENT_EFFORT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.URGENCY_REASON;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.URGENT_OR_WITHOUT_HEARING;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUT_NOTICE_FRUSTRATE_THE_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;

/** This class will do custom validation for c100 form sections 6 to 14. */
@Service
public class BulkScanC100Sec6ValidationService implements BulkScanSectionValidationService {

    /**
     * This method will validate C100 form, section 2. data.
     *
     * @param bulkScanValidationRequest request payload
     * @param bulkScanValidationResponse response payload (errors/warnings)
     */
    @Override
    public void validate(
            BulkScanValidationRequest bulkScanValidationRequest,
            BulkScanValidationResponse bulkScanValidationResponse) {
        Map<String, String> ocrDataFieldsMap =
                this.getOcrDataFieldAsMap(bulkScanValidationRequest.getOcrdatafields());
        List<String> errorItemList = bulkScanValidationResponse.getErrors().getItems();
        if (null != ocrDataFieldsMap
                && !ocrDataFieldsMap.isEmpty()
                && ocrDataFieldsMap.containsKey(URGENT_OR_WITHOUT_HEARING)
                && BooleanUtils.YES.equalsIgnoreCase(
                        ocrDataFieldsMap.get(URGENT_OR_WITHOUT_HEARING))) {
            List<String> section6aNonEmpty = getSection6aNonEmptyFields(ocrDataFieldsMap);
            List<String> section6bNonEmpty = getSection6bNonEmptyFields(ocrDataFieldsMap);
            if (!section6aNonEmpty.isEmpty() && !section6bNonEmpty.isEmpty()) {
                errorItemList.add(EITHER_SECTION_6_A_OR_6_B_SHOULD_BE_BE_FILLED_UP_NOT_BOTH);
            } else if (section6aNonEmpty.isEmpty() && section6bNonEmpty.isEmpty()) {
                errorItemList.add(NEITHER_6A_NOR_6B_HAS_BEEN_FILLED_UP);
            } else if (!section6aNonEmpty.isEmpty()) {
                validateSection6a(ocrDataFieldsMap, errorItemList);
            } else if (!section6bNonEmpty.isEmpty()) {
                validateSection6b(ocrDataFieldsMap, errorItemList);
            }
        }
    }

    private void validateSection6b(
            Map<String, String> ocrDataFieldsMap, List<String> errorItemList) {
        if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER)
                && BooleanUtils.YES.equalsIgnoreCase(
                        ocrDataFieldsMap.get(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER))
                && !ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON)) {
            errorItemList.add(
                    String.format(
                            MISSING_FIELD_MESSAGE, WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON));
        } else if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER)
                && BooleanUtils.YES.equalsIgnoreCase(
                        ocrDataFieldsMap.get(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER))
                && StringUtils.isEmpty(
                        ocrDataFieldsMap.get(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON))) {
            errorItemList.add(
                    String.format(
                            MANDATORY_ERROR_MESSAGE, WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON));
        }

        if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE)
                && BooleanUtils.YES.equalsIgnoreCase(
                        ocrDataFieldsMap.get(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE))
                && !ocrDataFieldsMap.containsKey(
                        WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS)) {
            errorItemList.add(
                    String.format(
                            MISSING_FIELD_MESSAGE,
                            WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS));
        } else if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE)
                && BooleanUtils.YES.equalsIgnoreCase(
                        ocrDataFieldsMap.get(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE))
                && StringUtils.isEmpty(
                        ocrDataFieldsMap.get(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS))) {
            errorItemList.add(
                    String.format(
                            MANDATORY_ERROR_MESSAGE,
                            WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS));
        }
    }

    private void validateSection6a(
            Map<String, String> ocrDataFieldsMap, List<String> errorItemList) {
        if (!ocrDataFieldsMap.containsKey(ORDER_DIRECTION_SOUGHT)) {
            errorItemList.add(String.format(MISSING_FIELD_MESSAGE, ORDER_DIRECTION_SOUGHT));
        } else if (StringUtils.isEmpty(ocrDataFieldsMap.get(ORDER_DIRECTION_SOUGHT))) {
            errorItemList.add(String.format(MANDATORY_ERROR_MESSAGE, ORDER_DIRECTION_SOUGHT));
        }

        if (!ocrDataFieldsMap.containsKey(URGENCY_REASON)) {
            errorItemList.add(String.format(MISSING_FIELD_MESSAGE, URGENCY_REASON));
        } else if (StringUtils.isEmpty(ocrDataFieldsMap.get(URGENCY_REASON))) {
            errorItemList.add(String.format(MANDATORY_ERROR_MESSAGE, URGENCY_REASON));
        }
    }

    private List<String> getSection6aNonEmptyFields(Map<String, String> ocrDataFieldsMap) {
        List<String> section6aNonEmptyFieldList = new ArrayList<>();
        if (ocrDataFieldsMap.containsKey(ORDER_DIRECTION_SOUGHT)) {
            section6aNonEmptyFieldList.add(ORDER_DIRECTION_SOUGHT);
        }
        if (ocrDataFieldsMap.containsKey(URGENCY_REASON)) {
            section6aNonEmptyFieldList.add(URGENCY_REASON);
        }
        if (ocrDataFieldsMap.containsKey(APPLICATION_TIMETABLE)) {
            section6aNonEmptyFieldList.add(APPLICATION_TIMETABLE);
        }
        if (ocrDataFieldsMap.containsKey(RESPONDENT_EFFORT)) {
            section6aNonEmptyFieldList.add(RESPONDENT_EFFORT);
        }
        return section6aNonEmptyFieldList;
    }

    private List<String> getSection6bNonEmptyFields(Map<String, String> ocrDataFieldsMap) {
        List<String> section6bNonEmptyFieldList = new ArrayList<>();
        if (ocrDataFieldsMap.containsKey(REASON_FOR_CONSIDERATION)) {
            section6bNonEmptyFieldList.add(REASON_FOR_CONSIDERATION);
        }
        if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE)) {
            section6bNonEmptyFieldList.add(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE);
        }
        if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS)) {
            section6bNonEmptyFieldList.add(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS);
        }
        if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER)) {
            section6bNonEmptyFieldList.add(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER);
        }
        if (ocrDataFieldsMap.containsKey(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON)) {
            section6bNonEmptyFieldList.add(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON);
        }
        return section6bNonEmptyFieldList;
    }
}
