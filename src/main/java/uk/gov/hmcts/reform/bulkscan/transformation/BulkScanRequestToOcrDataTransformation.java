package uk.gov.hmcts.reform.bulkscan.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.bulkscan.config.BulkScanFormDataTransformationConfigManager;
import uk.gov.hmcts.reform.bulkscan.model.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class BulkScanRequestToOcrDataTransformation {

    @Autowired
    BulkScanFormDataTransformationConfigManager bulkScanFormDataTransformationConfigManager;

    @Autowired
    BulkScanOcrDataToCaseDataTransformation bulkScanOcrDataToCaseDataTransformation;

    public BulkScanTransformationResponse transform(BulkScanTransformationRequest bulkScanTransformationRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OcrData ocrData = OcrData.builder().build();
        List<OcrDataField> ocrdatafields = bulkScanTransformationRequest.getOcrdatafields();

        // Set All primitives OcrData
        setPrimitives(ocrData, ocrdatafields);

        // Set child objects
        List<Child> childList = createChild(ocrdatafields);
        ocrData.setChildren(childList);

        CaseData caseData = bulkScanOcrDataToCaseDataTransformation.transform(ocrData);

        BulkScanTransformationResponse bulkScanTransformationResponse = getBulkScanTransformationResponse(caseData);

        return bulkScanTransformationResponse;
    }

    private BulkScanTransformationResponse getBulkScanTransformationResponse(CaseData caseData) {
        BulkScanTransformationResponse bulkScanTransformationResponse = new BulkScanTransformationResponse();
        CaseCreationDetails caseCreationDetails = new CaseCreationDetails();
        caseCreationDetails.caseData = caseData;
        bulkScanTransformationResponse.setCaseCreationDetails(caseCreationDetails);
        return bulkScanTransformationResponse;
    }

    private void setPrimitives(OcrData ocrData, List<OcrDataField> ocrdatafields) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> readOthersConfig = getTransformationConfig().getOthers();

        readOthersConfig.stream()
            .forEach(config ->
                     {
                         Method[] declaredMethods = ocrData.getClass().getDeclaredMethods();
                         OcrDataField ocrDataFieldByName = getOcrDataFieldByName(config, ocrdatafields);
                         Arrays.stream(declaredMethods)
                             .forEach(method ->
                                      {
                                          if(!Objects.isNull(ocrDataFieldByName) && method.getName().equalsIgnoreCase("set" + ocrDataFieldByName.getName())) {
                                              String name = method.getName();
                                              mapRequestFieldToOcrDataField(ocrData.getClass().getName(), name, ocrDataFieldByName.getValue());
                                          }
                                      });
                     });

    }

    private void mapRequestFieldToOcrDataField(String className, String setterMethodName, String setterMethodValue) {
        try {
            Class<?> clazz = Class.forName(className);
            Method setMethod = clazz.getDeclaredMethod(setterMethodName, String.class);
            setMethod.invoke(clazz.getConstructor().newInstance(), setterMethodValue);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private OcrDataField getOcrDataFieldByName(String config, List<OcrDataField> ocrdatafields) {
        for(OcrDataField ocrDataField: ocrdatafields) {
            if(ocrDataField.getName().equals(config))
                return ocrDataField;
        }
        return null;
    }

    private BulkScanFormDataTransformationConfigManager.TransformationConfig getTransformationConfig() {
        BulkScanFormDataTransformationConfigManager.TransformationConfig transformationConfig = bulkScanFormDataTransformationConfigManager.getTransformationConfig(
            FormType.C100);
        return transformationConfig;
    }

    private List<Child> createChild(List<OcrDataField> ocrDataFieldList) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<Child> childList = new ArrayList<>();
        List<String> childConfig = getTransformationConfig().getChild();
        List<Clazz> numberOfChild = getNumberOfChild(ocrDataFieldList, childConfig);

        numberOfChild.stream()
            .forEach(clazz ->
                     {
                         List<Field> fieldList = clazz.getFieldList();
                         Child child = Child.builder().build();
                         for(Field field: fieldList) {
                             String name = field.getName();
                             String value = field.getValue();
                             Method[] declaredMethods = child.getClass().getDeclaredMethods();
                             Arrays.stream(declaredMethods)
                                 .forEach(method ->
                                          {
                                              if(name != null && method.getName().equalsIgnoreCase("set" + name)) {
                                                  String methodName = method.getName();
                                                  mapRequestFieldToOcrDataField(child.getClass().getName(), methodName, value);
                                                  try {
                                                      Class<?> c = Class.forName("uk.gov.hmcts.reform.bulkscan.model.Child");
                                                      Method setMethod = c.getDeclaredMethod(methodName, String.class);
                                                      setMethod.invoke(child, value);
                                                  } catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {

                                                  }

                                              }
                                          });
                         }
                         childList.add(child);
                     });
        return childList;
    }

    private List<Clazz> getNumberOfChild(List<OcrDataField> ocrDataFieldList, List<String> childConfig) {
        List<Clazz> clazzList = new ArrayList<>();
        for(OcrDataField ocrDataField: ocrDataFieldList) {
            if(childConfig.contains(ocrDataField.getName())) {
                String[] fieldName = ocrDataField.getName().split("_");

                Field field = new Field(fieldName[1], ocrDataField.getValue());

                Optional<Clazz> clazzOptional = clazzList.stream().filter(clazz -> clazz.getName().equals(fieldName[0])).findAny();

                if(clazzOptional.isPresent()) {
                    clazzOptional.get().getFieldList().add(field);
                } else {
                    Clazz clazz = new Clazz();
                    clazz.setName(fieldName[0]);
                    List<Field> fieldList = new ArrayList<>();
                    fieldList.add(field);
                    clazz.setFieldList(fieldList);
                    clazzList.add(clazz);
                }
            }
        }
        return clazzList;
    }
}
