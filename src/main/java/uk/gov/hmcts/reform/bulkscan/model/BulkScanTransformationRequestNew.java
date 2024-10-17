package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkScanTransformationRequestNew {

    @JsonProperty("case_type_id")
    public String caseTypeId;

    @JsonProperty("id")
    public String id;

    @JsonProperty("po_box")
    public String poBox;

    @JsonProperty("po_box_jurisdiction")
    public String poBoxJurisdiction;

    @JsonProperty("form_type")
    public String formType;

    @JsonProperty("journey_classification")
    public JourneyClassification journeyClassification;

    @JsonProperty("delivery_date")
    public String deliveryDate;

    @JsonProperty("opening_date")
    public String openingDate;

    @JsonProperty("scanned_documents")
    public List<ScannedDocuments> scannedDocuments;

    @JsonProperty("Metadata_file")
    public List<OcrDataFieldNew> ocrdatafields;
}