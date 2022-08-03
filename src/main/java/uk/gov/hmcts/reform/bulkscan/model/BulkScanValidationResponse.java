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
    public Warnings warnings;
    public Errors errors;

    public void addWarning(List<String> warnings) {
        this.warnings.getItems().addAll(warnings);
    }

    public void addErrors(List<String> errors) {
        this.errors.getItems().addAll(errors);
    }

    public void changeStatus() {
        if (!errors.getItems().isEmpty()) {
            this.status = Status.ERRORS;
        } else if (!warnings.getItems().isEmpty()) {
            this.status = Status.WARNINGS;
        } else {
            this.status = Status.SUCCESS;
        }
    }
}
