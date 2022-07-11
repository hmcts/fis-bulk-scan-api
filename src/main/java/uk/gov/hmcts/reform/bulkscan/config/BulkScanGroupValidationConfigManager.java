package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "uk.gov.hmcts.form-group")
@Configuration
@Setter
@Getter
public class BulkScanGroupValidationConfigManager {

    private Map<String, Map<String, List<String>>> formType;
}
