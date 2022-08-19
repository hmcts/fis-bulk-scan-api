package uk.gov.hmcts.reform.bulkscan.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock AuthTokenValidator authTokenValidator;
    AuthService authService = null;

    @BeforeEach
    void setUp() {
        authService = new AuthService( authTokenValidator , "Fis_cos_api,fis_bulk_scn_api");
        
    }

    @Test
    void authenticate() {

    }

    @Test
    void assertIsAllowedToHandleService() {

    }
}
