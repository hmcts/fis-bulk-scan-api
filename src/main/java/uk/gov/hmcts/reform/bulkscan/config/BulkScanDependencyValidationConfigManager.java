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

    private Map<String, GroupDependencyFields> fieldGroupDependency;

    @Data
    public static class GroupDependencyFields {
        List<String> groupExemptionToAttendMiam;
        List<String> groupNoMiamDomesticViolence;
    }

    public GroupDependencyFields getGroupDependencyFields(FieldDependency fieldDependency) {
        return fieldGroupDependency.get(fieldDependency.name());
    }
}
