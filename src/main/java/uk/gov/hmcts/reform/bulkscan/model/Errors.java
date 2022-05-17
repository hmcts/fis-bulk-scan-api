package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder(toBuilder = true)
@Data
public class Errors {
    public List<String> items;
}

