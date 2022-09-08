package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_RELATIONSHIP_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_RELATIONSHIP_NONE_ABOVE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.VALID_DATE_WARNING_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

@Service
public class BulkScanFL401ValidationService {
    private static final int NO_APPLICANT_RESPONDENT_RELATIONSHIP_COUNT = 0;
    private static final int EXPECTED_APPLICANT_RESPONDENT_RELATIONSHIP_COUNT = 1;

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
            final Map<String, String> ocrPryApplicantRespondentRelationshipFieldsMap =
                    new TreeMap<>(
                            getApplicantRespondentRelationshipFieldsMap(
                                    ocrDataFieldsMap, APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS));
            final Map<String, String> ocrSecApplicantRespondentRelationshipFieldsMap =
                    new TreeMap<>(
                            getApplicantRespondentRelationshipFieldsMap(
                                    ocrDataFieldsMap,
                                    APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS));

            List<String> warningItems = new ArrayList<>();
            List<String> errorItems = new ArrayList<>();

            setApplicantRespondentErrorWarningMsg(
                    bulkScanValidationResponse,
                    warningItems,
                    errorItems,
                    "4.1",
                    applicantRespondentRelationshipCounter(
                            ocrPryApplicantRespondentRelationshipFieldsMap));

            if (ocrDataFieldsMap.containsKey(APPLICANT_RESPONDENT_RELATIONSHIP_NONE_ABOVE)
                    && ocrDataFieldsMap
                            .get(APPLICANT_RESPONDENT_RELATIONSHIP_NONE_ABOVE)
                            .equalsIgnoreCase(YES)) {

                setApplicantRespondentErrorWarningMsg(
                        bulkScanValidationResponse,
                        warningItems,
                        errorItems,
                        "4.4",
                        applicantRespondentRelationshipCounter(
                                ocrSecApplicantRespondentRelationshipFieldsMap));

                bulkScanValidationResponse.addErrors(
                        validateInputDate(
                                ocrDataFieldsMap,
                                "applicantRespondent_Relationship_StartDate",
                                APPLICANT_RESPONDENT_RELATIONSHIP_DATE,
                                "Start"));
                bulkScanValidationResponse.addWarning(
                        validateInputDate(
                                ocrDataFieldsMap,
                                "applicantRespondent_Relationship_EndDate",
                                APPLICANT_RESPONDENT_RELATIONSHIP_DATE,
                                "End"));
                bulkScanValidationResponse.addWarning(
                        validateInputDate(
                                ocrDataFieldsMap,
                                "applicantRespondent_PreviousMarried_Date",
                                APPLICANT_RESPONDENT_RELATIONSHIP_DATE,
                                "Previous Married"));
            }
        }

        bulkScanValidationResponse.changeStatus();

        return bulkScanValidationResponse;
    }

    private void setApplicantRespondentErrorWarningMsg(
            BulkScanValidationResponse bulkScanValidationResponse,
            List<String> warningItems,
            List<String> errorItems,
            String sectionName,
            int relationshipCounter) {
        if (relationshipCounter > EXPECTED_APPLICANT_RESPONDENT_RELATIONSHIP_COUNT) {
            setErrorWarningMsg(
                    bulkScanValidationResponse,
                    warningItems,
                    String.format(
                            "Section %s - Applicant has more than one relationship with the"
                                    + " respondent",
                            sectionName),
                    Status.WARNINGS);
        }

        if (relationshipCounter == NO_APPLICANT_RESPONDENT_RELATIONSHIP_COUNT) {
            setErrorWarningMsg(
                    bulkScanValidationResponse,
                    errorItems,
                    String.format(
                            "Section %s - Applicant must have a relationship with the "
                                    + "respondent",
                            sectionName),
                    Status.ERRORS);
        }
    }

    private int applicantRespondentRelationshipCounter(
            Map<String, String> ocrApplicantRespondentFieldsMap) {
        int relationshipCounter = 0;

        if (null != ocrApplicantRespondentFieldsMap && !ocrApplicantRespondentFieldsMap.isEmpty()) {
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

    private List<String> validateInputDate(
            Map<String, String> ocrDataFieldsMap,
            String fieldName,
            String message,
            String fieldSummaryDescr) {

        if (null != ocrDataFieldsMap
                && !ocrDataFieldsMap.isEmpty()
                && ocrDataFieldsMap.containsKey(fieldName)
                && StringUtils.hasText(ocrDataFieldsMap.get(fieldName))) {
            return validateDate(
                    Objects.requireNonNull(ocrDataFieldsMap.get(fieldName)),
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
