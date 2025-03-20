package uk.gov.hmcts.reform.bulkscan.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OCRMappingException extends RuntimeException {

    List<String> warnings = new ArrayList<>();

    public OCRMappingException(String message) {
        super(message);
    }

    public OCRMappingException(String message, List<String> warnings) {
        super(message);
        this.warnings = warnings;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}
