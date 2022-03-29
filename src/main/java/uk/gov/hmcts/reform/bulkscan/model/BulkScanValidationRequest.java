package uk.gov.hmcts.reform.bulkscan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class BulkScanValidationRequest {
    public ArrayList<OcrDataField> ocr_data_fields;
}
