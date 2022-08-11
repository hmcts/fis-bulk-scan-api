package uk.gov.hmcts.reform.bulkscan.services;

import static com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.BooleanUtils.FALSE;
import static org.apache.commons.lang3.BooleanUtils.TRUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ABOUT_OTHER_ORDERS_OR_PROCEEDINGS_THAT_AFFECT_THE_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOPTION_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOP_AGENCY_OR_LAADDRESS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOP_AGENCY_OR_LA_CONTACT_EMAIL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOP_AGENCY_OR_LA_CONTACT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOP_AGENCY_OR_LA_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOP_AGENCY_OR_LA_PHONE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ADOP_AGENCY_OR_L_AS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.ANY_OTHER_ORDERS_AVAILABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_DAY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_MONTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_SOT_YEAR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT1_STATEMENT_OF_TRUTH_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_FIRSTNAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_DAY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_MONTH;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_SOT_YEAR;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT2_STATEMENT_OF_TRUTH_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANTS_NON_DOMICILE_STATUS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_NATURAL_PARENT_DIED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_NATURAL_PARENT_NOT_FOUND;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_NO_OTHER_PARENT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_APPLYING_ALONE_OTHER_PARENT_EXCLUSION_JUSTIFIED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_DIVORCED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_INCAPABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_NOTFOUND;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_SEPARATED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_SINGLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_MARITAL_STATUS_WIDOW;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_FATHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_MOTHER_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_RELATION_TO_CHILD_NON_CIVIL_PARTNER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.APPLICANT_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASES_CONCERNING_A_RELATED_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_CATEGORY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_ADOPTION_AGENCY_ADDRESS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_ADOPTION_AGENCY_CONTACT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_ADOPTION_AGENCY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_ADOPTION_AGENCY_TELEPHONE_NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_CASE_NUMBER_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_COURT_AND_DATE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_DATE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_DATE_OF_ORDER_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_DONT_KNOW_PROCEEDING_DETAILS_WITH_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_COURT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_DATE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_TYPE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_COURT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_DATE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_TYPE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_IS_ADOPTION_AGENCY_DETAILS_AVAILABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_IS_ADOPTION_AGENCY_INVOLVED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_ADDRESS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_CONTACT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_OR_PARENTAL_ADDRESS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_OR_PARENTAL_CONTACT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_OR_PARENTAL_EMAIL;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_OR_PARENTAL_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_OR_PARENTAL_RESPONSIBILITY_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_OR_PARENTAL_TELEPHONE_NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_LA_TELEPHONE_NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_MAINTANENCE_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_NAME_OF_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_NAME_OF_COURT_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_NO_MAINTANENCE_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_NO_ORDER_AVAILABLE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_NO_PROCEEDING_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_NO_PROCEEDING_DETAILS_WITH_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PERMANENCE_ORDER_BY_SCOTLAND_CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PERMANENCE_ORDER_BY_SCOTLAND_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PERMANENCE_ORDER_BY_SCOTLAND_COURT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PERMANENCE_ORDER_BY_SCOTLAND_DATE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PERMANENCE_ORDER_BY_SCOTLAND_TYPE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACED_ADOPTION_AGENCY_ADDRESS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACED_ADOPTION_AGENCY_CONTACT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACED_ADOPTION_AGENCY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACED_ADOPTION_AGENCY_TELEPHONE_NO;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACED_ADOPTION_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACED_FOR_THE_PURPOSE_OF_ADOPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACEMENT_ORDER_BY_ENGLAND_AND_WALES_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_CASE_NUMBER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_COURT_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_DATE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_TYPE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PROCEEDING_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_PROCEEDING_DETAILS_WITH_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_TYPE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_TYPE_OF_ORDER_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CHILD_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_LANGUAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_LANGUAGE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.COURT_INTERPRETER_ASSISTANCE_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DATE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.DONT_KNOW;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FREEING_ORDER_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FREEING_ORDER_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FREEING_ORDER_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.FREEING_ORDER_TYPE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.HAS_MAINTANENCE_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.HAS_PROCEEDING_DETAILS;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.HAS_PROCEEDING_DETAILS_WITH_RELATION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.INVOLVED_IN_THE_PLACING_OF_THE_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.LA_OR_PARENTAL_RESPONSIBILITY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.LOCAL_AUTHORITY_HAS_PARENTAL_RESPONSIBILITY_OF_THE_CHILD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.L_AOR_ADOPTION_AGENCY_CATEGORY;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MAINTANENCE_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NAME_OF_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.NOTIFY_LOCAL_AUTHORITY_INTENTION_OF_ADOPTION;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.OTHER_PARTY_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PARTY_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PARTY_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PLACEMENT_ORDER_COURT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PLACEMENT_ORDER_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PLACEMENT_ORDER_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.PLACEMENT_ORDER_TYPE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_REQUIRE_INTERPRETER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.RESPONDENT_REQUIRE_INTERPRETER_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SLASH_DELIMITER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SPECIAL_ASSISTANCE_FACILITIES_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.TYPE_OF_ORDER;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.VALUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_CHILD_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_CHILD_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_PARTY_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_PARTY_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_WITNESS_NAME;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_PREFERENCE_WITNESS_NAME_CCD;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WELSH_SPOKEN_IN_COURT_REQUIRED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WITNESS_WELSH_LANGUAGE_PREFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.WITNESS_WELSH_LANGUAGE_PREFERENCE_CCD;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.APPLICANTS_DOMICILE_STATUS;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.APPLICANT_MARITAL_STATUS;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.APPLICANT_RELATION_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.FATHER;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.OTHER_PARENT;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.OTHER_PARENT_RELATIONSHIP_TO_CHILD;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.RELATIONSHIP_FATHER;
import static uk.gov.hmcts.reform.bulkscan.services.BulkScanA58Service.RELATIONSHIP_OTHER;

