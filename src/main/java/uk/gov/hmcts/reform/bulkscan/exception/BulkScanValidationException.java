package uk.gov.hmcts.reform.bulkscan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.reform.bulkscan.model.Errors;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class BulkScanValidationException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1234567L;
    Errors errors;
}
