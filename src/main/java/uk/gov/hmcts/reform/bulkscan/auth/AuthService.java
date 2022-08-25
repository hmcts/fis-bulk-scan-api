package uk.gov.hmcts.reform.bulkscan.auth;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.bulkscan.exception.ForbiddenException;
import uk.gov.hmcts.reform.bulkscan.exception.UnauthorizedException;

@Service
public class AuthService {

    public static final String COMMA_SEPARATOR = ",";
    public static final String MISSING_SERVICE_AUTHORIZATION_HEADER =
            "Missing ServiceAuthorization header";
    public static final String DOES_NOT_HAVE_PERMISSIONS_TO_REQUEST_CASE_CREATION =
            "Service %s does not have permissions to request case creation";

    private final AuthTokenValidator authTokenValidator;

    private final List<String> allowedServices;

    public AuthService(
            AuthTokenValidator authTokenValidator,
            @Value("${allowed-services}") String configuredServices) {
        this.authTokenValidator = authTokenValidator;
        this.allowedServices =
                Stream.of(configuredServices.split(COMMA_SEPARATOR))
                        .map(String::trim)
                        .collect(Collectors.toList());
    }

    public String authenticate(String authHeader) {
        if (!StringUtils.hasText(authHeader)) {
            throw new UnauthorizedException(MISSING_SERVICE_AUTHORIZATION_HEADER);
        } else {
            return authTokenValidator.getServiceName(authHeader);
        }
    }

    public void assertIsAllowedToHandleService(String serviceName) {
        if (!allowedServices.contains(serviceName)) {
            throw new ForbiddenException(
                    String.format(DOES_NOT_HAVE_PERMISSIONS_TO_REQUEST_CASE_CREATION, serviceName));
        }
    }
}
