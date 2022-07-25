package uk.gov.hmcts.reform.bulkscan.helper;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants;
import uk.gov.hmcts.reform.bulkscan.enums.MaritalStatusEnum;
import uk.gov.hmcts.reform.bulkscan.enums.RelationToChildEnum;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_DAY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_MONTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_YEAR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_STATEMENT_OF_TRUTH_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_FIRSTNAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_DAY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_MONTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_YEAR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_STATEMENT_OF_TRUTH_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_CHILD_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_CHILD_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_PARTY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PARTY_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PARTY_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_PARTY_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_SPOKEN_IN_COURT_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_WITNESS_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_WITNESS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WITNESS_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WITNESS_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.APPLICANT_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_LANGUAGE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_LANGUAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SLASH_DELIMITER;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.OTHER_PARENT_RELATIONSHIP_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.RELATIONSHIP_FATHER;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.FATHER;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.OTHER_PARENT;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.APPLICANTS_DOMICILE_STATUS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANTS_NON_DOMICILE_STATUS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_MOTHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_NON_CIVIL_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.APPLICANT_MARITAL_STATUS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_SINGLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_DIVORCED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_WIDOW;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_NOTFOUND;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_SEPARATED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_INCAPABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_NATURAL_PARENT_DIED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_NATURAL_PARENT_NOT_FOUND;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_NO_OTHER_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_OTHER_PARENT_EXCLUSION_JUSTIFIED;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.RELATIONSHIP_OTHER;

