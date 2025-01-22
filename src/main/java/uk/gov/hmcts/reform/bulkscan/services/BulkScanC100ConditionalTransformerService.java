package uk.gov.hmcts.reform.bulkscan.services;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMPTY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.VALUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.getTypeOfOrderEnumFields;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.getTypeOfOrderEnumMapping;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_AND_RESPONDENT_PARTY_ATTENDED_MIAM_SEPARATELY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_ONLY_ATTENDED_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_ONLY_ATTENDED_MIAM_TOGETHER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.CHILD_ARRANGEMENTS_ORDER_DESCRIPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.CHILD_ARRANGEMENT_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.INTERPRETER_NEEDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MEDIATION_NOT_PROCEEDING_APPLICANTS_AND_RESPONDENTS_ATTENDED_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MEDIATION_NOT_PROCEEDING_APPLICANT_ATTENDED_MIAM_ALONE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MEDIATION_NOT_PROCEEDING_HASSTARTED_BUT_BROKEN_WITH_SOMEISSUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MEDIATION_NOT_SUITABLE_FOR_RESOLVING_THE_DISPUTE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_FAILED_TO_ATTEND_MIAM_WITHOUT_GOOD_REASON;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_WILLING_TO_ATTEND_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MIAM_EXEMPTIONS_CHECKLIST;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MIAM_URGENCY_REASON_CHECKLIST;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_CHILD_PROTECTION_CONCERNS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_CHILD_PROTECTION_CONCERNS_CHECKLIST;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DOMESTIC_VIOLENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_ARRESTED_FOR_SIMILAR_OFFENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_COPY_OF_FACT_FINDING;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_COURT_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_DOMESTIC_VIOLENCE_ADVISOR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_EVIDENCE_FINANCIAL_MATTERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_EXPERT_EVIDENCE_REPORT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_HEALTH_PROFESSIONAL_REPORT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_MEMBER_OF_MULTI_AGENCY_RISK_ASSESSMENT_CONFERRANCE_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_NO_CROSS_UNDERTAKING_GIVEN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_OFFICER_EMPLOYED_LOCAL_AUTHORITY_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_PROTECTION_NOTICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_PROTECTIVE_INJUNCTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_PUBLIC_AUTHORITY_CONFIRMATION_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_REFERRAL_HEALTH_PROFESSIONAL_REPORT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_RELEVANT_CONVICTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_RELEVANT_CRIMINAL_PROCEEDING;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_RELEVANT_POLICE_CAUTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DVE_SECRETARY_OF_STATE_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_OTHER_REASONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_PREVIOUS_ATTENDENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_SUBJECT_OF_CPP_BY_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_SUBJECT_OF_ENQUIRIES_BY_LOCAL_AUTHORITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY_CONFLICT_WITH_OTHER_STATE_COURTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY_IRRETRIEVABLE_PROBLEM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY_RISK_OF_HARM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY_RISK_TO_LIFE_LIBERTY_OR_SAFETY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY_RISK_TO_MISCARRIAGE_OF_JUSTICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY_RISK_TO_UNLAWFUL_REMOVAL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY_UNREASONABLEHARDSHIP;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.ORDER_APPLIED_FOR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.OTHER_PROCEEDINGS_DETAILS_TABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.PARTY_ENUM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.PROHIBITED_STEPS_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.PROHIBITED_STEPS_ORDER_DESCRIPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.RESPONDENT_PARTY_ARRANGED_TO_ATTEND_MIAM_SEPARATELY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.SET_OUT_REASONS_BELOW;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.SPECIAL_ISSUE_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.SPECIFIC_ISSUE_ORDER_DESCRIPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.SPOKEN_WRITTEN_BOTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.TYPE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.URGENCY_REASON;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WELSH_NEEDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WELSH_NEEDS_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WHO_WELSH_NEEDS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.StringUtils;
import com.microsoft.applicationinsights.core.dependencies.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.ChildLiveWithEnum;
import uk.gov.hmcts.reform.bulkscan.enums.GroupMediatorCertifiesEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamChildProtectionConcernChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamDomesticViolenceChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamExemptionsChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamUrgencyReasonChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.PartyEnum;
import uk.gov.hmcts.reform.bulkscan.enums.PermissionRequiredEnum;
import uk.gov.hmcts.reform.bulkscan.enums.SpokenOrWrittenWelshEnum;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.ResponseScanDocument;
import uk.gov.hmcts.reform.bulkscan.model.ResponseScanDocumentNew;
import uk.gov.hmcts.reform.bulkscan.model.ResponseScanDocumentValueNew;
import uk.gov.hmcts.reform.bulkscan.model.ScannedDocuments;

