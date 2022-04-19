package uk.gov.hmcts.reform.bulkscan.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanC100Service;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanFL401Service;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanFL403Service;
import uk.gov.hmcts.reform.bulkscan.services.BulkScanService;

import java.util.Objects;


@RestController
@RequestMapping(path = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/", description = "Standard  API")
public class BulkScanEndpoint {

    public static final String SERVICEAUTHORIZATION = "serviceauthorization";
    public static final String CONTENT_TYPE = "content-type";

    @Autowired
    BulkScanC100Service bulkScanC100Service;

    @Autowired
    BulkScanFL401Service bulkScanFL401Service;

    @Autowired
    BulkScanFL403Service bulkScanFL403Service;

    @PostMapping(value = "/forms/{form-type}/validate-ocr")
    @ResponseStatus(HttpStatus.OK)
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
        validateOcrData(@RequestHeader(SERVICEAUTHORIZATION) String s2sToken,
                        @RequestHeader(CONTENT_TYPE) String contentType,
                        @PathVariable("form-type") FormType formType,
                        @RequestBody final BulkScanValidationRequest bulkScanValidationRequest) {

        BulkScanValidationResponse bulkScanResponse =
                Objects.requireNonNull(bulkScanServiceType(formType)).validate(bulkScanValidationRequest);
        return new ResponseEntity<>(bulkScanResponse, HttpStatus.OK);
    }

    @PostMapping (value = "/transform-exception-record")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(
        value = "",
        notes = " "
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Transformation of exception record into case data has been successful"),
        @ApiResponse(code = 400, message =
            "Request failed due to malformed syntax (and only for that reason). "
                + "This response results in a general error presented to the caseworker in CCD."),
        @ApiResponse(code = 401, message = "Provided S2S token is missing or invalid"),
        @ApiResponse(code = 403, message = "Calling service is not authorised to use the endpoint"),
        @ApiResponse(code = 404, message =
            "Exception record is well-formed, but the data it contains is invalid and case can't be created. "
                + "Messages from the body will be shown to the caseworker.")

    })
    public ResponseEntity<BulkScanTransformationResponse>
        transformationOcrData(@RequestHeader(SERVICEAUTHORIZATION)
                            String s2sToken, @RequestHeader(CONTENT_TYPE) String contentType,
                        @RequestBody final BulkScanTransformationRequest bulkScanTransformationRequest) {

        BulkScanTransformationResponse bulkScanTransformationResponse =
                Objects.requireNonNull(bulkScanServiceType(FormType.valueOf(bulkScanTransformationRequest
                                .getCaseTypeId()))).transform(bulkScanTransformationRequest);
        return new ResponseEntity<>(bulkScanTransformationResponse, HttpStatus.OK);
    }

    private BulkScanService bulkScanServiceType(FormType formType) {
        switch (formType) {
            case C100:
                return bulkScanC100Service;
            case FL401:
                return bulkScanFL401Service;
            case FL403:
                return bulkScanFL403Service;
            default:
                return null;
        }
    }
}
