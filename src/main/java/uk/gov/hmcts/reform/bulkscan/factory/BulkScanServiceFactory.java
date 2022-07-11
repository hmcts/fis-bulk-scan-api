package uk.gov.hmcts.reform.bulkscan.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public final class BulkScanServiceFactory {

    private static final Map<FormType, BulkScanService> bulkScanServiceCache = new HashMap<>();

    @Autowired
    private BulkScanServiceFactory(List<BulkScanService> services) {
        for (BulkScanService service : services) {
            bulkScanServiceCache.put(service.getCaseType(), service);
        }
    }

    public static BulkScanService getService(FormType caseType) {
        if (caseType.equals(FormType.A58_STEP_PARENT) || caseType.equals(FormType.A58_RELINQUISHED)) {
            return bulkScanServiceCache.get(FormType.A58);
        }

        if (caseType.equals(FormType.A60)) {
            return bulkScanServiceCache.get(FormType.A60);
        }
        return bulkScanServiceCache.get(caseType);
    }
}
