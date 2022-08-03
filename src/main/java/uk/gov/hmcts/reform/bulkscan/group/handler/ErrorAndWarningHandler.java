package uk.gov.hmcts.reform.bulkscan.group.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.MessageTypeEnum;

@Component
public class ErrorAndWarningHandler {
    @Getter @Setter private Map<MessageTypeEnum, List<String>> errorOrWarning = new HashMap<>();

    public void update(Map<MessageTypeEnum, List<String>> errorOrWarningHashMap) {
        final List<String> newWarningMessageList = new ArrayList<>();
        final List<String> oldWarningMessageList = new ArrayList<>();
        final List<String> newErrorMessageList = new ArrayList<>();
        final List<String> oldErrorMessageList = new ArrayList<>();

        newWarningMessageList.addAll(errorOrWarningHashMap.get(MessageTypeEnum.WARNING));
        oldWarningMessageList.addAll(errorOrWarning.get(MessageTypeEnum.WARNING));
        oldWarningMessageList.addAll(newWarningMessageList);

        newErrorMessageList.addAll(errorOrWarningHashMap.get(MessageTypeEnum.ERROR));
        oldErrorMessageList.addAll(errorOrWarning.get(MessageTypeEnum.ERROR));
        oldErrorMessageList.addAll(newErrorMessageList);

        errorOrWarning.put(MessageTypeEnum.WARNING, oldWarningMessageList);
        errorOrWarning.put(MessageTypeEnum.ERROR, oldErrorMessageList);
    }

    public void updateError(String errorMessage) {
        List<String> errorList = errorOrWarning.get(MessageTypeEnum.ERROR);
        errorList.add(errorMessage);
        errorOrWarning.put(MessageTypeEnum.ERROR, errorList);
    }

    public void updateWarning(String warningMessage) {
        List<String> warningList = errorOrWarning.get(MessageTypeEnum.WARNING);
        warningList.add(warningMessage);
        errorOrWarning.put(MessageTypeEnum.WARNING, warningList);
    }

    public void updateErrorList(List<String> errorList) {
        List<String> oldErrorList = errorOrWarning.get(MessageTypeEnum.ERROR);
        oldErrorList.addAll(errorList);
        errorOrWarning.put(MessageTypeEnum.ERROR, oldErrorList);
    }

    public void updateWarningList(List<String> warningList) {
        List<String> oldWarningList = errorOrWarning.get(MessageTypeEnum.WARNING);
        oldWarningList.addAll(warningList);
        errorOrWarning.put(MessageTypeEnum.WARNING, oldWarningList);
    }
}
