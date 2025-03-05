package uk.gov.hmcts.reform.bulkscan.services;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EMPTY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.VALUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.getTypeOfOrderEnumFields;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.getTypeOfOrderEnumMapping;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TWO_DIGIT_MONTH_FORMAT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_AND_RESPONDENT_PARTY_ATTENDED_MIAM_SEPARATELY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_ONLY_ATTENDED_MIAM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_ONLY_ATTENDED_MIAM_TOGETHER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.APPLICANT_REQUIRES_INTERPRETER_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.CHILD_ARRANGEMENTS_ORDER_DESCRIPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.CHILD_ARRANGEMENT_ORDER;
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
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.PARTY_ENUM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.PROHIBITED_STEPS_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.PROHIBITED_STEPS_ORDER_DESCRIPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.RESPONDENT_PARTY_ARRANGED_TO_ATTEND_MIAM_SEPARATELY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.SET_OUT_REASONS_BELOW;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.SPECIAL_ISSUE_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.SPECIFIC_ISSUE_ORDER_DESCRIPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WELSH_NEEDS_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.microsoft.applicationinsights.core.dependencies.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.Gender;
import uk.gov.hmcts.reform.bulkscan.enums.GroupMediatorCertifiesEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamChildProtectionConcernChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamDomesticViolenceChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamExemptionsChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamUrgencyReasonChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.PartyEnum;
import uk.gov.hmcts.reform.bulkscan.enums.PermissionRequiredEnum;
import uk.gov.hmcts.reform.bulkscan.enums.SpecialMeasuresEnum;
import uk.gov.hmcts.reform.bulkscan.enums.SpokenOrWrittenWelshEnum;
import uk.gov.hmcts.reform.bulkscan.enums.YesOrNo;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.OtherChildrenNotInTheCase;
import uk.gov.hmcts.reform.bulkscan.model.ResponseScanDocument;
import uk.gov.hmcts.reform.bulkscan.model.ResponseScanDocumentNew;
import uk.gov.hmcts.reform.bulkscan.model.ResponseScanDocumentValueNew;
import uk.gov.hmcts.reform.bulkscan.model.ScannedDocuments;
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

@SuppressWarnings({"PMD", "unchecked", "java:S3740"})
@Component
public class BulkScanC100ConditionalTransformerService {

    public static final String RESPONDENT_REASON_NOT_ATTENDING_MIAM = "respondentReasonNotAttendingMiam";
    public static final String RESPONDENT_WILLING_TO_ATTEND_MIAM = "respondentWillingToAttendMiam";
    public static final String APPLICANTS = "applicants";
    public static final String RESPONDENTS = "respondents";
    public static final String ID = "id";
    public static final String ROLE_ON_CASE = "roleOnCase";
    public static final String DETAILS = "details";
    public static final String PARTY_NAME = "partyName";
    public static final String APPLICANT_FULL_NAME = "applicantFullName";
    public static final String LAST_NAME = "lastName";
    public static final String FIRST_NAME = "firstName";
    public static final String CHILD_FULL_NAME = "childFullName";
    public static final String CHILD_AND_APPLICANT_RELATION = "childAndApplicantRelation";
    public static final String CHILD_LIVES_WITH = "childLivesWith";
    public static final String CA_APPLICANT_1_INTERNAL_FLAGS = "caApplicant1InternalFlags";
    public static final String CHILD_ID = "childId";
    public static final String APPLICANT_TWO_FIRST_NAME = "applicantTwoFirstName";
    public static final String CA_APPLICANT_2_EXTERNAL_FLAGS = "caApplicant2ExternalFlags";
    public static final String CA_APPLICANT_2_INTERNAL_FLAGS = "caApplicant2InternalFlags";
    public static final String RESPONDENT_ONE_FIRST_NAME = "respondentOneFirstName";
    public static final String CA_RESPONDENT_1_EXTERNAL_FLAGS = "caRespondent1ExternalFlags";
    public static final String CA_RESPONDENT_1_INTERNAL_FLAGS = "caRespondent1InternalFlags";
    public static final String HAVE_PREVIOUS_PARENTING_PLAN = "havePreviousParentingPlan";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String OTHER_EXEMPTION_ADDITONAL_INFORMATION = "otherExemption_additonalInformation";
    public static final String GENDER = "gender";
    public static final String APPLICANT_ONE_FIRST_NAME = "applicantOneFirstName";
    public static final String CA_APPLICANT_1_EXTERNAL_FLAGS = "caApplicant1ExternalFlags";
    public static final String RESPONDENT_TWO_FIRST_NAME = "respondentTwoFirstName";
    public static final String CA_RESPONDENT_2_EXTERNAL_FLAGS = "caRespondent2ExternalFlags";
    public static final String CA_RESPONDENT_2_INTERNAL_FLAGS = "caRespondent2InternalFlags";
    public static final String MPU_OTHER_EXEMPTION_REASONS = "mpuOtherExemptionReasons";
    public static final String NEW_CHILD_DETAILS = "newChildDetails";

