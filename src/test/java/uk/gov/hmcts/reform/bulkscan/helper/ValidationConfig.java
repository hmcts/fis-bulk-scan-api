package uk.gov.hmcts.reform.bulkscan.helper;

import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

public interface ValidationConfig {
    BulkScanFormValidationConfigManager.ValidationConfig getConfig();
}
