package uk.gov.hmcts.reform.bulkscan.endpoints;

import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.Objects;
import javax.validation.Valid;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import uk.gov.hmcts.reform.bulkscan.auth.AuthService;
import uk.gov.hmcts.reform.bulkscan.factory.BulkScanServiceFactory;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanValidationResponse;
import uk.gov.hmcts.reform.bulkscan.model.FormType;
import uk.gov.hmcts.reform.bulkscan.model.Status;
import uk.gov.hmcts.reform.bulkscan.services.postcode.PostcodeLookupService;
import uk.gov.hmcts.reform.bulkscan.utils.FileUtil;

@RestController
@RequestMapping(
        path = "/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/", description = "Standard  API")
public class BulkScanEndpoint {
    private static final Logger logger = getLogger(BulkScanEndpoint.class);
    public static final String SERVICEAUTHORIZATION = "serviceauthorization";
    public static final String CONTENT_TYPE = "content-type";

    @Autowired PostcodeLookupService postcodeLookupService;

    @Autowired AuthService authService;

    @PostMapping(value = "forms/{form-type}/validate-ocr")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = " ")
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Validation executed successfully"),
                @ApiResponse(code = 401, message = "Provided S2S token is missing or invalid"),
                @ApiResponse(code = 403, message = "S2S token is not authorized to use the service")
            })
    public ResponseEntity<?> validateOcrData(
            @RequestHeader(SERVICEAUTHORIZATION) String s2sToken,
            @RequestHeader(CONTENT_TYPE) String contentType,
            @PathVariable("form-type") String formType,
            @RequestBody final BulkScanValidationRequest bulkScanValidationRequest) {

        logger.info(
                "Request received to validate ocr data from service {}",
                FileUtil.objectToJson(bulkScanValidationRequest));

        if (formType == null || !EnumUtils.isValidEnum(FormType.class, formType)) {
            logger.error("Invalid form type {} received when validating bulk scan", formType);
            return ok().body(validateFormType(formType));
        }
        String serviceName = authService.authenticate(s2sToken);
        logger.info("Request received to validate ocr data from service {}", serviceName);

        authService.assertIsAllowedToHandleService(serviceName);

        FormType formTypeEnum = FormType.valueOf(formType);

        BulkScanValidationResponse bulkScanResponse =
                Objects.requireNonNull(BulkScanServiceFactory.getService(formTypeEnum))
                        .validate(bulkScanValidationRequest);

        logger.info("response object from service {}", FileUtil.objectToJson(bulkScanResponse));

        return ok(bulkScanResponse);
    }

    @PostMapping(value = "/transform-exception-record")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = " ")
    @ApiResponses(
            value = {
                @ApiResponse(
                        code = 200,
                        message =
                                "Transformation of exception record into case data has been"
                                        + " successful"),
                @ApiResponse(
                        code = 400,
                        message =
                                "Request failed due to malformed syntax (and only for that reason)."
                                    + " This response results in a general error presented to the"
                                    + " caseworker in CCD."),
                @ApiResponse(code = 401, message = "Provided S2S token is missing or invalid"),
                @ApiResponse(
                        code = 403,
                        message = "Calling service is not authorised to use the endpoint"),
                @ApiResponse(
                        code = 404,
                        message =
                                "Exception record is well-formed, but the data it contains is"
                                    + " invalid and case can't be created. Messages from the body"
                                    + " will be shown to the caseworker.")
            })
    public ResponseEntity<BulkScanTransformationResponse> transformationOcrData(
            @RequestHeader(SERVICEAUTHORIZATION) String s2sToken,
            @RequestHeader(CONTENT_TYPE) String contentType,
            @RequestBody final BulkScanTransformationRequest bulkScanTransformationRequest) {

        logger.info(
                "Request received to transformationOcrData ocr data from service {}",
                FileUtil.objectToJson(bulkScanTransformationRequest));

        String serviceName = authService.authenticate(s2sToken);
        logger.info(
                "Request received to transformationOcrData ocr data from service {}", serviceName);

        authService.assertIsAllowedToHandleService(serviceName);

        BulkScanTransformationResponse bulkScanTransformationResponse =
                Objects.requireNonNull(
                                BulkScanServiceFactory.getService(
                                        FormType.valueOf(
                                                bulkScanTransformationRequest.getFormType())))
                        .transform(bulkScanTransformationRequest);
        logger.info(
                "response received to transformationOcrData ocr data from service {}",
                FileUtil.objectToJson(bulkScanTransformationResponse));
        return new ResponseEntity<>(bulkScanTransformationResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/transform-scanned-data")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = " ")
    @ApiResponses(
            value = {
                @ApiResponse(
                        code = 200,
                        message =
                                "Transformation of exception record into case data has been"
                                        + " successful"),
                @ApiResponse(
                        code = 400,
                        message =
                                "Request failed due to malformed syntax (and only for that reason)."
                                    + " This response results in a general error presented to the"
                                    + " caseworker in CCD."),
                @ApiResponse(code = 401, message = "Provided S2S token is missing or invalid"),
                @ApiResponse(
                        code = 403,
                        message = "Calling service is not authorised to use the endpoint"),
                @ApiResponse(
                        code = 404,
                        message =
                                "Exception record is well-formed, but the data it contains is"
                                    + " invalid and case can't be created. Messages from the body"
                                    + " will be shown to the caseworker.")
            })
    public ResponseEntity<BulkScanTransformationResponse> transformScannedData(
            @RequestHeader(SERVICEAUTHORIZATION) String s2sToken,
            @RequestHeader(CONTENT_TYPE) String contentType,
            @Valid @RequestBody Object dataMap) {

        logger.info(
                "Request received to transformScannedData ocr data from service new {}", dataMap);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        BulkScanTransformationRequest bulkScanTransformationRequest =
                objectMapper.convertValue(dataMap, BulkScanTransformationRequest.class);

        String serviceName = authService.authenticate(s2sToken);
        logger.info(
                "Request received to transformScannedData ocr data from service {}", serviceName);

        authService.assertIsAllowedToHandleService(serviceName);

        BulkScanTransformationResponse bulkScanTransformationResponse =
                Objects.requireNonNull(
                                BulkScanServiceFactory.getService(
                                        FormType.valueOf(
                                                bulkScanTransformationRequest.getFormType())))
                        .transform(bulkScanTransformationRequest);
        logger.info(
                "response received to transformationOcrData ocr data from service {}",
                FileUtil.objectToJson(bulkScanTransformationResponse));
        return new ResponseEntity<>(bulkScanTransformationResponse, HttpStatus.OK);
    }

    private BulkScanValidationResponse validateFormType(String formType) {
        return BulkScanValidationResponse.builder()
                .status(Status.ERRORS)
                .warnings(new ArrayList<>())
                .errors(
                        singletonList(
                                "Form type '"
                                        + (null != formType ? formType : "No Form Type")
                                        + "' not found"))
                .build();
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<String> handleHttpBadRequestException(
            HttpClientErrorException.BadRequest exception) {
        logger.info(
                "Http Bad request exception handler handling the exception {}",
                exception.getMessage());
        logger.error(exception.getMessage(), exception);
        String errors =
                "Http Bad request exception handler handling the exception "
                        + ExceptionUtils.getStackTrace(exception);
        return status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    protected ResponseEntity<String> handleFeignBadRequestException(
            FeignException.BadRequest exception) {
        logger.info(
                "Feign Bad request exception handler handling the exception {}",
                exception.getMessage());
        logger.error(exception.getMessage(), exception);
        String errors =
                "Feign Bad request exception handler handling the exception "
                        + ExceptionUtils.getStackTrace(exception);
        return status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleDefaultException(Exception exception) {
        logger.info("Default exception handler handling the exception {}", exception.getMessage());
        logger.error(exception.getMessage(), exception);
        String errors =
                "Default exception handler handling the exception "
                        + ExceptionUtils.getStackTrace(exception);
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}
