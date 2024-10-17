package uk.gov.hmcts.reform.bulkscan.utils;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.BULK_SCAN_CASE_REFERENCE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.CASE_TYPE_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.EVENT_ID;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.SCAN_DOCUMENTS;
import static uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper.transformScanDocuments;

import java.util.HashMap;
import java.util.Map;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanTransformConfigManager;
import uk.gov.hmcts.reform.bulkscan.helper.BulkScanTransformHelper;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationRequest;
import uk.gov.hmcts.reform.bulkscan.model.BulkScanTransformationResponse;
import uk.gov.hmcts.reform.bulkscan.model.CaseCreationDetails;

public final class ServiceUtil {

    private ServiceUtil() {}

    @SuppressWarnings("unchecked")
    public static BulkScanTransformationResponse.BulkScanTransformationResponseBuilder
            getBulkScanTransformationResponseBuilder(
                    BulkScanTransformationRequest bulkScanTransformationRequest,
                    Map<String, String> inputFieldsMap,
                    BulkScanTransformConfigManager.TransformationConfig transformationConfig,
                    Map<String, Object> caseData) {
        caseData.put(BULK_SCAN_CASE_REFERENCE, bulkScanTransformationRequest.getId());

        Map<String, Object> populatedMap =
                (Map)
                        BulkScanTransformHelper.transformToCaseData(
                                new HashMap<>(transformationConfig.getCaseDataFields()),
                                inputFieldsMap);

        populatedMap.put(SCAN_DOCUMENTS, transformScanDocuments(bulkScanTransformationRequest));

        Map<String, String> caseTypeAndEventId = transformationConfig.getCaseFields();

        return BulkScanTransformationResponse.builder()
                        .caseCreationDetails(
                            CaseCreationDetails.builder()
                                    .caseTypeId(caseTypeAndEventId.get(CASE_TYPE_ID))
                                    .eventId(caseTypeAndEventId.get(EVENT_ID))
                                    .caseData(populatedMap)
                                    .build());
    }

}