@SuppressWarnings("PMD.GodClass")
@Component
public class BulkScanTransformerExtenderHelper {

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
        if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_placementOrderByEnglandAndWalesCourt"))) {
            populatedMap.put("placementOrderCourt",
                             inputFieldsMap.get("child_placmentOrderByEnglandAndWalesCourtName"));
            populatedMap.put("placementOrderId",
                             inputFieldsMap.get("child_placmentOrderByEnglandAndWalesCaseNumber"));
            populatedMap.put("placementOrderType",
                             inputFieldsMap.get("child_placmentOrderByEnglandAndWalesTypeOfOrder"));
            populatedMap.put("placementOrderDate",
                             inputFieldsMap.get("child_placmentOrderByEnglandAndWalesDateOfOrder"));
        } else if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_freeingOrderByEnglandAndWalesCourt"))) {
            populatedMap.put("freeingOrderCourt",
                             inputFieldsMap.get("child_freeingOrderByEnglandAndWalesCourtName"));
            populatedMap.put("freeingOrderId",
                             inputFieldsMap.get("child_freeingOrderByEnglandAndWalesCaseNumber"));
            populatedMap.put("freeingOrderType",
                             inputFieldsMap.get("child_freeingOrderByEnglandAndWalesTypeOfOrder"));
            populatedMap.put("freeingOrderDate",
                             inputFieldsMap.get("child_freeingOrderByEnglandAndWalesDateOfOrder"));
        } else if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_freeingOrderByNorthernIrelandCourt"))) {
            populatedMap.put("freeingOrderCourt",
                             inputFieldsMap.get("child_freeingOrderByNorthernIrelandCourtName"));
            populatedMap.put("freeingOrderId",
                             inputFieldsMap.get("child_freeingOrderByNorthernIrelandCaseNumber"));
            populatedMap.put("freeingOrderType",
                             inputFieldsMap.get("child_freeingOrderByNorthernIrelandTypeOfOrder"));
            populatedMap.put("freeingOrderDate",
                             inputFieldsMap.get("child_freeingOrderByNorthernIrelandDateOfOrder"));
        } else if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get("child_permanenceOrderByScotlandCourt"))) {
            populatedMap.put("freeingOrderCourt",
                             inputFieldsMap.get("child_permanenceOrderByScotlandCourtName"));
            populatedMap.put("freeingOrderId",
                             inputFieldsMap.get("child_permanenceOrderByScotlandCaseNumber"));
            populatedMap.put("freeingOrderType",
                             inputFieldsMap.get("child_permanenceOrderByScotlandTypeOfOrder"));
            populatedMap.put("freeingOrderDate",
                             inputFieldsMap.get("child_permanenceOrderByScotlandDateOfOrder"));
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noOrderAvailable"))) {
            populatedMap.put("anyOtherOrdersAvailable", "yes");
        } else if (FALSE.equalsIgnoreCase(inputFieldsMap.get("child_noOrderAvailable"))) {
            populatedMap.put("anyOtherOrdersAvailable", "no");
        }
    }

    public void buildChildNoLaOrParentalResponsibility(Map<String, String> inputFieldsMap,
                                                       Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noLaOrParentalResponsibility"))) {
            populatedMap.put("laOrParentalResponsibility", "yes");
        } else if (FALSE.equalsIgnoreCase(inputFieldsMap.get("child_noLaOrParentalResponsibility"))) {
            populatedMap.put("laOrParentalResponsibility", "no");
        }
    }

    @SuppressWarnings("unchecked")
    public void buildChildMaintanenceOrder(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        // nameOfCourt: child_courtAndDateOfOrder.
        // dateOfOrder: child_courtAndDateOfOrder
        Optional<Object> maintanenceOrderOptional = Optional.ofNullable(populatedMap.get("maintanenceOrder"));
        if (maintanenceOrderOptional.isPresent()) {
            LinkedHashMap maintanenceOrder = (LinkedHashMap) populatedMap.get("maintanenceOrder");
            Optional<String> childCourtAndDateOfOrderOptional =
                Optional.ofNullable(inputFieldsMap.get("child_courtAndDateOfOrder"));
            childCourtAndDateOfOrderOptional.ifPresent(childCourtAndDateOfOrder -> {
                maintanenceOrder.put("nameOfCourt", childCourtAndDateOfOrder.split(" ")[0]);
                maintanenceOrder.put("dateOfOrder", childCourtAndDateOfOrder.split(" ")[1]);
            });
            populatedMap.put("maintanenceOrder", maintanenceOrder);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noMaintanenceOrder"))) {
            populatedMap.put("hasMaintanenceOrder", "no");
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_maintanenceOrder"))) {
            populatedMap.put("hasMaintanenceOrder", "yes");
        }
    }

    public void buildChildProceedingDetails(Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noProceedingDetails"))) {
            populatedMap.put("hasProceedingDetails", "no");
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_proceedingDetails"))) {
            populatedMap.put("hasProceedingDetails", "yes");
        }
    }

    public void buildChildProceedingDetailsWithRelation(Map<String, String> inputFieldsMap,
                                                        Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_noProceedingDetailsWithRelation"))) {
            populatedMap.put("hasProceedingDetailsWithRelation", "no");
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_proceedingDetailsWithRelation"))) {
            populatedMap.put("hasProceedingDetailsWithRelation", "yes");
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get("child_dontKnowProceedingDetailsWithRelation"))) {
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
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(BulkScanConstants.APPLICANTS_DOMICILE_STATUS))) {
            return "true";
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANTS_NON_DOMICILE_STATUS))) {
            return "false";
        }
        return "";
    }

    private String getAplicantRelationToChild(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER))) {
            return RelationToChildEnum.FATHER.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_MOTHER_PARTNER))) {
            return RelationToChildEnum.MOTHER.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_NON_CIVIL_PARTNER))) {
            return RelationToChildEnum.CIVIL.getName();
        }
        return "";
    }

    private String getApplicantMaritalStatus(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_MARITAL_STATUS_SINGLE))) {
            return MaritalStatusEnum.SINGLE.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_MARITAL_STATUS_DIVORCED))) {
            return MaritalStatusEnum.DIVORCED.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_MARITAL_STATUS_WIDOW))) {
            return MaritalStatusEnum.WIDOW.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_NOTFOUND))) {
            return MaritalStatusEnum.SPOUSE_NOT_FOUND.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_SEPARATED))) {
            return MaritalStatusEnum.SPOUSE_SEPARATED.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_INCAPABLE))) {
            return MaritalStatusEnum.SPOUSE_INCAPABLE.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_APPLYING_ALONE_NATURAL_PARENT_DIED))) {
            return MaritalStatusEnum.NATURAL_PARAENT_DIED.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_APPLYING_ALONE_NATURAL_PARENT_NOT_FOUND))) {
            return MaritalStatusEnum.NATURAL_PARENT_NOT_FOUND.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_APPLYING_ALONE_NO_OTHER_PARENT))) {
            return MaritalStatusEnum.NO_OTHER_PARENT.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_APPLYING_ALONE_OTHER_PARENT_EXCLUSION_JUSTIFIED))) {
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
