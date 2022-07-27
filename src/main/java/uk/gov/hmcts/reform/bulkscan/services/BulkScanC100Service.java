package uk.gov.hmcts.reform.bulkscan.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.enums.ChildLiveWithEnum;
import uk.gov.hmcts.reform.bulkscan.enums.DomesticViolenceEvidenceEnum;
import uk.gov.hmcts.reform.bulkscan.enums.MaritalStatusEnum;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;
import uk.gov.hmcts.reform.bulkscan.model.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.*;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_applying_alone_other_parent_exclusion_justified;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

@Service
public class BulkScanC100Service implements BulkScanService {

    public static final String SCAN_DOCUMENTS = "scannedDocuments";

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Autowired
    BulkScanValidationHelper bulkScanValidationHelper;

    @Override
    public FormType getCaseType() {
        return FormType.C100;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        // Validating the Fields..
        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(bulkRequest.getOcrdatafields());

        BulkScanValidationResponse response = bulkScanValidationHelper
            .validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(), configManager.getValidationConfig(
                                                                              FormType.C100));
        List<String> manualValidationErrors = doChildRelatedValidation(inputFieldsMap);
        if (!manualValidationErrors.isEmpty()) {
            response.setStatus(Status.ERRORS);
            response.getErrors().items.addAll(manualValidationErrors);
        }

