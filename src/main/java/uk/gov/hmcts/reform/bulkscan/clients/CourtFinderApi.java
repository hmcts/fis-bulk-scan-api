package uk.gov.hmcts.reform.bulkscan.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.hmcts.reform.bulkscan.constants.BulkScanPrlConstants;
import uk.gov.hmcts.reform.bulkscan.model.court.Court;
import uk.gov.hmcts.reform.bulkscan.model.court.ServiceArea;

@FeignClient(name = "court-finder-api", primary = false, url = "${courtfinder.api.url}")
public interface CourtFinderApi {

    @GetMapping(value = BulkScanPrlConstants.DOMESTIC_ABUSE_POSTCODE_URL)
    ServiceArea findClosestDomesticAbuseCourtByPostCode(@PathVariable("postcode") String postcode);

    @GetMapping(value = BulkScanPrlConstants.COURT_DETAILS_URL)
    Court getCourtDetails(@PathVariable("court-slug") String courtSlug);
}
