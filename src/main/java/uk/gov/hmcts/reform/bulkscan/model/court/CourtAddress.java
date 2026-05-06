package uk.gov.hmcts.reform.bulkscan.model.court;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class CourtAddress {

    @JsonProperty("address_lines")
    private final List<String> addressLines;
    private final String postcode;
    private final String town;
    private final String type;

}
