package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.FieldDependency;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.form-validation")
@Configuration
@Setter
@Getter
public class BulkScanFormValidationConfigManager {

    private Map<String, ValidationConfig> caseTypeofApplication;
    private Map<String, GroupDependencyValidationConfig> groupDependencyOfForm;

    @Data
    public static class ValidationConfig {
        List<String> mandatoryFields;
        List<String> optionalFields;
        RegexValidationConfig regexValidationFields;
    }

    @Data
    public static class GroupDependencyValidationConfig {
        GroupDependencyFields groupFieldDependencyDetail;
    }

    @Data
    public static class GroupDependencyFields {
        GroupDependencyField groupDependency1;
        GroupDependencyField groupDependency2;
        GroupDependencyField groupDependency3;
        GroupDependencyField groupDependency4;
        GroupDependencyField groupDependency5;
        GroupDependencyField groupDependency6;
        GroupDependencyField groupDependency7;
    }

    @Data
    public static class GroupDependencyField {
        String groupFieldName;
        List<String> dependentFieldNames;
    }

    @Data
    public static class RegexValidationConfig {
        RegexFieldsConfig dateFields;
        RegexFieldsConfig emailFields;
        RegexFieldsConfig numericFields;
        RegexFieldsConfig postCodeFields;
        RegexFieldsConfig phoneNumberFields;
        RegexFieldsConfig faxNumberFields;
        RegexFieldsConfig xorConditionalFields;
        RegexFieldsConfig alphaNumericFields;
    }

    @Data
    public static class RegexFieldsConfig {
        List<String> fieldNames;
        String regex;
    }

    public GroupDependencyValidationConfig getFieldDependenyConfig(FieldDependency fieldDependency) {
        return groupDependencyOfForm.get(fieldDependency.name());
    }

    public ValidationConfig getValidationConfig(FormType formType) {
        return caseTypeofApplication.get(formType.name());
    }
}
