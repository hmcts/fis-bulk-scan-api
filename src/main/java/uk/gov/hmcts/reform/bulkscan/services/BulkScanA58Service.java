package uk.gov.hmcts.reform.bulkscan.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.enums.MaritalStatusEnum;
import uk.gov.hmcts.reform.bulkscan.enums.RelationToChildEnum;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanValidationHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT_ADVANCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT_AGENCY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_NO_CONSENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_CHILD_WELFARE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_PARENT_LACK_CAPACITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_PARENT_NOT_FOUND;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_applying_alone_natural_parent_died;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_applying_alone_natural_parent_not_found;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_applying_alone_no_other_parent;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_applying_alone_other_parent_exclusion_justified;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_marital_status_divorced;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_marital_status_married_spouse_incapable;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_marital_status_married_spouse_notfound;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_marital_status_married_spouse_separated;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_marital_status_single;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_marital_status_widow;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_relationToChild_father_partner;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_relationToChild_mother_partner;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicant_relationToChild_non_civil_partner;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicants_domicile_status;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.applicants_non_domicile_status;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A58;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A58_RELINQUISHED_ADOPTION;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.A58_STEP_PARENT;

@Service
@SuppressWarnings("PMD.ExcessiveImports")
public class BulkScanA58Service implements BulkScanService {

    public static final String STEP_PARENT_ADOPTION = "Step Parent";

    public static final String SCAN_DOCUMENTS = "scannedDocuments";
    private static final String applicantsDomicileStatus = "applicantsDomicileStatus";
    private static final String applicantRelationToChild = "applicantRelationToChild";
    private static final String applicantMarritalStatus = "applicantMarritalStatus";

    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Autowired
    BulkScanValidationHelper bulkScanValidationHelper;

    @Autowired
    BulkScanTransformConfigManager transformConfigManager;

    @Override
    public FormType getCaseType() {
        return A58;
    }

    @Override
    public BulkScanValidationResponse validate(BulkScanValidationRequest bulkRequest) {
        FormType formType = A58;
        if (isA58ParentFormType(getOcrDataFieldAsMap(bulkRequest.getOcrdatafields()))) {
            formType = A58_STEP_PARENT;
        }
        // Validating the Fields..
        return bulkScanValidationHelper.validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(),
                configManager.getValidationConfig(
                        formType));
    }

    @Override
    @SuppressWarnings("unchecked")
    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) {
        Map<String, Object> caseData = new HashMap<>();
        List<OcrDataField> inputFieldsList = bulkScanTransformationRequest.getOcrdatafields();

        FormType formType = A58;

        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        if (isA58ParentFormType(inputFieldsMap)) {
            formType = A58_STEP_PARENT;
        } else if (isA58RelinquishedAdoptionFormType(inputFieldsMap)) {
            formType = A58_RELINQUISHED_ADOPTION;
        }
        List<String> unknownFieldsList = null;

        BulkScanFormValidationConfigManager
                .ValidationConfig validationConfig = configManager.getValidationConfig(formType);
        if (nonNull(validationConfig)) {
            unknownFieldsList = bulkScanValidationHelper
               .findUnknownFields(inputFieldsList,
                                  validationConfig.getMandatoryFields(),
                                  validationConfig.getOptionalFields()
           );
        }
        Map<String, Object> populatedMap = (Map<String, Object>) BulkScanTransformHelper
                .transformToCaseData(new HashMap<>(transformConfigManager
                        .getTransformationConfig(formType).getCaseDataFields()), inputFieldsMap);

        // For A58 formtype we need to set some fields based on the Or Condition...
        if (formType.equals(A58)) {
            populatedMap.put(applicantsDomicileStatus, getDomicileStatus(inputFieldsMap));
            populatedMap.put(applicantRelationToChild, getAplicantRelationToChild(inputFieldsMap));
            // Marital status should be read if relation to child is null.
            if (populatedMap.get(applicantRelationToChild) == null
                || StringUtils.isEmpty((String)populatedMap.get(applicantRelationToChild))) {
                populatedMap.put(applicantMarritalStatus, getApplicantMarritalStatus(inputFieldsMap));
            }

        }

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId =
                transformConfigManager.getTransformationConfig(formType).getCaseFields();

        BulkScanTransformationResponse.BulkScanTransformationResponseBuilder builder = BulkScanTransformationResponse
                .builder().caseCreationDetails(
                        CaseCreationDetails.builder()
                                .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                .eventId(caseTypeAndEventId.get(EVENT_ID))
                                .caseData(populatedMap).build());
        if (null != unknownFieldsList && !unknownFieldsList.isEmpty()) {
            builder.warnings(Arrays.asList(String.format(UNKNOWN_FIELDS_MESSAGE,
                    String.join(",", unknownFieldsList))));
        }
        return builder.build();
    }

    private String getApplicantMarritalStatus(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_single))) {
            return MaritalStatusEnum.SINGLE.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_divorced))) {
            return MaritalStatusEnum.DIVORCED.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_widow))) {
            return MaritalStatusEnum.WIDOW.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_married_spouse_notfound))) {
            return MaritalStatusEnum.SPOUSE_NOT_FOUND.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_married_spouse_separated))) {
            return MaritalStatusEnum.SPOUSE_SEPARATED.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_married_spouse_incapable))) {
            return MaritalStatusEnum.SPOUSE_INCAPABLE.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_natural_parent_died))) {
            return MaritalStatusEnum.NATURAL_PARAENT_DIED.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_natural_parent_not_found))) {
            return MaritalStatusEnum.NATURAL_PARENT_NOT_FOUND.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_no_other_parent))) {
            return MaritalStatusEnum.NO_OTHER_PARENT.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_other_parent_exclusion_justified))) {
            return MaritalStatusEnum.OTHER_PARENT_EXCLUSION_JUSTIFIED.name();
        }

        return "";
    }

    private String getAplicantRelationToChild(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_relationToChild_father_partner))) {
            return RelationToChildEnum.FATHER.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_relationToChild_mother_partner))) {
            return RelationToChildEnum.MOTHER.name();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_relationToChild_non_civil_partner))) {
            return RelationToChildEnum.CIVIL.name();
        }
        return "";
    }

    private String getDomicileStatus(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicants_domicile_status))) {
            return "true";
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicants_non_domicile_status))) {
            return "false";
        }
        return "";
    }

    private boolean isA58RelinquishedAdoptionFormType(Map<String, String> inputFieldsMap) {
        return nonNull(inputFieldsMap.get(ADOPTION_ORDER_CONSENT))
            || nonNull(inputFieldsMap.get(ADOPTION_ORDER_CONSENT_ADVANCE))
            || nonNull(inputFieldsMap.get(ADOPTION_ORDER_CONSENT_AGENCY))
            || nonNull(inputFieldsMap.get(ADOPTION_ORDER_NO_CONSENT))
            || nonNull(inputFieldsMap.get(COURT_CONSENT_PARENT_NOT_FOUND))
            || nonNull(inputFieldsMap.get(COURT_CONSENT_PARENT_LACK_CAPACITY))
            || nonNull(inputFieldsMap.get(COURT_CONSENT_CHILD_WELFARE));
    }

    private boolean isA58ParentFormType(Map<String, String> inputFieldsMap) {
        return STEP_PARENT_ADOPTION.equalsIgnoreCase(inputFieldsMap.get(APPLICANT1_RELATION_TO_CHILD))
            || STEP_PARENT_ADOPTION.equalsIgnoreCase(inputFieldsMap.get(APPLICANT2_RELATION_TO_CHILD))
            || TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER))
            || FALSE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER));
    }
}
