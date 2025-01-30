package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChildLiveWithEnum {
    APPLICANT("applicant"),
    RESPONDENT("respondent"),
    OTHERPEOPLE("otherPeople");

    private final String name;
}
