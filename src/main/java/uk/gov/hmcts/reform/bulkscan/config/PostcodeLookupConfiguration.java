package uk.gov.hmcts.reform.bulkscan.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@SuppressWarnings("PMD")
public class PostcodeLookupConfiguration {

    private final String url;
    private final String accessKey;

    public PostcodeLookupConfiguration(
            @Value("${postcodelookup.api.url}") String url,
            @Value("${postcodelookup.api.key}") String accessKey) {
        this.url = url;
        this.accessKey = accessKey;
    }
}
