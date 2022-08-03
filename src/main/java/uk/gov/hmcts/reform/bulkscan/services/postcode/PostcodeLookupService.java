package uk.gov.hmcts.reform.bulkscan.services.postcode;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.bulkscan.config.PostcodeLookupConfiguration;
import uk.gov.hmcts.reform.bulkscan.exception.PostCodeValidationException;
import uk.gov.hmcts.reform.bulkscan.model.postcode.PostCodeResponse;

@Service
@Slf4j
@SuppressWarnings("unchecked")
public class PostcodeLookupService {

    private static final Logger LOG = LoggerFactory.getLogger(PostcodeLookupService.class);

    @Autowired ObjectMapper objectMapper;

    @Autowired RestTemplate restTemplate;

    @Autowired PostcodeLookupConfiguration configuration;

    public boolean isValidPostCode(String postcode, String addressOrHouseNumber) {
        PostCodeResponse response = fetchCountryFromPostCode(postcode.toUpperCase(Locale.UK));

        boolean isValidPostCode =
                response != null
                        && response.getResults() != null
                        && !response.getResults().isEmpty();

        boolean isHouseNumberValid = true;
        if (isValidPostCode && addressOrHouseNumber != null && !addressOrHouseNumber.isEmpty()) {
            List<String> buildingNumberList =
                    response.getResults().stream()
                            .filter(
                                    eachObj ->
                                            null != eachObj.getDpa()
                                                    && eachObj.getDpa().getBuildingNumber() != null
                                                    && !eachObj.getDpa()
                                                            .getBuildingNumber()
                                                            .isEmpty())
                            .map(eachObj -> eachObj.getDpa().getBuildingNumber())
                            .collect(Collectors.toList());
            if (!buildingNumberList.isEmpty()) {
                isHouseNumberValid =
                        buildingNumberList.stream()
                                .anyMatch(
                                        eachBuildNumber ->
                                                eachBuildNumber.contains(addressOrHouseNumber));
            }
        }

        return isValidPostCode && isHouseNumberValid;
    }

    private PostCodeResponse fetchCountryFromPostCode(String postcode) {
        PostCodeResponse results = null;
        try {
            Map<String, String> params = new HashMap<>();
            params.put("postcode", StringUtils.deleteWhitespace(postcode));
            String url = configuration.getUrl();
            String key = configuration.getAccessKey();
            params.put("key", key);
            if (null == url) {
                throw new PostCodeValidationException("Postcode URL is null");
            }
            if (null == key || StringUtils.isEmpty(key)) {
                throw new PostCodeValidationException("Postcode API Key is null");
            }
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/postcode");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");

            HttpEntity<String> response =
                    restTemplate.exchange(
                            builder.toUriString(),
                            HttpMethod.GET,
                            new HttpEntity(headers),
                            String.class);

            HttpStatus responseStatus = ((ResponseEntity) response).getStatusCode();

            if (responseStatus.value() == org.apache.http.HttpStatus.SC_OK) {
                results = objectMapper.readValue(response.getBody(), PostCodeResponse.class);

                return results;
            } else if (responseStatus.value() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
                LOG.info("Postcode " + postcode + " not found");
            } else {
                LOG.info("Postcode lookup failed with status {}", responseStatus.value());
            }

        } catch (Exception e) {
            LOG.error("Postcode Lookup Failed - ", e.getMessage());
            throw new PostCodeValidationException(e.getMessage(), e);
        }

        return results;
    }
}
