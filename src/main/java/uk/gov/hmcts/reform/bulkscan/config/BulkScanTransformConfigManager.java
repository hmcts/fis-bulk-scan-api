package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.transform")
@Configuration
@Setter
@Getter
public class BulkScanTransformConfigManager {

    private Map<String, TransformationConfig> caseTypeofApplication;

    @Data
    public static class TransformationConfig {
        Map<String, String> caseFields;
        Map<String, Object> caseDataFields;
    }

    public TransformationConfig getTransformationConfig(FormType formType) {
        return caseTypeofApplication.get(formType.name());
    }
}
