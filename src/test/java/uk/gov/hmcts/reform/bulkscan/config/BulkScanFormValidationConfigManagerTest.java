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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(map.containsKey(FormType.A1.name()));

    }

    @Test
    void testConfigShouldNotBeNull() {
        assertNotNull(getValidationConfigByFormType(FormType.C100));

        assertNotNull(getValidationConfigByFormType(FormType.A1));
        assertFalse(getValidationConfigByFormType(FormType.A1).mandatoryFields.isEmpty());
        assertEquals("applicant_name", getValidationConfigByFormType(FormType.A1).mandatoryFields.get(0));
        assertEquals("applicant_address", getValidationConfigByFormType(FormType.A1).mandatoryFields.get(1));
        assertEquals("applicant_ref", getValidationConfigByFormType(FormType.A1).mandatoryFields.get(2));
        assertEquals("applicant_postcode", getValidationConfigByFormType(FormType.A1).mandatoryFields.get(3));
        assertEquals("applicant_telephone_no", getValidationConfigByFormType(FormType.A1).mandatoryFields.get(4));
    }

    private BulkScanFormValidationConfigManager.ValidationConfig getValidationConfigByFormType(FormType formType) {
        return configManager.getValidationConfig(formType);
    }


}
