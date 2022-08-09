package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_ONE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_ONE_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_TWO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_TWO_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ASKING_PERMISSION_FOR_APPLICATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_OF_SAME_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_PARENTS_NAME_COLLECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN_SOCIAL_AUTHORITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.HAS_APPLICANT_ONE_LIVED_AT_THIS_ADDRESS_FOR_OVER_FIVE_YEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.HAS_APPLICANT_TWO_LIVED_AT_THIS_ADDRESS_FOR_OVER_FIVE_YEARS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ATTENDED_MIAM_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED_REASON;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.ATTENDED_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.EXEMPTION_TO_ATTEND_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.FAMILY_MEMBER_INTIMATION_ON_NO_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.PREVIOUS_OR_ONGOING_PROCEEDING;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;

/** This class will custom validation methods related to. C100 form custom validation */
@SuppressWarnings("PMD")
@Service
public class BulkScanC100ValidationService extends BulkScanC100OtherSectionValidationService {

    /**
     * This method will validate C100 form, section 2. data.
     *
     * @param ocrDataFields represents request payload
     * @param bulkScanValidationResponse is used to add error/warnings
     * @return BulkScanValidationResponse object
     */
    public BulkScanValidationResponse validateAttendMiam(
            List<OcrDataField> ocrDataFields,
            BulkScanValidationResponse bulkScanValidationResponse) {

        Map<String, String> ocrDataFieldsMap = getOcrDataFieldsMap(ocrDataFields);

        if (null != ocrDataFieldsMap && !ocrDataFieldsMap.isEmpty()) {

            List<String> items = bulkScanValidationResponse.getErrors().getItems();

            if (ocrDataFieldsMap.containsKey(PREVIOUS_OR_ONGOING_PROCEEDING)
                    && StringUtils.hasText(ocrDataFieldsMap.get(PREVIOUS_OR_ONGOING_PROCEEDING))
                    && BulkScanConstants.YES.equalsIgnoreCase(
                            ocrDataFieldsMap.get(PREVIOUS_OR_ONGOING_PROCEEDING))) {
                setErrorMsg(
                        bulkScanValidationResponse,
                        ocrDataFieldsMap,
                        items,
                        EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER);
            }

            if (ocrDataFieldsMap.containsKey(
                            EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER)
                    && StringUtils.hasText(
                            ocrDataFieldsMap.get(
                                    EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER))
                    && BulkScanConstants.NO.equalsIgnoreCase(
                            ocrDataFieldsMap.get(
                                    EXISTING_CASE_ON_EMERGENCY_PROTECTION_CARE_OR_SUPERVISION_ORDER))) {
                setErrorMsg(
                        bulkScanValidationResponse,
                        ocrDataFieldsMap,
                        items,
                        EXEMPTION_TO_ATTEND_MIAM);
            }

            if (ocrDataFieldsMap.containsKey(EXEMPTION_TO_ATTEND_MIAM)
                    && StringUtils.hasText(ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))
                    && BulkScanConstants.NO.equalsIgnoreCase(
                            ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))) {
                setErrorMsg(
                        bulkScanValidationResponse,
                        ocrDataFieldsMap,
                        items,
                        FAMILY_MEMBER_INTIMATION_ON_NO_MIAM);
            }

