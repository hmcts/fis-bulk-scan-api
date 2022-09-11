package uk.gov.hmcts.reform.bulkscan.model.postcode;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("PMD")
public class PostCodeResponse {
    private List<PostCodeResult> results;
}
