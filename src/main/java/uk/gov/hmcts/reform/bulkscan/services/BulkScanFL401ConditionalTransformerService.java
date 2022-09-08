package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_AUNT_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_BROTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_COUSIN_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_DAUGHTER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_FATHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_GRANDFATHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_GRANDMOTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_MOTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_NEPHEW_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_NIECE_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_OTHER_SPECIFY_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_RELATIONSHIP_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_RELATIONSHIP_OPTIONS_FIELDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_SISTER_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_SON_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RESPONDENT_UNCLE_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CCD_APPLICANT_RELATIONSHIOP_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CCD_RELATIONSHIP_DATE_COMPLEX_END_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CCD_RELATIONSHIP_DATE_COMPLEX_START_DATE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CCD_RELATIONSHIP_TO_RESPONDENT_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FL401_APPLICANT_RELATIONSHIP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FL401_APPLICANT_RELATIONSHIP_OPTIONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_BOY_GIRL_FRIEND_PARTNER_LIVEWITHME_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_ENGAGED_PROPOSED_CIVIL_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_FORMERLY_ENGAGED_PROPOSED_CIVIL_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_FORMERLY_LIVE_TOGETHER_AS_COUPLE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_FORMERLY_MARRIED_CIVIL_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_FORMER_BOY_GIRL_FRIEND_PARTNER_LIVEWITHME_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_LIVE_TOGETHER_AS_COUPLE_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_MARRIED_CIVIL_RELATIONSHIP_FIELD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TWO_DIGIT_MONTH_FORMAT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.BAIL_CONDITION_END_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.COMMA;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DELIVERATELY_EVADING_SERVICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DETERREDOR_PREVENTED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.REASON_FOR_ORDER_WITHOUT_GIVING_NOTICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RISK_OF_SIGNIFICANT_HARM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.WITHOUT_NOTICE_ORDER_TABLE;
import static uk.gov.hmcts.reform.bulkscan.enums.OrderWithouGivingNoticeReasonEnum.RISKOF_SIGNIFICANT_HARM;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

import com.microsoft.applicationinsights.core.dependencies.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.OrderWithouGivingNoticeReasonEnum;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

@SuppressWarnings({"PMD", "unchecked"})
@Component
public class BulkScanFL401ConditionalTransformerService {

    public void transform(
            Map<String, Object> populatedMap,
            Map<String, String> inputFieldsMap,
            BulkScanTransformationRequest bulkScanTransformationRequest) {

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

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));
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
            Map<String, String> relationshipFieldMap = buildRelationshipDetailMap();

            for (Map.Entry<String, String> relationShipField :
                    orderedRelationshipFieldMap.entrySet()) {
                if (null != relationShipField.getValue()
                        && relationShipField.getValue().equalsIgnoreCase(YES)) {
                    return getRelationshipDetail(relationShipField.getKey(), relationshipFieldMap);
                }
            }
        }

        return null;
    }

    private String getRelationshipDetail(
            String relationshipField, Map<String, String> relationshipDetailMap) {
        for (Map.Entry<String, String> relationship : relationshipDetailMap.entrySet()) {
            if (relationship.getKey().equalsIgnoreCase(relationshipField)) {
                return relationship.getValue();
            }
        }
        return null;
    }

    private TreeMap<String, String> buildRelationshipDetailMap() {
        TreeMap<String, String> relationshipFieldMap = new TreeMap<>();

        relationshipFieldMap.put(
                RESPONDENT_MARRIED_CIVIL_RELATIONSHIP_FIELD, "Married or in a civil partnership");
        relationshipFieldMap.put(
                RESPONDENT_FORMERLY_MARRIED_CIVIL_RELATIONSHIP_FIELD,
                "Formerly married or in a civil partnership");
        relationshipFieldMap.put(
                RESPONDENT_ENGAGED_PROPOSED_CIVIL_RELATIONSHIP_FIELD,
                "Engaged or proposed civil partnership");
        relationshipFieldMap.put(
                RESPONDENT_FORMERLY_ENGAGED_PROPOSED_CIVIL_RELATIONSHIP_FIELD,
                "Formerly engaged or proposed civil partnership");
        relationshipFieldMap.put(
                RESPONDENT_LIVE_TOGETHER_AS_COUPLE_FIELD, "Live together as a couple");
        relationshipFieldMap.put(
                RESPONDENT_FORMERLY_LIVE_TOGETHER_AS_COUPLE_FIELD,
                "Formerly lived together as a couple");
        relationshipFieldMap.put(
                RESPONDENT_BOY_GIRL_FRIEND_PARTNER_LIVEWITHME_FIELD,
                "Boyfriend, girlfriend or partner who does not live with me");
        relationshipFieldMap.put(
                RESPONDENT_FORMER_BOY_GIRL_FRIEND_PARTNER_LIVEWITHME_FIELD,
                "Former boyfriend, girlfriend or partner who did not live with me");
        relationshipFieldMap.put(
                APPLICANT_RESPONDENT_OTHER_RELATIONSHIP_FIELD, "None of the above");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_FATHER_RELATIONSHIP_FIELD, "Father");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_MOTHER_RELATIONSHIP_FIELD, "Mother");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_SON_RELATIONSHIP_FIELD, "Son");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_DAUGHTER_RELATIONSHIP_FIELD, "Daughter");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_BROTHER_RELATIONSHIP_FIELD, "Brother");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_SISTER_RELATIONSHIP_FIELD, "Sister");
        relationshipFieldMap.put(
                APPLICANT_RESPONDENT_GRANDFATHER_RELATIONSHIP_FIELD, "Grandfather");
        relationshipFieldMap.put(
                APPLICANT_RESPONDENT_GRANDMOTHER_RELATIONSHIP_FIELD, "Grandmother");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_UNCLE_RELATIONSHIP_FIELD, "Uncle");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_AUNT_RELATIONSHIP_FIELD, "Aunt");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_NEPHEW_RELATIONSHIP_FIELD, "Nephew");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_NIECE_RELATIONSHIP_FIELD, "Niece");
        relationshipFieldMap.put(APPLICANT_RESPONDENT_COUSIN_RELATIONSHIP_FIELD, "Cousin");
        relationshipFieldMap.put(
                APPLICANT_RESPONDENT_OTHER_SPECIFY_RELATIONSHIP_FIELD, "Other - please specify");

        return relationshipFieldMap;
    }
}
