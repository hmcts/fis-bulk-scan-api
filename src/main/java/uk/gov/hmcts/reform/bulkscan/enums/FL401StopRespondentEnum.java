package uk.gov.hmcts.reform.bulkscan.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum FL401StopRespondentEnum {

    BEING_VIOLENT_TOWARDS_ME_OR_THREATENING_ME("Being violent towards me or threatening me"),
    HARASSING_OR_INTIMIDATING_ME("Harassing or intimidating me"),
    POSTING_OR_PUBLISHING("Posting or publishing about me either in print or digitally"),
    CONTACTING_ME_DIRECTLY("Contacting me directly"),
    CAUSING_DAMAGE_TO_MY_POSSESSIONS("Causing damage to my possessions"),
    CAUSING_DAMAGE_TO_MY_HOME("Causing damage to my home"),
    COMING_INTO_MY_HOME("Coming into my home"),
    COMING_NEAR_MY_HOME("Coming near my home"),
    COMING_NEAR_MY_PLACE_OF_WORK("Coming near my place of work"),

    BEING_VIOLENT_TOWARDS_MY_CHILDREN("Being violent towards my children or threatening my children"),
    HARASSING_OR_INTIMIDATING_MY_CHILDREN("Harassing or intimidating my children"),
    POSTING_OR_PUBLISHING_ANYTHING_ABOUT_MY_CHILDREN_IN_PRINT("Posting or publishing anything about my children in print,  or digitally"),
    CONTACTING_MY_CHILDREN_DIRECTLY_WITHOUT_MY_CONSENT("Contacting my children directly without my consent"),
    GOING_TO_OR_NEAR_MY_CHILDREN_S_SCHOOL_OR_NURSERY("Going to or near my children’s school or nursery");

    private final String description;

}
