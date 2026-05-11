package uk.gov.hmcts.reform.bulkscan.model.court;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServiceArea {

    private String slug;
    private String name;
    private String onlineText;
    private String onlineUrl;
    List<Court> courts;


}
