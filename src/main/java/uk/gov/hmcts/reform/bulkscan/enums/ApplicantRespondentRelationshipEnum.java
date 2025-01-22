package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApplicantRespondentRelationshipEnum {
    appliantRespondent_Relationship_1("Married or in a civil partnership"),
    appliantRespondent_Relationship_2("Formerly married or in a civil partnership"),
    appliantRespondent_Relationship_3("Engaged or proposed civil partnership"),
    appliantRespondent_Relationship_4("Formerly engaged or proposed civil partnership"),
    appliantRespondent_Relationship_5("Live together as a couple"),
    appliantRespondent_Relationship_6("Formerly lived together as a couple"),
    appliantRespondent_Relationship_7(
            "Boyfriend, girlfriend or partner who does not live with me"),
    appliantRespondent_Relationship_8(
            "Former boyfriend, girlfriend or partner who did not live with me"),
    appliantRespondent_Relationship_9("None of the above"),
    appliantRespondent_Relationship_10("Father"),
    appliantRespondent_Relationship_11("Mother"),
    appliantRespondent_Relationship_12("Son"),
    appliantRespondent_Relationship_13("Daughter"),
    appliantRespondent_Relationship_14("Brother"),
    appliantRespondent_Relationship_15("Sister"),
    appliantRespondent_Relationship_16("Grandfather"),
    appliantRespondent_Relationship_17("Grandmother"),
    appliantRespondent_Relationship_18("Uncle"),
    appliantRespondent_Relationship_19("Aunt"),
    appliantRespondent_Relationship_20("Nephew"),
    appliantRespondent_Relationship_21("Niece"),
    appliantRespondent_Relationship_22("Cousin"),
    appliantRespondent_Relationship_23("Other - please specify");

    private final String relationshipDescription;

    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    public static ApplicantRespondentRelationshipEnum getValue(String key) {
        return ApplicantRespondentRelationshipEnum.valueOf(key);
    }
}
