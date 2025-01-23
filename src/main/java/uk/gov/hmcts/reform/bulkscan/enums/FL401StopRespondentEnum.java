package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FL401StopRespondentEnum {
    BEING_VIOLENT_TOWARDS_ME_OR_THREATENING_ME(
            "stopRespondentFromDoing_1", "applicantStopFromRespondentEnum_Value_1"),
    HARASSING_OR_INTIMIDATING_ME("stopRespondentFromDoing_2", "applicantStopFromRespondentEnum_Value_2"),
    POSTING_OR_PUBLISHING(
            "stopRespondentFromDoing_3 ",
            "applicantStopFromRespondentEnum_Value_3"),
    CONTACTING_ME_DIRECTLY("stopRespondentFromDoing_4", "applicantStopFromRespondentEnum_Value_4"),
    CAUSING_DAMAGE_TO_MY_POSSESSIONS(
            "stopRespondentFromDoing_5 ", "applicantStopFromRespondentEnum_Value_5"),
    CAUSING_DAMAGE_TO_MY_HOME("stopRespondentFromDoing_6", "applicantStopFromRespondentEnum_Value_6"),
    COMING_INTO_MY_HOME("stopRespondentFromDoing_7", "applicantStopFromRespondentEnum_Value_7"),
    COMING_NEAR_MY_HOME("stopRespondentFromDoing_8", "applicantStopFromRespondentEnum_Value_8"),
    COMING_NEAR_MY_PLACE_OF_WORK("stopRespondentFromDoing_9", "applicantStopFromRespondentEnum_Value_9");

    private final String key;
    private final String description;

    public static String getValue(String key) {

        return valueOf(key).getDescription();
    }
}
