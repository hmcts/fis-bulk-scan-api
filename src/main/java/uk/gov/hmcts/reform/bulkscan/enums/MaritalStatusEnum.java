package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MaritalStatusEnum {

    SINGLE("Single"),
    DIVORCED("Divorced"),
    WIDOW("Widow"),
    SPOUSE_NOT_FOUND("Spouse Not Found"),
    SPOUSE_SEPARATED("Spouse Separated"),
    SPOUSE_INCAPABLE("Spouse Incapable"),
    NATURAL_PARAENT_DIED("Natural Parent Died"),
    NATURAL_PARENT_NOT_FOUND("Natural Parent Not Found"),
    NO_OTHER_PARENT("No Other Parent"),
    OTHER_PARENT_EXCLUSION_JUSTIFIED("Other Parent Exclusion Justified");

    private final String name;
}
