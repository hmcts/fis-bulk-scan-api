package uk.gov.hmcts.reform.bulkscan.model;

public enum BulkScanValidationMandatoryFields {
    APPLELLANT_FIRSTNAME("appellant_firstName", "Appellant first name"),
    APPLELLANT_LASTNAME("appellant_lastName", "Appellant Last name"),
    APPLELLANT_ADDRESS("appellant_address", "Appellant address"),
    APPLELLANT_CONTACTNUMNER("appellant_contactNumber", "Appellant Contact Number");

    private final String key;
    private final String value;

    BulkScanValidationMandatoryFields(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
