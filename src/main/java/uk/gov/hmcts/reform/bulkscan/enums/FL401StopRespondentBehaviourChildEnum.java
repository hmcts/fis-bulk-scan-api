package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FL401StopRespondentBehaviourChildEnum {
    BEING_VIOLENT_TOWARDS_MY_CHILDREN(
            "stopRespondentFromDoingToChild_1",
            "Being violent towards my children or threatening my children"),
    HARASSING_OR_INTIMIDATING_MY_CHILDREN(
            "stopRespondentFromDoingToChild_2 ", "Harassing or intimidating my children"),
    POSTING_OR_PUBLISHING_ANYTHING_ABOUT_MY_CHILDREN_IN_PRINT(
            "stopRespondentFromDoingToChild_3 ",
            "Posting or publishing anything about my children in print,  or digitally"),
    CONTACTING_MY_CHILDREN_DIRECTLY_WITHOUT_MY_CONSENT(
            "stopRespondentFromDoingToChild_4 ",
            "Contacting my children directly without my consent"),
    GOING_TO_OR_NEAR_MY_CHILDREN_S_SCHOOL_OR_NURSERY(
            "stopRespondentFromDoingToChild_5 ",
            "Going to or near my children’s school or nursery");

    private final String key;
    private final String description;

    public static String getValue(String key) {

        return valueOf(key).getDescription();
    }
}
