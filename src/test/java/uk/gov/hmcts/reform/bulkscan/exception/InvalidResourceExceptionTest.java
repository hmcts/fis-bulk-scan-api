package uk.gov.hmcts.reform.bulkscan.exception;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.bulkscan.utils.PrlTestConstants.TEST_RESOURCE_NOT_FOUND;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.loadResource;

import org.junit.jupiter.api.Test;

class InvalidResourceExceptionTest {
    @Test
    void testInvalidResourceExceptionThrownForNonExistentFile() throws Exception {
        String createCaseDataFileNotExist = "FGMCaseDataNotExist.json";

        Exception exception =
                assertThrows(
                        Exception.class,
                        () -> {
                            byte[] caseDataJson = loadResource(createCaseDataFileNotExist);
                            assertNull(caseDataJson);
                        });

        assertTrue(exception.getMessage().contains(TEST_RESOURCE_NOT_FOUND), String.valueOf(true));
    }
}
