package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GroupMediatorCertifiesEnum {
    MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_WILLING_TO_ATTEND_MIAM(
            "mediation is not suitable as a means of resolving the dispute because none of the"
                    + " respondents is willing to attend a MIAM"),

    MEDIATION_NOT_SUITABLE_NONEOFTHERESPONDENTS_FAILED_TO_ATTEND_MIAM_WITHOUT_GOOD_REASON(
            "mediation is not suitable as a means of resolving the dispute because all of the"
                    + " respondents failed without good reason to attend a MIAM appointment"),

    MEDIATION_NOT_SUITABLE_FOR_RESOLVING_THE_DISPUTE(
            "mediation is otherwise not suitable as a means of resolving the dispute."),

    APPLICANT_ONLY_ATTENDED_MIAM("The prospective applicant only attended a MIAM."),

    APPLICANT_AND_RESPONDENT_ATTENDED_MIAM_TOGETHER(
            "The prospective applicant and respondent party(s) attended the MIAM " + "together."),

    APPLICANT_AND_RESPONDENT_PARTY_ATTENDED_MIAM_SEPARATELY(
            "The prospective applicant and respondent(s) have each attended a " + "separate MIAM."),

    RESPONDENT_PARTY_ARRANGED_TO_ATTEND_MIAM_SEPARATELY(
            "The prospective respondent party(s) has/have made or is/are making "
                    + "arrangements to attend a separate MIAM."),

    MEDIATION_NOT_PROCEEDING_APPLICATION_ATTENDED_MIAM_ALONE(
            "The applicant has attended a MIAM alone and \r\n"
                    + "• the applicant does not wish to start or continue mediation; or \r\n"
                    + "• the mediator has determined that mediation is unsuitable; or \r\n"
                    + "• the respondent did not wish to attend a MIAM"),

    MEDIATION_NOT_PROCEEDING_APPLICANTS_AND_RESPONDENTS_ATTENDED_MIAM(
            "Both the applicant and respondent have attended a MIAM "
                    + "(separately or together) and \r\n"
                    + "• the applicant does not wish to start or continue mediation; or \r\n"
                    + "• the respondent does not wish to start or continue mediation; or \r\n"
                    + "• the mediator has determined that mediation is unsuitable"),

    MEDIATION_NOT_PROCEEDING_BROKEN_DOWN_UNRESOLVED(
            "Mediation has started, but has: \r\n"
                    + "• broken down; or \r\n"
                    + "• concluded with some or all issues unresolved");

    private String description;
}
