package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.CaseType;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.form-validation")
@Configuration
@Setter
@Getter
public class BulkScanFormValidationConfigManager {

    private Map<String, ValidationConfig> caseTypeofApplication;

    @Data
    public static class ValidationConfig {
        private List<String> mandatoryFields;
        private RegexValidationConfig regexValidationFields;
    }

    @Data
    public static class RegexValidationConfig {
        private RegexFieldsConfig dateFields;
        private RegexFieldsConfig emailFields;
        private RegexFieldsConfig numericFields;
    }

    @Data
    public static class RegexFieldsConfig {
        private List<String> fieldNames;
        private String regex;
    }

    public ValidationConfig getValidationConfig(CaseType caseType) {
        return caseTypeofApplication.get(caseType.name());
    }
}
