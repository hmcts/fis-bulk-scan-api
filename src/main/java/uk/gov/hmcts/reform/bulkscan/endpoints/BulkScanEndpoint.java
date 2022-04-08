package uk.gov.hmcts.reform.bulkscan.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanValidationService;


@RestController
@RequestMapping(path = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/", description = "Standard  API")
public class BulkScanEndpoint {

    public static final String SERVICEAUTHORIZATION = "serviceauthorization";
    public static final String CONTENT_TYPE = "content-type";

    @Autowired
    BulkScanValidationService bulkScanValidationService;

    @PostMapping(value = "/forms/{case_type}/validate-ocr/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
        value = "",
        notes = " "
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "success"),
        @ApiResponse(code = 401, message = "Provided S2S token is missing or invalid"),
        @ApiResponse(code = 403, message = "S2S token is not authorized to use the service"),
        @ApiResponse(code = 404, message = "Form name not found")

    })
    public ResponseEntity<BulkScanValidationResponse>
        validateOcrData(@RequestHeader(SERVICEAUTHORIZATION)
                        String s2sToken, @RequestHeader(CONTENT_TYPE) String contentType,
                    @RequestBody final BulkScanValidationRequest bulkScanValidationRequest) {
        BulkScanValidationResponse bulkScanResponse =
                bulkScanValidationService.validateBulkService(bulkScanValidationRequest);
        return new ResponseEntity<BulkScanValidationResponse>(bulkScanResponse, HttpStatus.OK);
    }
}

