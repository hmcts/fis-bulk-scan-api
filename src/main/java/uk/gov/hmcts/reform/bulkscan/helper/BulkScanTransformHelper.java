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

    @SuppressWarnings("unchecked")
    public static Object getMapObjectAndValue(Object object, Map<String, String> inputFieldsMap) {
        List<Object> list = new ArrayList<>();
        if (object instanceof Map) {
            Map<String, Object> innerMap = (Map<String, Object>) object;
            innerMap.forEach((k, v) -> {
                if (k instanceof String && StringUtils.isNumeric(k)) {
                    list.add(innerMap.get(k));
                } else if (v != null) {
                    innerMap.put(k, getMapObjectAndValue(v, inputFieldsMap));
                }
            });

            if (!list.isEmpty()) {
                list.stream().forEach(eachList -> {
                    getMapObjectAndValue(eachList, inputFieldsMap);
                });
                object = list;
            }
        } else if (object instanceof String && inputFieldsMap.containsKey(object)) {
            object = inputFieldsMap.get(object);
        }
        return object;
    }
}