import com.microsoft.applicationinsights.core.dependencies.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.MaritalStatusEnum;
import uk.gov.hmcts.reform.bulkscan.enums.RelationToChildEnum;

// Divided this class to multiple classes based on formtype and remove suppress warning
@SuppressWarnings({"PMD.GodClass", "PMD.ExcessiveImports"})
@Component
public class BulkScanA58ConditionalTransformerService {

    public void conditionalTransform(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        populatedMap.put(APPLICANTS_DOMICILE_STATUS, getDomicileStatus(inputFieldsMap));
        populatedMap.put(APPLICANT_RELATION_TO_CHILD, getAplicantRelationToChild(inputFieldsMap));

        // Marital status should be read if relation to child is null.
        if (populatedMap.get(APPLICANT_RELATION_TO_CHILD) == null
                || StringUtils.isEmpty((String) populatedMap.get(APPLICANT_RELATION_TO_CHILD))) {
            populatedMap.put(APPLICANT_MARITAL_STATUS, getApplicantMaritalStatus(inputFieldsMap));
        }

        populatedMap.put(
                OTHER_PARENT_RELATIONSHIP_TO_CHILD, buildRelationshipToChild(inputFieldsMap));
        populatedMap.put(
                APPLICANT1_STATEMENT_OF_TRUTH_DATE,
                buildApplicant1StatementOfTruth(inputFieldsMap));

        if (isNotEmpty(inputFieldsMap.get(APPLICANT2_FIRSTNAME))) {
            populatedMap.put(
                    APPLICANT2_STATEMENT_OF_TRUTH_DATE,
                    buildApplicant2StatementOfTruth(inputFieldsMap));
        }

        if (isNotEmpty(inputFieldsMap.get(APPLICANT2_FIRSTNAME))) {
            populatedMap.put(
                    APPLICANT2_STATEMENT_OF_TRUTH_DATE,
                    buildApplicant2StatementOfTruth(inputFieldsMap));
        }

        buildWelshSpokenPreferences(inputFieldsMap, populatedMap);
        buildInterpreterRequiredFields(inputFieldsMap, populatedMap);
        buildSpecialAssistanceRequiredFields(inputFieldsMap, populatedMap);
        buildCourtInformation(inputFieldsMap, populatedMap);
        buildChildNoLaOrParentalResponsibility(inputFieldsMap, populatedMap);
        buildChildMaintanenceOrder(inputFieldsMap, populatedMap);
        buildChildProceedingDetails(inputFieldsMap, populatedMap);
        buildChildProceedingDetailsWithRelation(inputFieldsMap, populatedMap);
        buildadopAgencyOrLAs(inputFieldsMap, populatedMap);
        buildProceedingDetails(inputFieldsMap, populatedMap);
    }

