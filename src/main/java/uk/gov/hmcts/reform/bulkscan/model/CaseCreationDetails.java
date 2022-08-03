package uk.gov.hmcts.reform.bulkscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CaseCreationDetails {

    @JsonProperty("case_type_id")
    public final String caseTypeId;

    @JsonProperty("event_id")
    public final String eventId;

    @JsonProperty("case_data")
    public final Map<String, Object> caseData;

    public CaseCreationDetails(String caseTypeId, String eventId, Map<String, Object> caseData) {
        this.caseTypeId = caseTypeId;
        this.eventId = eventId;
        this.caseData = caseData;
    }
}
