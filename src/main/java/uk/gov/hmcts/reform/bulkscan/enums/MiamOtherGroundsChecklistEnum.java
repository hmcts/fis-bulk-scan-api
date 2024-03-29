package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MiamOtherGroundsChecklistEnum {
    miamOtherGroundsChecklistEnum_Value_1(
            "The applicant is bankrupt evidenced by an "
                    + "application by the prospective applicant for a bankruptcy order;"),
    miamOtherGroundsChecklistEnum_Value_2(
            "The applicant is bankrupt evidenced by a petition by a creditor of "
                    + "the prospective applicant for a bankruptcy order"),
    miamOtherGroundsChecklistEnum_Value_3(
            "The applicant is bankrupt evidenced by a bankruptcy order in respect of the"
                    + " prospective applicant. "),
    miamOtherGroundsChecklistEnum_Value_4(
            "The prospective applicant does not have sufficient contact details for any of the"
                    + " prospective respondents to enable a family mediator to contact any of the"
                    + " prospective respondents for the purpose of scheduling the MIAM. "),
    miamOtherGroundsChecklistEnum_Value_5(
            "The application would be made without notice (Paragraph 5.1 of Practice Direction 18A"
                + " sets out the circumstances in which applications may be made without notice.)"
                + " "),
    miamOtherGroundsChecklistEnum_Value_6(
            "(i) the prospective applicant is or all of the prospective respondents are subject to"
                + " a disability or other inability that would prevent attendance at a MIAM unless"
                + " appropriate facilities can be offered by an authorised mediator; (ii) the"
                + " prospective applicant has contacted as many authorised family mediators as have"
                + " an office within fifteen miles of his or her home (or three of them if there"
                + " are three or more), and all have stated that they are unable to provide such"
                + " facilities; and (iii)the names, postal addresses and telephone numbers or"
                + " e-mail addresses for such authorised family mediators, and the dates of"
                + " contact, can be provided to the court if requested."),
    miamOtherGroundsChecklistEnum_Value_7(
            "the prospective applicant or all of the prospective respondents cannot attend a MIAM"
                + " because he or she is, or they are, as the case may be (i) in prison or any"
                + " other institution in which he or she is or they are required to be detained;"
                + " (ii) subject to conditions of bail that prevent contact with the other person;"
                + " or (iii) subject to a licence with a prohibited contact requirement in relation"
                + " to the other person."),
    miamOtherGroundsChecklistEnum_Value_8(
            "The prospective applicant or all of the prospective respondents are not habitually"
                    + " resident in England and Wales. "),
    miamOtherGroundsChecklistEnum_Value_9(
            "A child is one of the prospective parties by virtue of Rule 12.3(1). "),
    miamOtherGroundsChecklistEnum_Value_10(
            "(i) the prospective applicant has contacted as many authorised family mediators as"
                    + " have an office within fifteen miles of his or her home (or three of them if"
                    + " there are three or more), and all of them have stated that they are not"
                    + " available to conduct a MIAM within fifteen business days of the date\r\n"
                    + "of contact; and (ii) the names, postal addresses and telephone numbers\r\n"
                    + "or e-mail addresses for such authorised family mediators, and the dates of"
                    + " contact, can be provided to the court if requested."),
    miamOtherGroundsChecklistEnum_Value_11(
            "There is no authorised family mediator with an office within fifteen miles of the"
                    + " prospective applicant’s home. ");

    private final String displayedValue;

    public String getDisplayedValue() {
        return displayedValue;
    }

    public static MiamOtherGroundsChecklistEnum getValue(String key) {
        return MiamOtherGroundsChecklistEnum.valueOf(key);
    }
}
