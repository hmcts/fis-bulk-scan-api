package uk.gov.hmcts.reform.bulkscan.config;

import com.microsoft.applicationinsights.core.dependencies.google.common.reflect.TypeToken;
import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

@ConfigurationProperties(prefix = "uk.gov.hmcts.transform")
@Configuration
@Setter
@Getter
public class BulkScanTransformConfigManager {

    private Map<String, TransformationConfig> caseTypeofApplication;

    @Data
    public static class TransformationConfig {
        Map<String, String> caseFields;

        @Getter(AccessLevel.NONE)
        Map<String, Object> caseDataFields;

        public Map<String, Object> getCaseDataFields() {
            Gson gson = new Gson();
            return gson.fromJson(
                    gson.toJson(this.caseDataFields),
                    new TypeToken<HashMap<String, Object>>() {}.getType());
        }
    }

    public TransformationConfig getTransformationConfig(FormType formType) {
        return caseTypeofApplication.get(formType.name());
    }
}
