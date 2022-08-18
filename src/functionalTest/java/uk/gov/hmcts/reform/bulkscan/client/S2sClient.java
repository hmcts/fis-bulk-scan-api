package uk.gov.hmcts.reform.bulkscan.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@TestPropertySource("classpath:application-e2e.yaml")
@Service
public class S2sClient {

    @Value("${idam.s2s-auth.url}")
    private String testURL;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    public String serviceAuthTokenGenerator() {
        System.out.println(testURL);
        return authTokenGenerator.generate();
    }
}
