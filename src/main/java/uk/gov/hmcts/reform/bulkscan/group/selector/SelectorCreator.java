package uk.gov.hmcts.reform.bulkscan.group.selector;

import uk.gov.hmcts.reform.bulkscan.group.validation.enums.ChildRelationEnum;

public class SelectorCreator {
    public Selector getSelector(ChildRelationEnum childRelationEnum) {
        if (ChildRelationEnum.ONE_CHILD_REQUIRED.equals(childRelationEnum)) {
            return new OneOfThemSelector();
        } else if (ChildRelationEnum.ALL_CHILD_REQUIRED.equals(childRelationEnum)) {
            return new AllOfThemSelector();
        }
        return null;
    }
}
