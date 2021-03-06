package uk.gov.hmcts.reform.bulkscan.exception;

public class InvalidResourceException extends RuntimeException {

    private static final long serialVersionUID = 7442998222674411077L;

    public InvalidResourceException(String message) {
        super(message);
    }

    public InvalidResourceException(String message, Exception cause) {
        super(message, cause);
    }
}
