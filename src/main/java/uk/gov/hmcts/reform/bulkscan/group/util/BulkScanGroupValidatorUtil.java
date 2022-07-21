package uk.gov.hmcts.reform.bulkscan.group.util;

import uk.gov.hmcts.reform.bulkscan.group.FormIndividualGroup;
import uk.gov.hmcts.reform.bulkscan.group.creation.Group;
import uk.gov.hmcts.reform.bulkscan.group.fields.CompositeField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BulkScanGroupValidatorUtil {
    private BulkScanGroupValidatorUtil() {

    }

    public static List<String> getAllConfiguredGroupFields(Group group) {
        List<String> fieldNameList = new ArrayList<>();
        if (Optional.ofNullable(group).isPresent()) {
            FormIndividualGroup formIndividualGroup = group.create();
            formIndividualGroup.getCompositeList().stream()
                .forEach(field -> {
                    if (field instanceof CompositeField) {
                        CompositeField compositeField = (CompositeField) field;
                        fieldNameList.add(compositeField.getName());
                        traverseFieldTree(compositeField, fieldNameList);
                    } else {
                        fieldNameList.add(field.getName());
                    }
                });
        }
        return fieldNameList;
    }

    private static void traverseFieldTree(CompositeField compositeField, List<String> fieldNameList) {
        compositeField.getFieldList().stream()
            .forEach(field -> {
                if (field instanceof CompositeField) {
                    CompositeField composite = (CompositeField) field;
                    fieldNameList.add(composite.getName());
                    traverseFieldTree(composite, fieldNameList);
                } else {
                    fieldNameList.add(field.getName());
                }
            });
    }
}
