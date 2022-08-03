package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChildLiveWithEnum {
    APPLICANT("Applicant"),
    RESPONDENT("Respondent"),
    OTHERPEOPLE("OtherPeople");

    private final String name;
}
