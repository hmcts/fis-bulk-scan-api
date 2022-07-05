package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseScanDocument {

    @JsonProperty("document_url")
    public String url;
    @JsonProperty("document_filename")
    public String filename;
    @JsonProperty("document_binary_url")
    public String binaryUrl;
}
