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
public class BulkScanValidationRequest {
    @JsonProperty("ocr_data_fields")
    public List<OcrDataField> ocrdatafields;
}
