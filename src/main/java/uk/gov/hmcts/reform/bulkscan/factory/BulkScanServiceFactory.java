package uk.gov.hmcts.reform.bulkscan.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.model.CaseType;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BulkScanServiceFactory {

    private static final Map<CaseType, BulkScanService> bulkScanServiceCache = new HashMap<>();

    @Autowired
    private BulkScanServiceFactory(List<BulkScanService> services) {
        for (BulkScanService service : services) {
            bulkScanServiceCache.put(service.getCaseType(), service);
        }
    }

    public static BulkScanService getService(CaseType caseType) {
        BulkScanService service = bulkScanServiceCache.get(caseType);
        if (service == null) {
            throw new RuntimeException("Unknown service type: " + caseType.name());
        }
        return service;
    }
}