    public void transform(
            Map<String, Object> populatedMap,
            Map<String, String> inputFieldsMap,
            BulkScanTransformationRequest bulkScanTransformationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        populatedMap.put(
            SCAN_DOCUMENTS,
            objectMapper.convertValue(transformScanDocuments(bulkScanTransformationRequest), List.class));
        //transform type of application
        populatedMap.put(ORDER_APPLIED_FOR, transformOrderAppliedFor(inputFieldsMap));

        // transform initial checks
        transformInitialChecks(inputFieldsMap, populatedMap);

        //transform applicant details
        transformPartyDetails(populatedMap, APPLICANTS);

        //transform respondent details
        transformPartyDetails(populatedMap, RESPONDENTS);

        //transform other person details
        transformPartyDetails(populatedMap, "otherPartyInTheCaseRevised");

        //transform child details
        transformChildDetails(inputFieldsMap, populatedMap);

        //transform applicant child relationship
        transformApplicantChildRelationship(inputFieldsMap, populatedMap);

        //transform respondent child relationship
        transformRespondentChildRelationship(inputFieldsMap, populatedMap);

        //transform other person, child relationship
        //transformOtherPersonChildRelationship(inputFieldsMap, populatedMap);

        //transform other children not in the case
        tranformOtheChildrenNotInTheCase(inputFieldsMap, populatedMap);

        //transform Miam details
        transformMiamDetails(populatedMap, inputFieldsMap);

        //transform permission required
        transformPermissionRequiredFromCourt(inputFieldsMap, populatedMap);

        //transform hearing urgency - Section 6a
        transformHearingUrgency(populatedMap, inputFieldsMap);

        //transform previous or ongoing proceedings - Section 7
        transformPreviousOrOngoingProceedings(inputFieldsMap, populatedMap);

        //transform international elements
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("international_or_factorsAffectingLitigation"))) {
            transformInternationalElements(inputFieldsMap, populatedMap);
        }

        //transform litigation capacity
        transformLitigationCapacity(inputFieldsMap, populatedMap);

        //transform attending the court
        transformAttendingTheCourt(inputFieldsMap, populatedMap);

        //transform allegations of harm
        transformAllegationsOfHarm(inputFieldsMap, populatedMap);

        //transformMediatorCertifiesMiamExemption(inputFieldsMap);
        //transformMediatorCertifiesApplicantAttendMiam(inputFieldsMap);
        //transformMediatorCertifiesDisputeResolutionNotProceeding(inputFieldsMap);
        transformFlags(inputFieldsMap, populatedMap);
        populatedMap.values().removeIf(Objects::isNull);
    }

    private void tranformOtheChildrenNotInTheCase(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<Map<String, Object>> childrenList = new ArrayList<>();
        transformOtherChildOne(inputFieldsMap, childrenList);
        transformOtherChildTwo(inputFieldsMap, childrenList);
        if (childrenList.isEmpty()) {
            populatedMap.put("childrenNotPartInTheCaseYesNo", YesOrNo.No);
        } else {
            populatedMap.put("childrenNotPartInTheCaseYesNo", YesOrNo.Yes);
            populatedMap.put("childrenNotInTheCase", childrenList);
        }
    }

    private void transformOtherChildOne(Map<String, String> inputFieldsMap, List<Map<String, Object>> childrenList) {
        if (StringUtils.isNotEmpty(inputFieldsMap.get("OtherChildrenOneFullName"))) {
            Map<String, Object> otherChild = new HashMap<>();
            String dob = null;
            if (StringUtils.isNotEmpty(inputFieldsMap.get("OtherChildrenOneDateOfBirth"))) {
                dob = DateUtil.transformDate(inputFieldsMap.get("OtherChildrenOneDateOfBirth"),
                                             "dd/mm/yyyy", "yyyy-MM-dd");
            }

            otherChild.put(ID, UUID.randomUUID());
            otherChild.put(VALUE, OtherChildrenNotInTheCase.builder()
                .firstName(inputFieldsMap.get("otherChildrenOneFullName"))
                .gender("male".equalsIgnoreCase(inputFieldsMap.get("OtherChildrenOneGender")) ? Gender.male : Gender.female)
                .isDateOfBirthKnown(TRUE.equalsIgnoreCase(inputFieldsMap.get("OtherChildrenOneDateOfBirthDontKnow"))
                                        ? YesOrNo.Yes : YesOrNo.No)
                .dateOfBirth(StringUtils.isNotEmpty(dob) ? LocalDate.parse(dob) : null)
                .build());
            childrenList.add(otherChild);
        }
    }

    private void transformOtherChildTwo(Map<String, String> inputFieldsMap, List<Map<String, Object>> childrenList) {
        if (StringUtils.isNotEmpty(inputFieldsMap.get("OtherChildrenTwoFullName"))) {
            Map<String, Object> otherChild = new HashMap<>();
            String dob = DateUtil.transformDate(inputFieldsMap.get("OtherChildrenTwoDateOfBirth"),
                                                "dd/mm/yyyy", "yyyy-MM-dd");
            otherChild.put(ID, UUID.randomUUID());
            otherChild.put(VALUE, OtherChildrenNotInTheCase.builder()
                .firstName(inputFieldsMap.get("OtherChildrenTwoFullName"))
                .gender("male".equalsIgnoreCase(inputFieldsMap.get("OtherChildrenTwoGender")) ? Gender.male : Gender.female)
                .isDateOfBirthKnown(TRUE.equalsIgnoreCase(inputFieldsMap.get("OtherChildrenTwoDateOfBirthDontKnow"))
                                        ? YesOrNo.Yes : YesOrNo.No)
                .dateOfBirth(StringUtils.isNotEmpty(dob) ? LocalDate.parse(dob) : null)
                .build());
            childrenList.add(otherChild);
        }
    }

    private void transformRespondentChildRelationship(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<Map<String, Object>> childrenList = (List<Map<String, Object>>) populatedMap.get(NEW_CHILD_DETAILS);
        List<Map<String, Object>> respondents = (List<Map<String, Object>>) populatedMap.get(RESPONDENTS);
        String key = "child%s_respondent%s_relationship";
        String childLivesWith = "";
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_LIVING_WITH_RESPONDENT))) {
            childLivesWith = YES;
        } else {
            childLivesWith = NO;
        }
        List<Map<String, String>> childRespondantRelations = new ArrayList<>();
        for (int i = 0; i < childrenList.size(); i++) {
            Map<String, Object> child = childrenList.get(i);
            for (int j = 0; j < respondents.size(); j++) {
                Map<String, String> childRespondantRelation = new HashMap<>();
                Map<String, Object> respondentValue = (Map<String, Object>) respondents.get(j).get(VALUE);
                String relationship = inputFieldsMap.get(String.format(key, i, j));
                childRespondantRelation.put("respondentFullName", respondentValue.get(FIRST_NAME) + " "
                    + respondentValue.get(LAST_NAME));
                childRespondantRelation.put(CHILD_FULL_NAME, child.get(FIRST_NAME) + " " + child.get(LAST_NAME));
                childRespondantRelation.put("childAndRespondentRelation", relationship);
                childRespondantRelation.put(CHILD_LIVES_WITH, childLivesWith);
                childRespondantRelation.put("respondentId", String.valueOf(respondents.get(j).get(ID)));
                childRespondantRelation.put(CHILD_ID, String.valueOf(child.get(ID)));
                childRespondantRelations.add(childRespondantRelation);
            }
        }
        populatedMap.put("childAndRespondentRelations", childRespondantRelations);
    }

    private void transformApplicantChildRelationship(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<Map<String, Object>> childrenList = (List<Map<String, Object>>) populatedMap.get(NEW_CHILD_DETAILS);
        List<Map<String, Object>> applicants = (List<Map<String, Object>>) populatedMap.get(APPLICANTS);
        String key = "child%s_applicant%s_relationship";
        String childLivesWith = "";
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_LIVING_WITH_APPLICANT))) {
            childLivesWith = YES;
        } else {
            childLivesWith = NO;
        }
        List<Map<String, String>> childApplicantRelations = new ArrayList<>();
        for (int i = 0; i < childrenList.size(); i++) {
            Map<String, Object> child = childrenList.get(i);
            for (int j = 0; j < applicants.size(); j++) {
                Map<String, String> childApplicantRelation = new HashMap<>();
                Map<String, Object> applicantValue = (Map<String, Object>) applicants.get(j).get(VALUE);
                String relationship = inputFieldsMap.get(String.format(key, i, j));
                childApplicantRelation.put(APPLICANT_FULL_NAME, applicantValue.get(FIRST_NAME) + " "
                    + applicantValue.get(LAST_NAME));
                childApplicantRelation.put(CHILD_FULL_NAME, child.get(FIRST_NAME) + " " + child.get(LAST_NAME));
                childApplicantRelation.put(CHILD_AND_APPLICANT_RELATION, relationship);
                childApplicantRelation.put(CHILD_LIVES_WITH, childLivesWith);
                childApplicantRelation.put("applicantId", String.valueOf(applicants.get(j).get(ID)));
                childApplicantRelation.put(CHILD_ID, String.valueOf(child.get(ID)));
                childApplicantRelations.add(childApplicantRelation);
            }
        }
        populatedMap.put("childAndApplicantRelations", childApplicantRelations);
    }

    private void transformOtherPersonChildRelationship(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<Map<String, Object>> childrenList = (List<Map<String, Object>>) populatedMap.get(NEW_CHILD_DETAILS);
        List<Map<String, Object>> applicants = (List<Map<String, Object>>) populatedMap.get(APPLICANTS);
        String key = "child%s_applicant%s_relationship";
        String childLivesWith = "";
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_LIVING_WITH_OTHERS))) {
            childLivesWith = YES;
        } else {
            childLivesWith = NO;
        }
        List<Map<String, String>> childApplicantRelations = new ArrayList<>();
        for (int i = 0; i < childrenList.size(); i++) {
            Map<String, Object> child = childrenList.get(i);
            for (int j = 0; j < applicants.size(); j++) {
                Map<String, String> childApplicantRelation = new HashMap<>();
                String relationship = inputFieldsMap.get(String.format(key, i, j));
                childApplicantRelation.put(APPLICANT_FULL_NAME, applicants.get(j).get(FIRST_NAME) + " "
                    + applicants.get(j).get(LAST_NAME));
                childApplicantRelation.put(CHILD_FULL_NAME, child.get(FIRST_NAME) + " " + child.get(LAST_NAME));
                childApplicantRelation.put(CHILD_AND_APPLICANT_RELATION, relationship);
                childApplicantRelation.put(CHILD_LIVES_WITH, childLivesWith);
                childApplicantRelation.put("applicantId", String.valueOf(applicants.get(j).get(ID)));
                childApplicantRelation.put(CHILD_ID, String.valueOf(child.get(ID)));
                childApplicantRelations.add(childApplicantRelation);
            }
        }
        //populatedMap.put("childAndApplicantRelations", childApplicantRelations);
    }

    private void transformInitialChecks(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("isConsentOrder"))) {
            populatedMap.put("consentOrder", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("welshRequired"))) {
            populatedMap.put("welshLanguageRequirement", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("urgent_or_withoutHearing"))) {
            populatedMap.put("isCaseUrgent", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(HAVE_PREVIOUS_PARENTING_PLAN))) {
            populatedMap.put(HAVE_PREVIOUS_PARENTING_PLAN, YES);
        } else {
            populatedMap.put(HAVE_PREVIOUS_PARENTING_PLAN, NO);
        }
    }

    private void transformFlags(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<String> details = new ArrayList<>();
        if (StringUtils.isNotEmpty(inputFieldsMap.get(APPLICANT_ONE_FIRST_NAME))) {
            LinkedTreeMap caApplicant1ExternalFlags = (LinkedTreeMap) populatedMap.get(CA_APPLICANT_1_EXTERNAL_FLAGS);
            LinkedTreeMap caApplicant1InternalFlags = (LinkedTreeMap) populatedMap.get(CA_APPLICANT_1_INTERNAL_FLAGS);
            caApplicant1InternalFlags.put(PARTY_NAME, inputFieldsMap.get(APPLICANT_ONE_FIRST_NAME) + " "
                + inputFieldsMap.get("applicantOneLastName"));
            caApplicant1InternalFlags.put(ROLE_ON_CASE, "Applicant 1");
            caApplicant1ExternalFlags.put(DETAILS, details);
            caApplicant1InternalFlags.put(DETAILS, details);
            caApplicant1ExternalFlags.put(PARTY_NAME, inputFieldsMap.get(APPLICANT_ONE_FIRST_NAME) + " "
                + inputFieldsMap.get("applicantOneLastName"));
            caApplicant1ExternalFlags.put(ROLE_ON_CASE, "Applicant 1");
            populatedMap.put(CA_APPLICANT_1_EXTERNAL_FLAGS, caApplicant1ExternalFlags);
            populatedMap.put(CA_APPLICANT_1_INTERNAL_FLAGS, caApplicant1InternalFlags);
        } else {
            populatedMap.remove(CA_APPLICANT_1_EXTERNAL_FLAGS);
            populatedMap.remove(CA_APPLICANT_1_INTERNAL_FLAGS);
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get(APPLICANT_TWO_FIRST_NAME))) {
            LinkedTreeMap caApplicant2ExternalFlags = (LinkedTreeMap) populatedMap.get(CA_APPLICANT_2_EXTERNAL_FLAGS);
            LinkedTreeMap caApplicant2InternalFlags = (LinkedTreeMap) populatedMap.get(CA_APPLICANT_2_INTERNAL_FLAGS);
            caApplicant2InternalFlags.put(PARTY_NAME, inputFieldsMap.get(APPLICANT_TWO_FIRST_NAME) + " "
                + inputFieldsMap.get("applicantTwoLastName"));
            caApplicant2ExternalFlags.put(DETAILS, details);
            caApplicant2InternalFlags.put(DETAILS, details);
            caApplicant2InternalFlags.put(ROLE_ON_CASE, "Applicant 2");
            caApplicant2ExternalFlags.put(PARTY_NAME, inputFieldsMap.get(APPLICANT_TWO_FIRST_NAME) + " "
                + inputFieldsMap.get("applicantTwoLastName"));
            caApplicant2ExternalFlags.put(ROLE_ON_CASE, "Applicant 2");
            populatedMap.put(CA_APPLICANT_2_EXTERNAL_FLAGS, caApplicant2ExternalFlags);
            populatedMap.put(CA_APPLICANT_2_INTERNAL_FLAGS, caApplicant2InternalFlags);
        } else {
            populatedMap.remove(CA_APPLICANT_2_EXTERNAL_FLAGS);
            populatedMap.remove(CA_APPLICANT_2_INTERNAL_FLAGS);
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get(RESPONDENT_ONE_FIRST_NAME))) {
            LinkedTreeMap caRespondent1ExternalFlags = (LinkedTreeMap) populatedMap.get(CA_RESPONDENT_1_EXTERNAL_FLAGS);
            caRespondent1ExternalFlags.put(PARTY_NAME, inputFieldsMap.get(RESPONDENT_ONE_FIRST_NAME) + " "
                + inputFieldsMap.get("respondentOneLastName"));
            caRespondent1ExternalFlags.put(ROLE_ON_CASE, "Respondent 1");
            LinkedTreeMap caRespondent1InternalFlags = (LinkedTreeMap) populatedMap.get(CA_RESPONDENT_1_INTERNAL_FLAGS);
            caRespondent1InternalFlags.put(PARTY_NAME, inputFieldsMap.get(RESPONDENT_ONE_FIRST_NAME) + " "
                + inputFieldsMap.get("respondentOneLastName"));
            caRespondent1InternalFlags.put(ROLE_ON_CASE, "Respondent 1");
            caRespondent1ExternalFlags.put(DETAILS, details);
            caRespondent1InternalFlags.put(DETAILS, details);
            populatedMap.put(CA_RESPONDENT_1_EXTERNAL_FLAGS, caRespondent1ExternalFlags);
            populatedMap.put(CA_RESPONDENT_1_INTERNAL_FLAGS, caRespondent1InternalFlags);
        } else {
            populatedMap.put(CA_RESPONDENT_1_EXTERNAL_FLAGS, null);
            populatedMap.put(CA_RESPONDENT_1_INTERNAL_FLAGS, null);
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get(RESPONDENT_TWO_FIRST_NAME))) {
            LinkedTreeMap caRespondent2ExternalFlags = (LinkedTreeMap) populatedMap.get(CA_RESPONDENT_2_EXTERNAL_FLAGS);
            caRespondent2ExternalFlags.put(PARTY_NAME, inputFieldsMap.get(RESPONDENT_TWO_FIRST_NAME) + " "
                + inputFieldsMap.get("respondentTwoLastName"));
            caRespondent2ExternalFlags.put(ROLE_ON_CASE, "Respondent 2");
            LinkedTreeMap caRespondent2InternalFlags = (LinkedTreeMap) populatedMap.get(CA_RESPONDENT_2_EXTERNAL_FLAGS);
            caRespondent2InternalFlags.put(PARTY_NAME, inputFieldsMap.get(RESPONDENT_TWO_FIRST_NAME) + " "
                + inputFieldsMap.get("respondentTwoLastName"));
            caRespondent2ExternalFlags.put(DETAILS, details);
            caRespondent2InternalFlags.put(DETAILS, details);
            caRespondent2InternalFlags.put(ROLE_ON_CASE, "Respondent 2");
            populatedMap.put(CA_RESPONDENT_2_EXTERNAL_FLAGS, caRespondent2ExternalFlags);
            populatedMap.put(CA_RESPONDENT_2_INTERNAL_FLAGS, caRespondent2InternalFlags);
        } else {
            populatedMap.put(CA_RESPONDENT_2_EXTERNAL_FLAGS, null);
            populatedMap.put(CA_RESPONDENT_2_INTERNAL_FLAGS, null);
        }
    }

    private void transformPartyDetails(Map<String, Object> populatedMap,
                                       String partyType) {
        List<Map<String, Object>> parties = (List<Map<String, Object>>) populatedMap.get(partyType);
        populatedMap.put(partyType, parties.stream()
            .filter(party -> {
                Map<String, Object> partyValue = (Map<String, Object>) party.get(VALUE);
                return partyValue.get(FIRST_NAME) != null;
            })
            .map(party -> {
                party.put(ID, UUID.randomUUID());
                Map<String, Object> value = (Map<String, Object>) party.get(VALUE);
                if (ObjectUtils.isNotEmpty(value.get(DATE_OF_BIRTH))) {
                    value.put(DATE_OF_BIRTH, DateUtil.transformDate(value.get(DATE_OF_BIRTH).toString(),
                                                                    TEXT_AND_NUMERIC_MONTH_PATTERN,
                                                                    TWO_DIGIT_MONTH_FORMAT));
                    party.put(VALUE, value);
                }

                return party;
            }).toList());
    }

    private void transformAttendingTheCourt(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("applicantRequiresWelsh"))) {
            populatedMap.put("isWelshNeeded", YES);
            populateWelshNeeds(inputFieldsMap, populatedMap);
        } else {
            populatedMap.put("isWelshNeeded", NO);
        }
        populateInterpreterNeeds(inputFieldsMap, populatedMap);
        populateIntermediaryNeeds(inputFieldsMap, populatedMap);
        populateDisabilityNeeds(inputFieldsMap, populatedMap);
        populateSpecialArrangementNeeds(inputFieldsMap, populatedMap);
    }

    private void populateSpecialArrangementNeeds(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<String> specialArrangements = new ArrayList<>();

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("Attendence_separateWaitingRoom"))) {
            specialArrangements.add(SpecialMeasuresEnum.separateWaitingRoom.toString());
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("Attendence_separateEntryAndExit"))) {
            specialArrangements.add(SpecialMeasuresEnum.seperateEntranceExit.toString());
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("Attendence_privacyScreen"))) {
            specialArrangements.add(SpecialMeasuresEnum.shieldedByScreen.toString());
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("Attendence_joinHearingByVideo"))) {
            specialArrangements.add(SpecialMeasuresEnum.joinByVideoLink.toString());
        }
        if (!specialArrangements.isEmpty()) {
            populatedMap.put("isSpecialArrangementsRequired", YES);
            populatedMap.put("specialArrangementsRequired", String.join(",", specialArrangements));
        }
    }

    private void populateDisabilityNeeds(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("requiredSpecialAssistanceOrFacilities"))) {
            populatedMap.put("isDisabilityPresent", YES);
            if (StringUtils.isNotEmpty(inputFieldsMap.get("requiredSpecialAssistanceOrFacilities_details"))) {
                populatedMap.put("adjustmentsRequired",
                                 inputFieldsMap.get("requiredSpecialAssistanceOrFacilities_details"));
            }
        } else {
            populatedMap.put("isDisabilityPresent", NO);
        }
    }

    private void populateIntermediaryNeeds(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("applicantRequiresInterpreter_intermediaryRequired"))) {
            populatedMap.put("isIntermediaryNeeded", YES);
            if (StringUtils.isNotEmpty(inputFieldsMap.get("applicantRequiresInterpreter_intermediaryRequired_details"))) {
                populatedMap.put("reasonsForIntermediary",
                                 inputFieldsMap.get("applicantRequiresInterpreter_intermediaryRequired_details"));
            }
        } else {
            populatedMap.put("isIntermediaryNeeded", NO);
        }
    }

    private void populateInterpreterNeeds(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("applicantRequiresInterpreter"))) {
            populatedMap.put("isInterpreterNeeded", YES);
            Map<String, Object> interpreterNeed = new HashMap<>();
            interpreterNeed.put(ID, UUID.randomUUID());
            Map<String, Object> value = new HashMap<>();
            value.put(PARTY_ENUM, transformParty(inputFieldsMap));
            if (StringUtils.isNotEmpty(inputFieldsMap.get("applicantRequiresInterpreter_otherParty_dialect"))) {
                value.put("language", inputFieldsMap.get("applicantRequiresInterpreter_otherParty_dialect"));
            }
            if (StringUtils.isNotEmpty(inputFieldsMap.get("applicantRequiresInterpreter_otherParty_details"))) {
                value.put("otherAssistance", inputFieldsMap.get("applicantRequiresInterpreter_otherParty_details"));
            }
            interpreterNeed.put(VALUE, value);
            List<Map<String, Object>> interpreterNeeds = new ArrayList<>();
            interpreterNeeds.add(interpreterNeed);
        } else {
            populatedMap.put("isInterpreterNeeded", NO);
        }
    }

    private void transformLitigationCapacity(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (StringUtils.isNotEmpty(inputFieldsMap.get("factorAffectingLitigationCapacity"))) {
            populatedMap.put("litigationCapacityFactors", inputFieldsMap.get("factorAffectingLitigationCapacity"));
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("assessmentByAdultLearningTeam"))) {
            populatedMap.put("litigationCapacityReferrals", inputFieldsMap.get("assessmentByAdultLearningTeam"));
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("factorsAffectingPersonInCourt"))) {
            populatedMap.put("litigationCapacityOtherFactors", YES);
            populatedMap.put("litigationCapacityOtherFactorsDetails", inputFieldsMap.get("factorsAffectingPersonInCourt"));
        }
    }

    private void transformInternationalElements(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("internationalElement_Resident_of_another_state"))) {
            populatedMap.put("habitualResidentInOtherState", YES);
            if (StringUtils.isNotEmpty(inputFieldsMap.get("internationalElement_Resident_of_another_state_details"))) {
                populatedMap.put("habitualResidentInOtherStateGiveReason",
                                 inputFieldsMap.get("internationalElement_Resident_of_another_state_details"));
            }
        } else {
            populatedMap.put("habitualResidentInOtherState", NO);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("internationalElement_jurisdictionIssue"))) {
            populatedMap.put("jurisdictionIssue", YES);
            if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_jurisdictionIssue_details"))) {
                populatedMap.put("jurisdictionIssueGiveReason",
                                 inputFieldsMap.get("withoutNotice_jurisdictionIssue_details"));
            }
        } else {
            populatedMap.put("jurisdictionIssue", NO);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("internationalElement_request_toCentral_or_Consular_authority"))) {
            populatedMap.put("requestToForeignAuthority", YES);
            if (StringUtils.isNotEmpty(inputFieldsMap
                                           .get("internationalElement_request_toCentral_or_Consular_authority_details"))) {
                populatedMap.put("requestToForeignAuthorityGiveReason",
                                 inputFieldsMap.get("internationalElement_request_toCentral_or_Consular_authority_details"));
            }
        } else {
            populatedMap.put("requestToForeignAuthority", NO);
        }
    }

    private void transformPreviousOrOngoingProceedings(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("previous_or_ongoingProceeding"))) {
            populatedMap.put("previousOrOngoingProceedingsForChildren", "yes");
            Map<String, Object> proceeding = new HashMap<>();
            proceeding.put(ID, UUID.randomUUID());
            List<String> children = getChildrenInOtherProceedings(inputFieldsMap);
            Map<String, Object> value = new HashMap<>();
            value.put("nameOfChildrenInvolved", String.join(",", children));
            if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_otherReasons_name_of_the_court"))) {
                value.put("nameOfCourt", inputFieldsMap.get("withoutNotice_otherReasons_name_of_the_court"));
            }
            if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_otherReasons_CaseNo"))) {
                value.put("caseNumber", inputFieldsMap.get("withoutNotice_otherReasons_CaseNo"));
            }
            if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_otherReasons_CAFCASS_Name_and_officeAddress"))) {
                value.put("nameAndOffice", inputFieldsMap.get("withoutNotice_otherReasons_CAFCASS_Name_and_officeAddress"));
            }
            proceeding.put(VALUE, value);

            List<String> typeOfOrders = transformTypeOfOrder(inputFieldsMap);
            proceeding.put("typeOfOrder", typeOfOrders);
            List<Map<String, Object>> proceedings = new ArrayList<>();
            proceedings.add(proceeding);
            populatedMap.put("existingProceedings", proceedings);
        } else {
            populatedMap.put("previousOrOngoingProceedingsForChildren", "no");
        }
    }

    private List<String> getChildrenInOtherProceedings(Map<String, String> inputFieldsMap) {
        List<String> children = new ArrayList<>();
        if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_otherReasons_child1_name"))) {
            children.add(inputFieldsMap.get("withoutNotice_otherReasons_child1_name"));
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_otherReasons_child2_name"))) {
            children.add(inputFieldsMap.get("withoutNotice_otherReasons_child2_name"));
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_otherReasons_child3_name"))) {
            children.add(inputFieldsMap.get("withoutNotice_otherReasons_child3_name"));
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("withoutNotice_otherReasons_child4_name"))) {
            children.add(inputFieldsMap.get("withoutNotice_otherReasons_child4_name"));
        }
        return children;
    }

    private void transformMiamDetails(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("ExistingCase_onEmergencyProtection_Care_or_supervisionOrder"))) {
            populatedMap.put("mpuChildInvolvedInMiam", YES);
        } else {
            populatedMap.put("mpuChildInvolvedInMiam", NO);
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get("attended_MIAM"))) {
                populatedMap.put("mpuApplicantAttendedMiam", YES);
                populatedMap.put("soleTraderName", inputFieldsMap.get("soleTraderName"));
                populatedMap.put("mediatorRegistrationNumber", inputFieldsMap.get("fmcRegistrationNumber"));
                populatedMap.put("familyMediatorServiceName", inputFieldsMap.get("familyMediationServiceName"));
            } else {
                handleMiamExemptions(populatedMap, inputFieldsMap);
            }
        }
    }

    private void handleMiamExemptions(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("exemption_to_attend_MIAM"))) {
            populatedMap.put("mpuClaimingExemptionMiam", YesOrNo.Yes);
            populatedMap.put(
                MIAM_EXEMPTIONS_CHECKLIST, transformMiamExemptionsChecklist(inputFieldsMap));
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_DOMESTIC_VIOLENCE))) {
                populatedMap.put(
                    "mpuDomesticAbuseEvidences",
                    transformMiamDomesticViolenceChecklist(inputFieldsMap));
            }
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_CHILD_PROTECTION_CONCERNS))) {
                transformNoMiamChildProtectionConcerns(inputFieldsMap, populatedMap);
            }
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY))) {
                transformMiamUrgencyReasonChecklist(inputFieldsMap, populatedMap);
            }
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_PREVIOUS_ATTENDENCE))) {
                transformMiamPreviousAttendance(populatedMap, inputFieldsMap);
            }
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_OTHER_REASONS))) {
                transformMiamOtherReasonsExemptions(populatedMap, inputFieldsMap);
            }

        } else {
            populatedMap.put("mpuApplicantAttendedMiam", NO);
        }
    }

    private void transformMiamOtherReasonsExemptions(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("otherExemption_withoutNotice"))) {
            populatedMap.put(MPU_OTHER_EXEMPTION_REASONS, "miamPolicyUpgradeOtherGrounds_Value_1");
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("otherExemption_byVirture_of_Rule12_3"))) {
            populatedMap.put(MPU_OTHER_EXEMPTION_REASONS, "miamPolicyUpgradeOtherGrounds_Value_2");
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("otherExemption_mediator_notAvailableToConduct"))) {
            populatedMap.put(MPU_OTHER_EXEMPTION_REASONS, "miamPolicyUpgradeOtherGrounds_Value_3");
            populatedMap.put("mpuApplicantUnableToAttendMiamReason1", inputFieldsMap.get(
                OTHER_EXEMPTION_ADDITONAL_INFORMATION));
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("otherExemption_disability_or_inabilityToAttend"))) {
            populatedMap.put(MPU_OTHER_EXEMPTION_REASONS, "miamPolicyUpgradeOtherGrounds_Value_4");
            populatedMap.put("mpuApplicantUnableToAttendMiamReason1", inputFieldsMap.get(
                OTHER_EXEMPTION_ADDITONAL_INFORMATION));
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("otherExemption_NotSufficient_Respondent_ContactDetails"))) {
            populatedMap.put(MPU_OTHER_EXEMPTION_REASONS, "miamPolicyUpgradeOtherGrounds_Value_5");
            populatedMap.put("mpuApplicantUnableToAttendMiamReason2", inputFieldsMap.get(
                OTHER_EXEMPTION_ADDITONAL_INFORMATION));
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("otherExemption_applicant_or_respondent_inPrison"))) {
            populatedMap.put(MPU_OTHER_EXEMPTION_REASONS, "miamPolicyUpgradeOtherGrounds_Value_6");
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("otherExemption_NoEvidence_reason"))) {
            populatedMap.put(
                "mpuNoDomesticAbuseEvidenceReason",
                inputFieldsMap.get("otherExemption_NoEvidence_reason")
            );
        }
    }

    private void transformMiamPreviousAttendance(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("previousMIAM_nonCourtDisputeResolution_4month"))) {
            populatedMap.put("mpuPreviousMiamAttendanceReason", "miamPolicyUpgradePreviousAttendance_Value_1");
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("previousMIAM_existingProceeding_exepmtMIAM"))) {
            populatedMap.put("mpuPreviousMiamAttendanceReason", "miamPolicyUpgradePreviousAttendance_Value_2");
        }
    }

    private void transformChildDetails(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<Map<String, Object>> children = (List<Map<String, Object>>) populatedMap.get(NEW_CHILD_DETAILS);
        populatedMap.put(NEW_CHILD_DETAILS, populateChildren(children, inputFieldsMap));
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("children_of_same_parent"))) {
            populatedMap.put("isChildrenWithSameParents", "yes");
        } else {
            populatedMap.put("isChildrenWithSameParents", "no");
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("childrenServicesAuthority"))) {
            populatedMap.put("childrenKnownToLocalAuthority", "yes");
        } else {
            populatedMap.put("childrenKnownToLocalAuthority", "no");
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("subject_to_childProtectionPlan"))) {
            populatedMap.put("childrenSubjectOfChildProtectionPlan", "yes");
        } else {
            populatedMap.put("childrenSubjectOfChildProtectionPlan", "no");
        }
    }

    private List<Map<String, Object>> populateChildren(List<Map<String, Object>> children, Map<String, String> inputFieldsMap) {
        List<Map<String, Object>> childrenList = new ArrayList<>();
        for (Map<String, Object> child : children) {
            child.put(ID, UUID.randomUUID());
            Map<String, Object> childValue = (Map<String, Object>) child.get(VALUE);
            if (null != childValue.get(FIRST_NAME)) {
                childValue.put("parentalResponsibilityDetails", inputFieldsMap.get("parentalResponsibilityDetails"));
                if (ObjectUtils.isNotEmpty(childValue.get(DATE_OF_BIRTH))) {
                    childValue.put(DATE_OF_BIRTH, DateUtil.transformDate(childValue.get(DATE_OF_BIRTH).toString(),
                                                                         TEXT_AND_NUMERIC_MONTH_PATTERN,
                                                                         TWO_DIGIT_MONTH_FORMAT));
                }
                if ("male".equalsIgnoreCase((String) childValue.get(GENDER))) {
                    childValue.put(GENDER, "male");
                } else {
                    childValue.put(GENDER, "female");
                }
                child.put(VALUE, childValue);
                childrenList.add(child);
            }
        }
        return childrenList;
    }

    private void transformAllegationsOfHarm(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("domesticAbuse"))) {
            populatedMap.put("allegationsOfHarmDomesticAbuseYesNo", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("childAbduction"))) {
            populatedMap.put("allegationsOfHarmChildAbductionYesNo", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("childAbuse"))) {
            populatedMap.put("allegationsOfHarmChildAbuseYesNo", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("otherSafety_or_welfareAbuse"))) {
            populatedMap.put("allegationsOfHarmOtherConcernsYesNo", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("drugs_alcohol_substanceAbuse"))) {
            populatedMap.put("allegationsOfHarmSubstanceAbuseYesNo", YES);
        }
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
            partyDetails.add(PartyEnum.applicant.toString());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_REQUIRES_INTERPRETER_RESPONDENT))) {
            partyDetails.add(PartyEnum.respondent.toString());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_REQUIRES_INTERPRETER_OTHER_PARTY))) {
            partyDetails.add(PartyEnum.other.toString());
        }
        return partyDetails;
    }

    @SuppressWarnings("unchecked")
    private void transformHearingUrgency(
            Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        String urgencyReason = "";
        if (StringUtils.isNotEmpty(inputFieldsMap.get("urgency_reason"))) {
            urgencyReason = inputFieldsMap.get("urgency_reason");
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("order_direction_sought"))) {
            urgencyReason += inputFieldsMap.get("order_direction_sought");
        }
        if (StringUtils.isNotEmpty(inputFieldsMap.get("application_timetable"))) {
            urgencyReason += String.format("Application should be considered within %s hours/days",
                                           inputFieldsMap.get("application_timetable"));
        }
        populatedMap.put("caseUrgencyTimeAndReason", urgencyReason);
        if (StringUtils.isNotEmpty(inputFieldsMap.get("reason_for_consideration"))) {
            populatedMap.put("reasonsForApplicationWithoutNotice", inputFieldsMap.get("reason_for_consideration"));
            populatedMap.put("doYouNeedAWithoutNoticeHearing", YES);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("withoutNotice_abridged_or_informalNotice"))) {
            populatedMap.put("withoutAbridgedOrInformalNotice", YES);
            populatedMap.put(SET_OUT_REASONS_BELOW, inputFieldsMap.get(WITHOUT_NOTICE_ABRIDGED_OR_INFORMAL_NOTICE_REASONS));
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("withoutNotice_frustrateTheOrder"))) {
            populatedMap.put("doesWithNoticeHearingFrustrateRespondent", YES);
            populatedMap.put("reasonForFrustrationOfNotice", inputFieldsMap.get(WITHOUT_NOTICE_FRUSTRATE_THE_ORDER_REASON));
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
     * @param populatedMap All input key-value pair from transform object
     */
    private void transformMiamUrgencyReasonChecklist(
        Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_URGENCY_RISK_TO_LIFE_LIBERTY_OR_SAFETY))) {
            populatedMap.put(MIAM_URGENCY_REASON_CHECKLIST,
                    MiamUrgencyReasonChecklistEnum.miamPolicyUpgradeUrgencyReason_Value_1);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_RISK_OF_HARM))) {
            populatedMap.put(MIAM_URGENCY_REASON_CHECKLIST,
                    MiamUrgencyReasonChecklistEnum.miamPolicyUpgradeUrgencyReason_Value_2);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_RISK_TO_UNLAWFUL_REMOVAL))) {
            populatedMap.put(MIAM_URGENCY_REASON_CHECKLIST,
                    MiamUrgencyReasonChecklistEnum.miamPolicyUpgradeUrgencyReason_Value_3);
        }

        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_URGENCY_RISK_TO_MISCARRIAGE_OF_JUSTICE))) {
            populatedMap.put(MIAM_URGENCY_REASON_CHECKLIST,
                    MiamUrgencyReasonChecklistEnum.miamPolicyUpgradeUrgencyReason_Value_4);
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_UNREASONABLEHARDSHIP))) {
            populatedMap.put(MIAM_URGENCY_REASON_CHECKLIST,
                    MiamUrgencyReasonChecklistEnum.miamPolicyUpgradeUrgencyReason_Value_5);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY_IRRETRIEVABLE_PROBLEM))) {
            populatedMap.put(MIAM_URGENCY_REASON_CHECKLIST,
                    MiamUrgencyReasonChecklistEnum.miamPolicyUpgradeUrgencyReason_Value_6);
        }

        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_URGENCY_CONFLICT_WITH_OTHER_STATE_COURTS))) {
            populatedMap.put(MIAM_URGENCY_REASON_CHECKLIST,
                    MiamUrgencyReasonChecklistEnum.miamPolicyUpgradeUrgencyReason_Value_7);
        }
    }

    /**
     * C100 form Section 3b.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @param populatedMap All input key-value pair from transform object
     */
    private void transformNoMiamChildProtectionConcerns(
        Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(NO_MIAM_SUBJECT_OF_ENQUIRIES_BY_LOCAL_AUTHORITY))) {
            populatedMap.put(NO_MIAM_CHILD_PROTECTION_CONCERNS_CHECKLIST,
                             MiamChildProtectionConcernChecklistEnum.mpuChildProtectionConcern_value_1.toString());
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_SUBJECT_OF_CPP_BY_LOCAL_AUTHORITY))) {
            populatedMap.put(NO_MIAM_CHILD_PROTECTION_CONCERNS_CHECKLIST,
                             MiamChildProtectionConcernChecklistEnum.mpuChildProtectionConcern_value_2.toString());
        }
    }

    /**
     * C100 form beginning of Section 3.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of enum values for miamExemptionsChecklist field in the transformation output.
     */
    private List<String> transformMiamExemptionsChecklist(
            Map<String, String> inputFieldsMap) {
        List<String> miamExemptionsChecklist = new ArrayList<>();
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_DOMESTIC_VIOLENCE))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.domesticViolence.toString());
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_CHILD_PROTECTION_CONCERNS))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.childProtectionConcern.toString());
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_URGENCY))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.urgency.toString());
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_PREVIOUS_ATTENDENCE))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.previousMIAMattendance.toString());
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_OTHER_REASONS))) {
            miamExemptionsChecklist.add(MiamExemptionsChecklistEnum.other.toString());
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

        return miamDomesticViolenceChecklistEnumList.stream().sorted().toList();
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
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_1);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_RELEVANT_POLICE_CAUTION,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_2);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_RELEVANT_CRIMINAL_PROCEEDING,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_3);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_RELEVANT_CONVICTION,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_4);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_COURT_ORDER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_5);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_PROTECTION_NOTICE,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_6);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_PROTECTIVE_INJUNCTION,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_7);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_NO_CROSS_UNDERTAKING_GIVEN,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_8);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_COPY_OF_FACT_FINDING,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_9);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_EXPERT_EVIDENCE_REPORT,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_10);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_HEALTH_PROFESSIONAL_REPORT,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_11);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_REFERRAL_HEALTH_PROFESSIONAL_REPORT,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_12);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_MEMBER_OF_MULTI_AGENCY_RISK_ASSESSMENT_CONFERRANCE_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_13);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_DOMESTIC_VIOLENCE_ADVISOR,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_15);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_16);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_OFFICER_EMPLOYED_LOCAL_AUTHORITY_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_17);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_18);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_19);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_PUBLIC_AUTHORITY_CONFIRMATION_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_20);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_SECRETARY_OF_STATE_LETTER,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_21);
        miamDomesticViolenceChecklistEnumMap.put(
                NO_MIAM_DVE_EVIDENCE_FINANCIAL_MATTERS,
                MiamDomesticViolenceChecklistEnum.miamDomesticAbuseChecklistEnum_Value_22);
        return miamDomesticViolenceChecklistEnumMap;
    }

    private void transformPermissionRequiredFromCourt(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (YES.equalsIgnoreCase(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            populatedMap.put(APPLICATION_PERMISSION_REQUIRED, PermissionRequiredEnum.yes.getDisplayedValue());
        }
        if ("No, permission Not required".equals(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            populatedMap.put(APPLICATION_PERMISSION_REQUIRED, PermissionRequiredEnum.noNotRequired.getDisplayedValue());
        }
        if ("No, permission Now sought".equals(inputFieldsMap.get(PERMISSION_REQUIRED))) {
            populatedMap.put(APPLICATION_PERMISSION_REQUIRED, PermissionRequiredEnum.noNowSought.getDisplayedValue());
        }
    }

    /**
     * C100 form Above fields of Section 7.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @return list of values for typeoforder field in the transformation output.
     */
    private List<String> transformTypeOfOrder(Map<String, String> inputFieldsMap) {
        List<String> typeOfOrderField =
                getTypeOfOrderEnumFields().stream()
                        .filter(eachField -> TRUE.equalsIgnoreCase(inputFieldsMap.get(eachField)))
                    .map(orderType -> getTypeOfOrderEnumMapping().get(orderType))
                    .toList();
        return !typeOfOrderField.isEmpty() ? typeOfOrderField : null;
    }

    /**
     * <<<<<<< HEAD C100 form fields of Section 10.
     *
     * @param inputFieldsMap All input key-value pair from transformation request.
     * @param populatedMap All input key-value pair from transform object
     */
    private void populateWelshNeeds(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        List<Map<String, Object>> welshNeeds = new ArrayList<>();
        getWelshNeeds(inputFieldsMap, "1", welshNeeds);
        getWelshNeeds(inputFieldsMap, "2", welshNeeds);
        getWelshNeeds(inputFieldsMap, "3", welshNeeds);
        getWelshNeeds(inputFieldsMap, "4", welshNeeds);
        getWelshNeeds(inputFieldsMap, "5", welshNeeds);
        getWelshNeeds(inputFieldsMap, "6", welshNeeds);
        getWelshNeeds(inputFieldsMap, "7", welshNeeds);
        populatedMap.put(WELSH_NEEDS_CCD, welshNeeds);
    }

    private void getWelshNeeds(Map<String, String> inputFieldsMap, String personCount, List<Map<String, Object>> welshNeeds) {
        String personName = "nameOfPartyWhoNeedsWelsh_" + personCount;
        if (StringUtils.isNotEmpty(inputFieldsMap.get(personName))) {
            Map<String, Object> welshNeed = new HashMap<>();
            welshNeed.put(ID, UUID.randomUUID());
            String spoken = String.format("applicantRequiresWelsh_%s_Spoken", personCount);
            String written = String.format("applicantRequiresWelsh_%s_written", personCount);
            String both = String.format("applicantRequiresWelsh_%s_Both", personCount);
            List<String> spokenOrWritten = new ArrayList<>();
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(spoken))) {
                spokenOrWritten.add(SpokenOrWrittenWelshEnum.spoken.toString());
            }
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(written))) {
                spokenOrWritten.add(SpokenOrWrittenWelshEnum.written.toString());
            }
            if (TRUE.equalsIgnoreCase(inputFieldsMap.get(both))) {
                spokenOrWritten.add(SpokenOrWrittenWelshEnum.both.toString());
            }
            Map<String, Object> value = new HashMap<>();
            value.put("whoNeedsWelsh", inputFieldsMap.get(personName));
            value.put("spokenOrWritten", spokenOrWritten);
            welshNeed.put(VALUE, value);
            welshNeeds.add(welshNeed);
        }
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
            inputFieldsMap.put(RESPONDENT_WILLING_TO_ATTEND_MIAM, "No");
            inputFieldsMap.put(RESPONDENT_REASON_NOT_ATTENDING_MIAM, GroupMediatorCertifiesEnum
                .MEDIATION_NOT_PROCEEDING_APPLICATION_ATTENDED_MIAM_ALONE
                .getDescription());
        } else if (TRUE.equals(
                inputFieldsMap.get(
                        MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_FAILED_TO_ATTEND_MIAM_WITHOUT_GOOD_REASON))) {
            inputFieldsMap.put(RESPONDENT_WILLING_TO_ATTEND_MIAM, "No");
            inputFieldsMap.put(RESPONDENT_REASON_NOT_ATTENDING_MIAM, GroupMediatorCertifiesEnum
                    .MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_FAILED_TO_ATTEND_MIAM_WITHOUT_GOOD_REASON
                    .getDescription());
        } else if (TRUE.equals(
                inputFieldsMap.get(MEDIATION_NOT_SUITABLE_FOR_RESOLVING_THE_DISPUTE))) {
            inputFieldsMap.put(RESPONDENT_WILLING_TO_ATTEND_MIAM, "No");
            inputFieldsMap.put(
                RESPONDENT_REASON_NOT_ATTENDING_MIAM,
                GroupMediatorCertifiesEnum.MEDIATION_NOT_SUITABLE_FOR_RESOLVING_THE_DISPUTE.getDescription());
        } else {
            inputFieldsMap.put(RESPONDENT_WILLING_TO_ATTEND_MIAM, "No");
            inputFieldsMap.put(RESPONDENT_REASON_NOT_ATTENDING_MIAM, "");
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
            ? scannedDocumentsList.stream()
            .map(
                scanDocument ->
                    ResponseScanDocumentValueNew.builder()
                        .id(UUID.randomUUID().toString())
                        .value(
                            ResponseScanDocumentNew.builder()
                                .scannedDate(scanDocument.getScannedDate())
                                .controlNumber(scanDocument.getControlNumber())
                                .fileName(scanDocument.getFileName())
                                .url(ResponseScanDocument.builder().url(scanDocument.getScanDocument().getUrl())
                                .binaryUrl(scanDocument.getScanDocument().getBinaryUrl())
                                .filename(scanDocument.getScanDocument().getFilename())
                                .build()).build())
                        .build())
            .toList()
            : Collections.emptyList();
    }
}
