package uk.gov.hmcts.reform.bulkscan.group.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uk.gov.hmcts.reform.bulkscan.group.handler.ErrorAndWarningHandler;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldRequiredTypeEnum;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.List;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Field {
    private String name;
    private FieldRequiredTypeEnum fieldRequiredType;

    public abstract void validate(List<OcrDataField> ocrDataFieldList, ErrorAndWarningHandler errorAndWarningHandler);

    OcrDataField getOcrDataFieldByName(List<OcrDataField> ocrDataFieldList) {
        return ocrDataFieldList.stream()
            .filter(ocrDataField -> ocrDataField.getName().equals(this.getName()))
            .findAny()
            .orElse(null);
    }
}
