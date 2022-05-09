package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Child {

    private String firstName;
    private String lastName;
}
