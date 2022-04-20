package uk.gov.hmcts.reform.bulkscan.exception.response;

import lombok.Builder;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.Status;

@Builder(toBuilder = true)
public class BulkScanExceptionResponse {
    Enum<Status> status;
    Errors errors;
    String message;
}
