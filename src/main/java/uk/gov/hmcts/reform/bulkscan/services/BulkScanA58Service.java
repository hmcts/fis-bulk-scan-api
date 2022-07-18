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
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.StringUtils.isNotEmpty;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT_ADVANCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_CONSENT_AGENCY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_ORDER_NO_CONSENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_DAY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_MONTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_YEAR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_STATEMENT_OF_TRUTH_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_FIRSTNAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_LEGAL_REP_SIGNATURE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_LEGAL_REP_SIGNING;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_LEGAL_REP_SOT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SIGNING;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_DAY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_MONTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_YEAR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_STATEMENT_OF_TRUTH_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_CHILD_WELFARE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_PARENT_LACK_CAPACITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_CONSENT_PARENT_NOT_FOUND;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_LANGUAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_LANGUAGE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PARTY_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PARTY_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.UNKNOWN_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_CHILD_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_CHILD_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_PARTY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_PARTY_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_WITNESS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_WITNESS_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_SPOKEN_IN_COURT_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WITNESS_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WITNESS_WELSH_LANGUAGE_PREFERENCE_CCD;
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
import static uk.gov.hmcts.reform.bulkscan.model.Status.ERRORS;

@Service
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.GodClass"})
public class BulkScanA58Service implements BulkScanService {

    public static final String STEP_PARENT_ADOPTION = "Step Parent";
    public static final String SLASH_DELIMITER = "/";
    public static final String YES_VALUE = "Yes";

    public static final String SCAN_DOCUMENTS = "scannedDocuments";
    public static final String OTHER_PARENT_RELATIONSHIP_TO_CHILD = "otherParentRelationshipToChild";
    public static final String RELATIONSHIP_FATHER = "child_relationship_father";
    public static final String RELATIONSHIP_OTHER = "child_relationship_other";
    public static final String FATHER = "Father";
    public static final String OTHER_PARENT = "Other parent";
    public static final String APPLICANTS_DOMICILE_STATUS = "applicantsDomicileStatus";
    public static final String APPLICANT_RELATION_TO_CHILD = "applicantRelationToChild";
    public static final String APPLICANT_MARITAL_STATUS = "applicantMaritalStatus";

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

        List<OcrDataField> inputFieldsList = bulkRequest.getOcrdatafields();

        Map<String, String> inputFieldsMap = getOcrDataFieldAsMap(inputFieldsList);

        if (isA58ParentFormType(getOcrDataFieldAsMap(bulkRequest.getOcrdatafields()))) {
            formType = A58_STEP_PARENT;
        } else if (isA58RelinquishedAdoptionFormType(inputFieldsMap)) {
            formType = A58_RELINQUISHED_ADOPTION;
        }
        // Validating the Fields..
        BulkScanValidationResponse bulkScanValidationResponse =
                bulkScanValidationHelper.validateMandatoryAndOptionalFields(bulkRequest.getOcrdatafields(),
                configManager.getValidationConfig(formType));

