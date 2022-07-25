package uk.gov.hmcts.reform.bulkscan.group;

import lombok.Data;
import uk.gov.hmcts.reform.bulkscan.group.fields.Field;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormIndividualGroup {
    List<Field> compositeList = new ArrayList<>();

    public void add(Field field) {
        compositeList.add(field);
    }
}
