package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseScanDocumentNew {

    @JsonProperty("url")
    public ResponseScanDocument url;
}
