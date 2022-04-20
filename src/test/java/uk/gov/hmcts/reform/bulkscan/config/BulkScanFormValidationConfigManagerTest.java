package uk.gov.hmcts.reform.bulkscan.config;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BulkScanFormValidationConfigManager.class)
@TestPropertySource("classpath:application-test-form.yaml")
public class BulkScanFormValidationConfigManagerTest {

    @Autowired
    private BulkScanFormValidationConfigManager configManager;

    @Test
    @Ignore
    public void test() {
        assertNotNull(configManager.getCaseTypeofApplication());
    }

}
