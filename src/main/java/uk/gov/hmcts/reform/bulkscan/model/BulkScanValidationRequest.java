package uk.gov.hmcts.reform.bulkscan.model;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class BulkScanValidationRequest {
    public List<OcrDataField> ocrdatafields;
}
