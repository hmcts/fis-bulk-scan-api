package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.dependency")
@Configuration
@Setter
@Getter
public class BulkScanDependencyValidationConfigManager {

    private Map<String, List<GroupDependencyConfig>> fieldGroupDependency;

    @Data
    public static class GroupDependencyValidationConfig {
        List<GroupDependencyConfig> groupDependencyFields;
    }

    @Data
    public static class GroupDependencyConfig {
        List<String> dependentFields;
        String dependentFieldValue;
        String groupFieldName;
        String groupValidationValue;
        String fieldValueToBePresent;
    }

    public List<GroupDependencyConfig> getGroupDependencyValidationConfig(FormType formType) {
        return fieldGroupDependency.get(formType.name());
    }
}
