package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScannedDocuments {

    @JsonProperty("type")
    public String type;

    @JsonProperty("subtype")
    public String subtype;

    @JsonProperty("url")
    public ScanDocument scanDocument;

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
