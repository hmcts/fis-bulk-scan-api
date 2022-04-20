package uk.gov.hmcts.reform.bulkscan.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.gov.hmcts.reform.bulkscan.exception.BulkScanValidationException;
import uk.gov.hmcts.reform.bulkscan.exception.CaseTypeOfApplicationNotFoundException;
import uk.gov.hmcts.reform.bulkscan.exception.response.BulkScanExceptionResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BulkScanValidationException.class)
    public ResponseEntity<?> handleValidationExceptions(BulkScanValidationException exception) {
        BulkScanExceptionResponse response = BulkScanExceptionResponse.builder().errors(exception.getErrors())
            .status(Status.ERRORS).build();
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(CaseTypeOfApplicationNotFoundException.class)
    public ResponseEntity<?> handleCaseTypeNotValidException(CaseTypeOfApplicationNotFoundException exception) {
        BulkScanExceptionResponse response = BulkScanExceptionResponse.builder().message(exception.getMessage())
            .status(Status.ERRORS).build();
        return ResponseEntity.badRequest().body(response);
    }
}
