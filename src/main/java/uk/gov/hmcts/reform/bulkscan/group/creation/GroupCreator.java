package uk.gov.hmcts.reform.bulkscan.group.creation;

import uk.gov.hmcts.reform.bulkscan.model.FormType;

public class GroupCreator {

    public Group getGroup(FormType formType) {
        if (FormType.A58.equals(formType)) {
            return new A58Group();
        }
        return null;
    }
}
