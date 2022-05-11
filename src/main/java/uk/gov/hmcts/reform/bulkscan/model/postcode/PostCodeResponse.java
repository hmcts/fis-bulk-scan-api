package uk.gov.hmcts.reform.bulkscan.model.postcode;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@SuppressWarnings("PMD")
public class PostCodeResponse {
    private List<PostCodeResult> results;
}
