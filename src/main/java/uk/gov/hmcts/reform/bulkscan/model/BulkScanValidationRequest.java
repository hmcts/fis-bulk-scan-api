package uk.gov.hmcts.reform.bulkscan.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class BulkScanValidationRequest {
    @JsonProperty("ocr_data_fields")
    public List<OcrDataField> ocrdatafields;
}
