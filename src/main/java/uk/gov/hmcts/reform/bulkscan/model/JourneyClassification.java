package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum JourneyClassification {
    @JsonProperty("exception")
    exception,
    @JsonProperty("new_application")
    newApplication,
    @JsonProperty("supplementary_evidence")
    supplementaryEvidence,
    @JsonProperty("EXCEPTION")
    EXCEPTION,
    @JsonProperty("NEW_APPLICATION")
    NEW_APPLICATION,
    @JsonProperty("SUPPLEMENTARY_EVIDENCE")
    SUPPLEMENTARY_EVIDENCE,
    @JsonProperty("SUPPLEMENTARY_EVIDENCE_WITH_OCR")
    SUPPLEMENTARY_EVIDENCE_WITH_OCR;
}
