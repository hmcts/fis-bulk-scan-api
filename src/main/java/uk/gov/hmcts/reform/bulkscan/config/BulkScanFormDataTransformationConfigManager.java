package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.transformation")
@Configuration
@Setter
@Getter
public class BulkScanFormDataTransformationConfigManager {

    private Map<String, TransformationConfig> caseTypeofApplication;

    @Data
    public static class TransformationConfig {
        List<String> others;
        List<String> child;
        List<String> appellant;
        List<String> respondent;
    }

    public TransformationConfig getTransformationConfig(FormType formType) {
        return caseTypeofApplication.get(formType.name());
    }
}