        if (formType.equals(A58) && isNotEmpty(inputFieldsMap.get(APPLICANT2_FIRSTNAME))) {
            List<String> errorList = getApplicant2ErrorList(inputFieldsMap, bulkScanValidationResponse);
            return bulkScanValidationResponse.toBuilder()
                    .status(!errorList.isEmpty() ? ERRORS : bulkScanValidationResponse.getStatus())
                    .errors(Errors.builder().items(errorList).build()).build();
        }
        return bulkScanValidationResponse;
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
            populatedMap.put(APPLICANTS_DOMICILE_STATUS, getDomicileStatus(inputFieldsMap));
            populatedMap.put(APPLICANT_RELATION_TO_CHILD, getAplicantRelationToChild(inputFieldsMap));
            // Marital status should be read if relation to child is null.
            if (populatedMap.get(APPLICANT_RELATION_TO_CHILD) == null
                    || StringUtils.isEmpty((String)populatedMap.get(APPLICANT_RELATION_TO_CHILD))) {
                populatedMap.put(APPLICANT_MARITAL_STATUS, getApplicantMaritalStatus(inputFieldsMap));
            }
            populatedMap.put(OTHER_PARENT_RELATIONSHIP_TO_CHILD, buildRelationshipToChild(inputFieldsMap));
            populatedMap.put(APPLICANT1_STATEMENT_OF_TRUTH_DATE, buildApplicant1StatementOfTruth(inputFieldsMap));
            if (isNotEmpty(inputFieldsMap.get(APPLICANT2_FIRSTNAME))) {
                populatedMap.put(APPLICANT2_STATEMENT_OF_TRUTH_DATE, buildApplicant2StatementOfTruth(inputFieldsMap));
            }
            if (isNotEmpty(inputFieldsMap.get(APPLICANT2_FIRSTNAME))) {
                populatedMap.put(APPLICANT2_STATEMENT_OF_TRUTH_DATE, buildApplicant2StatementOfTruth(inputFieldsMap));
            }
            buildWelshSpokenPreferences(inputFieldsMap, populatedMap);
            buildInterpreterRequiredFields(inputFieldsMap, populatedMap);
            buildSpecialAssistanceRequiredFields(inputFieldsMap, populatedMap);
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

