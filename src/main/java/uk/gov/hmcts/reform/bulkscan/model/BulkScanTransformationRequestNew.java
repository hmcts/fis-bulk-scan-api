package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkScanTransformationRequestNew {

    @JsonProperty("exception_record_case_type_id")
    public String caseTypeId;

    @JsonProperty("po_box")
    public String poBox;

    @JsonProperty("po_box_jurisdiction")
    public String poBoxJurisdiction;

    @JsonProperty("form_type")
    public String formType;

    @JsonProperty("journey_classification")
    public JourneyClassification journeyClassification;

    @JsonProperty("delivery_date")
    public LocalDateTime deliveryDate;

    @JsonProperty("opening_date")
    public LocalDateTime openingDate;

    @JsonProperty("scanned_documents")
    public List<ScannedDocuments> scannedDocuments;

    @JsonProperty("ocr_data_fields")
    public List<OcrDataFieldNew> ocrdatafields;

    @JsonProperty("exception_record_id")
    public String exceptionRecordId;

    @JsonProperty("envelope_id")
    public String envelopeId;

    @JsonProperty("is_automated_process")
    public Boolean isAutomatedProcess;
}
