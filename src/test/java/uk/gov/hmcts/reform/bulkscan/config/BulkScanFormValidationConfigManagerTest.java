package uk.gov.hmcts.reform.bulkscan.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class BulkScanFormValidationConfigManagerTest {

    @Spy
    @Autowired
    BulkScanFormValidationConfigManager configManager;

    @Test
    void testConfigManager() {
        Map<String, BulkScanFormValidationConfigManager.ValidationConfig> map = configManager
            .getCaseTypeofApplication();
        assertNotNull(map);
        assertTrue(map.containsKey(FormType.C100.name()));
        assertTrue(map.containsKey(FormType.FL401.name()));
        assertTrue(map.containsKey(FormType.FL403.name()));

    }

    @Test
    void testConfigShouldNotBeNull() {
        BulkScanFormValidationConfigManager.ValidationConfig validationConfig =
            configManager.getValidationConfig(FormType.C100);
        assertNotNull(validationConfig);
    }
}
