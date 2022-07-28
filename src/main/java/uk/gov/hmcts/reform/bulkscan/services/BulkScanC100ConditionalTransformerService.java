package uk.gov.hmcts.reform.bulkscan.services;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.ChildLiveWithEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamDomesticViolenceChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.PermissionRequiredEnum;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MIAM_DOMESTIC_VIOLENCE_CHECKLIST;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_ARRESTED_FOR_SIMILAR_OFFENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_RELEVANT_POLICE_CAUTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_RELEVANT_CRIMINAL_PROCEEDING;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_RELEVANT_CONVICTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_COURT_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_PROTECTION_NOTICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_PROTECTIVE_INJUNCTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_NO_CROSS_UNDERTAKING_GIVEN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_COPY_OF_FACT_FINDING;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_EXPERT_EVIDENCE_REPORT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_HEALTH_PROFESSIONAL_REPORT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_REFERRAL_HEALTH_PROFESSIONAL_REPORT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_MEMBER_OF_MULTI_AGENCY_RISK_ASSESSMENT_CONFERRANCE_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_DOMESTIC_VIOLENCE_ADVISOR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_OFFICER_EMPLOYED_LOCAL_AUTHORITY_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_PUBLIC_AUTHORITY_CONFIRMATION_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_SECRETARY_OF_STATE_LETTER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NO_MIAM_DVE_EVIDENCE_FINANCIAL_MATTERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

@Component
public class BulkScanC100ConditionalTransformerService {

    public void transform(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap,
                          BulkScanTransformationRequest bulkScanTransformationRequest) {
        populatedMap.put(APPLICATION_PERMISSION_REQUIRED, getApplicationPermissionRequired(inputFieldsMap));
        populatedMap.put(CHILD_LIVE_WITH_KEY, transformChildLiveWith(inputFieldsMap));
        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));
        populatedMap.put(MIAM_DOMESTIC_VIOLENCE_CHECKLIST, transformMiamDomesticViolenceChecklist(inputFieldsMap));
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

    private List<MiamDomesticViolenceChecklistEnum> transformMiamDomesticViolenceChecklist(
        Map<String, String> inputFieldsMap) {
        List<MiamDomesticViolenceChecklistEnum> miamDomesticViolenceChecklistEnumList = new ArrayList<>();
        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_ARRESTED_FOR_SIMILAR_OFFENCE))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_1);
        }
        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_RELEVANT_POLICE_CAUTION))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_2);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_RELEVANT_CRIMINAL_PROCEEDING))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_3);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_RELEVANT_CONVICTION))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_4);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_COURT_ORDER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_5);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_PROTECTION_NOTICE))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_6);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_PROTECTIVE_INJUNCTION))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_7);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_NO_CROSS_UNDERTAKING_GIVEN))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_8);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_COPY_OF_FACT_FINDING))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_9);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_EXPERT_EVIDENCE_REPORT))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_10);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_HEALTH_PROFESSIONAL_REPORT))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_11);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_REFERRAL_HEALTH_PROFESSIONAL_REPORT))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_12);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(
            NO_MIAM_DVE_MEMBER_OF_MULTI_AGENCY_RISK_ASSESSMENT_CONFERRANCE_LETTER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_13);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_DOMESTIC_VIOLENCE_ADVISOR))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_15);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR_LETTER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_16);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_OFFICER_EMPLOYED_LOCAL_AUTHORITY_LETTER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_17);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_LETTER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_18);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_19);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_PUBLIC_AUTHORITY_CONFIRMATION_LETTER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_20);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_SECRETARY_OF_STATE_LETTER))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_21);
        }

        if (BooleanUtils.TRUE.equals(inputFieldsMap.get(NO_MIAM_DVE_EVIDENCE_FINANCIAL_MATTERS))) {
            miamDomesticViolenceChecklistEnumList
                .add(MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_22);
        }
        return miamDomesticViolenceChecklistEnumList;
    }


    private String getApplicationPermissionRequired(Map<String, String> inputFieldsMap) {
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
}
