package uk.gov.hmcts.reform.bulkscan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.reform.bulkscan.model.Errors;

@AllArgsConstructor
@Getter
public class BulkScanValidationException extends  RuntimeException {
    private Errors errors;
}
