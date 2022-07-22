package uk.gov.hmcts.reform.bulkscan.helper;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.MaritalStatusEnum;
import uk.gov.hmcts.reform.bulkscan.enums.RelationToChildEnum;

import java.util.Map;

import static com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.*;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.*;

@Component
public class BulkScanConditionalTransformerHelper {

    public void transform(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
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
        buildCourtInformation(inputFieldsMap, populatedMap);
        buildChildNoLaOrParentalResponsibility(inputFieldsMap, populatedMap);
        buildChildMaintanenceOrder(inputFieldsMap, populatedMap);
        buildChildProceedingDetails(inputFieldsMap, populatedMap);
        buildChildProceedingDetailsWithRelation(inputFieldsMap, populatedMap);

    }

    public void buildCourtInformation(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if(BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_placementOrderByEnglandAndWalesCourt"))) {
            populatedMap.put("placementOrderCourt", inputFieldsMap.get("child_placmentOrderByEnglandAndWalesCourtName"));
            populatedMap.put("placementOrderId", inputFieldsMap.get("child_placmentOrderByEnglandAndWalesCaseNumber"));
            populatedMap.put("placementOrderType", inputFieldsMap.get("child_placmentOrderByEnglandAndWalesTypeOfOrder"));
            populatedMap.put("placementOrderDate", inputFieldsMap.get("child_placmentOrderByEnglandAndWalesDateOfOrder"));
        } else if(BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_freeingOrderByEnglandAndWalesCourt"))) {
            populatedMap.put("freeingOrderCourt", inputFieldsMap.get("child_freeingOrderByEnglandAndWalesCourtName"));
            populatedMap.put("freeingOrderId", inputFieldsMap.get("child_freeingOrderByEnglandAndWalesCaseNumber"));
            populatedMap.put("freeingOrderType", inputFieldsMap.get("child_freeingOrderByEnglandAndWalesTypeOfOrder"));
            populatedMap.put("freeingOrderDate", inputFieldsMap.get("child_freeingOrderByEnglandAndWalesDateOfOrder"));
        } else if(BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_freeingOrderByNorthernIrelandCourt"))) {
            populatedMap.put("freeingOrderCourt", inputFieldsMap.get("child_freeingOrderByNorthernIrelandCourtName"));
            populatedMap.put("freeingOrderId", inputFieldsMap.get("child_freeingOrderByNorthernIrelandCaseNumber"));
            populatedMap.put("freeingOrderType", inputFieldsMap.get("child_freeingOrderByNorthernIrelandTypeOfOrder"));
            populatedMap.put("freeingOrderDate", inputFieldsMap.get("child_freeingOrderByNorthernIrelandDateOfOrder"));
        } else if(BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_permanenceOrderByScotlandCourt"))) {
            populatedMap.put("freeingOrderCourt", inputFieldsMap.get("child_permanenceOrderByScotlandCourtName"));
            populatedMap.put("freeingOrderId", inputFieldsMap.get("child_permanenceOrderByScotlandCaseNumber"));
            populatedMap.put("freeingOrderType", inputFieldsMap.get("child_permanenceOrderByScotlandTypeOfOrder"));
            populatedMap.put("freeingOrderDate", inputFieldsMap.get("child_permanenceOrderByScotlandDateOfOrder"));
        } else if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noOrderAvailable"))) {
            populatedMap.put("anyOtherOrdersAvailable", "yes");
        } else if(FALSE.equalsIgnoreCase(inputFieldsMap.get("child_noOrderAvailable"))) {
            populatedMap.put("anyOtherOrdersAvailable", "no");
        }
    }

    public void buildChildNoLaOrParentalResponsibility(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noLaOrParentalResponsibility"))) {
            populatedMap.put("laOrParentalResponsibility", "yes");
        } else if(FALSE.equalsIgnoreCase(inputFieldsMap.get("child_noLaOrParentalResponsibility"))) {
            populatedMap.put("laOrParentalResponsibility", "no");
        }
    }

    public void buildChildMaintanenceOrder(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noMaintanenceOrder"))) {
            populatedMap.put("hasMaintanenceOrder", "no");
        } else if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_maintanenceOrder"))) {
            populatedMap.put("hasMaintanenceOrder", "yes");
        }
    }

    public void buildChildProceedingDetails(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noProceedingDetails"))) {
            populatedMap.put("hasProceedingDetails", "no");
        } else if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_proceedingDetails"))) {
            populatedMap.put("hasProceedingDetails", "yes");
        }
    }

    public void buildChildProceedingDetailsWithRelation(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noProceedingDetailsWithRelation"))) {
            populatedMap.put("hasProceedingDetailsWithRelation", "no");
        } else if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_proceedingDetailsWithRelation"))) {
            populatedMap.put("hasProceedingDetailsWithRelation", "yes");
        } else if(TRUE.equalsIgnoreCase(inputFieldsMap.get("child_dontKnowProceedingDetailsWithRelation"))) {
            populatedMap.put("hasProceedingDetailsWithRelation", "DontKnow");
        }
    }

    private void buildWelshSpokenPreferences(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get(WELSH_SPOKEN_IN_COURT_REQUIRED))) {
            populatedMap.put(WELSH_PREFERENCE_PARTY_NAME_CCD, inputFieldsMap.get(WELSH_PREFERENCE_PARTY_NAME));
            populatedMap.put(WELSH_PREFERENCE_WITNESS_NAME_CCD, inputFieldsMap.get(WELSH_PREFERENCE_WITNESS_NAME));
            populatedMap.put(WELSH_PREFERENCE_CHILD_NAME_CCD, inputFieldsMap.get(WELSH_PREFERENCE_CHILD_NAME));
            populatedMap.put(PARTY_WELSH_LANGUAGE_PREFERENCE_CCD, inputFieldsMap.get(PARTY_WELSH_LANGUAGE_PREFERENCE));
            populatedMap.put(WITNESS_WELSH_LANGUAGE_PREFERENCE_CCD,
                             inputFieldsMap.get(WITNESS_WELSH_LANGUAGE_PREFERENCE));
            populatedMap.put(CHILD_WELSH_LANGUAGE_PREFERENCE_CCD, inputFieldsMap.get(CHILD_WELSH_LANGUAGE_PREFERENCE));
        }
    }

    private void buildInterpreterRequiredFields(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get(COURT_INTERPRETER_ASSISTANCE_REQUIRED))) {
            populatedMap.put(APPLICANT_REQUIRE_INTERPRETER_CCD, inputFieldsMap.get(APPLICANT_REQUIRE_INTERPRETER));
            populatedMap.put(RESPONDENT_REQUIRE_INTERPRETER_CCD, inputFieldsMap.get(RESPONDENT_REQUIRE_INTERPRETER));
            populatedMap.put(OTHER_PARTY_REQUIRE_INTERPRETER_CCD, inputFieldsMap.get(OTHER_PARTY_REQUIRE_INTERPRETER));
            populatedMap.put(OTHER_PARTY_NAME_CCD, inputFieldsMap.get(OTHER_PARTY_NAME));
            populatedMap.put(COURT_INTERPRETER_ASSISTANCE_LANGUAGE_CCD,
                             inputFieldsMap.get(COURT_INTERPRETER_ASSISTANCE_LANGUAGE));
        }
    }

    private void buildSpecialAssistanceRequiredFields(Map<String, String> inputFieldsMap,
                                                      Map<String, Object> populatedMap) {
        if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get(SPECIAL_ASSISTANCE_FACILITIES_REQUIRED))) {
            populatedMap.put(SPECIAL_ASSISTANCE_FACILITIES_CCD, inputFieldsMap.get(SPECIAL_ASSISTANCE_FACILITIES));
        }
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

    private String buildRelationshipToChild(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(RELATIONSHIP_FATHER))) {
            return FATHER;
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get(RELATIONSHIP_OTHER))) {
            return OTHER_PARENT;
        }
        return null;
    }

    private String buildApplicant1StatementOfTruth(Map<String, String> inputFieldsMap) {
        return inputFieldsMap.get(APPLICANT1_SOT_DAY) + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT1_SOT_MONTH)
            + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT1_SOT_YEAR);
    }

    private String buildApplicant2StatementOfTruth(Map<String, String> inputFieldsMap) {
        return inputFieldsMap.get(APPLICANT2_SOT_DAY) + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT2_SOT_MONTH)
            + SLASH_DELIMITER + inputFieldsMap.get(APPLICANT2_SOT_YEAR);
    }
}
