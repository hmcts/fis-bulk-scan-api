package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MiamExemptionsChecklistEnum {
    mpuDomesticAbuse("Domestic abuse"),
    mpuUrgency("Urgency"),
    mpuPreviousMiamAttendance("Previous attendance of a MIAM or non-court dispute resolution"),
    mpuOther("Other"),
    mpuChildProtectionConcern("Child protection concerns");

    private final String displayedValue;

    public String getDisplayedValue() {
        return displayedValue;
    }

    public static MiamExemptionsChecklistEnum getValue(String key) {
        return MiamExemptionsChecklistEnum.valueOf(key);
    }
}
