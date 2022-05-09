package uk.gov.hmcts.reform.bulkscan.transformation;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.model.CaseData;
import uk.gov.hmcts.reform.bulkscan.model.Child;
import uk.gov.hmcts.reform.bulkscan.model.Element;
import uk.gov.hmcts.reform.bulkscan.model.PartyDetails;
import uk.gov.hmcts.reform.bulkscan.model.OcrData;

import java.util.ArrayList;
import java.util.List;

@Component
public class BulkScanOcrDataToCaseDataTransformation {

    public CaseData transform(OcrData ocrData) {
        return CaseData.builder()
                .id(ocrData.getId())
                .caseTypeOfApplication(ocrData.getCaseTypeOfApplication())
                .applicantCaseName(ocrData.getApplicantCaseName())
                .children(transformChild(ocrData))
                .build();
    }

    private List<Element<PartyDetails>> transformPartyDetails(OcrData ocrData) {
        List<Element<PartyDetails>> partyDetailsArrayList = new ArrayList<>();
        List<PartyDetails> partyDetailsList = ocrData.getApplicants();
        for(PartyDetails partyDetails: partyDetailsList) {
            Element<PartyDetails> partyDetailsElement = new Element<>(null, partyDetails);
            partyDetailsArrayList.add(partyDetailsElement);
        }
        return partyDetailsArrayList;
    }

    private List<Element<Child>> transformChild(OcrData ocrData) {
        List<Element<Child>> childArrList = new ArrayList<>();
        List<Child> childList = ocrData.getChildren();
        for(Child child: childList) {
            Element<Child> childElement = new Element<>(null, child);
            childArrList.add(childElement);
        }
        return childArrList;
    }
}
