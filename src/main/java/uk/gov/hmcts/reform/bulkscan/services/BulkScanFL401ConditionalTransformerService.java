package uk.gov.hmcts.reform.bulkscan.services;

import static org.springframework.util.StringUtils.hasText;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_CHILD_RELATIONSHIP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_FAMILY_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_SHARE_PARENTAL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_FOR_YOUR_FAMILY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_FOR_YOU_ONLY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILDREN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_AGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FULL_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_ADDRESS_ROW_1;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_ADDRESS_ROW_2;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_ADDRESS_ROW_3;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_ADDRESS_ROW_4;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DOB;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DOES_APPLICANT_HAVE_CHILDREN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DO_CHILDREN_LIVE_ADDRESS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FAM_CHILD_DETAILS_ROW_1;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FAM_CHILD_DETAILS_ROW_2;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FAM_CHILD_DETAILS_ROW_3;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FAM_CHILD_DETAILS_ROW_4;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FL401_OTHER_PROCEEDINGS_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FULL_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NAME_OF_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ONGOING_COURT_PROCEEDING_ROW1;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ONGOING_COURT_PROCEEDING_ROW2;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ONGOING_COURT_PROCEEDING_ROW3;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_CHILD_LIVE_ADDRESS_ROW_1;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_CHILD_LIVE_ADDRESS_ROW_2;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_CHILD_LIVE_ADDRESS_ROW_3;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_CHILD_LIVE_ADDRESS_ROW_4;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_CHILD_RELATIONSHIP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_RESPONSIBLE_FOR_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TYPE_OF_CASE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.VALUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.ADDRESS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.ADDRESS_LINE1;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.ADDRESS_LINE2;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_ADDRESS_BUILDING_AND_STREET;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_ADDRESS_COUNTY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_ADDRESS_POSTCODE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_ADDRESS_SECOND_LINE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_ADDRESS_TOWN_OR_CITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_CONTACT_CONFIDENTIALITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_EMAIL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_PHONE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_PREFER_TO_BE_CONTACTED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_SOLICITOR_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLYING_FOR_NON_MOLES_STATION_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.ATTEND_HEARING_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.BAIL_CONDITION_END_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_APPLICANT_RELATIONSHIOP_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_RELATIONSHIP_DATE_COMPLEX_END_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_RELATIONSHIP_DATE_COMPLEX_START_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_RELATIONSHIP_TO_RESPONDENT_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.COMMA;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.COUNTY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DELIVERATELY_EVADING_SERVICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DETERREDOR_PREVENTED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.EMAIL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.EMPTY_SPACE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_APPLICANT_RELATIONSHIP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_APPLICANT_RELATIONSHIP_OPTIONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_APPLICANT_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_SOLICITOR_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.IS_ADDRESS_CONFIDENTIAL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.IS_EMAIL_ADDRESS_CONFIDENTIAL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.IS_PHONE_NUMBER_CONFIDENTIAL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.OTHERS_STOP_RESPONDENT_BEHAVIOUR_OPTIONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.PHONE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.POSTCODE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.POSTTOWN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.PREFER_TO_BE_CONTACTED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.REASON_FOR_ORDER_WITHOUT_GIVING_NOTICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.REPRESENTATIVE_FIRST_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.REPRESENTATIVE_LAST_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RISK_OF_SIGNIFICANT_HARM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.SPACE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.SPECIAL_MEASURE_AT_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.SPECIAL_MEASURE_AT_COURT_ROW_1;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.SPECIAL_MEASURE_AT_COURT_ROW_2;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.SPECIAL_MEASURE_AT_COURT_ROW_3;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.SPECIAL_MEASURE_AT_COURT_ROW_4;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.STOP_RESPONDENT_BEHAVIOUR_CHILD_OPTIONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.STOP_RESPONDENT_BEHAVIOUR_OPTIONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.STOP_RESPONDENT_BEHAVIOUR_OPTIONS_6;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.STOP_RESPONDENT_BEHAVIOUR_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TWO_DIGIT_MONTH_FORMAT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.WITHOUT_NOTICE_ORDER_TABLE;
import static uk.gov.hmcts.reform.bulkscan.enums.OrderWithouGivingNoticeReasonEnum.RISKOF_SIGNIFICANT_HARM;

import com.microsoft.applicationinsights.core.dependencies.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.enums.ApplicantRespondentRelationshipEnum;
import uk.gov.hmcts.reform.bulkscan.enums.FL401StopRespondentBehaviourChildEnum;
import uk.gov.hmcts.reform.bulkscan.enums.FL401StopRespondentEnum;
import uk.gov.hmcts.reform.bulkscan.enums.OrderWithouGivingNoticeReasonEnum;
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

