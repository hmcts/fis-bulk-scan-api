package uk.gov.hmcts.reform.bulkscan.exception.response;

import lombok.Builder;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.Status;

@Builder(toBuilder = true)
public class BulkScanExceptionResponse {
    private Enum<Status> status;
    private Errors errors;
    private String message;
}
