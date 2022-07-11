package uk.gov.hmcts.reform.bulkscan.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanGroupValidationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BulkScanGroupValidatorHelper {

    @Autowired
    private BulkScanGroupValidationConfigManager configManager;

    public List<String> validate(List<OcrDataField> ocrDataFields, FormType formType) {
        Map<String, Map<String, List<String>>> configManagerFormType = configManager.getFormType();
        List<Map<String, List<String>>> mapList = configManagerFormType.entrySet()
            .stream().filter(stringMapEntry -> stringMapEntry.getKey().equalsIgnoreCase(formType.name()))
            .map(stringMapEntry -> stringMapEntry.getValue())
            .collect(Collectors.toList());

        return ocrDataFields.stream()
            .map(ocrDataField -> getSubFieldsByParent(ocrDataField, mapList))
            .flatMap(subFields -> subFields.stream())
            .collect(Collectors.toList());
    }


    private List<String> getSubFieldsByParent(OcrDataField ocrDataField, List<Map<String, List<String>>> fields) {
        List<String> subFields = new ArrayList<>();
        fields.get(0).entrySet().stream()
            .forEach(stringListEntry ->
                     {
                         if (stringListEntry.getKey().equals(ocrDataField.getName()) && "true".equals(ocrDataField.getValue()))
                             subFields.addAll(stringListEntry.getValue());
                     });
        return subFields;
    }
}