@SuppressWarnings({"PMD", "unchecked"})
@Component
public class BulkScanFL401ConditionalTransformerService {

    public void transform(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {

        LinkedTreeMap withoutNoticeOrderTableMap =
                (LinkedTreeMap) populatedMap.get(WITHOUT_NOTICE_ORDER_TABLE);
        final String bailConditionEndDate =
                (String) withoutNoticeOrderTableMap.get(BAIL_CONDITION_END_DATE);

        withoutNoticeOrderTableMap.put(
                BAIL_CONDITION_END_DATE,
                DateUtil.transformDate(
                        bailConditionEndDate,
                        TEXT_AND_NUMERIC_MONTH_PATTERN,
                        TWO_DIGIT_MONTH_FORMAT));

        withoutNoticeOrderTableMap.put(
                REASON_FOR_ORDER_WITHOUT_GIVING_NOTICE,
                transformReasonForOrderWithoutGivingNotice(inputFieldsMap));

        LinkedTreeMap relationshipToRespondentTableMap =
                (LinkedTreeMap) populatedMap.get(CCD_RELATIONSHIP_TO_RESPONDENT_TABLE);

        final String relationshipDateComplexStartDate =
                (String)
                        relationshipToRespondentTableMap.get(
                                CCD_RELATIONSHIP_DATE_COMPLEX_START_DATE_FIELD);

        if (null != relationshipDateComplexStartDate
                && !relationshipDateComplexStartDate.isEmpty()) {
            relationshipToRespondentTableMap.put(
                    CCD_RELATIONSHIP_DATE_COMPLEX_START_DATE_FIELD,
                    DateUtil.transformDate(
                            relationshipDateComplexStartDate,
                            TEXT_AND_NUMERIC_MONTH_PATTERN,
                            TWO_DIGIT_MONTH_FORMAT));
        }
        final String relationshipDateComplexEndDate =
                (String)
                        relationshipToRespondentTableMap.get(
                                CCD_RELATIONSHIP_DATE_COMPLEX_END_DATE_FIELD);

        if (null != relationshipDateComplexEndDate && !relationshipDateComplexEndDate.isEmpty()) {
            relationshipToRespondentTableMap.put(
                    CCD_RELATIONSHIP_DATE_COMPLEX_END_DATE_FIELD,
                    DateUtil.transformDate(
                            relationshipDateComplexEndDate,
                            TEXT_AND_NUMERIC_MONTH_PATTERN,
                            TWO_DIGIT_MONTH_FORMAT));
        }
        final String applicantRelationshipDate =
                (String) relationshipToRespondentTableMap.get(CCD_APPLICANT_RELATIONSHIOP_DATE);

        if (null != applicantRelationshipDate && !applicantRelationshipDate.isEmpty()) {
            relationshipToRespondentTableMap.put(
                    CCD_APPLICANT_RELATIONSHIOP_DATE,
                    DateUtil.transformDate(
                            applicantRelationshipDate,
                            TEXT_AND_NUMERIC_MONTH_PATTERN,
                            TWO_DIGIT_MONTH_FORMAT));
        }

        final String applicantRelationship =
                (String) relationshipToRespondentTableMap.get(FL401_APPLICANT_RELATIONSHIP);

        if (null != applicantRelationship && !applicantRelationship.isEmpty()) {
            relationshipToRespondentTableMap.put(
                    FL401_APPLICANT_RELATIONSHIP,
                    getApplicantRelationships(
                            inputFieldsMap, APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS));
        }

        final String applicantRelationshipOptions =
                (String) relationshipToRespondentTableMap.get(FL401_APPLICANT_RELATIONSHIP_OPTIONS);

        if (null != applicantRelationshipOptions
                && !applicantRelationshipOptions.isEmpty()
                && null != inputFieldsMap.get(APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD)
                && inputFieldsMap
                        .get(APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD)
                        .equalsIgnoreCase(YES)) {
            relationshipToRespondentTableMap.put(
                    FL401_APPLICANT_RELATIONSHIP_OPTIONS,
                    getApplicantRelationships(
                            inputFieldsMap, APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS));
        } else {
            relationshipToRespondentTableMap.put(FL401_APPLICANT_RELATIONSHIP_OPTIONS, null);
        }

        LinkedTreeMap attendHearingTableMap =
                (LinkedTreeMap) populatedMap.get(ATTEND_HEARING_TABLE);

        attendHearingTableMap.put(
                SPECIAL_MEASURE_AT_COURT, getFormattedSpecialMeasureAtCourt(inputFieldsMap));
        LinkedTreeMap respondentBehaviourTable =
                (LinkedTreeMap) populatedMap.get(STOP_RESPONDENT_BEHAVIOUR_TABLE);

        if (StringUtils.hasText(inputFieldsMap.get(APPLYING_FOR_NON_MOLES_STATION_ORDER))
                && inputFieldsMap.get(APPLYING_FOR_NON_MOLES_STATION_ORDER).equalsIgnoreCase(YES)) {
            respondentBehaviourTable.put(
                    STOP_RESPONDENT_BEHAVIOUR_OPTIONS,
                    getRespondentBehaviourOptions(inputFieldsMap));
            respondentBehaviourTable.put(
                    STOP_RESPONDENT_BEHAVIOUR_CHILD_OPTIONS,
                    getRespondentBehaviourChildOptions(inputFieldsMap));
            respondentBehaviourTable.put(
                    OTHERS_STOP_RESPONDENT_BEHAVIOUR_OPTIONS,
                    inputFieldsMap.get(STOP_RESPONDENT_BEHAVIOUR_OPTIONS_6));
        }

        LinkedTreeMap fl401ApplicantTable = (LinkedTreeMap) populatedMap.get(FL401_APPLICANT_TABLE);

        String applicantContactConfidentiality =
                inputFieldsMap.get(APPLICANT_CONTACT_CONFIDENTIALITY);

        transformApplicantConfidentiality(fl401ApplicantTable, applicantContactConfidentiality);
        fl401ApplicantTable.put(
                ADDRESS,
                transformApplicantAddress(inputFieldsMap, applicantContactConfidentiality));
        transformContactDetails(
                inputFieldsMap, fl401ApplicantTable, applicantContactConfidentiality);

        LinkedTreeMap fl401SolicitorDetailsTable =
                (LinkedTreeMap) populatedMap.get(FL401_SOLICITOR_TABLE);
        transformSolicitorName(inputFieldsMap, fl401SolicitorDetailsTable);

        populatedMap.put(CHILDREN, buildTransformChild(populatedMap, inputFieldsMap));

        populatedMap.put(APPLICANT_FAMILY_TABLE, transformApplicantChildObjects(inputFieldsMap));

        populatedMap.put(FL401_OTHER_PROCEEDINGS_TABLE,transformOngoingFamilyCourtProceedings(inputFieldsMap));
    }

    private String getFormattedSpecialMeasureAtCourt(Map<String, String> inputFieldsMap) {
        String row1 = inputFieldsMap.get(SPECIAL_MEASURE_AT_COURT_ROW_1);
        String row2 = inputFieldsMap.get(SPECIAL_MEASURE_AT_COURT_ROW_2);
        String row3 = inputFieldsMap.get(SPECIAL_MEASURE_AT_COURT_ROW_3);
        String row4 = inputFieldsMap.get(SPECIAL_MEASURE_AT_COURT_ROW_4);
        String str =
                (row1 != null ? row1 + COMMA : SPACE)
                        + (row2 != null ? row2 + COMMA : SPACE)
                        + (row3 != null ? row3 + COMMA : SPACE)
                        + (row4 != null ? row4 : SPACE);
        return str;
    }

    private List buildTransformChild(
            Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        ArrayList<LinkedTreeMap> children = new ArrayList<>();

        final String row1 = inputFieldsMap.get(CHILD_LIVE_ADDRESS_ROW_1);
        final String row2 = inputFieldsMap.get(CHILD_LIVE_ADDRESS_ROW_2);
        final String row3 = inputFieldsMap.get(CHILD_LIVE_ADDRESS_ROW_3);
        final String row4 = inputFieldsMap.get(CHILD_LIVE_ADDRESS_ROW_4);
        final String otherChildrenRow1 = inputFieldsMap.get(OTHER_CHILD_LIVE_ADDRESS_ROW_1);
        final String otherChildrenRow2 = inputFieldsMap.get(OTHER_CHILD_LIVE_ADDRESS_ROW_2);
        final String otherChildrenRow3 = inputFieldsMap.get(OTHER_CHILD_LIVE_ADDRESS_ROW_3);
        final String otherChildrenRow4 = inputFieldsMap.get(OTHER_CHILD_LIVE_ADDRESS_ROW_4);

        if (row1 != null) {
            populatedMap.put(DO_CHILDREN_LIVE_ADDRESS, BooleanUtils.YES);
        }

        ArrayList<String> childInput = new ArrayList<>();
        childInput.add(row1);
        childInput.add(row2);
        childInput.add(row3);
        childInput.add(row4);
        childInput.add(otherChildrenRow1);
        childInput.add(otherChildrenRow2);
        childInput.add(otherChildrenRow3);
        childInput.add(otherChildrenRow4);

        for (String input : childInput) {
            LinkedTreeMap<String, String> childDetails = new LinkedTreeMap<>();

            final LinkedTreeMap<String, LinkedTreeMap<String, String>> childrenLinkedTreeMap =
                    new LinkedTreeMap();

            if (null != input) {
                final String[] columnDetails = input.split(",");

                final String childName = columnDetails[0];

                String childAge = null;
                String isRespondentResponsibleForChild = null;
                if (columnDetails.length > 2) {
                    childAge = columnDetails[1];
                    isRespondentResponsibleForChild = columnDetails[2];
                }

                childDetails.put(CHILD_FULL_NAME, childName);
                childDetails.put(CHILD_AGE, childAge);
                childDetails.put(
                    RESPONDENT_RESPONSIBLE_FOR_CHILD, isRespondentResponsibleForChild);

                childrenLinkedTreeMap.put(VALUE, childDetails);

                children.add(childrenLinkedTreeMap);
            }
        }
        return children;
    }

    private Map transformApplicantChildObjects(Map<String, String> inputFieldsMap){

        Map<String, Object> applicantChildMap = new HashMap<>();
        applicantChildMap.put(APPLICANT_CHILD, transformApplicantChild(inputFieldsMap));

        final String row1 = inputFieldsMap.get(APPLICATION_FOR_YOU_ONLY);
        final String row2 = inputFieldsMap.get(APPLICATION_FOR_YOUR_FAMILY);

        if(hasText(row1) && row1.equalsIgnoreCase(YES)){
            applicantChildMap.put(DOES_APPLICANT_HAVE_CHILDREN, YES);

        } else if (hasText(row2) && row2.equalsIgnoreCase(YES)) {
            applicantChildMap.put(DOES_APPLICANT_HAVE_CHILDREN, YES);
        }

        return applicantChildMap;
    }

    private List transformApplicantChild(Map<String, String> inputFieldsMap) {
        ArrayList<LinkedTreeMap> childrenDetails = new ArrayList<>();

        final String row1 = inputFieldsMap.get(FAM_CHILD_DETAILS_ROW_1);
        final String row2 = inputFieldsMap.get(FAM_CHILD_DETAILS_ROW_2);
        final String row3 = inputFieldsMap.get(FAM_CHILD_DETAILS_ROW_3);
        final String row4 = inputFieldsMap.get(FAM_CHILD_DETAILS_ROW_4);

        ArrayList<String> childInput = new ArrayList<>();
        childInput.add(row1);
        childInput.add(row2);
        childInput.add(row3);
        childInput.add(row4);

        for (String input : childInput) {
            LinkedTreeMap<String, String> childDetails = new LinkedTreeMap<>();

            final LinkedTreeMap<String, LinkedTreeMap<String, String>> childrenLinkedTreeMap =
                new LinkedTreeMap();

            if (null != input) {
                final String[] columnDetails = input.split(",");

                final String childName = columnDetails[0];

                String childDOB = null;
                String yourRelationshipWithChild = null;
                String doYouAndRespondentHaveParentalResponsibility = null;
                String respondentsRelationshipWithChild = null;

                if (columnDetails.length > 4) {
                    childDOB = columnDetails[1];
                    yourRelationshipWithChild = columnDetails[2];
                    doYouAndRespondentHaveParentalResponsibility = columnDetails[3];
                    respondentsRelationshipWithChild = columnDetails[4];
                }

                childDetails.put(FULL_NAME, childName);
                childDetails.put(DOB, childDOB);
                childDetails.put(APPLICANT_CHILD_RELATIONSHIP, yourRelationshipWithChild);
                childDetails.put(APPLICANT_RESPONDENT_SHARE_PARENTAL, doYouAndRespondentHaveParentalResponsibility);
                childDetails.put(RESPONDENT_CHILD_RELATIONSHIP, respondentsRelationshipWithChild);

                childrenLinkedTreeMap.put(VALUE, childDetails);

                childrenDetails.add(childrenLinkedTreeMap);

            }
        }
        return childrenDetails;
    }

    private List transformOngoingFamilyCourtProceedings(Map<String, String> inputFieldsMap) {
        ArrayList<LinkedTreeMap> familyCourtProceedingsDetails = new ArrayList<>();

        final String row1 = inputFieldsMap.get(ONGOING_COURT_PROCEEDING_ROW1);
        final String row2 = inputFieldsMap.get(ONGOING_COURT_PROCEEDING_ROW2);
        final String row3 = inputFieldsMap.get(ONGOING_COURT_PROCEEDING_ROW3);

        ArrayList<String> courtInput = new ArrayList<>();
        courtInput.add(row1);
        courtInput.add(row2);
        courtInput.add(row3);

        for (String input : courtInput) {
            LinkedTreeMap<String, String> caseDetails = new LinkedTreeMap<>();

            final LinkedTreeMap<String, LinkedTreeMap<String, String>> childrenLinkedTreeMap =
                new LinkedTreeMap();

            if (null != input) {
                final String[] columnDetails = input.split(",");

                final String nameOfCourt = columnDetails[0];

                String caseNumber = null;
                String typeOfCase = null;

                if (columnDetails.length > 2) {
                    caseNumber = columnDetails[1];
                    typeOfCase = columnDetails[2];
                }

                caseDetails.put(NAME_OF_COURT, nameOfCourt);
                caseDetails.put(CASE_NUMBER, caseNumber);
                caseDetails.put(TYPE_OF_CASE, typeOfCase);

                childrenLinkedTreeMap.put(VALUE, caseDetails);

                familyCourtProceedingsDetails.add(childrenLinkedTreeMap);

            }
        }
        return familyCourtProceedingsDetails;
    }

    private String transformReasonForOrderWithoutGivingNotice(Map<String, String> inputFieldsMap) {

        StringBuilder orderWithoutGivingNoticeReason = new StringBuilder();

        if (YES.equalsIgnoreCase(inputFieldsMap.get(RISK_OF_SIGNIFICANT_HARM))) {
            orderWithoutGivingNoticeReason.append(RISKOF_SIGNIFICANT_HARM.getDescription());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(DETERREDOR_PREVENTED))) {

            orderWithoutGivingNoticeReason =
                    orderWithoutGivingNoticeReason.length() != 0
                            ? orderWithoutGivingNoticeReason
                                    .append(COMMA)
                                    .append(
                                            OrderWithouGivingNoticeReasonEnum.DETERRED_OR_PREVENTED
                                                    .getDescription())
                            : orderWithoutGivingNoticeReason.append(
                                    OrderWithouGivingNoticeReasonEnum.DETERRED_OR_PREVENTED
                                            .getDescription());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(DELIVERATELY_EVADING_SERVICE))) {

            orderWithoutGivingNoticeReason =
                    orderWithoutGivingNoticeReason.length() != 0
                            ? orderWithoutGivingNoticeReason
                                    .append(COMMA)
                                    .append(
                                            OrderWithouGivingNoticeReasonEnum
                                                    .DELIBERATELYEVADING_SERVICE
                                                    .getDescription())
                            : orderWithoutGivingNoticeReason.append(
                                    OrderWithouGivingNoticeReasonEnum.DELIBERATELYEVADING_SERVICE
                                            .getDescription());
        }

