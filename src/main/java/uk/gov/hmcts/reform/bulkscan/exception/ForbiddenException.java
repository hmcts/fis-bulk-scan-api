package uk.gov.hmcts.reform.bulkscan.exception;

public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 1641102126427991709L;

    public ForbiddenException(String message) {
        super(message);
    }
}
