package uk.gov.hmcts.reform.bulkscan.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Field {
    private String name;
    private String value;
}
