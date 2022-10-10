package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.THIRD_LINE_OF_ADDRESS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;

@Service
public class BulkScanC63ValidationService {

    public void thirdLineOfAddressMissing(
            Map<String, String> inputFieldsMap,
            BulkScanValidationResponse bulkScanValidationResponse) {

        String thirdLineOfAddress = inputFieldsMap.get(THIRD_LINE_OF_ADDRESS);
        List<String> warningItem = new ArrayList<>();

        if (StringUtils.isEmpty(thirdLineOfAddress)) {
            warningItem.add(String.format(MISSING_FIELD_MESSAGE, THIRD_LINE_OF_ADDRESS));
            bulkScanValidationResponse.addWarning(warningItem);
        }
    }
}
