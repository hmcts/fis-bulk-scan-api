package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OcrDataFieldNew {
    @JsonProperty("metadata_field_name")
    public String name;

    @JsonProperty("metadata_field_value")
    public String value;
}