@SuppressWarnings({"PMD", "unchecked"})
@Component
public class BulkScanC100ConditionalTransformerService {

    public void transform(
            Map<String, Object> populatedMap,
            Map<String, String> inputFieldsMap,
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        String permissionsRequired = transformPermissionRequired(inputFieldsMap);
        if (StringUtils.isNotEmpty(permissionsRequired)) {
            populatedMap.put(APPLICATION_PERMISSION_REQUIRED, permissionsRequired);
        }
        //String childLivesWith = transformChildLiveWith(inputFieldsMap);
        //if (StringUtils.isNotEmpty(childLivesWith)) {
        //    populatedMap.put(CHILD_LIVE_WITH_KEY, childLivesWith);
        //}
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        populatedMap.put(
                SCAN_DOCUMENTS,
                objectMapper.convertValue(transformScanDocuments(bulkScanTransformationRequest), List.class));
        populatedMap.put(
            "mpuDomesticAbuseEvidences",
                transformMiamDomesticViolenceChecklist(inputFieldsMap));
        populatedMap.put(
                MIAM_EXEMPTIONS_CHECKLIST, transformMiamExemptionsChecklist(inputFieldsMap));
        populatedMap.put(
                NO_MIAM_CHILD_PROTECTION_CONCERNS_CHECKLIST,
                transformNoMiamChildProtectionConcerns(inputFieldsMap));
        populatedMap.put(
                MIAM_URGENCY_REASON_CHECKLIST, transformMiamUrgencyReasonChecklist(inputFieldsMap));
        populatedMap.put(ORDER_APPLIED_FOR, transformOrderAppliedFor(inputFieldsMap));

        List<LinkedTreeMap> list = (List) populatedMap.get(OTHER_PROCEEDINGS_DETAILS_TABLE);
        LinkedTreeMap innerValue = list.get(0);
        LinkedTreeMap values = (LinkedTreeMap) innerValue.get(VALUE);
        values.put(TYPE_OF_ORDER, transformTypeOfOrder(inputFieldsMap));

        populatedMap.put(OTHER_PROCEEDINGS_DETAILS_TABLE, list);

        // C100 Attending the hearing fields transform
        populatedMap.put(WELSH_NEEDS_CCD, populateWelshNeeds(inputFieldsMap));

        List<LinkedTreeMap> interpreterNeeds = (List) populatedMap.get(INTERPRETER_NEEDS);
        LinkedTreeMap innerinterpreterValue = interpreterNeeds.get(0);
        LinkedTreeMap interpreterValues = (LinkedTreeMap) innerinterpreterValue.get(VALUE);
        interpreterValues.put(PARTY_ENUM, transformParty(inputFieldsMap));

        setOutReasonsBelow(populatedMap, inputFieldsMap);
        transformMediatorCertifiesMiamExemption(inputFieldsMap);
        transformMediatorCertifiesApplicantAttendMiam(inputFieldsMap);
        transformMediatorCertifiesDisputeResolutionNotProceeding(inputFieldsMap);
        populatedMap.values().removeIf(Objects::isNull);
    }

