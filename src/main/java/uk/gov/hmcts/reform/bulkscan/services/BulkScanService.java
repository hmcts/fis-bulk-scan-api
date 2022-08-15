package uk.gov.hmcts.reform.bulkscan.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

public interface BulkScanService {

    FormType getCaseType();

    BulkScanValidationResponse validate(BulkScanValidationRequest bulkScanValidationRequest);

    BulkScanTransformationResponse transform(
            BulkScanTransformationRequest bulkScanTransformationRequest);

    default Map<String, String> getOcrDataFieldAsMap(List<OcrDataField> ocrdatafields) {
        return ocrdatafields.stream()
                .filter(ocrDataField -> StringUtils.hasText(ocrDataField.getName()))
                .collect(
                        Collectors.toUnmodifiableMap(
                                OcrDataField::getName,
                                each ->
                                        StringUtils.hasText(each.getValue())
                                                ? each.getValue()
                                                : org.apache.commons.lang3.StringUtils.EMPTY));
    }
}
