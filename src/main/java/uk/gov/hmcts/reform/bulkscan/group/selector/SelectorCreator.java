package uk.gov.hmcts.reform.bulkscan.group.selector;

import uk.gov.hmcts.reform.bulkscan.group.validation.enums.SelectorEnum;

public class SelectorCreator {
    public Selector getSelector(SelectorEnum selectorEnum) {
        if (SelectorEnum.ONE_CHILD_REQUIRED.equals(selectorEnum)) {
            return new OneOfThemSelector();
        } else if (SelectorEnum.ALL_CHILD_REQUIRED.equals(selectorEnum)) {
            return new AllOfThemSelector();
        }
        return null;
    }
}
