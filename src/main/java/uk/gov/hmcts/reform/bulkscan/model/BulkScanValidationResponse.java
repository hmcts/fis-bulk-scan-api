package uk.gov.hmcts.reform.bulkscan.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Data
@ToString
@Builder(toBuilder = true)
public class BulkScanValidationResponse {

    public Enum<Status> status;

    private final List<String> warnings;
    private final List<String> errors;

    public void addWarning(List<String> warning) {
        //StringUtils.hasText(warnings)?this.getWarnings().addAll(warnings);this.warnings = warnings:this.warnings = warnings:


        //Boolean ans = warnings.isEmpty();
        this.warnings=(warnings.isEmpty()? warning : warnings.addAll(warning));


        //.add(String.valueOf(warnings)); //rough - maybe not add, maybe addAll!!!!!
        //create new feature branch, create from master
        //take the warnings from the parm and put in the list warnings above, make sure it's not null to start,
        //use the conditional operator!, not if block (concise)
        //if not null append, if null, assign

            //this.getWarnings().addAll(warnings);
    }

    public void addErrors(List<String> errors) {
        this.getErrors().addAll(errors);
    }

    public void changeStatus() {
        if (!this.getErrors().isEmpty()) {
            this.status = Status.ERRORS;
        } else if (!this.getWarnings().isEmpty()) {
            this.status = Status.WARNINGS;
        } else {
            this.status = Status.SUCCESS;
        }
    }
}