        return response;
    }

    /**
     * 1. Checking if any one child living with option should be available
     * 2. if Child same parent is yes then parent name should have value
     * 3. if Child same parent is no then parent collection name should have value
     * 4. if social authority is yes then local authority or social worker field should have value
     * */
    List<String> doChildRelatedValidation(Map<String, String> inputFieldsMap) {
        List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(inputFieldsMap.get(CHILD_LIVING_WITH_APPLICANT))
            && StringUtils.isEmpty(inputFieldsMap.get(CHILD_LIVING_WITH_RESPONDENT))
            && StringUtils.isEmpty(inputFieldsMap.get(CHILD_LIVING_WITH_OTHERS))) {
            errors.add(String.format(XOR_CONDITIONAL_FIELDS_MESSAGE, CHILD_LIVING_WITH_APPLICANT
                .concat(",").concat(CHILD_LIVING_WITH_RESPONDENT).concat(",").concat(CHILD_LIVING_WITH_OTHERS)));
        }

        if (YES.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_OF_SAME_PARENT))
            && StringUtils.isEmpty(inputFieldsMap.get(CHILDREN_PARENTS_NAME))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILDREN_PARENTS_NAME));
        }

        if (NO.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_OF_SAME_PARENT))
            && StringUtils.isEmpty(inputFieldsMap.get(CHILDREN_PARENTS_NAME_COLLECTION))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILDREN_PARENTS_NAME_COLLECTION));
        }

        if (YES.equalsIgnoreCase(inputFieldsMap.get(CHILDREN_SOCIAL_AUTHORITY))
            && StringUtils.isEmpty(inputFieldsMap.get(CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER))) {
            errors.add(String.format(MANDATORY_ERROR_MESSAGE, CHILD_LOCAL_AUTHORITY_OR_SOCIAL_WORKER));
        }

        return errors;
    }


    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = inputFieldsList.stream().collect(
            Collectors.toMap(OcrDataField::getName, OcrDataField::getValue));

        Map<String, Object> populatedMap = (Map<String, Object>) BulkScanTransformHelper
            .transformToCaseData(new HashMap<>(transformConfigManager.getTransformationConfig(FormType.C100)
                    .getCaseDataFields()), inputFieldsMap);

        populatedMap.put(CHILD_LIVE_WITH_KEY, getChildLiveWith(inputFieldsMap));

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
            transformConfigManager.getTransformationConfig(FormType.C100).getCaseFields();


        return BulkScanTransformationResponse.builder().caseCreationDetails(
            CaseCreationDetails.builder()
                .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                .eventId(caseTypeAndEventId.get(EVENT_ID))
                .caseData(populatedMap).build()).build();
    }

    private String getChildLiveWith(Map<String, String> inputFieldsMap) {
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

    private String getApplicantDomesticViolenceEvidenceStatus(Map<String, String> inputFieldsMap) {
        if (YES.equalsIgnoreCase(inputFieldsMap.get(EVIDENCE_OF_ARREST_FOR_RELEVANT_DOMESTIC_VIOLENCE_OFFENCE))) {
            return DomesticViolenceEvidenceEnum.ARRESTED_FOR_DOMESTIC_VIOLENCE_OFFENCE.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(EVIDENCE_OF_POLICE_CAUTION_FOR_DOMESTIC_VIOLENCE_OFFENCE))) {
            return DomesticViolenceEvidenceEnum.POLICE_CAUTION_FOR_DOMESTIC_VIOLENCE_OFFENCE.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(EVIDENCE_OF_CRIMINAL_PROCEEDINGS_FOR_DOMESTIC_VIOLENCE_NOT_CONCLUDED))) {
            return DomesticViolenceEvidenceEnum.CRIMINAL_PROCEEDINGS_FOR_DOMESTIC_VIOLENCE_NOT_CONCLUDED.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(EVIDENCE_OF_CONVICTION_FOR_DOMESTIC_VIOLENCE_OFFENCE))) {
            return DomesticViolenceEvidenceEnum.CONVICTION_FOR_DOMESTIC_VIOLENCE_OFFENCE.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(
            COURT_ORDER_BINDING_PROSPECTIVE_PARTY_IN_CONNECTION_WITH_DOMESTIC_VIOLENCE_OFFENCE))) {
            return DomesticViolenceEvidenceEnum.COURT_ORDER_BINDING_PARTY_IN_CONNECTION_WITH_DOMESTIC_VIOLENCE.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(
            DOMESTIC_VIOLENCE_PROTECTION_NOTICE_ISSUED_S24_CSA_AGAINST_A_PROSPECTIVE_PARTY))) {
            return DomesticViolenceEvidenceEnum.DOMESTIC_VIOLENCE_PROTECTION_NOTICE_ISSUED_S24_CSA.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(RELEVANT_PROTECTIVE_INJUNCTION))) {
            return DomesticViolenceEvidenceEnum.PROTECTIVE_INJUNCTION.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(
            UNDERTAKING_S46_OR_63E_PROVIDED_CROSS_UNDERTAKING_RELATED_TO_DOMESTIC_VIOLENCE_NOT_GIVEN_BY_OTHER))) {
            return DomesticViolenceEvidenceEnum.UNDERTAKING_PROVIDED_CROSS_UNDERTAKING_RELATED_TO_DOMESTIC_VIOLENCE_NOT_GIVEN_BY_OTHER.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(COPY_OF_FINDING_FACT_OF_DOMESTIC_VIOLENCE))) {
            return DomesticViolenceEvidenceEnum.COPY_OF_FINDING_OF_FACT_IN_UK_OF_DOMESTIC_VIOLENCE.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(EXPERT_REPORT_EVIDENCE_PARTY_ASSESSED_AS_A_VICTIM))) {
            return DomesticViolenceEvidenceEnum.EXPERT_REPORT_ASSESSED_AS_A_VICTIM.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(HEALTH_PROFESSIONAL_REPORT_IN_PERSON_ON_DOMESTIC_ABUSE_INJURIES))) {
            return DomesticViolenceEvidenceEnum.HEALTH_PROFESSIONAL_REPORT_ON_DOMESTIC_ABUSE_INJURIES.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(REFERRAL_HEALTH_REPORT_TO_SPECIALIST_SUPPORT))) {
            return DomesticViolenceEvidenceEnum.REFERRAL_HEALTH_REPORT.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(MULTI_AGENCY_RISK_ASSESSMENT_CONFERENCE_LETTER_PART_AT_RISK))) {
            return DomesticViolenceEvidenceEnum.MULTI_AGENCY_RISK_ASSESSMENT_CONFERENCE_LETTER.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(DOMESTIC_VIOLENCE_ADVISOR_CONFIRMING_SUPPORT))) {
            return DomesticViolenceEvidenceEnum.DOMESTIC_VIOLENCE_ADVISOR.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(LETTER_FROM_INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR))) {
            return DomesticViolenceEvidenceEnum.INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(LETTER_LOCAL_AUTHORITY_FOR_SUPPORTING_TENANTS))) {
            return DomesticViolenceEvidenceEnum.LETTER_LOCAL_AUTHORITY.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(LETTER_FROM_DOMESTIC_VIOLENCE_SUPPORT_CHARITY))) {
            return DomesticViolenceEvidenceEnum.DOMESTIC_VIOLENCE_SUPPORT_CHARITY.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE))) {
            return DomesticViolenceEvidenceEnum.DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(PUBLIC_AUTHORITY_CONFIRMATION_LETTER_OR_COPY))) {
            return DomesticViolenceEvidenceEnum.PUBLIC_AUTHORITY_CONFIRMATION_LETTER.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(SECRETARY_OF_STATE))) {
            return DomesticViolenceEvidenceEnum.SECRETARY_OF_STATE_LETTER.getName();
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(EVIDENCE_OF_ABUSE_OF_FINANCIAL_MATTERS))) {
            return DomesticViolenceEvidenceEnum.EVIDENCE_OF_FINANCIAL_MATTERS.getName();
        }

        return "";
    }

}
