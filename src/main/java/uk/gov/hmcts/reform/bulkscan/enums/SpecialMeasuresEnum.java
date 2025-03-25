package uk.gov.hmcts.reform.bulkscan.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SpecialMeasuresEnum {

    @JsonProperty("separateWaitingRoom")
    separateWaitingRoom("separateWaitingRoom", "A separate waiting room in the court building"),

    @JsonProperty("seperateEntranceExit")
    seperateEntranceExit("seperateEntranceExit", "A separate entrance and exit from the court building"),

    @JsonProperty("shieldedByScreen")
    shieldedByScreen("shieldedByScreen", "To be shielded by a privacy screen in the courtroom"),

    @JsonProperty("joinByVideoLink")
    joinByVideoLink("joinByVideoLink", "To join the hearing by video link rather than in person");

    private final String id;
    private final String displayedValue;

    @JsonValue
    public String getDisplayedValue() {
        return displayedValue;
    }

    @JsonCreator
    public static SpecialMeasuresEnum getValue(String key) {
        return SpecialMeasuresEnum.valueOf(key);
    }
}
