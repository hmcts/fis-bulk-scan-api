package uk.gov.hmcts.reform.bulkscan.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@TestPropertySource(locations = "classpath:application_e2e.yaml")
@Service
public class S2sClient {

    private String s2sToken;

    @Autowired private AuthTokenGenerator authTokenGenerator;

    public String serviceAuthTokenGenerator() {
        if (!StringUtils.hasText(this.s2sToken)) {
            this.s2sToken = authTokenGenerator.generate();
        }
        return this.s2sToken;
    }
}
