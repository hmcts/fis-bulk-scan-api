package uk.gov.hmcts.reform.bulkscan.group.validation.enums;

public enum CheckboxEnum {
    TRUE("true"),
    FALSE("false");

    final String value;

    CheckboxEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
