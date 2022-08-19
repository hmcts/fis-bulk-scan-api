package uk.gov.hmcts.reform.bulkscan.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.bulkscan.exception.ForbiddenException;
import uk.gov.hmcts.reform.bulkscan.exception.UnauthorizedException;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthService {

    private final AuthTokenValidator authTokenValidator;

    private final List<String> allowedServices;

    public AuthService(
        AuthTokenValidator authTokenValidator,
        @Value("${allowed-services}") String configuredServicesCsv
    ) {
        this.authTokenValidator = authTokenValidator;
        List<String> items= Stream.of(configuredServicesCsv.split(","))
            .map(String::trim)
            .collect(Collectors.toList());
        this.allowedServices = items;
    }

    public String authenticate(String authHeader) {
        if (authHeader == null) {
            throw new UnauthorizedException("Missing ServiceAuthorization header");
        } else {
            return authTokenValidator.getServiceName(authHeader);
        }
    }

    public void assertIsAllowedToHandleService(String serviceName) {
        if (!allowedServices.contains(serviceName)) {
            throw new ForbiddenException(
                "Service " + serviceName + " does not have permissions to request case creation"
            );
        }
    }
}
