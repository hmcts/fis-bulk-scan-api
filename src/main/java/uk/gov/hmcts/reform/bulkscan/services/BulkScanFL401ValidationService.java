package uk.gov.hmcts.reform.bulkscan.services;

import static org.springframework.util.StringUtils.hasText;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_HAS_MORETHAN_ONE_RELATIONSHIP_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_MUST_HAVE_RELATIONSHIP_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_RELATIONSHIP_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICATION_FOR_YOUR_FAMILY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICATION_FOR_YOU_ONLY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.EXPECTED_APPLICANT_RESPONDENT_RELATIONSHIP_MINIMUM_COUNT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FIELD_SUMMARY_END;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FIELD_SUMMARY_PREVIOUS_MARRIED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FIELD_SUMMARY_START;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_RELATIONSHIP_TO_RESPONDENT_SECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_RESPONDENT_RELATIONSHIP_TO_YOU_SECTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.NEED_FOR_PARENTAL_RESPONSIBILITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.NO_APPLICANT_RESPONDENT_RELATIONSHIP_COUNT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.VALID_DATE_WARNING_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.WHO_IS_APPLICATION_FOR_S5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

@Service
public class BulkScanFL401ValidationService {

    /**
     * This method will validate FL401 form, section 4. data.
     *
     * @param ocrDataFieldsMap represents Map of payload fields and values
     * @param bulkScanValidationResponse is used to add error/warnings
     * @return BulkScanValidationResponse object
     */
    public BulkScanValidationResponse validateApplicantRespondentRelationhip(
            Map<String, String> ocrDataFieldsMap,
            BulkScanValidationResponse bulkScanValidationResponse) {

        if (null != ocrDataFieldsMap && !ocrDataFieldsMap.isEmpty()) {
            validateRelationships(ocrDataFieldsMap, bulkScanValidationResponse);
        }

        bulkScanValidationResponse.changeStatus();

        return bulkScanValidationResponse;
    }

    private void validateRelationships(Map<String, String> ocrDataFieldsMap,
                                       BulkScanValidationResponse bulkScanValidationResponse) {
        final Map<String, String> ocrPryApplicantRespondentRelationshipFieldsMap =
                new TreeMap<>(
                        getApplicantRespondentRelationshipFieldsMap(
                            ocrDataFieldsMap, APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS));
        List<String> warningItems = new ArrayList<>();
        List<String> errorItems = new ArrayList<>();

        setApplicantRespondentErrorWarningMsg(
            bulkScanValidationResponse,
                warningItems,
                errorItems,
                FL401_RELATIONSHIP_TO_RESPONDENT_SECTION,
                applicantRespondentRelationshipCounter(
                        ocrPryApplicantRespondentRelationshipFieldsMap));

        if (ocrDataFieldsMap.containsKey(APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD)
                && ocrDataFieldsMap
                        .get(APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD)
                        .equalsIgnoreCase(YES)) {
            final Map<String, String> ocrSecApplicantRespondentRelationshipFieldsMap =
                new TreeMap<>(
                    getApplicantRespondentRelationshipFieldsMap(
                        ocrDataFieldsMap,
                        APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS));

            warningItems = new ArrayList<>();
            errorItems = new ArrayList<>();

            setApplicantRespondentErrorWarningMsg(
                bulkScanValidationResponse,
                    warningItems,
                    errorItems,
                    FL401_RESPONDENT_RELATIONSHIP_TO_YOU_SECTION,
                    applicantRespondentRelationshipCounter(
                            ocrSecApplicantRespondentRelationshipFieldsMap));
            String startDate = buildDate(
                ocrDataFieldsMap.get("relationship_Start_DD"),
                ocrDataFieldsMap.get("relationship_Start_MM"),
                ocrDataFieldsMap.get("relationship_Start_YYYY"));
            String endDate = buildDate(
                ocrDataFieldsMap.get("relationship_End_DD"),
                ocrDataFieldsMap.get("relationship_End_MM"),
                ocrDataFieldsMap.get("relationship_End_YYYY"));
            String previosMarriedDate = buildDate(
                ocrDataFieldsMap.get("relationship_PreviousMarried_DD"),
                ocrDataFieldsMap.get("relationship_PreviousMarried_MM"),
                ocrDataFieldsMap.get("relationship_PreviousMarried_YYYY"));
            bulkScanValidationResponse.addErrors(
                    validateInputDateWithFieldSummary(
                            startDate,
                            APPLICANT_RESPONDENT_RELATIONSHIP_DATE,
                            FIELD_SUMMARY_START));
            bulkScanValidationResponse.addWarning(
                validateInputDateWithFieldSummary(
                    endDate,
                    APPLICANT_RESPONDENT_RELATIONSHIP_DATE,
                    FIELD_SUMMARY_END));
            bulkScanValidationResponse.addWarning(
                validateInputDateWithFieldSummary(
                    previosMarriedDate,
                    APPLICANT_RESPONDENT_RELATIONSHIP_DATE,
                    FIELD_SUMMARY_PREVIOUS_MARRIED));
        }
    }

