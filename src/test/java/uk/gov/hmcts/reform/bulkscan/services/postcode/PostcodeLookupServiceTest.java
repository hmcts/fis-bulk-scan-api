package uk.gov.hmcts.reform.bulkscan.services.postcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.bulkscan.config.PostcodeLookupConfiguration;
import uk.gov.hmcts.reform.bulkscan.model.postcode.AddressDetails;
import uk.gov.hmcts.reform.bulkscan.model.postcode.PostCodeResponse;
import uk.gov.hmcts.reform.bulkscan.model.postcode.PostCodeResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PostcodeLookupService.class)
@SuppressWarnings("PMD")
class PostcodeLookupServiceTest {

    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private PostcodeLookupConfiguration postcodeLookupConfiguration;

    @Autowired
    private PostcodeLookupService postcodeLookupService;

    @BeforeEach
    private void setup() {
        when(postcodeLookupConfiguration.getUrl()).thenReturn("https://api.os.uk/search/places/v1");
        when(postcodeLookupConfiguration.getAccessKey()).thenReturn("dummy");
    }

    @Test
    public void shouldReturnFalseWhenResultIsNullGivenPostCodeIsValid() {

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("Ok", HttpStatus.ACCEPTED);
        when(restTemplate.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.<HttpEntity<?>>any(),
            ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);

        assertThat(postcodeLookupService.isValidPostCode("IG11 7YL", null)).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenCountryIsNullGivenPostCodeIsValid() {

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("Ok", HttpStatus.ACCEPTED);
        when(postcodeLookupConfiguration.getUrl()).thenReturn("https://api.os.uk/search/places/v1");
        when(postcodeLookupConfiguration.getAccessKey()).thenReturn("dummy");
        when(restTemplate.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.<HttpEntity<?>>any(),
            ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);

        assertThat(postcodeLookupService.isValidPostCode("TW3 1NN", "FLAT 900")).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenCountryIsNullGivenPostCodeIsValidAndWrongHouseNumber() {

        ResponseEntity<String> responseEntity = new ResponseEntity<String>("Ok", HttpStatus.ACCEPTED);

        when(restTemplate.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.<HttpEntity<?>>any(),
            ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);

        assertThat(postcodeLookupService.isValidPostCode("TW3 1NN", "FLAT 900")).isFalse();
    }

    @Test
    public void shouldReturnFalseWhenCountryIsNullGivenPostCodeIsValidAndRightHouseNumber()
        throws JsonProcessingException {

        PostCodeResult result = PostCodeResult.builder().dpa(AddressDetails.builder()
                                         .address("Flat 801 Prince Regent Road")
                                         .buildingNumber("FLAT 801")
                                         .countryCode("UK")
                                         .build()).build();
        ObjectMapper mapper = new ObjectMapper();

        PostCodeResponse res = PostCodeResponse.builder().results(Arrays.asList(result)).build();
        String resultJson = mapper.writeValueAsString(res);
        ResponseEntity<String> responseEntity = ResponseEntity.ok(resultJson);

        when(objectMapper.readValue(anyString(), eq(PostCodeResponse.class))).thenReturn(res);
        when(restTemplate.exchange(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod.class),
            ArgumentMatchers.<HttpEntity<?>>any(),
            ArgumentMatchers.<Class<String>>any()
        )).thenReturn(responseEntity);

        assertThat(postcodeLookupService.isValidPostCode("TW3 1NN", "FLAT 801")).isTrue();
    }


    @Test
    public void shouldReturnExpcetionWhenUrlIsEmpty() {
        when(postcodeLookupConfiguration.getUrl()).thenReturn(null);
        assertThrows(
            RuntimeException.class, () ->
                postcodeLookupService.isValidPostCode("IG11 7YL", null)
        );
    }

    @Test
    public void shouldReturnExpcetionWhenKeyIsEmpty() {
        when(postcodeLookupConfiguration.getAccessKey()).thenReturn("");
        assertThrows(
            RuntimeException.class, () ->
                postcodeLookupService.isValidPostCode("IG11 7YL", null)
        );
    }

}
