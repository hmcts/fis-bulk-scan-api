package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

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
