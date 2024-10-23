package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OcrDataFieldNew {
    @JsonProperty("name")
    public String name;

    @JsonProperty("value")
    public String value;
}