    @SuppressWarnings("unchecked")
    private void buildProceedingDetails(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        final ArrayList adoptAgencyOrLaasArrayList = new ArrayList();
        if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_NO_PROCEEDING_DETAILS))
                && TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_NO_PROCEEDING_DETAILS))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS, BooleanUtils.NO);
        } else if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_PROCEEDING_DETAILS))
                && TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_PROCEEDING_DETAILS))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS, BooleanUtils.YES);
            final LinkedTreeMap<String, LinkedTreeMap> linkedTreeMapOne = new LinkedTreeMap<>();
            final LinkedTreeMap<String, String> childOne = new LinkedTreeMap();
            childOne.put(TYPE_OF_ORDER, inputFieldsMap.get(CHILD_TYPE_OF_ORDER));
            childOne.put(DATE_OF_ORDER, inputFieldsMap.get(CHILD_DATE_OF_ORDER));
            childOne.put(NAME_OF_COURT, inputFieldsMap.get(CHILD_NAME_OF_COURT));
            childOne.put(CASE_NUMBER, inputFieldsMap.get(CHILD_CASE_NUMBER));
            childOne.put(
                    CASE_TYPE_CATEGORY, ABOUT_OTHER_ORDERS_OR_PROCEEDINGS_THAT_AFFECT_THE_CHILD);
            linkedTreeMapOne.put(VALUE, childOne);
            adoptAgencyOrLaasArrayList.add(linkedTreeMapOne);
        }

        if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_NO_PROCEEDING_DETAILS_WITH_RELATION))
                && TRUE.equalsIgnoreCase(
                        inputFieldsMap.get(CHILD_NO_PROCEEDING_DETAILS_WITH_RELATION))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS_WITH_RELATION, BooleanUtils.NO);
        } else if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_PROCEEDING_DETAILS_WITH_RELATION))
                && TRUE.equalsIgnoreCase(
                        inputFieldsMap.get(CHILD_PROCEEDING_DETAILS_WITH_RELATION))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS_WITH_RELATION, BooleanUtils.YES);
            final LinkedTreeMap<String, LinkedTreeMap> linkedTreeMapTwo = new LinkedTreeMap();
            final LinkedTreeMap<String, String> childTwo = new LinkedTreeMap();
            childTwo.put(TYPE_OF_ORDER, inputFieldsMap.get(CHILD_TYPE_OF_ORDER_RELATION));
            childTwo.put(DATE_OF_ORDER, inputFieldsMap.get(CHILD_DATE_OF_ORDER_RELATION));
            childTwo.put(NAME_OF_COURT, inputFieldsMap.get(CHILD_NAME_OF_COURT_RELATION));
            childTwo.put(CASE_NUMBER, inputFieldsMap.get(CHILD_CASE_NUMBER_RELATION));
            childTwo.put(CASE_TYPE_CATEGORY, CASES_CONCERNING_A_RELATED_CHILD);
            linkedTreeMapTwo.put(VALUE, childTwo);
            adoptAgencyOrLaasArrayList.add(linkedTreeMapTwo);
        } else if (!StringUtils.isEmpty(
                        inputFieldsMap.get(CHILD_DONT_KNOW_PROCEEDING_DETAILS_WITH_RELATION))
                && TRUE.equalsIgnoreCase(
                        inputFieldsMap.get(CHILD_DONT_KNOW_PROCEEDING_DETAILS_WITH_RELATION))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS_WITH_RELATION, DONT_KNOW);
        }

        populatedMap.put(ADOP_AGENCY_OR_L_AS, adoptAgencyOrLaasArrayList);
    }

    @SuppressWarnings("unchecked")
    private void buildadopAgencyOrLAs(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        final ArrayList adoptAgencyOrLaasArrayList = new ArrayList();

        if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_PLACED_ADOPTION_DATE))) {
            populatedMap.put(ADOPTION_DATE, inputFieldsMap.get(CHILD_PLACED_ADOPTION_DATE));
        }
        final LinkedTreeMap<String, LinkedTreeMap> linkedTreeMapTwo = new LinkedTreeMap();
        final LinkedTreeMap<String, String> childTwo = new LinkedTreeMap();
        childTwo.put(ADOP_AGENCY_OR_LA_NAME, inputFieldsMap.get(CHILD_PLACED_ADOPTION_AGENCY_NAME));
        childTwo.put(
                ADOP_AGENCY_OR_LAADDRESS, inputFieldsMap.get(CHILD_PLACED_ADOPTION_AGENCY_ADDRESS));
        childTwo.put(
                ADOP_AGENCY_OR_LA_CONTACT_NAME,
                inputFieldsMap.get(CHILD_PLACED_ADOPTION_AGENCY_CONTACT_NAME));
        childTwo.put(
                ADOP_AGENCY_OR_LA_PHONE_NUMBER,
                inputFieldsMap.get(CHILD_PLACED_ADOPTION_AGENCY_TELEPHONE_NO));
        childTwo.put(L_AOR_ADOPTION_AGENCY_CATEGORY, CHILD_PLACED_FOR_THE_PURPOSE_OF_ADOPTION);
        linkedTreeMapTwo.put(VALUE, childTwo);
        adoptAgencyOrLaasArrayList.add(linkedTreeMapTwo);

        if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_IS_ADOPTION_AGENCY_INVOLVED))
                && TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_IS_ADOPTION_AGENCY_INVOLVED))) {
            populatedMap.put(
                    "isAdoptionAgencyInvolved",
                    inputFieldsMap.get(CHILD_IS_ADOPTION_AGENCY_INVOLVED));
        } else if (!StringUtils.isEmpty(
                        inputFieldsMap.get(CHILD_IS_ADOPTION_AGENCY_DETAILS_AVAILABLE))
                && TRUE.equalsIgnoreCase(
                        inputFieldsMap.get(CHILD_IS_ADOPTION_AGENCY_DETAILS_AVAILABLE))) {
            final LinkedTreeMap<String, LinkedTreeMap> linkedTreeMapThree = new LinkedTreeMap();
            final LinkedTreeMap<String, String> childThree = new LinkedTreeMap();
            childThree.put(ADOP_AGENCY_OR_LA_NAME, inputFieldsMap.get(CHILD_ADOPTION_AGENCY_NAME));
            childThree.put(
                    ADOP_AGENCY_OR_LAADDRESS, inputFieldsMap.get(CHILD_ADOPTION_AGENCY_ADDRESS));
            childThree.put(
                    ADOP_AGENCY_OR_LA_CONTACT_NAME,
                    inputFieldsMap.get(CHILD_ADOPTION_AGENCY_CONTACT_NAME));
            childThree.put(
                    ADOP_AGENCY_OR_LA_PHONE_NUMBER,
                    inputFieldsMap.get(CHILD_ADOPTION_AGENCY_TELEPHONE_NO));
            childThree.put(L_AOR_ADOPTION_AGENCY_CATEGORY, INVOLVED_IN_THE_PLACING_OF_THE_CHILD);
            linkedTreeMapThree.put(VALUE, childThree);
            adoptAgencyOrLaasArrayList.add(linkedTreeMapThree);
        }

        final LinkedTreeMap<String, LinkedTreeMap> linkedTreeMapOne = new LinkedTreeMap();
        final LinkedTreeMap<String, String> childOne = new LinkedTreeMap();
        childOne.put(ADOP_AGENCY_OR_LA_NAME, inputFieldsMap.get(CHILD_LA_NAME));
        childOne.put(ADOP_AGENCY_OR_LAADDRESS, inputFieldsMap.get(CHILD_LA_ADDRESS));
        childOne.put(ADOP_AGENCY_OR_LA_CONTACT_NAME, inputFieldsMap.get(CHILD_LA_CONTACT_NAME));
        childOne.put(ADOP_AGENCY_OR_LA_PHONE_NUMBER, inputFieldsMap.get(CHILD_LA_TELEPHONE_NO));
        childOne.put(L_AOR_ADOPTION_AGENCY_CATEGORY, NOTIFY_LOCAL_AUTHORITY_INTENTION_OF_ADOPTION);
        linkedTreeMapOne.put(VALUE, childOne);
        adoptAgencyOrLaasArrayList.add(linkedTreeMapOne);

        if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY))
                && TRUE.equalsIgnoreCase(
                        inputFieldsMap.get(CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY))) {
            populatedMap.put(LA_OR_PARENTAL_RESPONSIBILITY, BooleanUtils.YES);
        } else if (!StringUtils.isEmpty(inputFieldsMap.get(CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY))
                && FALSE.equalsIgnoreCase(
                        inputFieldsMap.get(CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY))) {
            populatedMap.put(LA_OR_PARENTAL_RESPONSIBILITY, BooleanUtils.NO);
        } else if (!StringUtils.isEmpty(
                        inputFieldsMap.get(CHILD_LA_OR_PARENTAL_RESPONSIBILITY_DETAILS))
                && TRUE.equalsIgnoreCase(
                        inputFieldsMap.get(CHILD_LA_OR_PARENTAL_RESPONSIBILITY_DETAILS))) {
            final LinkedTreeMap<String, LinkedTreeMap> linkedTreeMapFour = new LinkedTreeMap();
            final LinkedTreeMap<String, String> childFour = new LinkedTreeMap();
            childFour.put(ADOP_AGENCY_OR_LA_NAME, inputFieldsMap.get(CHILD_LA_OR_PARENTAL_NAME));
            childFour.put(
                    ADOP_AGENCY_OR_LAADDRESS, inputFieldsMap.get(CHILD_LA_OR_PARENTAL_ADDRESS));
            childFour.put(
                    ADOP_AGENCY_OR_LA_CONTACT_NAME,
                    inputFieldsMap.get(CHILD_LA_OR_PARENTAL_CONTACT_NAME));
            childFour.put(
                    ADOP_AGENCY_OR_LA_PHONE_NUMBER,
                    inputFieldsMap.get(CHILD_LA_OR_PARENTAL_TELEPHONE_NO));
            childFour.put(
                    ADOP_AGENCY_OR_LA_CONTACT_EMAIL,
                    inputFieldsMap.get(CHILD_LA_OR_PARENTAL_EMAIL));
            childFour.put(
                    L_AOR_ADOPTION_AGENCY_CATEGORY,
                    LOCAL_AUTHORITY_HAS_PARENTAL_RESPONSIBILITY_OF_THE_CHILD);
            linkedTreeMapFour.put(VALUE, childFour);
            adoptAgencyOrLaasArrayList.add(linkedTreeMapFour);
        }

        populatedMap.put(ADOP_AGENCY_OR_L_AS, adoptAgencyOrLaasArrayList);
    }

    public void buildCourtInformation(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(CHILD_PLACEMENT_ORDER_BY_ENGLAND_AND_WALES_COURT))) {
            populatedMap.put(
                    PLACEMENT_ORDER_COURT,
                    inputFieldsMap.get(CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_COURT_NAME));
            populatedMap.put(
                    PLACEMENT_ORDER_ID,
                    inputFieldsMap.get(CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_CASE_NUMBER));
            populatedMap.put(
                    PLACEMENT_ORDER_TYPE,
                    inputFieldsMap.get(CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_TYPE_OF_ORDER));
            populatedMap.put(
                    PLACEMENT_ORDER_DATE,
                    inputFieldsMap.get(CHILD_PLACMENT_ORDER_BY_ENGLAND_AND_WALES_DATE_OF_ORDER));
        } else if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_COURT))) {
            populatedMap.put(
                    FREEING_ORDER_COURT,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_COURT_NAME));
            populatedMap.put(
                    FREEING_ORDER_ID,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_CASE_NUMBER));
            populatedMap.put(
                    FREEING_ORDER_TYPE,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_TYPE_OF_ORDER));
            populatedMap.put(
                    FREEING_ORDER_DATE,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_ENGLAND_AND_WALES_DATE_OF_ORDER));
        } else if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_COURT))) {
            populatedMap.put(
                    FREEING_ORDER_COURT,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_COURT_NAME));
            populatedMap.put(
                    FREEING_ORDER_ID,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_CASE_NUMBER));
            populatedMap.put(
                    FREEING_ORDER_TYPE,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_TYPE_OF_ORDER));
            populatedMap.put(
                    FREEING_ORDER_DATE,
                    inputFieldsMap.get(CHILD_FREEING_ORDER_BY_NORTHERN_IRELAND_DATE_OF_ORDER));
        } else if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(CHILD_PERMANENCE_ORDER_BY_SCOTLAND_COURT))) {
            populatedMap.put(
                    FREEING_ORDER_COURT,
                    inputFieldsMap.get(CHILD_PERMANENCE_ORDER_BY_SCOTLAND_COURT_NAME));
            populatedMap.put(
                    FREEING_ORDER_ID,
                    inputFieldsMap.get(CHILD_PERMANENCE_ORDER_BY_SCOTLAND_CASE_NUMBER));
            populatedMap.put(
                    FREEING_ORDER_TYPE,
                    inputFieldsMap.get(CHILD_PERMANENCE_ORDER_BY_SCOTLAND_TYPE_OF_ORDER));
            populatedMap.put(
                    FREEING_ORDER_DATE,
                    inputFieldsMap.get(CHILD_PERMANENCE_ORDER_BY_SCOTLAND_DATE_OF_ORDER));
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_NO_ORDER_AVAILABLE))) {
            populatedMap.put(ANY_OTHER_ORDERS_AVAILABLE, "yes");
        } else if (FALSE.equalsIgnoreCase(inputFieldsMap.get(CHILD_NO_ORDER_AVAILABLE))) {
            populatedMap.put(ANY_OTHER_ORDERS_AVAILABLE, "no");
        }
    }

    public void buildChildNoLaOrParentalResponsibility(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY))) {
            populatedMap.put(LA_OR_PARENTAL_RESPONSIBILITY, "yes");
        } else if (FALSE.equalsIgnoreCase(
                inputFieldsMap.get(CHILD_NO_LA_OR_PARENTAL_RESPONSIBILITY))) {
            populatedMap.put(LA_OR_PARENTAL_RESPONSIBILITY, "no");
        }
    }

    @SuppressWarnings("unchecked")
    public void buildChildMaintanenceOrder(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        // nameOfCourt: child_courtAndDateOfOrder.
        // dateOfOrder: child_courtAndDateOfOrder
        Optional<Object> maintanenceOrderOptional =
                Optional.ofNullable(populatedMap.get(MAINTANENCE_ORDER));
        if (maintanenceOrderOptional.isPresent()) {
            LinkedTreeMap maintanenceOrder = (LinkedTreeMap) populatedMap.get(MAINTANENCE_ORDER);
            Optional<String> childCourtAndDateOfOrderOptional =
                    Optional.ofNullable(inputFieldsMap.get(CHILD_COURT_AND_DATE_OF_ORDER));
            childCourtAndDateOfOrderOptional.ifPresent(
                    childCourtAndDateOfOrder -> {
                        maintanenceOrder.put(NAME_OF_COURT, childCourtAndDateOfOrder.split(" ")[0]);
                        maintanenceOrder.put(DATE_OF_ORDER, childCourtAndDateOfOrder.split(" ")[1]);
                    });
            populatedMap.put(MAINTANENCE_ORDER, maintanenceOrder);
        }

        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_NO_MAINTANENCE_ORDER))) {
            populatedMap.put(HAS_MAINTANENCE_ORDER, "no");
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_MAINTANENCE_ORDER))) {
            populatedMap.put(HAS_MAINTANENCE_ORDER, "yes");
        }
    }

    public void buildChildProceedingDetails(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_NO_PROCEEDING_DETAILS))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS, "no");
        } else if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_PROCEEDING_DETAILS))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS, "yes");
        }
    }

    public void buildChildProceedingDetailsWithRelation(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(CHILD_NO_PROCEEDING_DETAILS_WITH_RELATION))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS_WITH_RELATION, "no");
        } else if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(CHILD_PROCEEDING_DETAILS_WITH_RELATION))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS_WITH_RELATION, "yes");
        } else if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(CHILD_DONT_KNOW_PROCEEDING_DETAILS_WITH_RELATION))) {
            populatedMap.put(HAS_PROCEEDING_DETAILS_WITH_RELATION, DONT_KNOW);
        }
    }

    private void buildWelshSpokenPreferences(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (BooleanUtils.YES.equalsIgnoreCase(inputFieldsMap.get(WELSH_SPOKEN_IN_COURT_REQUIRED))) {
            populatedMap.put(
                    WELSH_PREFERENCE_PARTY_NAME_CCD,
                    inputFieldsMap.get(WELSH_PREFERENCE_PARTY_NAME));
            populatedMap.put(
                    WELSH_PREFERENCE_WITNESS_NAME_CCD,
                    inputFieldsMap.get(WELSH_PREFERENCE_WITNESS_NAME));
            populatedMap.put(
                    WELSH_PREFERENCE_CHILD_NAME_CCD,
                    inputFieldsMap.get(WELSH_PREFERENCE_CHILD_NAME));
            populatedMap.put(
                    PARTY_WELSH_LANGUAGE_PREFERENCE_CCD,
                    inputFieldsMap.get(PARTY_WELSH_LANGUAGE_PREFERENCE));
            populatedMap.put(
                    WITNESS_WELSH_LANGUAGE_PREFERENCE_CCD,
                    inputFieldsMap.get(WITNESS_WELSH_LANGUAGE_PREFERENCE));
            populatedMap.put(
                    CHILD_WELSH_LANGUAGE_PREFERENCE_CCD,
                    inputFieldsMap.get(CHILD_WELSH_LANGUAGE_PREFERENCE));
        }
    }

    private void buildInterpreterRequiredFields(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (BooleanUtils.YES.equalsIgnoreCase(
                inputFieldsMap.get(COURT_INTERPRETER_ASSISTANCE_REQUIRED))) {
            populatedMap.put(
                    APPLICANT_REQUIRE_INTERPRETER_CCD,
                    inputFieldsMap.get(APPLICANT_REQUIRE_INTERPRETER));
            populatedMap.put(
                    RESPONDENT_REQUIRE_INTERPRETER_CCD,
                    inputFieldsMap.get(RESPONDENT_REQUIRE_INTERPRETER));
            populatedMap.put(
                    OTHER_PARTY_REQUIRE_INTERPRETER_CCD,
                    inputFieldsMap.get(OTHER_PARTY_REQUIRE_INTERPRETER));
            populatedMap.put(OTHER_PARTY_NAME_CCD, inputFieldsMap.get(OTHER_PARTY_NAME));
            populatedMap.put(
                    COURT_INTERPRETER_ASSISTANCE_LANGUAGE_CCD,
                    inputFieldsMap.get(COURT_INTERPRETER_ASSISTANCE_LANGUAGE));
        }
    }

    private void buildSpecialAssistanceRequiredFields(
            Map<String, String> inputFieldsMap, Map<String, Object> populatedMap) {
        if (BooleanUtils.YES.equalsIgnoreCase(
                inputFieldsMap.get(SPECIAL_ASSISTANCE_FACILITIES_REQUIRED))) {
            populatedMap.put(
                    SPECIAL_ASSISTANCE_FACILITIES_CCD,
                    inputFieldsMap.get(SPECIAL_ASSISTANCE_FACILITIES));
        }
    }

    private String getDomicileStatus(Map<String, String> inputFieldsMap) {
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANTS_DOMICILE_STATUS))) {
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
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(APPLICANT_RELATION_TO_CHILD_NON_CIVIL_PARTNER))) {
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
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_NOTFOUND))) {
            return MaritalStatusEnum.SPOUSE_NOT_FOUND.getName();
        }
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_SEPARATED))) {
            return MaritalStatusEnum.SPOUSE_SEPARATED.getName();
        }
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(APPLICANT_MARITAL_STATUS_MARRIED_SPOUSE_INCAPABLE))) {
            return MaritalStatusEnum.SPOUSE_INCAPABLE.getName();
        }
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(APPLICANT_APPLYING_ALONE_NATURAL_PARENT_DIED))) {
            return MaritalStatusEnum.NATURAL_PARAENT_DIED.getName();
        }
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(APPLICANT_APPLYING_ALONE_NATURAL_PARENT_NOT_FOUND))) {
            return MaritalStatusEnum.NATURAL_PARENT_NOT_FOUND.getName();
        }
        if (TRUE.equalsIgnoreCase(inputFieldsMap.get(APPLICANT_APPLYING_ALONE_NO_OTHER_PARENT))) {
            return MaritalStatusEnum.NO_OTHER_PARENT.getName();
        }
        if (TRUE.equalsIgnoreCase(
                inputFieldsMap.get(APPLICANT_APPLYING_ALONE_OTHER_PARENT_EXCLUSION_JUSTIFIED))) {
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
        return inputFieldsMap.get(APPLICANT1_SOT_DAY)
                + SLASH_DELIMITER
                + inputFieldsMap.get(APPLICANT1_SOT_MONTH)
                + SLASH_DELIMITER
                + inputFieldsMap.get(APPLICANT1_SOT_YEAR);
    }

    private String buildApplicant2StatementOfTruth(Map<String, String> inputFieldsMap) {
        return inputFieldsMap.get(APPLICANT2_SOT_DAY)
                + SLASH_DELIMITER
                + inputFieldsMap.get(APPLICANT2_SOT_MONTH)
                + SLASH_DELIMITER
                + inputFieldsMap.get(APPLICANT2_SOT_YEAR);
    }
}