    /**
     * This method will validate section 5.1. Choice must be made, Yes for one. This method makes
     * sure both are not yes, or no.
     *
     * @param inputFieldMap as input
     * @return warning
     */
    public List<String> validateJustYouOrYouAndFamilySectionFive(
            Map<String, String> inputFieldMap) {

        String needForParentalResponsiblity = inputFieldMap.get(NEED_FOR_PARENTAL_RESPONSIBILITY);
        String justYou = inputFieldMap.get(APPLICATION_FOR_YOU_ONLY);
        String youAndFamily = inputFieldMap.get(APPLICATION_FOR_YOUR_FAMILY);
        List<String> warningLst = new ArrayList<>();

        if (hasText(needForParentalResponsiblity)
                && needForParentalResponsiblity.equalsIgnoreCase(YES)) {
            if (hasText(justYou)
                    && !justYou.equalsIgnoreCase(YES)
                    && hasText(youAndFamily)
                    && !youAndFamily.equalsIgnoreCase(YES)) {
                warningLst.add(String.format(MISSING_FIELD_MESSAGE, WHO_IS_APPLICATION_FOR_S5));

            } else if (hasText(justYou)
                    && justYou.equalsIgnoreCase(YES)
                    && hasText(youAndFamily)
                    && youAndFamily.equalsIgnoreCase(YES)) {
                warningLst.add(String.format(MISSING_FIELD_MESSAGE, WHO_IS_APPLICATION_FOR_S5));
            }
        }
        return warningLst;
    }

    public String buildDate(String dd, String mm, String yyyy) {
        if (StringUtils.isBlank(dd) || StringUtils.isBlank(mm) || StringUtils.isBlank(yyyy)) {
            return null;
        }
        return dd + "/" + mm + "/" + yyyy;
    }

    private void setApplicantRespondentErrorWarningMsg(
            BulkScanValidationResponse bulkScanValidationResponse,
            List<String> warningItems,
            List<String> errorItems,
            String sectionName,
            int relationshipCounter) {
        if (relationshipCounter > EXPECTED_APPLICANT_RESPONDENT_RELATIONSHIP_MINIMUM_COUNT) {
            setErrorWarningMsg(
                    bulkScanValidationResponse,
                    warningItems,
                    String.format(APPLICANT_HAS_MORETHAN_ONE_RELATIONSHIP_MESSAGE, sectionName),
                    Status.WARNINGS);
        }

        if (relationshipCounter == NO_APPLICANT_RESPONDENT_RELATIONSHIP_COUNT) {
            setErrorWarningMsg(
                    bulkScanValidationResponse,
                    errorItems,
                    String.format(APPLICANT_MUST_HAVE_RELATIONSHIP_MESSAGE, sectionName),
                    Status.ERRORS);
        }
    }

    private int applicantRespondentRelationshipCounter(
            Map<String, String> ocrApplicantRespondentFieldsMap) {
        int relationshipCounter = 0;

        if (null != ocrApplicantRespondentFieldsMap) {
            for (Map.Entry<String, String> applicantRespondentRelationshipField :
                    ocrApplicantRespondentFieldsMap.entrySet()) {
                if (isValidRelationshipApplicantRespondent(
                        ocrApplicantRespondentFieldsMap,
                        applicantRespondentRelationshipField.getKey(),
                        YES)) {
                    relationshipCounter++;
                }
            }
        }

        return relationshipCounter;
    }

    private boolean isValidRelationshipApplicantRespondent(
            Map<String, String> ocrApplicantRespondentFieldsMap,
            String fieldName,
            String fieldValidationVal) {

        return ocrApplicantRespondentFieldsMap.containsKey(fieldName)
                && ocrApplicantRespondentFieldsMap
                        .get(fieldName)
                        .equalsIgnoreCase(fieldValidationVal);
    }

    private void setErrorWarningMsg(
            BulkScanValidationResponse bulkScanValidationResponse,
            List<String> items,
            String fieldMsg,
            Status status) {

        if (status.equals(Status.ERRORS)) {
            items.add(fieldMsg);
            bulkScanValidationResponse.addErrors(items);
            bulkScanValidationResponse.setStatus(status);
        } else if (status.equals(Status.WARNINGS)) {
            items.add(fieldMsg);
            bulkScanValidationResponse.addWarning(items);
            bulkScanValidationResponse.setStatus(status);
        }

        bulkScanValidationResponse.changeStatus();
    }

    private Map<String, String> getApplicantRespondentRelationshipFieldsMap(
            Map<String, String> ocrDataFields, String relationshipRegexField) {

        TreeMap<String, String> relationshipMap = new TreeMap<>();

        if (null != ocrDataFields && !ocrDataFields.isEmpty()) {
            for (Map.Entry<String, String> applRespRelationFieldMap : ocrDataFields.entrySet()) {
                if (applRespRelationFieldMap.getKey().matches(relationshipRegexField)) {
                    relationshipMap.put(
                            applRespRelationFieldMap.getKey(), applRespRelationFieldMap.getValue());
                }
            }
        }

        return relationshipMap;
    }

    private List<String> validateInputDateWithFieldSummary(
            String field,
            String message,
            String fieldSummaryDescr) {

        if (null != field) {
            return validateDate(
                    Objects.requireNonNull(field),
                    String.format(message, fieldSummaryDescr),
                    TEXT_AND_NUMERIC_MONTH_PATTERN);
        }

        return Collections.emptyList();
    }

    private List<String> validateDate(String dateFieldInput, String fieldName, String datePattern) {

        final boolean validateDate = DateUtil.validateDate(dateFieldInput, datePattern);

        if (!validateDate) {
            return List.of(String.format(VALID_DATE_WARNING_MESSAGE, fieldName));
        }
        return Collections.emptyList();
    }
}
