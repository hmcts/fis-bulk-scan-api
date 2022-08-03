package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MiamChildProtectionConcernChecklistEnum {
    MIAMChildProtectionConcernChecklistEnum_value_1(
            "The subject of enquiries by a local authority under"
                    + " section 47 of the Children Act 1989 Act"),
    MIAMChildProtectionConcernChecklistEnum_value_2(
            "The subject of a child protection plan" + " put in place by a local authority");

    private final String displayedValue;
}