    private String buildRelationshipToChild(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(RELATIONSHIP_FATHER))) {
            return FATHER;
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get(RELATIONSHIP_OTHER))) {
            return OTHER_PARENT;
        }
        return null;
    }

    private String getApplicantMaritalStatus(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_single))) {
            return MaritalStatusEnum.SINGLE.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_divorced))) {
            return MaritalStatusEnum.DIVORCED.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_widow))) {
            return MaritalStatusEnum.WIDOW.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_married_spouse_notfound))) {
            return MaritalStatusEnum.SPOUSE_NOT_FOUND.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_married_spouse_separated))) {
            return MaritalStatusEnum.SPOUSE_SEPARATED.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_marital_status_married_spouse_incapable))) {
            return MaritalStatusEnum.SPOUSE_INCAPABLE.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_natural_parent_died))) {
            return MaritalStatusEnum.NATURAL_PARAENT_DIED.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_natural_parent_not_found))) {
            return MaritalStatusEnum.NATURAL_PARENT_NOT_FOUND.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_no_other_parent))) {
            return MaritalStatusEnum.NO_OTHER_PARENT.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_applying_alone_other_parent_exclusion_justified))) {
            return MaritalStatusEnum.OTHER_PARENT_EXCLUSION_JUSTIFIED.getName();
        }
        return "";
    }

    private String getAplicantRelationToChild(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_relationToChild_father_partner))) {
            return RelationToChildEnum.FATHER.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_relationToChild_mother_partner))) {
            return RelationToChildEnum.MOTHER.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(applicant_relationToChild_non_civil_partner))) {
            return RelationToChildEnum.CIVIL.getName();
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

    private List<String> getApplicant2ErrorList(Map<String, String> inputFieldsMap,
                                                BulkScanValidationResponse bulkScanValidationResponse) {
        return flattenLists(
                bulkScanValidationResponse.getErrors().getItems(),
                notNull(inputFieldsMap.get(APPLICANT2_SOT), APPLICANT2_SOT),
                notNull(inputFieldsMap.get(APPLICANT2_LEGAL_REP_SOT), APPLICANT2_LEGAL_REP_SOT),
                notNull(inputFieldsMap.get(APPLICANT2_LEGAL_REP_SIGNATURE), APPLICANT2_LEGAL_REP_SIGNATURE),
                notNull(inputFieldsMap.get(APPLICANT2_SIGNING), APPLICANT2_SIGNING),
                notNull(inputFieldsMap.get(APPLICANT2_LEGAL_REP_SIGNING), APPLICANT2_LEGAL_REP_SIGNING),
                notNull(inputFieldsMap.get(APPLICANT2_SOT_DAY), APPLICANT2_SOT_DAY),
                notNull(inputFieldsMap.get(APPLICANT2_SOT_MONTH), APPLICANT2_SOT_MONTH),
                notNull(inputFieldsMap.get(APPLICANT2_SOT_YEAR), APPLICANT2_SOT_YEAR)
        );
    }

    private static List<String> notNull(String value, String field) {
        return isEmpty(value) ? List.of(String.format(MANDATORY_ERROR_MESSAGE, field)) : emptyList();
    }

    @SafeVarargs
    public static <E> List<E> flattenLists(List<E>... lists) {
        return Arrays.stream(lists).flatMap(Collection::stream).collect(toList());
    }

    private String buildApplicant1StatementOfTruth(Map<String, String> inputFieldsMap) {
        return inputFieldsMap.get(APPLICANT1_SOT_DAY) + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT1_SOT_MONTH)
                + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT1_SOT_YEAR);
    }

    private String buildApplicant2StatementOfTruth(Map<String, String> inputFieldsMap) {
        return inputFieldsMap.get(APPLICANT2_SOT_DAY) + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT2_SOT_MONTH)
                + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT2_SOT_YEAR);
    }

    private void buildSpecialAssistanceRequiredFields(Map<String, String> inputFieldsMap,
                                                      Map<String, Object> populatedMap) {
        if (YES_VALUE.equalsIgnoreCase(inputFieldsMap.get(SPECIAL_ASSISTANCE_FACILITIES_REQUIRED))) {
            populatedMap.put(SPECIAL_ASSISTANCE_FACILITIES_CCD, inputFieldsMap.get(SPECIAL_ASSISTANCE_FACILITIES));
        }
    }

    private void buildInterpreterRequiredFields(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (YES_VALUE.equalsIgnoreCase(inputFieldsMap.get(COURT_INTERPRETER_ASSISTANCE_REQUIRED))) {
            populatedMap.put(APPLICANT_REQUIRE_INTERPRETER_CCD, inputFieldsMap.get(APPLICANT_REQUIRE_INTERPRETER));
            populatedMap.put(RESPONDENT_REQUIRE_INTERPRETER_CCD, inputFieldsMap.get(RESPONDENT_REQUIRE_INTERPRETER));
            populatedMap.put(OTHER_PARTY_REQUIRE_INTERPRETER_CCD, inputFieldsMap.get(OTHER_PARTY_REQUIRE_INTERPRETER));
            populatedMap.put(OTHER_PARTY_NAME_CCD, inputFieldsMap.get(OTHER_PARTY_NAME));
            populatedMap.put(COURT_INTERPRETER_ASSISTANCE_LANGUAGE_CCD,
                    inputFieldsMap.get(COURT_INTERPRETER_ASSISTANCE_LANGUAGE));
        }
    }

    private void buildWelshSpokenPreferences(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (YES_VALUE.equalsIgnoreCase(inputFieldsMap.get(WELSH_SPOKEN_IN_COURT_REQUIRED))) {
            populatedMap.put(WELSH_PREFERENCE_PARTY_NAME_CCD, inputFieldsMap.get(WELSH_PREFERENCE_PARTY_NAME));
            populatedMap.put(WELSH_PREFERENCE_WITNESS_NAME_CCD, inputFieldsMap.get(WELSH_PREFERENCE_WITNESS_NAME));
            populatedMap.put(WELSH_PREFERENCE_CHILD_NAME_CCD, inputFieldsMap.get(WELSH_PREFERENCE_CHILD_NAME));
            populatedMap.put(PARTY_WELSH_LANGUAGE_PREFERENCE_CCD, inputFieldsMap.get(PARTY_WELSH_LANGUAGE_PREFERENCE));
            populatedMap.put(WITNESS_WELSH_LANGUAGE_PREFERENCE_CCD,
                    inputFieldsMap.get(WITNESS_WELSH_LANGUAGE_PREFERENCE));
            populatedMap.put(CHILD_WELSH_LANGUAGE_PREFERENCE_CCD, inputFieldsMap.get(CHILD_WELSH_LANGUAGE_PREFERENCE));
        }
    }
}
