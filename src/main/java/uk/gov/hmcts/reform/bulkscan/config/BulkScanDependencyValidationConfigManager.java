package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.FieldDependency;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.dependency")
@Configuration
@Setter
@Getter
public class BulkScanDependencyValidationConfigManager {

    private Map<String, List<GroupDependencyField>> fieldGroupDependency;

    @Data
    public static class GroupDependencyValidationConfig {
        List<GroupDependencyField> groupDependencyFields;
    }

    @Data
    public static class GroupDependencyField {
        List<String> dependentFields;
        String dependentValue;
        String groupField;
        String groupValidation;
        String validationCount;
    }

    public List<GroupDependencyField> getGroupDependencyValidationConfig(FieldDependency fieldDependency) {
        return fieldGroupDependency.get(fieldDependency.name());
    }
}
