package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderWithouGivingNoticeReasonEnum {
    RISKOF_SIGNIFICANT_HARM(
            "there is risk of significant harm to the applicant or a relevant child, attributable"
                    + " to conduct of the respondent, if the order is not made immediately"),

    DETERRED_OR_PREVENTED(
            "it is likely that I will be deterred or prevented from pursuing the application "
                    + "if an order is not made immediately"),

    DELIBERATELYEVADING_SERVICE(
            "I believe that the respondent is aware of the proceedings but is deliberately evading"
                    + " service and that I or a relevant child will be seriously prejudiced by the"
                    + " delay in effecting substituted service");

    private String description;
}
