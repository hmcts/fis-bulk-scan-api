package uk.gov.hmcts.reform.bulkscan.exceptionhandlers;

public class ApiError {
    public final String error;

    public ApiError(String error) {
        this.error = error;
    }
}
