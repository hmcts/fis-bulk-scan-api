package uk.gov.hmcts.reform.bulkscan.model;



import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString

public class Warnings {

    public List<String> items;
}
