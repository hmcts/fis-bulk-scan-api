package uk.gov.hmcts.reform.bulkscan.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.EnumUtils;
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
import uk.gov.hmcts.reform.bulkscan.factory.BulkScanServiceFactory;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.Errors;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.model.Warnings;

import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/", description = "Standard  API")
public class BulkScanEndpoint {

    public static final String SERVICEAUTHORIZATION = "serviceauthorization";
    public static final String CONTENT_TYPE = "content-type";

    @PostMapping(value = "forms/{form-type}/validate-ocr")
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
    public ResponseEntity<?>
        validateOcrData(@RequestHeader(SERVICEAUTHORIZATION) String s2sToken,
                        @RequestHeader(CONTENT_TYPE) String contentType,
                        @PathVariable("form-type") FormType caseType,
                        @RequestBody final BulkScanValidationRequest bulkScanValidationRequest) {

        if (caseType == null || !EnumUtils.isValidEnum(FormType.class, caseType.name())) {
            return ok().body(BulkScanValidationResponse.builder().status(Status.ERRORS)
                                 .warnings(Warnings.builder().items(emptyList()).build())
                                 .errors(Errors.builder()
                                             .items(singletonList("Form type '" + caseType.name() + "' not found"))
                                             .build())
                                 .build());
        }

        BulkScanValidationResponse bulkScanResponse =
                Objects.requireNonNull(BulkScanServiceFactory.getService(caseType)).validate(bulkScanValidationRequest);
        return ok(bulkScanResponse);
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
            Objects.requireNonNull(BulkScanServiceFactory.getService(FormType.valueOf(bulkScanTransformationRequest
                                                                            .getCaseTypeId())))
                .transform(bulkScanTransformationRequest);
        return new ResponseEntity<>(bulkScanTransformationResponse, HttpStatus.OK);
    }
}
