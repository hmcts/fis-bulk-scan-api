package uk.gov.hmcts.reform.bulkscan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
public class PartyDetails {
    private final String partyName;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
}
