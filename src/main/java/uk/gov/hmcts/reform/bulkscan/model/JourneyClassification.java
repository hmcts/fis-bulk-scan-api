package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum JourneyClassification {
    @JsonProperty("exception")
        exception,
    @JsonProperty("new_application")
        newApplication,
    @JsonProperty("supplementary_evidence")
        supplementaryEvidence;

}

