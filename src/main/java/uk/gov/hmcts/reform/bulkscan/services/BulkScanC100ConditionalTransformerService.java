package uk.gov.hmcts.reform.bulkscan.services;

import com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.MiamDomesticViolenceChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MiamExemptionsChecklistEnum;
import uk.gov.hmcts.reform.bulkscan.enums.PermissionRequiredEnum;
import uk.gov.hmcts.reform.bulkscan.enums.ChildLiveWithEnum;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICATION_PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVE_WITH_KEY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_APPLICANT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_OTHERS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LIVING_WITH_RESPONDENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MIAM_DOMESTIC_VIOLENCE_CHECKLIST;
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
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PERMISSION_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_URGENCY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.MIAM_EXEMPTIONS_CHECKLIST;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_DOMESTIC_VIOLANCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_CHILD_PROTECTION_CONCERNS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_PREVIOUS_ATTENDENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants.NO_MIAM_OTHER_REASONS;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

@SuppressWarnings({"PMD.ExcessiveImports"})
@Component
public class BulkScanC100ConditionalTransformerService {

    public void transform(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap,
                          BulkScanTransformationRequest bulkScanTransformationRequest) {
        populatedMap.put(APPLICATION_PERMISSION_REQUIRED, transformPermissionRequired(inputFieldsMap));
        populatedMap.put(CHILD_LIVE_WITH_KEY, transformChildLiveWith(inputFieldsMap));
        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));
        populatedMap.put(MIAM_DOMESTIC_VIOLENCE_CHECKLIST, transformMiamDomesticViolenceChecklist(inputFieldsMap));
        populatedMap.put(MIAM_EXEMPTIONS_CHECKLIST, transformMiamExemptionsChecklist(inputFieldsMap));
    }

    private List<MiamExemptionsChecklistEnum> transformMiamExemptionsChecklist(Map<String, String> inputFieldsMap) {
        List<MiamExemptionsChecklistEnum> miamExemptionsChecklist = new ArrayList<>();
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(NO_MIAM_DOMESTIC_VIOLANCE))) {
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

    private List<MiamDomesticViolenceChecklistEnum> transformMiamDomesticViolenceChecklist(
        Map<String, String> inputFieldsMap) {

        Map<String, MiamDomesticViolenceChecklistEnum> miamDomesticViolenceChecklistEnumMap
            = getStringMiamDomesticViolenceChecklistEnumMap();

        List<MiamDomesticViolenceChecklistEnum> miamDomesticViolenceChecklistEnumList = new ArrayList<>();
        miamDomesticViolenceChecklistEnumMap.keySet()
            .forEach(key -> {
                Optional<String> inputFieldOptional = Optional.ofNullable(inputFieldsMap.get(key));
                inputFieldOptional.ifPresent(s -> {
                    if (BooleanUtils.TRUE.equals(inputFieldOptional.get())) {
                        miamDomesticViolenceChecklistEnumList
                            .add(miamDomesticViolenceChecklistEnumMap.get(key));
                    }
                });
            });

        return miamDomesticViolenceChecklistEnumList.stream().sorted().collect(Collectors.toList());
    }

    private Map<String, MiamDomesticViolenceChecklistEnum> getStringMiamDomesticViolenceChecklistEnumMap() {
        Map<String, MiamDomesticViolenceChecklistEnum> miamDomesticViolenceChecklistEnumMap
            = new HashMap<>();
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_ARRESTED_FOR_SIMILAR_OFFENCE,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_1);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_RELEVANT_POLICE_CAUTION,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_2);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_RELEVANT_CRIMINAL_PROCEEDING,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_3);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_RELEVANT_CONVICTION,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_4);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_COURT_ORDER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_5);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_PROTECTION_NOTICE,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_6);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_PROTECTIVE_INJUNCTION,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_7);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_NO_CROSS_UNDERTAKING_GIVEN,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_8);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_COPY_OF_FACT_FINDING,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_9);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_EXPERT_EVIDENCE_REPORT,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_10);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_HEALTH_PROFESSIONAL_REPORT,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_11);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_REFERRAL_HEALTH_PROFESSIONAL_REPORT,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_12);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_MEMBER_OF_MULTI_AGENCY_RISK_ASSESSMENT_CONFERRANCE_LETTER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_13);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_DOMESTIC_VIOLENCE_ADVISOR,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_15);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR_LETTER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_16);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_OFFICER_EMPLOYED_LOCAL_AUTHORITY_LETTER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_17);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_LETTER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_18);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_19);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_PUBLIC_AUTHORITY_CONFIRMATION_LETTER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_20);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_SECRETARY_OF_STATE_LETTER,
                 MiamDomesticViolenceChecklistEnum.miamDomesticViolenceChecklistEnum_Value_21);
        miamDomesticViolenceChecklistEnumMap
            .put(NO_MIAM_DVE_EVIDENCE_FINANCIAL_MATTERS,
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
}
