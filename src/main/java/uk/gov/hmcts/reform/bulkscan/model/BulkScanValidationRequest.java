package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
@Data
@ToString
public class BulkScanValidationRequest {
    public ArrayList<OcrDataField> ocr_data_fields;
}
