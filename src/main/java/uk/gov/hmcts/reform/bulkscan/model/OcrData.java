package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import uk.gov.hmcts.reform.bulkscan.model.Child;
import uk.gov.hmcts.reform.bulkscan.model.PartyDetails;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder(toBuilder = true)
public class OcrData {

    private String id;

    private String caseTypeOfApplication;

    private String applicantCaseName;

    /**
     * Applicant details.
     */
    private List<PartyDetails> applicants;

    /**
     * Child details.
     */
    private List<Child> children;

    /**
     * Respondent details.
     */
    private List<PartyDetails> respondents;

}
