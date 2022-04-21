package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder(toBuilder = true)
public class BulkScanValidationResponse {

    public Enum<Status> status;
    public Warnings warnings;
    public Errors errors;
}
