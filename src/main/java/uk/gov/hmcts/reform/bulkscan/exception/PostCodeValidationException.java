package uk.gov.hmcts.reform.bulkscan.exception;

import java.io.Serializable;

public class PostCodeValidationException extends  RuntimeException implements Serializable {
    private static final long serialVersionUID = 29923904L;

    public PostCodeValidationException(String errorMessage) {
        super(errorMessage);
    }

    public PostCodeValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
