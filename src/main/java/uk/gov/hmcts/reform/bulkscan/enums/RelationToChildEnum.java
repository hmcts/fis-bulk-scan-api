package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RelationToChildEnum {
    FATHER("Father"),
    MOTHER("Mother"),
    CIVIL("Civil");

    private final String name;
}
