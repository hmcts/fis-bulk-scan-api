package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApplicantRespondentRelationshipEnum {
    APPLICANT_RESPONDENT_RELATIONSHIP_01("Married or in a civil partnership"),
    APPLICANT_RESPONDENT_RELATIONSHIP_02("Formerly married or in a civil partnership"),
    APPLICANT_RESPONDENT_RELATIONSHIP_03("Engaged or proposed civil partnership"),
    APPLICANT_RESPONDENT_RELATIONSHIP_04("Formerly engaged or proposed civil partnership"),
    APPLICANT_RESPONDENT_RELATIONSHIP_05("Live together as a couple"),
    APPLICANT_RESPONDENT_RELATIONSHIP_06("Formerly lived together as a couple"),
    APPLICANT_RESPONDENT_RELATIONSHIP_07(
            "Boyfriend, girlfriend or partner who does not live with me"),
    APPLICANT_RESPONDENT_RELATIONSHIP_08(
            "Former boyfriend, girlfriend or partner who did not live with me"),
    APPLICANT_RESPONDENT_RELATIONSHIP_09("None of the above"),
    APPLICANT_RESPONDENT_RELATIONSHIP_10("Father"),
    APPLICANT_RESPONDENT_RELATIONSHIP_11("Mother"),
    APPLICANT_RESPONDENT_RELATIONSHIP_12("Son"),
    APPLICANT_RESPONDENT_RELATIONSHIP_13("Daughter"),
    APPLICANT_RESPONDENT_RELATIONSHIP_14("Brother"),
    APPLICANT_RESPONDENT_RELATIONSHIP_15("Sister"),
    APPLICANT_RESPONDENT_RELATIONSHIP_16("Grandfather"),
    APPLICANT_RESPONDENT_RELATIONSHIP_17("Grandmother"),
    APPLICANT_RESPONDENT_RELATIONSHIP_18("Uncle"),
    APPLICANT_RESPONDENT_RELATIONSHIP_19("Aunt"),
    APPLICANT_RESPONDENT_RELATIONSHIP_20("Nephew"),
    APPLICANT_RESPONDENT_RELATIONSHIP_21("Niece"),
    APPLICANT_RESPONDENT_RELATIONSHIP_22("Cousin"),
    APPLICANT_RESPONDENT_RELATIONSHIP_23("Other - please specify");

    private final String relationshipDescription;

    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    public static ApplicantRespondentRelationshipEnum getValue(String key) {
        return ApplicantRespondentRelationshipEnum.valueOf(key);
    }
}
