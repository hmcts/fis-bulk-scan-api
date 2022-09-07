package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanDocument {

    @JsonProperty("document_url")
    public String url;

    @JsonProperty("document_filename")
    public String filename;

    @JsonProperty("document_binary_url")
    public String binaryUrl;
}
