package uk.gov.hmcts.reform.bulkscan.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OcrMappingException extends RuntimeException {

    List<String> warnings = new ArrayList<>();
    @Serial
    private static final long serialVersionUID = 12345678L;

    public OcrMappingException(String message) {
        super(message);
    }

    public OcrMappingException(String message, List<String> warnings) {
        super(message);
        this.warnings = warnings;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}