    /**
     * C100 form Interpreter needs party enum.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of values for party field in the transformation output.
     */
    private List<String> transformParty(Map<String, String> inputFieldsMap) {
        List<String> partyDetails = new ArrayList<>();
        if (YES.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_REQUIRES_INTERPRETER_APPLICANT))) {
            partyDetails.add(PartyEnum.applicant.getDisplayedValue());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_REQUIRES_INTERPRETER_RESPONDENT))) {
            partyDetails.add(PartyEnum.respondent.getDisplayedValue());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY))) {
            partyDetails.add(PartyEnum.other.getDisplayedValue());
        }

        return partyDetails;
    }

    @SuppressWarnings("unchecked")
    private void setOutReasonsBelow(
            Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        if (!org.apache.commons.lang3.StringUtils.isEmpty(inputFieldsMap.get(URGENCY_REASON))) {
            populatedMap.put(SET_OUT_REASONS_BELOW, inputFieldsMap.get(URGENCY_REASON));
        } else if (!org.apache.commons.lang3.StringUtils.isEmpty(
                inputFieldsMap.get(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS))) {
            populatedMap.put(
                    SET_OUT_REASONS_BELOW,
                    inputFieldsMap.get(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS));
        }
    }

    /**
     * C100 form Above fields of Section 1.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of values for ordersApplyingFor field in the transformation output.
     */
    private List<String> transformOrderAppliedFor(Map<String, String> inputFieldsMap) {
        List<String> orderAppliedForList = new ArrayList<>();

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_ARRANGEMENT_ORDER))) {
            orderAppliedForList.add(CHILD_ARRANGEMENTS_ORDER_DESCRIPTION);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(PROHIBITED_STEPS_ORDER))) {
            orderAppliedForList.add(PROHIBITED_STEPS_ORDER_DESCRIPTION);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(SPECIAL_ISSUE_ORDER))) {
            orderAppliedForList.add(SPECIFIC_ISSUE_ORDER_DESCRIPTION);
        }
        return orderAppliedForList;
    }

    /**
     * C100 form Section 3c.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of enum values for miamUrgencyReasonChecklist field in the transformation
     *     output.
     */
    private List<MiamUrgencyReasonChecklistEnum> transformMiamUrgencyReasonChecklist(
            Map<String, String> inputFieldsMap) {
        List<MiamUrgencyReasonChecklistEnum> miamUrgencyReasonChecklistEnumList = new ArrayList<>();
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_URGENCY_RISK_TO_LIFE_LIBERTY_OR_SAFETY))) {
            miamUrgencyReasonChecklistEnumList.add(
                    MiamUrgencyReasonChecklistEnum.miamUrgencyReasonChecklistEnum_Value_1);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_RISK_OF_HARM))) {
            miamUrgencyReasonChecklistEnumList.add(
                    MiamUrgencyReasonChecklistEnum.miamUrgencyReasonChecklistEnum_Value_2);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_RISK_TO_UNLAWFUL_REMOVAL))) {
            miamUrgencyReasonChecklistEnumList.add(
                    MiamUrgencyReasonChecklistEnum.miamUrgencyReasonChecklistEnum_Value_3);
        }

        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_URGENCY_RISK_TO_MISCARRIAGE_OF_JUSTICE))) {
            miamUrgencyReasonChecklistEnumList.add(
                    MiamUrgencyReasonChecklistEnum.miamUrgencyReasonChecklistEnum_Value_4);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_UNREASONABLEHARDSHIP))) {
            miamUrgencyReasonChecklistEnumList.add(
                    MiamUrgencyReasonChecklistEnum.miamUrgencyReasonChecklistEnum_Value_5);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_IRRETRIEVABLE_PROBLEM))) {
            miamUrgencyReasonChecklistEnumList.add(
                    MiamUrgencyReasonChecklistEnum.miamUrgencyReasonChecklistEnum_Value_6);
        }

        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_URGENCY_CONFLICT_WITH_OTHER_STATE_COURTS))) {
            miamUrgencyReasonChecklistEnumList.add(
                    MiamUrgencyReasonChecklistEnum.miamUrgencyReasonChecklistEnum_Value_7);
        }
        return miamUrgencyReasonChecklistEnumList;
    }

    /**
     * C100 form Section 3b.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of enum values for miamChildProtectionConcernList field in the transformation
     *     output.
     */
    private List<MiamChildProtectionConcernChecklistEnum> transformNoMiamChildProtectionConcerns(
            Map<String, String> inputFieldsMap) {
        List<MiamChildProtectionConcernChecklistEnum> miamChildProtectionConcernList =
                new ArrayList<>();
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_SUBJECT_OF_ENQUIRIES_BY_LOCAL_AUTHORITY))) {
            miamChildProtectionConcernList.add(
                    MiamChildProtectionConcernChecklistEnum
                            .MIAMChildProtectionConcernChecklistEnum_value_1);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_SUBJECT_OF_CPP_BY_LOCAL_AUTHORITY))) {
            miamChildProtectionConcernList.add(
                    MiamChildProtectionConcernChecklistEnum
                            .MIAMChildProtectionConcernChecklistEnum_value_2);
        }
        return miamChildProtectionConcernList;
    }

    /**
     * C100 form beginning of Section 3.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of enum values for miamExemptionsChecklist field in the transformation output.
     */
    private List<MiamExemptionsChecklistEnum> transformMiamExemptionsChecklist(
            Map<String, String> inputFieldsMap) {
        List<MiamExemptionsChecklistEnum> miamExemptionsChecklist = new ArrayList<>();
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_DOMESTIC_VIOLENCE))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.domesticViolence);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_CHILD_PROTECTION_CONCERNS))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.childProtectionConcern);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.urgency);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_PREVIOUS_ATTENDENCE))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.previousMIAMattendance);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_OTHER_REASONS))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.other);
        }

        return miamExemptionsChecklist;
    }

    /**
     * C100 form Section 3a.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of enum values for miamDomesticViolenceChecklist field in the transformation
     *     output.
     */
    private List<MiamDomesticViolenceChecklistEnum> transformMiamDomesticViolenceChecklist(
            Map<String, String> inputFieldsMap) {

        Map<String, MiamDomesticViolenceChecklistEnum> miamDomesticViolenceChecklistEnumMap =
                getStringMiamDomesticViolenceChecklistEnumMap();

        List<MiamDomesticViolenceChecklistEnum> miamDomesticViolenceChecklistEnumList =
                new ArrayList<>();
        miamDomesticViolenceChecklistEnumMap
                .keySet()
                .forEach(
                        key -> {
                            Optional<String> inputFieldOptional =
                                    Optional.ofNullable(inputFieldsMap.get(key));
                            inputFieldOptional.ifPresent(
                                    s -> {
                                        if (BooleanUtils.TRUE.equals(inputFieldOptional.get())) {
                                            miamDomesticViolenceChecklistEnumList.add(
                                                    miamDomesticViolenceChecklistEnumMap.get(key));
                                        }
                                    });
                        });

        return miamDomesticViolenceChecklistEnumList.stream().sorted().collect(Collectors.toList());
    }

    /**
     * C100 form Section 3a.
     *
     * @return prepare a Map key-value pair for MiamDomesticViolenceChecklist.
     */
    private Map<String, MiamDomesticViolenceChecklistEnum>
            getStringMiamDomesticViolenceChecklistEnumMap() {
        Map<String, MiamDomesticViolenceChecklistEnum> miamDomesticViolenceChecklistEnumMap =
                new HashMap<>();
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_ARRESTED_FOR_SIMILAR_OFFENCE,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_1);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_RELEVANT_POLICE_CAUTION,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_2);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_RELEVANT_CRIMINAL_PROCEEDING,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_3);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_RELEVANT_CONVICTION,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_4);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_COURT_ORDER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_5);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_PROTECTION_NOTICE,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_6);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_PROTECTIVE_INJUNCTION,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_7);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_NO_CROSS_UNDERTAKING_GIVEN,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_8);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_COPY_OF_FACT_FINDING,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_9);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_EXPERT_EVIDENCE_REPORT,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_10);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_HEALTH_PROFESSIONAL_REPORT,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_11);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_REFERRAL_HEALTH_PROFESSIONAL_REPORT,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_12);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_MEMBER_OF_MULTI_AGENCY_RISK_ASSESSMENT_CONFERRANCE_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_13);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_DOMESTIC_VIOLENCE_ADVISOR,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_15);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_16);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_OFFICER_EMPLOYED_LOCAL_AUTHORITY_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_17);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_18);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_19);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_PUBLIC_AUTHORITY_CONFIRMATION_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_20);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_SECRETARY_OF_STATE_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_21);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_EVIDENCE_FINANCIAL_MATTERS,
                MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_22);
        return miamDomesticViolenceChecklistEnumMap;
    }

    private String transformChildLiveWith(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_LIVING_WITH_APPLICANT))) {
            return ChildLiveWithEnum.APPLICANT.getName();
        }
        if (TRUE.equals(inputFieldsMap.get(CHILD_LIVING_WITH_RESPONDENT))) {
            return ChildLiveWithEnum.RESPONDENT.getName();
        }
        if (TRUE.equals(inputFieldsMap.get(CHILD_LIVING_WITH_OTHERS))) {
            return ChildLiveWithEnum.OTHERPEOPLE.getName();
        }
        return StringUtils.EMPTY;
    }

    private String transformPermissionRequired(Map<String, String> inputFieldsMap) {
        if (YES.equalsIgnoreCase(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            return PermissionRequiredEnum.yes.getDisplayedValue();
        }
        if ("No, permission Not required".equals(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            return PermissionRequiredEnum.noNotRequired.getDisplayedValue();
        }
        if ("No, permission Now sought".equals(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            return PermissionRequiredEnum.noNowSought.getDisplayedValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * C100 form Above fields of Section 7.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of values for typeoforder field in the transformation output.
     */
    private String transformTypeOfOrder(Map<String, String> inputFieldsMap) {
        Optional<String> typeOfOrderField =
                getTypeOfOrderEnumFields().stream()
                        .filter(eachField -> YES.equalsIgnoreCase(inputFieldsMap.get(eachField)))
                        .findFirst();

        if (typeOfOrderField.isPresent()) {
            return getTypeOfOrderEnumMapping().get(typeOfOrderField.get());
        }
        return null;
    }

    /**
     * <<<<<<< HEAD C100 form fields of Section 10.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of values for welshneeds field in the transformation output.
     */
    private List<LinkedTreeMap> populateWelshNeeds(Map<String, String> inputFieldsMap) {
        List<LinkedTreeMap> list = new ArrayList<>();
        String welshNeeds = inputFieldsMap.get(WELSH_NEEDS);
        // child1 Yes no no;child2 No Yes Yes;
        String[] splittedArr = org.apache.commons.lang3.StringUtils.split(welshNeeds, ";");
        if (splittedArr != null) {
            for (String eachStr : splittedArr) {
                LinkedTreeMap<String, Object> linkedTreeMap = new LinkedTreeMap<>();
                List<String> enums = new ArrayList<>();
                String childName = eachStr.substring(0, eachStr.indexOf("["));
                String spokenEnum =
                        eachStr.substring(eachStr.indexOf("[") + 1, eachStr.lastIndexOf("]"));
                String[] spokenOrWrittenOrBoth = spokenEnum.split(" ");
                // 0 - Spoken , 1- Written, 2- Both
                if (spokenOrWrittenOrBoth.length > 0) {
                    if (YES.equalsIgnoreCase(spokenOrWrittenOrBoth[0])) {
                        enums.add(SpokenOrWrittenWelshEnum.spoken.getDisplayedValue());
                    }
                    if (YES.equalsIgnoreCase(spokenOrWrittenOrBoth[1])) {
                        enums.add(SpokenOrWrittenWelshEnum.written.getDisplayedValue());
                    }
                    if (YES.equalsIgnoreCase(spokenOrWrittenOrBoth[2])) {
                        enums.add(SpokenOrWrittenWelshEnum.both.getDisplayedValue());
                    }
                }
                linkedTreeMap.put(WHO_WELSH_NEEDS, childName);
                linkedTreeMap.put(SPOKEN_WRITTEN_BOTH, enums);

                LinkedTreeMap<String, Object> valueLinkMap = new LinkedTreeMap<>();
                valueLinkMap.put(VALUE, linkedTreeMap);
                list.add(valueLinkMap);
            }
        }

        return list;
    }

    /* C100 form Above fields of Section 4a.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return Mediator Certifies MIAM exemption field in the transformation output.
     */
    private void transformMediatorCertifiesMiamExemption(Map<String, String> inputFieldsMap) {

        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(
                        MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_WILLING_TO_ATTEND_MIAM))) {
            inputFieldsMap.put("respondentWillingToAttendMiam", "No");
            inputFieldsMap.put("respondentReasonNotAttendingMiam", GroupMediatorCertifiesEnum
                .MEDIATION_NOT_PROCEEDING_APPLICATION_ATTENDED_MIAM_ALONE
                .getDescription());
        } else if (TRUE.equals(
                inputFieldsMap.get(
                        MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_FAILED_TO_ATTEND_MIAM_WITHOUT_GOOD_REASON))) {
            inputFieldsMap.put("respondentWillingToAttendMiam", "No");
            inputFieldsMap.put("respondentReasonNotAttendingMiam",  GroupMediatorCertifiesEnum
                    .MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_FAILED_TO_ATTEND_MIAM_WITHOUT_GOOD_REASON
                    .getDescription());
        } else if (TRUE.equals(
                inputFieldsMap.get(MEDIATION_NOT_SUITABLE_FOR_RESOLVING_THE_DISPUTE))) {
            inputFieldsMap.put("respondentWillingToAttendMiam", "No");
            inputFieldsMap.put("respondentReasonNotAttendingMiam",
                               GroupMediatorCertifiesEnum.MEDIATION_NOT_SUITABLE_FOR_RESOLVING_THE_DISPUTE.getDescription());
        } else {
            inputFieldsMap.put("respondentWillingToAttendMiam", "No");
            inputFieldsMap.put("respondentReasonNotAttendingMiam", "");
        }
    }

    /**
     * C100 form Above fields of Section 4b part 1.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return Mediator Certifies Applicant attend MIAM field in the transformation output.
     */
    private String transformMediatorCertifiesApplicantAttendMiam(
            Map<String, String> inputFieldsMap) {

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_ONLY_ATTENDED_MIAM))) {
            return GroupMediatorCertifiesEnum.APPLICANT_ONLY_ATTENDED_MIAM.getDescription();
        } else if (TRUE.equals(inputFieldsMap.get(APPLICANT_ONLY_ATTENDED_MIAM_TOGETHER))) {
            return GroupMediatorCertifiesEnum.APPLICANT_AND_RESPONDENT_ATTENDED_MIAM_TOGETHER
                    .getDescription();
        } else if (TRUE.equals(
                inputFieldsMap.get(APPLICANT_AND_RESPONDENT_PARTY_ATTENDED_MIAM_SEPARATELY))) {
            return GroupMediatorCertifiesEnum
                    .APPLICANT_AND_RESPONDENT_PARTY_ATTENDED_MIAM_SEPARATELY
                    .getDescription();
        } else if (TRUE.equals(
                inputFieldsMap.get(RESPONDENT_PARTY_ARRANGED_TO_ATTEND_MIAM_SEPARATELY))) {
            return GroupMediatorCertifiesEnum.RESPONDENT_PARTY_ARRANGED_TO_ATTEND_MIAM_SEPARATELY
                    .getDescription();
        }
        return EMPTY;
    }

    /**
     * C100 form Above fields of Section 4b part 2.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return Mediator Certifies Applicant attend MIAM field in the transformation output.
     */
    private String transformMediatorCertifiesDisputeResolutionNotProceeding(
            Map<String, String> inputFieldsMap) {

        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(MEDIATION_NOT_PROCEEDING_APPLICANT_ATTENDED_MIAM_ALONE))) {
            return GroupMediatorCertifiesEnum
                    .MEDIATION_NOT_PROCEEDING_APPLICATION_ATTENDED_MIAM_ALONE
                    .getDescription();
        } else if (TRUE.equals(
                inputFieldsMap.get(
                        MEDIATION_NOT_PROCEEDING_APPLICANTS_AND_RESPONDENTS_ATTENDED_MIAM))) {
            return GroupMediatorCertifiesEnum
                    .MEDIATION_NOT_PROCEEDING_APPLICANTS_AND_RESPONDENTS_ATTENDED_MIAM
                    .getDescription();
        } else if (TRUE.equals(
                inputFieldsMap.get(
                        MEDIATION_NOT_PROCEEDING_HASSTARTED_BUT_BROKEN_WITH_SOMEISSUE))) {
            return GroupMediatorCertifiesEnum.MEDIATION_NOT_PROCEEDING_BROKEN_DOWN_UNRESOLVED
                    .getDescription();
        }
        return EMPTY;
    }

    public static List<ResponseScanDocumentValueNew> transformScanDocuments(
        BulkScanTransformationRequest bulkScanTransformationRequest) {
        List<ScannedDocuments> scannedDocumentsList =
            bulkScanTransformationRequest.getScannedDocuments();
        return nonNull(scannedDocumentsList)
            ? bulkScanTransformationRequest.getScannedDocuments().stream()
            .map(
                scanDocument ->
                    ResponseScanDocumentValueNew.builder()
                        .value(
                            ResponseScanDocumentNew.builder().url(ResponseScanDocument.builder()
                                .url(
                                    scanDocument
                                        .getScanDocument()
                                        .getUrl())
                                .binaryUrl(
                                    scanDocument
                                        .getScanDocument()
                                        .getBinaryUrl())
                                .filename(
                                    scanDocument
                                        .getScanDocument()
                                        .getFilename())
                                .build()).build())
                        .build())
            .collect(Collectors.toList())
            : Collections.emptyList();
    }
}
