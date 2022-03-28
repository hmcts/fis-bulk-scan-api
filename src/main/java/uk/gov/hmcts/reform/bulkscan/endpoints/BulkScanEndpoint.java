package uk.gov.hmcts.reform.bulkscan.endpoints;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Status;

@RestController
@RequestMapping(path = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/", description = "Standard  API")
public class BulkScanEndpoint {

    public static final String SERVICEAUTHORIZATION = "serviceauthorization";
    public static final String CONTENT_TYPE = "content-type";

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
    public ResponseEntity<BulkScanValidationResponse> validateOcrData(@RequestHeader(SERVICEAUTHORIZATION) String s2sToken,
                                                  @RequestHeader(CONTENT_TYPE) String contentType,
                                                  @RequestBody final BulkScanValidationRequest bulkScanValidationRequest) {

  BulkScanValidationResponse bulkScanResponse=new BulkScanValidationResponse();
  bulkScanResponse.setStatus(Status.SUCCESS);
  bulkScanResponse.setWarnings("Warning");
  bulkScanResponse.setErrors("Errors");
  return new ResponseEntity<BulkScanValidationResponse>(bulkScanResponse, HttpStatus.OK);
    }
}
