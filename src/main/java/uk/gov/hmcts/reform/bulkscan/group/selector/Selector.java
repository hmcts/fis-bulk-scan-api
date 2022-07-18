package uk.gov.hmcts.reform.bulkscan.group.selector;

import uk.gov.hmcts.reform.bulkscan.group.fields.Field;
import uk.gov.hmcts.reform.bulkscan.model.OcrDataField;

import java.util.List;

public interface Selector {
    List<Field> apply(List<Field> fieldList, List<OcrDataField> ocrDataFields);
}
