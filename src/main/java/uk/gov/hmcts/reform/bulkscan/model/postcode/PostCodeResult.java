package uk.gov.hmcts.reform.bulkscan.model.postcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("PMD")
public class PostCodeResult {
    @JsonProperty("DPA")
    private AddressDetails dpa;
}
