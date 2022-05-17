package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BulkScanTransformationResponse {

    @JsonProperty("case_creation_details")
    public final CaseCreationDetails caseCreationDetails;

    @JsonProperty("warnings")
    @Builder.Default
    public final List<String> warnings = new ArrayList<>();


}
