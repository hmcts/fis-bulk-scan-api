package uk.gov.hmcts.reform.bulkscan.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder(toBuilder = true)
public class Warnings {

    public List<String> items;
}
