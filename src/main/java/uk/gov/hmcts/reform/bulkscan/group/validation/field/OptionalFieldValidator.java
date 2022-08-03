package uk.gov.hmcts.reform.bulkscan.group.validation.field;

import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.FormatValidatorCreator;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.FormatValidator;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

public class OptionalFieldValidator implements FieldValidator {
    @Override
    public void validate(
            String fieldName,
            FieldTypeEnum fieldType,
            Optional<OcrDataField> ocrDataFieldOptional,
            ErrorAndWarningHandler errorAndWarningHandler) {
        List<String> errorList = new ArrayList<>();
        if (ocrDataFieldOptional.isPresent()
                && !ObjectUtils.isEmpty(ocrDataFieldOptional.get().getValue())) {
            FormatValidatorCreator formatValidatorCreator = new FormatValidatorCreator();
            FormatValidator formatValidator = formatValidatorCreator.getValidator(fieldType);
            String error =
                    formatValidator.validateFormat(
                            fieldName, ocrDataFieldOptional.get().getValue());
            if (!VALID_MESSAGE.contains(error)) {
                errorList.add(error);
            }
        }
        errorAndWarningHandler.updateErrorList(errorList);
    }
}