            if (ocrDataFieldsMap.containsKey(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM)
                    && StringUtils.hasText(
                            ocrDataFieldsMap.get(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM))
                    && BulkScanConstants.NO.equalsIgnoreCase(
                            ocrDataFieldsMap.get(FAMILY_MEMBER_INTIMATION_ON_NO_MIAM))) {
                setErrorMsg(bulkScanValidationResponse, ocrDataFieldsMap, items, ATTENDED_MIAM);

                isMiamAttended(ocrDataFieldsMap, bulkScanValidationResponse, items);
            }
        }
        return bulkScanValidationResponse;
    }

    private void isMiamAttended(
            Map<String, String> ocrDataFieldsMap,
            BulkScanValidationResponse bulkScanValidationResponse,
            List<String> items) {
        if (ocrDataFieldsMap.containsKey(ATTENDED_MIAM)
                && StringUtils.hasText(ocrDataFieldsMap.get(ATTENDED_MIAM))
                && BulkScanConstants.NO.equalsIgnoreCase(
                        ocrDataFieldsMap.get(EXEMPTION_TO_ATTEND_MIAM))
                && BulkScanConstants.NO.equalsIgnoreCase(ocrDataFieldsMap.get(ATTENDED_MIAM))) {
            items.add(String.format(MANDATORY_ATTENDED_MIAM_MESSAGE, ATTENDED_MIAM));
            bulkScanValidationResponse.setStatus(Status.ERRORS);
        }
    }

    private void setErrorMsg(
            BulkScanValidationResponse bulkScanValidationResponse,
            Map<String, String> ocrDataFieldsMap,
            List<String> items,
            String field) {
        if (!StringUtils.hasText(ocrDataFieldsMap.get(field))) {

            items.add(String.format(MANDATORY_ERROR_MESSAGE, field));

            bulkScanValidationResponse.setStatus(Status.ERRORS);
        }
    }

    /**
     * if Asking permission for application is selected as yes then permission reqiured reson. in
     * section 5 is mandatory.
     */
    public List<String> doPermissionRelatedFieldValidation(Map<String, String> inputFieldsMap) {
        List<String> errors = new ArrayList<>();
        if (null != inputFieldsMap.get(ASKING_PERMISSION_FOR_APPLICATION)
                && YES.equalsIgnoreCase(inputFieldsMap.get(ASKING_PERMISSION_FOR_APPLICATION))
                && null == inputFieldsMap.get(PERMISSION_REQUIRED_REASON)) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, PERMISSION_REQUIRED_REASON));
        }
        return errors;
    }

    /**
     * 1. Checking if any one child living with option should be available 2. if Child same parent
     * is yes then parent name should have value 3. if Child same parent is no then parent
     * collection name should have value 4. if social authority is yes then local authority or
     * social worker field should have value
     */
    List<String> doChildRelatedValidation(Map<String, String> inputFieldsMap) {
        List<String> errors = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(
                        inputFieldsMap.get(CHILD_LIVING_WITH_APPLICANT))
                && org.apache.commons.lang3.StringUtils.isEmpty(
                        inputFieldsMap.get(CHILD_LIVING_WITH_RESPONDENT))
                && org.apache.commons.lang3.StringUtils.isEmpty(
                        inputFieldsMap.get(CHILD_LIVING_WITH_OTHERS))) {
            errors.add(
                    String.format(
                            XOR_CONDITIONAL_FIELDS_MESSAGE,
                            CHILD_LIVING_WITH_APPLICANT
                                    .concat(",")
                                    .concat(CHILD_LIVING_WITH_RESPONDENT)
                                    .concat(",")
                                    .concat(CHILD_LIVING_WITH_OTHERS)));
        }

        if (YES.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_OF_SAME_PARENT))
                && org.apache.commons.lang3.StringUtils.isEmpty(
                        inputFieldsMap.get(CHILDREN_PARENTS_NAME))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILDREN_PARENTS_NAME));
        }

        if (NO.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_OF_SAME_PARENT))
                && org.apache.commons.lang3.StringUtils.isEmpty(
                        inputFieldsMap.get(CHILDREN_PARENTS_NAME_COLLECTION))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILDREN_PARENTS_NAME_COLLECTION));
        }

        if (YES.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_SOCIAL_AUTHORITY))
                && org.apache.commons.lang3.StringUtils.isEmpty(
                        inputFieldsMap.get(CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER))) {
            errors.add(
                    String.format(MANDATORY_ERROR_MESSAGE, CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER));
        }

        return errors;
    }

    private Map<String, String> getOcrDataFieldsMap(List<OcrDataField> ocrDataFields) {
        return null != ocrDataFields
                ? ocrDataFields.stream()
                        .collect(
                                Collectors.toMap(
                                        ocrDataField -> ocrDataField.getName(),
                                        ocrDataField ->
                                                ocrDataField.getValue() == null
                                                        ? org.apache.commons.lang3.StringUtils.EMPTY
                                                        : ocrDataField.getValue()))
                : null;
    }

    public BulkScanValidationResponse validateApplicantAddressFiveYears(
            List<OcrDataField> ocrDataFields,
            BulkScanValidationResponse bulkScanValidationResponse) {

        Map<String, String> ocrDataFieldsMap = getOcrDataFieldsMap(ocrDataFields);

        if (null != ocrDataFieldsMap && !ocrDataFieldsMap.isEmpty()) {

            List<String> items = new ArrayList<>();

            if (ocrDataFieldsMap.containsKey(
                            HAS_APPLICANT_ONE_LIVED_AT_THIS_ADDRESS_FOR_OVER_FIVE_YEARS)
                    && ocrDataFieldsMap
                            .get(HAS_APPLICANT_ONE_LIVED_AT_THIS_ADDRESS_FOR_OVER_FIVE_YEARS)
                            .equalsIgnoreCase(NO)
                    && ocrDataFieldsMap.containsKey(APPLICANT_ONE_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS)
                    && !StringUtils.hasText(
                            ocrDataFieldsMap.get(
                                    APPLICANT_ONE_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS))) {
                items.add(
                        String.format(
                                ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE,
                                APPLICANT_ONE,
                                APPLICANT_ONE_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS));
            }

            if (ocrDataFieldsMap.containsKey(
                            HAS_APPLICANT_TWO_LIVED_AT_THIS_ADDRESS_FOR_OVER_FIVE_YEARS)
                    && ocrDataFieldsMap
                            .get(HAS_APPLICANT_TWO_LIVED_AT_THIS_ADDRESS_FOR_OVER_FIVE_YEARS)
                            .equalsIgnoreCase(NO)
                    && ocrDataFieldsMap.get(
                                    HAS_APPLICANT_TWO_LIVED_AT_THIS_ADDRESS_FOR_OVER_FIVE_YEARS)
                            != null
                    && ocrDataFieldsMap.containsKey(APPLICANT_TWO_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS)
                    && !StringUtils.hasText(
                            ocrDataFieldsMap.get(
                                    APPLICANT_TWO_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS))) {
                items.add(
                        String.format(
                                ADDRESS_NOT_LIVED_FOR_FIVE_YEARS_MESSAGE,
                                APPLICANT_TWO,
                                APPLICANT_ONE_ALL_ADDRESSES_FOR_FIVE_LAST_YEARS));
            }

            if (!items.isEmpty()) {
                bulkScanValidationResponse.addWarning(items);
            }
        }
        return bulkScanValidationResponse;
    }
}
