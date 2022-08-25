package uk.gov.hmcts.reform.bulkscan.auth;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.bulkscan.exception.ForbiddenException;
import uk.gov.hmcts.reform.bulkscan.exception.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    @MockBean AuthTokenValidator mockAuthTokenValidator;

    AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(mockAuthTokenValidator, "Fis_cos_api, fis_bulk_scn_api");
    }

    @Test
    void authenticateNullException() {
        Assertions.assertThrows(
                UnauthorizedException.class,
                () -> {
                    authService.authenticate(null);
                });
    }

    @Test
    void authenticateSuccess() {
        when(mockAuthTokenValidator.getServiceName("Fis_cos_api")).thenReturn("Fis_cos_api");

        Assertions.assertEquals("Fis_cos_api", authService.authenticate("Fis_cos_api"));
    }

    @Test
    void assertIsAllowedToHandleServiceSuccess() {
        authService.assertIsAllowedToHandleService("Fis_cos_api");
    }

    @Test
    void assertIsAllowedToHandleServiceException() {
        Assertions.assertThrows(
                ForbiddenException.class,
                () -> {
                    authService.assertIsAllowedToHandleService(null);
                });
    }
}
