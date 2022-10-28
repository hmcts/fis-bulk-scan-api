package uk.gov.hmcts.reform.bulkscan.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder(toBuilder = true)
public class BulkScanValidationResponse {

    public Enum<Status> status;

    private List<String> warnings;
    private List<String> errors;

    public void addWarning(List<String> warningLst) {

        if (null == warnings) {
            this.warnings = warningLst;
        } else {
            this.warnings.addAll(warningLst);
        }
    }

    public void addErrors(List<String> errorsLst) {
        if (null == errors) {
            this.errors = errorsLst;
        } else {
            this.errors.addAll(errorsLst);
        }
    }

    public void changeStatus() {
        if (null != this.getErrors() && !this.getErrors().isEmpty()) {
            this.status = Status.ERRORS;
        } else if (null != this.getWarnings() && !this.getWarnings().isEmpty()) {
            this.status = Status.WARNINGS;
        } else {
            this.status = Status.SUCCESS;
        }
    }
}
