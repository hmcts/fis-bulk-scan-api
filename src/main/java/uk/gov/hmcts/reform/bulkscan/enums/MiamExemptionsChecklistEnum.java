package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MiamExemptionsChecklistEnum {
    domesticViolence("Domestic violence"),
    urgency("Urgency"),
    previousMIAMattendance("Previous MIAM attendance or previous MIAM exemption"),
    other("Other"),
    childProtectionConcern("Child Protection Concern");

    private final String displayedValue;

    public String getDisplayedValue() {
        return displayedValue;
    }

    public static MiamExemptionsChecklistEnum getValue(String key) {
        return MiamExemptionsChecklistEnum.valueOf(key);
    }
}
