package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder(toBuilder = true)
public class CaseData  {

    private final String id;

    private final String caseTypeOfApplication;

    private final String applicantCaseName;

    /**
     * Applicant details.
     */
    private final List<Element<PartyDetails>> applicants;

    /**
     * Child details.
     */
    private final List<Element<Child>> children;

    /**
     * Respondent details.
     */
    private final List<Element<PartyDetails>> respondents;

}
