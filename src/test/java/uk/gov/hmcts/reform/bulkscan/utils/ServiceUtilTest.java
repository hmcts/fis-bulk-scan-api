package uk.gov.hmcts.reform.bulkscan.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.hmcts.reform.bulkscan.model.FormType.FL401A;
import static uk.gov.hmcts.reform.bulkscan.utils.TestResourceUtil.readFileFrom;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;

@ExtendWith(SpringExtension.class)
class ServiceUtilTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String FL401_TRANSFORM_REQUEST_PATH =
            "classpath:request/bulk-scan-fl401-transform-input.json";
    @Mock BulkScanTransformConfigManager transformConfigManager;

    @Test
    void testValidDateFormat() throws IOException {
        Map<String, Object> caseDataFields = new HashMap<>();
        caseDataFields.put("test", "test");
        Map<String, String> caseFields = new HashMap<>();
        caseFields.put("CASE_TYPE_ID", "test");
        BulkScanTransformConfigManager.TransformationConfig transformationConfig =
                new BulkScanTransformConfigManager.TransformationConfig();
        transformationConfig.setCaseDataFields(caseDataFields);
        transformationConfig.setCaseFields(caseFields);
        Mockito.when(transformConfigManager.getTransformationConfig(Mockito.any()))
                .thenReturn(transformationConfig);
        BulkScanTransformationRequest bulkScanTransformationRequest =
                mapper.readValue(
                        readFileFrom(FL401_TRANSFORM_REQUEST_PATH),
                        BulkScanTransformationRequest.class);
        BulkScanTransformationResponse bulkScanTransformationResponse =
                ServiceUtil.getBulkScanTransformationResponseBuilder(
                                bulkScanTransformationRequest,
                                new HashMap<>(),
                                transformConfigManager.getTransformationConfig(FL401A),
                                new HashMap<>())
                        .build();
        assertNotNull(bulkScanTransformationResponse);
    }
}
