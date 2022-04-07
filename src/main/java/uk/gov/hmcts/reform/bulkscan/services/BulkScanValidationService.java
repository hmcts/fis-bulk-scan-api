package uk.gov.hmcts.reform.bulkscan.services;


import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationMandatoryFields;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@NoArgsConstructor
@Service
public class BulkScanValidationService {

    BulkScanValidationResponse bulkScanResponse;
    Errors errors;

    public BulkScanValidationResponse validateBulkService(BulkScanValidationRequest bulkRequest) {
        bulkScanResponse = new BulkScanValidationResponse();
        errors = new Errors();
        validateOcrDataFields(bulkRequest);
        if (bulkScanResponse.getErrors() == null) {
            bulkScanResponse.setStatus(Status.SUCCESS);
        } else {
            bulkScanResponse.setStatus(Status.ERRORS);
        }
        return bulkScanResponse;
    }

    private void validateOcrDataFields(BulkScanValidationRequest bulkRequest) {
        try {
            // Validate field names
            ArrayList<String> item = new ArrayList<>();
            List<String> input = bulkRequest.getOcrdatafields().stream().map(ocrDataField
                                                                                 -> ocrDataField.getName()).collect(
                Collectors.toList());
            Stream.of(BulkScanValidationMandatoryFields.values())
                .forEach(fieldName -> validateOcrDataFieldName(fieldName.getKey(), input, item));

            //Validate Field Values
            long ocrNullValue = bulkRequest.getOcrdatafields().stream()
                .filter(ocrDataField -> ocrDataField.getValue() == null).count();
            if (ocrNullValue == 0) {
                if (bulkRequest.getOcrdatafields().stream()
                    .filter(ocrDataField -> ocrDataField.getValue().trim().isEmpty())
                    .findAny().isPresent()) {
                    item.add("Value should not be empty");
                    errors.setItems(item);
                    bulkScanResponse.setErrors(errors);
                }
            } else {
                item.add("Values should not be null");
                errors.setItems(item);
                bulkScanResponse.setErrors(errors);
            }
        } catch (Exception e) {
            ArrayList<String> item = new ArrayList<>();
            item.add("Value should not be null or empty");
            errors.setItems(item);
            bulkScanResponse.setErrors(errors);
        }

    }



    private void validateOcrDataFieldName(String fieldName, List<String> input,List<String> items) {
        if (!input.contains(fieldName)) {
            items.add(fieldName + " is a mandatory field");
            errors.setItems(items);
            bulkScanResponse.setErrors(errors);
        }
    }
}
