package uk.gov.hmcts.reform.bulkscan.helper;

public class ValidationConfigFactory {

    public ValidationConfig getValidationConfig(String formType) {
        if ("C100".equals(formType)) {
            return new C100ValidationConfig();
        } else if ("FL401A".equals(formType)) {
            return new FL401AValidationConfig();
        } else {
            return null;
        }
    }
}
