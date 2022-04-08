package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BulkScanTransformationResponse {

    public CaseCreationDetails caseCreationDetails;
    public Warnings warnings;
    public Errors errors;


}
