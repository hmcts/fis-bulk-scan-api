package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MiamUrgencyReasonChecklistEnum {

    miamUrgencyReasonChecklistEnum_Value_1("There is risk to the life, liberty or physical safety of the "
                                               + "prospective applicant or his"
                                               + " or her family or his or her home; or"),
    miamUrgencyReasonChecklistEnum_Value_2("Any delay caused by MIAM would cause significant risk "
                                               + "of a miscarriage of justice"),
    miamUrgencyReasonChecklistEnum_Value_3("Any delay caused by MIAM would cause unreasonable hardship "
                                               + "to the prospective applicant"),
    miamUrgencyReasonChecklistEnum_Value_4("Any delay caused by MIAM would cause irretrievable problems "
                                               + "in dealing with the dispute "
                                               + "(including the irretrievable loss of significant evidence)"),
    miamUrgencyReasonChecklistEnum_Value_5("There  is a significant risk that in the period necessary "
                                               + "to schedule and attend a MIAM, "
                                               + "proceedings relating to the dispute will be brought "
                                               + "in another state in which a valid "
                                               + "claim to jurisdiction may exist, such that a court "
                                               + "in that other State would be seized of the "
                                               + "dispute before a court in England and Wales."),
    miamUrgencyReasonChecklistEnum_Value_6("There is a risk of unlawful removal "
                                               + "of a child from the United Kingdom,"
                                               + "or a risk of unlawful retention of a child "
                                               + "who is currently outside England and Wales"),
    miamUrgencyReasonChecklistEnum_Value_7("There is a risk of harm to a child");

    private final String displayedValue;

    public String getDisplayedValue() {
        return displayedValue;
    }

    public static MiamUrgencyReasonChecklistEnum getValue(String key) {
        return MiamUrgencyReasonChecklistEnum.valueOf(key);
    }


}
