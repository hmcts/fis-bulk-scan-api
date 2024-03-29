package uk.gov.hmcts.reform.bulkscan.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

public interface BulkScanSectionValidationService {

    void validate(
            BulkScanValidationRequest bulkScanValidationRequest,
            BulkScanValidationResponse bulkScanValidationResponse);

    default Map<String, String> getOcrDataFieldAsMap(List<OcrDataField> ocrdatafields) {
        return ocrdatafields.stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                OcrDataField::getName,
                                each -> each.getValue() != null ? each.getValue() : ""));
    }
}
