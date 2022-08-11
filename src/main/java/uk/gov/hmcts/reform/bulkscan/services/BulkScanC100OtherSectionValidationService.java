package uk.gov.hmcts.reform.bulkscan.services;

import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.MANDATORY_ERROR_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.XOR_CONDITIONAL_FIELDS_MESSAGE;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.YES;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.getOtherProceedingFields;
import static uk.gov.hmcts.reform.bulkscan.constants.BulkScanConstants.getTypeOfOrderEnumFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormValidationConfigManager;

/** This class will custom validation methods related to. C100 form custom validation */
@SuppressWarnings("PMD")
@Service
public class BulkScanC100OtherSectionValidationService {

    public List<String> validateOtherProceedingFields(
            Map<String, String> inputFieldMap,
            BulkScanFormValidationConfigManager.ValidationConfig validationConfig) {
        List<String> errors = new ArrayList<>();
        if (YES.equalsIgnoreCase(
                inputFieldMap.get("existingCase_onEmergencyProtection_Care_or_supervisioNorder"))) {
            List<String> mandatoryListFields =
                    getOtherProceedingFields().stream()
                            .filter(
                                    eachField ->
                                            org.apache.commons.lang3.StringUtils.isEmpty(
                                                    inputFieldMap.get(eachField)))
                            .collect(Collectors.toUnmodifiableList());
            if (mandatoryListFields.size() > 0) {
                errors.add(
                        String.format(
                                MANDATORY_ERROR_MESSAGE,
                                org.apache.commons.lang3.StringUtils.join(
                                        mandatoryListFields, ",")));
            }

            boolean typeOfOrderSelected =
                    getTypeOfOrderEnumFields().stream()
                            .anyMatch(
                                    eachField ->
                                            !org.apache.commons.lang3.StringUtils.isEmpty(
                                                    inputFieldMap.get(eachField)));
            if (!typeOfOrderSelected) {
                errors.add(
                        String.format(
                                XOR_CONDITIONAL_FIELDS_MESSAGE,
                                org.apache.commons.lang3.StringUtils.join(
                                        getTypeOfOrderEnumFields(), ",")));
            }
        }

        return errors;
    }
}