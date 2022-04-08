package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScannedDocuments {

    @JsonProperty("type")
    public String type;
    @JsonProperty("subtype")
    public String subtype;
    @JsonProperty("url")
    public String url;
    @JsonProperty("control_number")
    public String controlNumber;
    @JsonProperty("file_name")
    public String fileName;
    @JsonProperty("scanned_date")
    public String scannedDate;
    @JsonProperty("delivery_date")
    public String deliveryDate;

}
