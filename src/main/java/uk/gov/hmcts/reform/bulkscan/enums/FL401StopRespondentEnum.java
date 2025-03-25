package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FL401StopRespondentEnum {
    BEING_VIOLENT_TOWARDS_ME_OR_THREATENING_ME(
            "StopRespondentFromDoing_1", "applicantStopFromRespondentEnum_Value_1"),
    HARASSING_OR_INTIMIDATING_ME("StopRespondentFromDoing_2", "applicantStopFromRespondentEnum_Value_2"),
    POSTING_OR_PUBLISHING(
            "StopRespondentFromDoing_3 ",
            "applicantStopFromRespondentEnum_Value_3"),
    CONTACTING_ME_DIRECTLY("StopRespondentFromDoing_4", "applicantStopFromRespondentEnum_Value_4"),
    CAUSING_DAMAGE_TO_MY_POSSESSIONS("StopRespondentFromDoing_5", "applicantStopFromRespondentEnum_Value_5"),
    CAUSING_DAMAGE_TO_MY_HOME("StopRespondentFromDoing_6", "applicantStopFromRespondentEnum_Value_6"),
    COMING_INTO_MY_HOME("StopRespondentFromDoing_7", "applicantStopFromRespondentEnum_Value_7"),
    COMING_NEAR_MY_HOME("StopRespondentFromDoing_8", "applicantStopFromRespondentEnum_Value_8"),
    COMING_NEAR_MY_PLACE_OF_WORK("StopRespondentFromDoing_9", "applicantStopFromRespondentEnum_Value_9");

    private final String key;
    private final String description;

    public static String getValue(String key) {

        return valueOf(key).getDescription();
    }
}
