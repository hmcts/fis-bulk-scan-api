package uk.gov.hmcts.reform.bulkscan.model.postcode;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@SuppressWarnings("PMD")
public class PostCodeResponse {
    private List<PostCodeResult> results;
}
