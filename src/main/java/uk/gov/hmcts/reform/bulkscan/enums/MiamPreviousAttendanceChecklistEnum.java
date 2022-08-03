package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MiamPreviousAttendanceChecklistEnum {
    miamPreviousAttendanceChecklistEnum_Value_1(
            "In the 4 months prior to making the application, the person attended a MIAM or"
                + " participated in another form of non-court dispute resolution relating to the"
                + " same or substantially the same dispute"),
    miamPreviousAttendanceChecklistEnum_Value_2(
            "At the time of making the application, the person is participating in another form of"
                    + " non-court dispute resolution relating to the same or substantially the same"
                    + " dispute"),
    miamPreviousAttendanceChecklistEnum_Value_3(
            "In the 4 months prior to making the application, the person filed a relevant family"
                    + " application confirming that a MIAM exemption applied and that application"
                    + " related to the same or substantially the same dispute"),
    miamPreviousAttendanceChecklistEnum_Value_4(
            "The application would be made in existing proceedings which are continuing and the"
                    + " prospective applicant attended a MIAM before initiating those proceedings"),
    miamPreviousAttendanceChecklistEnum_Value_5(
            "The application would be made in existing proceedings which are continuing and a MIAM"
                    + " exemption applied to the application for those proceedings");

    private final String displayedValue;

    public String getDisplayedValue() {
        return displayedValue;
    }

    public static MiamPreviousAttendanceChecklistEnum getValue(String key) {
        return MiamPreviousAttendanceChecklistEnum.valueOf(key);
    }
}
