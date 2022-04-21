package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder(toBuilder = true)
public class Warnings {

    public List<String> items;
}
