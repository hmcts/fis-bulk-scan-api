package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApplicantRespondentRelationshipEnum {
    applicantRespondent_Relationship_01("Married or in a civil partnership"),
    applicantRespondent_Relationship_02("Formerly married or in a civil partnership"),
    applicantRespondent_Relationship_03("Engaged or proposed civil partnership"),
    applicantRespondent_Relationship_04("Formerly engaged or proposed civil partnership"),
    applicantRespondent_Relationship_05("Live together as a couple"),
    applicantRespondent_Relationship_06("Formerly lived together as a couple"),
    applicantRespondent_Relationship_07(
            "Boyfriend, girlfriend or partner who does not live with me"),
    applicantRespondent_Relationship_08(
            "Former boyfriend, girlfriend or partner who did not live with me"),
    applicantRespondent_Relationship_09("None of the above"),
    applicantRespondent_Relationship_10("Father"),
    applicantRespondent_Relationship_11("Mother"),
    applicantRespondent_Relationship_12("Son"),
    applicantRespondent_Relationship_13("Daughter"),
    applicantRespondent_Relationship_14("Brother"),
    applicantRespondent_Relationship_15("Sister"),
    applicantRespondent_Relationship_16("Grandfather"),
    applicantRespondent_Relationship_17("Grandmother"),
    applicantRespondent_Relationship_18("Uncle"),
    applicantRespondent_Relationship_19("Aunt"),
    applicantRespondent_Relationship_20("Nephew"),
    applicantRespondent_Relationship_21("Niece"),
    applicantRespondent_Relationship_22("Cousin"),
    applicantRespondent_Relationship_23("Other - please specify");

    private final String relationshipDescription;

    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    public static ApplicantRespondentRelationshipEnum getValue(String key) {
        return ApplicantRespondentRelationshipEnum.valueOf(key);
    }
}
