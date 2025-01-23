package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FL401StopRespondentBehaviourChildEnum {
    BEING_VIOLENT_TOWARDS_MY_CHILDREN(
            "stopRespondentFromDoingToChild_1",
            "applicantStopFromRespondentDoingToChildEnum_Value_1"),
    HARASSING_OR_INTIMIDATING_MY_CHILDREN(
            "stopRespondentFromDoingToChild_2 ", "applicantStopFromRespondentDoingToChildEnum_Value_2"),
    POSTING_OR_PUBLISHING_ANYTHING_ABOUT_MY_CHILDREN_IN_PRINT(
            "stopRespondentFromDoingToChild_3 ",
            "applicantStopFromRespondentDoingToChildEnum_Value_3"),
    CONTACTING_MY_CHILDREN_DIRECTLY_WITHOUT_MY_CONSENT(
            "stopRespondentFromDoingToChild_4 ",
            "applicantStopFromRespondentDoingToChildEnum_Value_4"),
    GOING_TO_OR_NEAR_MY_CHILDREN_S_SCHOOL_OR_NURSERY(
            "stopRespondentFromDoingToChild_5 ",
            "applicantStopFromRespondentDoingToChildEnum_Value_5");

    private final String key;
    private final String description;

    public static String getValue(String key) {

        return valueOf(key).getDescription();
    }
}