        return orderWithoutGivingNoticeReason.toString();
    }

    private String getApplicantRelationships(
            Map<String, String> inputFieldsMap, String relationshipField) {
        TreeMap<String, String> orderedRelationshipFieldMap = new TreeMap<>();

        if (null == inputFieldsMap || inputFieldsMap.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, String> relationship : inputFieldsMap.entrySet()) {
            if (relationship.getKey().matches(relationshipField)) {
                orderedRelationshipFieldMap.put(relationship.getKey(), relationship.getValue());
            }
        }

        if (!orderedRelationshipFieldMap.isEmpty()) {
            for (Map.Entry<String, String> relationShipField :
                    orderedRelationshipFieldMap.entrySet()) {
                if (null != relationShipField.getValue()
                        && relationShipField.getValue().equalsIgnoreCase(YES)
                        && ApplicantRespondentRelationshipEnum.getValue(relationShipField.getKey())
                                        .getRelationshipDescription()
                                != null) {
                    return ApplicantRespondentRelationshipEnum.getValue(relationShipField.getKey())
                            .getRelationshipDescription();
                }
            }
        }

        return null;
    }

    private String getRespondentBehaviourOptions(Map<String, String> inputFieldsMap) {
        StringBuilder sb = new StringBuilder();
        for (FL401StopRespondentEnum l : EnumSet.allOf(FL401StopRespondentEnum.class)) {
            String key = l.getKey();
            if (null != inputFieldsMap.get(key) && inputFieldsMap.get(key).equalsIgnoreCase(YES)) {

                sb.append(l.getValue(l.name())).append(COMMA);
            }
        }

        return sb.toString();
    }

    private String getRespondentBehaviourChildOptions(Map<String, String> inputFieldsMap) {
        StringBuilder sb = new StringBuilder();
        for (FL401StopRespondentBehaviourChildEnum l :
                EnumSet.allOf(FL401StopRespondentBehaviourChildEnum.class)) {
            String key = l.getKey();
            if (null != inputFieldsMap.get(key) && inputFieldsMap.get(key).equalsIgnoreCase(YES)) {

                sb.append(l.getValue(l.name())).append(COMMA);
            }
        }

        return sb.toString();
    }

    private void transformApplicantConfidentiality(
            LinkedTreeMap fl401ApplicantTable, String applicantContactConfidentiality) {

        if (null != applicantContactConfidentiality
                && applicantContactConfidentiality.equalsIgnoreCase(YES)) {

            fl401ApplicantTable.put(IS_ADDRESS_CONFIDENTIAL, YES);
            fl401ApplicantTable.put(IS_PHONE_NUMBER_CONFIDENTIAL, YES);
            fl401ApplicantTable.put(IS_EMAIL_ADDRESS_CONFIDENTIAL, YES);

        } else if (null != applicantContactConfidentiality
                && applicantContactConfidentiality.equalsIgnoreCase(NO)) {
            fl401ApplicantTable.put(IS_ADDRESS_CONFIDENTIAL, NO);
            fl401ApplicantTable.put(IS_PHONE_NUMBER_CONFIDENTIAL, NO);
            fl401ApplicantTable.put(IS_EMAIL_ADDRESS_CONFIDENTIAL, NO);
        }
    }

    private LinkedTreeMap transformApplicantAddress(
            Map<String, String> inputFieldsMap, String applicantContactConfidentiality) {
        LinkedTreeMap address = new LinkedTreeMap<>();
        if (null != applicantContactConfidentiality
                && applicantContactConfidentiality.equalsIgnoreCase(NO)) {
            address.put(ADDRESS_LINE1, inputFieldsMap.get(APPLICANT_ADDRESS_BUILDING_AND_STREET));
            address.put(ADDRESS_LINE2, inputFieldsMap.get(APPLICANT_ADDRESS_SECOND_LINE));
            address.put(POSTTOWN, inputFieldsMap.get(APPLICANT_ADDRESS_TOWN_OR_CITY));
            address.put(COUNTY, inputFieldsMap.get(APPLICANT_ADDRESS_COUNTY));
            address.put(POSTCODE, inputFieldsMap.get(APPLICANT_ADDRESS_POSTCODE));
        }
        return address;
    }

    private void transformContactDetails(
            Map<String, String> inputFieldsMap,
            LinkedTreeMap fl401ApplicantTable,
            String applicantContactConfidentiality) {
        if (null != applicantContactConfidentiality
                && applicantContactConfidentiality.equalsIgnoreCase(NO)) {

            fl401ApplicantTable.put(PHONE_NUMBER, inputFieldsMap.get(APPLICANT_PHONE_NUMBER));
            fl401ApplicantTable.put(EMAIL, inputFieldsMap.get(APPLICANT_EMAIL));
            ;
            fl401ApplicantTable.put(
                    PREFER_TO_BE_CONTACTED, inputFieldsMap.get(APPLICANT_PREFER_TO_BE_CONTACTED));
        }
    }

    private void transformSolicitorName(
            Map<String, String> inputFieldsMap, LinkedTreeMap fl401SolicitorDetailsTable) {

        final String applicantSolicitorName = inputFieldsMap.get(APPLICANT_SOLICITOR_NAME);

        if (applicantSolicitorName != null) {
            final String[] representativeName = applicantSolicitorName.split(EMPTY_SPACE);
            fl401SolicitorDetailsTable.put(REPRESENTATIVE_FIRST_NAME, representativeName[0]);
            if (representativeName.length == 2) {
                fl401SolicitorDetailsTable.put(REPRESENTATIVE_LAST_NAME, representativeName[1]);
            }
        }
    }
}
