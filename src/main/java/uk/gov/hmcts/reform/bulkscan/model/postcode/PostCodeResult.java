package uk.gov.hmcts.reform.bulkscan.model.postcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@SuppressWarnings("PMD")
public class PostCodeResult {
    @JsonProperty("DPA")
    private AddressDetails dpa;
}
