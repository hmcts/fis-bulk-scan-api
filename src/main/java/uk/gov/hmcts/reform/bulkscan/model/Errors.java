package uk.gov.hmcts.reform.bulkscan.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Errors {
    public List<String> items;
}
