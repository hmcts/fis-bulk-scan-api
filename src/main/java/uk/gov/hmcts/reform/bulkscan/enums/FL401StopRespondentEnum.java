package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FL401StopRespondentEnum {
    BEING_VIOLENT_TOWARDS_ME_OR_THREATENING_ME(
            "stopRespondentFromDoing_1", "Being violent towards me or threatening me"),
    HARASSING_OR_INTIMIDATING_ME("stopRespondentFromDoing_2", "Harassing or intimidating me"),
    POSTING_OR_PUBLISHING(
            "stopRespondentFromDoing_3 ",
            "Posting or publishing about me either in print or digitally"),
    CONTACTING_ME_DIRECTLY("stopRespondentFromDoing_4", "Contacting me directly"),
    CAUSING_DAMAGE_TO_MY_POSSESSIONS(
            "stopRespondentFromDoing_5 ", "Causing damage to my possessions"),
    CAUSING_DAMAGE_TO_MY_HOME("stopRespondentFromDoing_6", "Causing damage to my home"),
    COMING_INTO_MY_HOME("stopRespondentFromDoing_7", "Coming into my home"),
    COMING_NEAR_MY_HOME("stopRespondentFromDoing_8", "Coming near my home"),
    COMING_NEAR_MY_PLACE_OF_WORK("stopRespondentFromDoing_9", "Coming near my place of work");

    private final String key;
    private final String description;

    public static String getValue(String key) {

        return valueOf(key).getDescription();
    }
}
