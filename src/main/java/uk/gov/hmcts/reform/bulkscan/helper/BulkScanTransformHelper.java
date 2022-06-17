package uk.gov.hmcts.reform.bulkscan.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings("PMD.AvoidReassigningParameters")
public final class BulkScanTransformHelper {

    private BulkScanTransformHelper() {

    }

    /*
    * Returns transformed CCD object.
    * This method is recursive method to replace the values from OCR_DATA_FIELD values and
    * as per CCD requirement list of object should be tranformed as list when sending to CCD
    * it will be transformed into list instead of map while gettting from yaml to java.
     */
    @SuppressWarnings("unchecked")
    public static Object transformToCaseData(Object object, Map<String, String> inputFieldsMap) {
        List<Object> list = new ArrayList<>();
        if (object instanceof Map) {
            Map<String, Object> innerMap = (Map<String, Object>) object;
            innerMap.forEach((k, v) -> {
                if (k instanceof String && StringUtils.isNumeric(k)) {
                    list.add(innerMap.get(k));
                } else if (v != null) {
                    innerMap.put(k, transformToCaseData(v, inputFieldsMap));
                }
            });

            if (!list.isEmpty()) {
                list.forEach(eachList -> {
                    transformToCaseData(eachList, inputFieldsMap);
                });
                object = list;
            }
        } else if (object instanceof String && inputFieldsMap.containsKey(object)) {
            object = inputFieldsMap.get(object);
        } else if (object instanceof String && !inputFieldsMap.containsKey(object)) {
            object = null;
        }
        return object;
    }
}
