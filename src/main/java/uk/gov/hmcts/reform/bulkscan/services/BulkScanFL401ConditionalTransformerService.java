package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.APPLYING_FOR_NON_MOLES_STATION_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.ATTEND_HEARING_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.BAIL_CONDITION_END_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_APPLICANT_RELATIONSHIOP_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_RELATIONSHIP_DATE_COMPLEX_END_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_RELATIONSHIP_DATE_COMPLEX_START_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.CCD_RELATIONSHIP_TO_RESPONDENT_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.COMMA;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DELIVERATELY_EVADING_SERVICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DETERREDOR_PREVENTED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_APPLICANT_RELATIONSHIP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.FL401_APPLICANT_RELATIONSHIP_OPTIONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.OTHERS_STOP_RESPONDENT_BEHAVIOUR_OPTIONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.REASON_FOR_ORDER_WITHOUT_GIVING_NOTICE;
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
import java.util.EnumSet;
import java.util.Map;
import java.util.TreeMap;
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
}
