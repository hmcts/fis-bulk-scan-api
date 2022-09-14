package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.VALUE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.BAIL_CONDITION_END_DATE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.COMMA;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DELIVERATELY_EVADING_SERVICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.DETERREDOR_PREVENTED;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.REASON_FOR_ORDER_WITHOUT_GIVING_NOTICE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.RISK_OF_SIGNIFICANT_HARM;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TEXT_AND_NUMERIC_MONTH_PATTERN;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.TWO_DIGIT_MONTH_FORMAT;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanFl401Constants.WITHOUT_NOTICE_ORDER_TABLE;
import static uk.gov.hmcts.reform.bulkscan.enums.OrderWithouGivingNoticeReasonEnum.RISKOF_SIGNIFICANT_HARM;

import com.microsoft.applicationinsights.core.dependencies.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.enums.OrderWithouGivingNoticeReasonEnum;
import uk.gov.hmcts.reform.bulkscan.utils.DateUtil;

@SuppressWarnings({"PMD", "unchecked"})
@Component
public class BulkScanFL401ConditionalTransformerService {

    public void transform(Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {

        LinkedTreeMap withoutNoticeOrderTableMap =
                (LinkedTreeMap) populatedMap.get(WITHOUT_NOTICE_ORDER_TABLE);
        final String bailConditionEndDate =
                (String) withoutNoticeOrderTableMap.get(BAIL_CONDITION_END_DATE);

        withoutNoticeOrderTableMap.put(
                BAIL_CONDITION_END_DATE,
                DateUtil.transformDate(
                        bailConditionEndDate,
                        TEXT_AND_NUMERIC_MONTH_PATTERN,
                        TWO_DIGIT_MONTH_FORMAT));

        withoutNoticeOrderTableMap.put(
                REASON_FOR_ORDER_WITHOUT_GIVING_NOTICE,
                transformReasonForOrderWithoutGivingNotice(inputFieldsMap));

        populatedMap.put("children", buildTransformChild(populatedMap, inputFieldsMap));
    }

    private List buildTransformChild(
            Map<String, Object> populatedMap, Map<String, String> inputFieldsMap) {
        ArrayList<LinkedTreeMap> children = new ArrayList<>();

        final String row1 = inputFieldsMap.get("childLivesAtAddress_Row1");
        final String row2 = inputFieldsMap.get("childLivesAtAddress_Row2");
        final String row3 = inputFieldsMap.get("childLivesAtAddress_Row3");
        final String row4 = inputFieldsMap.get("childLivesAtAddress_Row4");
        final String otherChildrenRow1 = inputFieldsMap.get("otherChildren_Row1");
        final String otherChildrenRow2 = inputFieldsMap.get("otherChildren_Row2");
        final String otherChildrenRow3 = inputFieldsMap.get("otherChildren_Row3");
        final String otherChildrenRow4 = inputFieldsMap.get("otherChildren_Row4");

        if (row1 != null) {
            populatedMap.put("doAnyChildrenLiveAtAddress", BooleanUtils.YES);
        }
        ArrayList<String> childInput = new ArrayList<>();
        childInput.add(row1);
        childInput.add(row2);
        childInput.add(row3);
        childInput.add(row4);
        childInput.add(otherChildrenRow1);
        childInput.add(otherChildrenRow2);
        childInput.add(otherChildrenRow3);
        childInput.add(otherChildrenRow4);

        for (String input : childInput) {
            LinkedTreeMap<String, String> childDetails = new LinkedTreeMap<>();

            final LinkedTreeMap<String, LinkedTreeMap<String, String>> childrenLinkedTreeMap =
                    new LinkedTreeMap();

            if (null != input) {
                final String[] columnDetails = input.split(",");

                final String childName = columnDetails[0];

                String childAge = null;
                String isRespondentResponsibleForChild = null;
                if (columnDetails.length > 2) {
                    childAge = columnDetails[1];
                    isRespondentResponsibleForChild = columnDetails[2];
                }

                childDetails.put("childFullName", childName);
                childDetails.put("childsAge", childAge);
                childDetails.put(
                        "isRespondentResponsibleForChild", isRespondentResponsibleForChild);

                childrenLinkedTreeMap.put(VALUE, childDetails);

                children.add(childrenLinkedTreeMap);
            }
        }
        return children;
    }

    private String transformReasonForOrderWithoutGivingNotice(Map<String, String> inputFieldsMap) {

        StringBuilder orderWithoutGivingNoticeReason = new StringBuilder();

        if (YES.equalsIgnoreCase(inputFieldsMap.get(RISK_OF_SIGNIFICANT_HARM))) {
            orderWithoutGivingNoticeReason.append(RISKOF_SIGNIFICANT_HARM.getDescription());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(DETERREDOR_PREVENTED))) {

            orderWithoutGivingNoticeReason =
                    orderWithoutGivingNoticeReason.length() != 0
                            ? orderWithoutGivingNoticeReason
                                    .append(COMMA)
                                    .append(
                                            OrderWithouGivingNoticeReasonEnum.DETERRED_OR_PREVENTED
                                                    .getDescription())
                            : orderWithoutGivingNoticeReason.append(
                                    OrderWithouGivingNoticeReasonEnum.DETERRED_OR_PREVENTED
                                            .getDescription());
        }
        if (YES.equalsIgnoreCase(inputFieldsMap.get(DELIVERATELY_EVADING_SERVICE))) {

            orderWithoutGivingNoticeReason =
                    orderWithoutGivingNoticeReason.length() != 0
                            ? orderWithoutGivingNoticeReason
                                    .append(COMMA)
                                    .append(
                                            OrderWithouGivingNoticeReasonEnum
                                                    .DELIBERATELYEVADING_SERVICE
                                                    .getDescription())
                            : orderWithoutGivingNoticeReason.append(
                                    OrderWithouGivingNoticeReasonEnum.DELIBERATELYEVADING_SERVICE
                                            .getDescription());
        }

        return orderWithoutGivingNoticeReason.toString();
    }
}
