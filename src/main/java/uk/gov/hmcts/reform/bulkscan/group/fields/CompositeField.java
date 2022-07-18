package uk.gov.hmcts.reform.bulkscan.group.fields;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.FormatValidatorCreator;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.ChildRelationEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldRequiredTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.format.FormatValidator;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.MISSING_FIELD_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.group.constants.BulkScanGroupConstants.VALID_MESSAGE;

@Setter
@Getter
@SuperBuilder
public class CompositeField extends Field {
    private final List<Field> fieldList = new ArrayList<>();
    private final FieldTypeEnum fieldType;
    private final ChildRelationEnum childRelation;

    public void add(Field field) {
        fieldList.add(field);
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList.clear();
        this.fieldList.addAll(fieldList);
    }

    @Override
    public void validate(List<OcrDataField> ocrDataFieldList, ErrorAndWarningHandler errorAndWarningHandler) {
        List<String> errorList = new ArrayList<>();

        FormatValidatorCreator formatValidatorCreator = new FormatValidatorCreator();
        Optional<OcrDataField> ocrDataFieldOptional = Optional.ofNullable(this.getOcrDataFieldByName(ocrDataFieldList));

        if (FieldRequiredTypeEnum.MANDATORY.equals(this.getFieldRequiredType())) {
            if (!ocrDataFieldOptional.isPresent()) {
                errorList.add(String.format(MISSING_FIELD_MESSAGE, this.getName()));
            } else if (ObjectUtils.isEmpty(ocrDataFieldOptional.get().getValue())) {
                errorList.add(String.format(MANDATORY_ERROR_MESSAGE, this.getName()));
            } else {
                FormatValidator formatValidator = formatValidatorCreator.getValidator(this.getFieldType());
                String error = formatValidator.validateFormat(this.getName(), ocrDataFieldOptional.get().getValue());
                if (!VALID_MESSAGE.contains(error)) {
                    errorList.add(error);
                }
            }
        } else if (FieldRequiredTypeEnum.OPTIONAL.equals(this.getFieldRequiredType())
            && ocrDataFieldOptional.isPresent()
            && !ObjectUtils.isEmpty(ocrDataFieldOptional.get().getValue())) {
            FormatValidator formatValidator = formatValidatorCreator.getValidator(this.getFieldType());
            String error = formatValidator.validateFormat(this.getName(), ocrDataFieldOptional.get().getValue());
            if (!VALID_MESSAGE.contains(error)) {
                errorList.add(error);
            }
        }
        errorAndWarningHandler.updateErrorList(errorList);
    }
}
