package uk.gov.hmcts.reform.bulkscan.utils;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

//@FeignClient(name = "case-orchestration-api", url = "${case.orchestration.service.base.uri}")
public interface BulkScanApiClient {

    @ApiOperation("Root endpoint returning welcome text")
    @GetMapping("/")
    String welcome();

    @ApiOperation("Retrieve service's swagger specs")
    @GetMapping("/v2/api-docs")
    byte[] apiDocs();
}
