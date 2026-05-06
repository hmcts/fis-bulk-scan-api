package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseScanDocumentNew {

    @JsonProperty("url")
    public ResponseScanDocument url;

    @JsonProperty("type")
    public String type;

    @JsonProperty("subtype")
    public String subtype;

    @JsonProperty("control_number")
    public String controlNumber;

    @JsonProperty("file_name")
    public String fileName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @JsonProperty("scanned_date")
    public LocalDateTime scannedDate;

    @JsonProperty("delivery_date")
    public LocalDateTime deliveryDate;
}
