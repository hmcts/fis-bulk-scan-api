package uk.gov.hmcts.reform.bulkscan.model;


import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class BulkScanValidationResponse {

public Enum<Status> status;
public String warnings;
public String errors;


}
