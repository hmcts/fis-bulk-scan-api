package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.bulkscan.model.FormType;

import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.transform")
@Configuration
@Setter
public class BulkScanTransformConfigManager {

    private Map<String, Object> caseTypeofApplication;


    public Object getSourceAndTargetFields(FormType formType) {
        return caseTypeofApplication.get(formType.name());
    }
}
