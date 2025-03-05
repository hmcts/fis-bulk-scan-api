package uk.gov.hmcts.reform.bulkscan.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {

    @JsonProperty("female")
    female("female", "Female"),
    @JsonProperty("male")
    male("male", "Male"),
    @JsonProperty("other")
    other("other", "They identify in another way");

    private final String id;
    private final String displayedValue;

    @JsonValue
    public String getDisplayedValue() {
        return displayedValue;
    }

    @JsonCreator
    public static Gender getValue(String key) {
        return Gender.valueOf(key);
    }

    public static Gender getDisplayedValueFromEnumString(String enteredValue) {
        if ("female".equalsIgnoreCase(enteredValue)) {
            return Gender.female;
        } else if ("male".equalsIgnoreCase(enteredValue)) {
            return Gender.male;
        } else if ("other".equalsIgnoreCase(enteredValue)) {
            return Gender.other;
        }
        return null;
    }
}
