package uk.gov.hmcts.reform.bulkscan.services;

import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BulkScanService {

    FormType getCaseType();

    BulkScanValidationResponse validate(BulkScanValidationRequest bulkScanValidationRequest);

    BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest);

    default Map<String, String> getOcrDataFieldAsMap(List<OcrDataField> ocrdatafields) {
        return ocrdatafields.stream().collect(
            Collectors.toUnmodifiableMap(OcrDataField::getName,
                                         each -> each.getValue() != null ? each.getValue() : ""));
    }
}
