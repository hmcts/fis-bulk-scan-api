package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkScanTransformationRequest {

    @NotNull(message = "exception_record_case_type_id cannot be null")
    @JsonProperty("exception_record_case_type_id")
    public String caseTypeId;

    @NotNull(message = "po_box type cannot be null")
    @JsonProperty("po_box")
    public String poBox;

    @NotNull(message = "po_box_jurisdiction type cannot be null")
    @JsonProperty("po_box_jurisdiction")
    public String poBoxJurisdiction;

    @NotNull(message = "form_type type cannot be null")
    @JsonProperty("form_type")
    public String formType;

    @NotNull(message = "journey_classification type cannot be null")
    @JsonProperty("journey_classification")
    public JourneyClassification journeyClassification;

    @NotNull(message = "delivery_date type cannot be null")
    @JsonProperty("delivery_date")
    public LocalDateTime deliveryDate;

    @NotNull(message = "opening_date type cannot be null")
    @JsonProperty("opening_date")
    public LocalDateTime openingDate;

    @NotNull(message = "scanned_documents type cannot be null")
    @JsonProperty("scanned_documents")
    public List<ScannedDocuments> scannedDocuments;

    @NotNull(message = "ocr_data_fields type cannot be null")
    @JsonProperty("ocr_data_fields")
    public List<OcrDataField> ocrdatafields;

    @NotNull(message = "exception_record_id type cannot be null")
    @JsonProperty("exception_record_id")
    public String exceptionRecordId;

    @NotNull(message = "envelope_id type cannot be null")
    @JsonProperty("envelope_id")
    public String envelopeId;

    @NotNull(message = "is_automated_process type cannot be null")
    @JsonProperty("is_automated_process")
    public boolean isAutomatedProcess;
}
