package uk.gov.hmcts.reform.bulkscan.exception;

import java.io.Serializable;

public class CaseTypeOfApplicationNotFoundException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1234567890343L;
    String message;
}
