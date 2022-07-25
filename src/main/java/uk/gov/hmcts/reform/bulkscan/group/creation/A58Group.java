package uk.gov.hmcts.reform.bulkscan.group.creation;

import uk.gov.hmcts.reform.bulkscan.group.FormIndividualGroup;
import uk.gov.hmcts.reform.bulkscan.group.fields.CheckboxField;
import uk.gov.hmcts.reform.bulkscan.group.fields.CompositeField;
import uk.gov.hmcts.reform.bulkscan.group.fields.DateField;
import uk.gov.hmcts.reform.bulkscan.group.fields.EmailField;
import uk.gov.hmcts.reform.bulkscan.group.fields.PhoneNumberField;
import uk.gov.hmcts.reform.bulkscan.group.fields.TextField;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldRequiredTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.FieldTypeEnum;
import uk.gov.hmcts.reform.bulkscan.group.validation.enums.SelectorEnum;

public class A58Group implements Group {

    @Override
    public FormIndividualGroup create() {

        final FormIndividualGroup formIndividualGroup = new FormIndividualGroup();
        final CheckboxField childGender = getChildGender();
        final CompositeField compositeFieldGroupChildDateOfBirth = getCompositeFieldGroupChildDateOfBirth();
        final TextField childNationality = getChildNationality();
        final CheckboxField childIsMarried = getChildIsMarried();
        final DateField childLiveAtHomeFrom = getChildLiveAtHomeFrom();
        final TextField childIsAdoptedByAgency = getChildIsAdoptedByAgency();
        final DateField childPlacedAdoptionDate = getChildPlacedAdoptionDate();
        final TextField childPlacedAdoptionAgencyName = getChildPlacedAdoptionAgencyName();
        final TextField childPlacedAdoptionAgencyAddress = getChildPlacedAdoptionAgencyAddress();
        final TextField childPlacedAdoptionAgencyContactName = getChildPlacedAdoptionAgencyContactName();
        final PhoneNumberField childPlacedAdoptionAgencyTelephoneNo = getChildPlacedAdoptionAgencyTelephoneNo();
        final CompositeField compositeFieldGroupchildAdoptionAgencyDetailsAvailable =
                getCompositeFieldGroupchildAdoptionAgencyDetailsAvailable();
        final TextField childLaName = getChildLaName();
        final TextField childLaAddress = getChildLaAddress();
        final DateField childLaNotifiedDate = getChildLaNotifiedDate();
        final TextField childLaContactName = getChildLaContactName();
        final PhoneNumberField childLaTelephoneNo = getChildLaTelephoneNo();
        final EmailField childLlaEmail = getChildLlaEmail();
        final CompositeField compositeFieldGroupChildCourt = getCompositeFieldGroupChildCourt();
        final CompositeField compositeFieldGroupchildLaOrParentalResponsibilityDetails =
                getCompositeFieldGroupchildLaOrParentalResponsibilityDetails();
        final CompositeField compositeFieldGroupChildMaintanenceOrder =
                getCompositeFieldGroupChildMaintanenceOrder();
        final CompositeField compositeFieldGroupChildProceedingDetails =
            getCompositeFieldGroupChildProceedingDetails();
        final CompositeField compositeFieldGroupChildProceedingDetailsWithRelation =
            getCompositeFieldGroupChildProceedingDetailsWithRelation();

        // Add groups
        formIndividualGroup.add(compositeFieldGroupChildProceedingDetailsWithRelation);
        formIndividualGroup.add(compositeFieldGroupChildProceedingDetails);
        formIndividualGroup.add(compositeFieldGroupChildMaintanenceOrder);
        formIndividualGroup.add(compositeFieldGroupchildLaOrParentalResponsibilityDetails);
        formIndividualGroup.add(compositeFieldGroupChildCourt);
        formIndividualGroup.add(compositeFieldGroupchildAdoptionAgencyDetailsAvailable);
        formIndividualGroup.add(childGender);
        formIndividualGroup.add(childNationality);
        formIndividualGroup.add(compositeFieldGroupChildDateOfBirth);
        formIndividualGroup.add(childIsMarried);
        formIndividualGroup.add(childLiveAtHomeFrom);
        formIndividualGroup.add(childIsAdoptedByAgency);
        formIndividualGroup.add(childPlacedAdoptionDate);
        formIndividualGroup.add(childPlacedAdoptionAgencyName);
        formIndividualGroup.add(childPlacedAdoptionAgencyAddress);
        formIndividualGroup.add(childPlacedAdoptionAgencyContactName);
        formIndividualGroup.add(childPlacedAdoptionAgencyTelephoneNo);
        formIndividualGroup.add(childLaName);
        formIndividualGroup.add(childLaAddress);
        formIndividualGroup.add(childLaNotifiedDate);
        formIndividualGroup.add(childLaContactName);
        formIndividualGroup.add(childLaTelephoneNo);
        formIndividualGroup.add(childLlaEmail);

        return formIndividualGroup;
    }

    private CompositeField getCompositeFieldGroupChildMaintanenceOrder() {
        //child no Maintanence Order
        //child maintanence Order
        final CompositeField compositeFieldGroupChildMaintanenceOrder = CompositeField.builder()
                .selectorType(SelectorEnum.ONE_CHILD_REQUIRED)
                .fieldType(FieldTypeEnum.ROOT)
                .build();

        final CompositeField childMaintanenceOrder = CompositeField.builder()
            .name("child_maintanenceOrder")
            .fieldType(FieldTypeEnum.CHECKBOX)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childMaintanenceOrder.add(TextField.builder()
                                      .name("child_personName")
                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                      .build());
        childMaintanenceOrder.add(TextField.builder()
                                      .name("child_personAddress")
                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                      .build());
        childMaintanenceOrder.add(TextField.builder()
                                      .name("child_courtAndDateOfOrder")
                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                      .build());
        childMaintanenceOrder.add(DateField.builder()
                                      .name("child_maintanenceAgreementDate")
                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                      .build());

        CheckboxField childNoMaintanenceOrder = CheckboxField.builder()
                                                .name("child_noMaintanenceOrder")
                                                .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                .build();

        compositeFieldGroupChildMaintanenceOrder.add(childMaintanenceOrder);
        compositeFieldGroupChildMaintanenceOrder.add(childNoMaintanenceOrder);
        return compositeFieldGroupChildMaintanenceOrder;
    }

    private CompositeField getCompositeFieldGroupchildLaOrParentalResponsibilityDetails() {
        //child no Local authority Or Parental Responsibility
        //child Local authority or parental Responsibility Details
        final CompositeField compositeFieldGroupchildLaOrParentalResponsibilityDetails = CompositeField.builder()
                .selectorType(SelectorEnum.ONE_CHILD_REQUIRED)
                .fieldType(FieldTypeEnum.ROOT)
                .build();

        final CompositeField childLaOrParentalResponsibilityDetails = CompositeField.builder()
            .name("child_LaOrParentalResponsibilityDetails")
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldType(FieldTypeEnum.CHECKBOX)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childLaOrParentalResponsibilityDetails.add(TextField.builder()
                                                       .name("child_laOrParentalName")
                                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                       .build());
        childLaOrParentalResponsibilityDetails.add(TextField.builder()
                                                       .name("child_laOrParentalAddress")
                                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                       .build());
        childLaOrParentalResponsibilityDetails.add(DateField.builder()
                                                       .name("child_laOrParentalNotifiedDate")
                                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                       .build());
        childLaOrParentalResponsibilityDetails.add(TextField.builder()
                                                       .name("child_laOrParentalContactName")
                                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                       .build());
        childLaOrParentalResponsibilityDetails.add(PhoneNumberField.builder()
                                                       .name("child_laOrParentalTelephoneNo")
                                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                       .build());
        childLaOrParentalResponsibilityDetails.add(EmailField.builder()
                                                       .name("child_laOrParentalEmail")
                                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                       .build());

        final CheckboxField childNoLaOrParentalResponsibility = CheckboxField.builder()
            .name("child_noLaOrParentalResponsibility")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();

        compositeFieldGroupchildLaOrParentalResponsibilityDetails.add(childLaOrParentalResponsibilityDetails);
        compositeFieldGroupchildLaOrParentalResponsibilityDetails.add(childNoLaOrParentalResponsibility);
        return compositeFieldGroupchildLaOrParentalResponsibilityDetails;
    }

    private CompositeField getCompositeFieldGroupChildCourt() {
        //Group field
        final CompositeField compositeFieldGroupChildCourt = CompositeField.builder()
                .selectorType(SelectorEnum.ONE_CHILD_REQUIRED)
                .fieldType(FieldTypeEnum.ROOT)
                .build();

        final CompositeField childPlacementOrderByEnglandAndWalesCourt = getChildPlacementOrderByEnglandAndWalesCourt();

        final CompositeField childFreeingOrderByEnglandAndWalesCourt = getChildFreeingOrderByEnglandAndWalesCourt();

        final CompositeField childFreeingOrderByNorthernIrelandCourt = getChildFreeingOrderByNorthernIrelandCourt();

        final CompositeField childPermanenceOrderByScotlandCourt = getChildPermanenceOrderByScotlandCourt();

        final CheckboxField childNoOrderAvaialble = CheckboxField.builder()
            .name("child_noOrderAvailable")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();

        compositeFieldGroupChildCourt.add(childPlacementOrderByEnglandAndWalesCourt);
        compositeFieldGroupChildCourt.add(childFreeingOrderByEnglandAndWalesCourt);
        compositeFieldGroupChildCourt.add(childFreeingOrderByNorthernIrelandCourt);
        compositeFieldGroupChildCourt.add(childPermanenceOrderByScotlandCourt);
        compositeFieldGroupChildCourt.add(childNoOrderAvaialble);
        return compositeFieldGroupChildCourt;
    }

    private CompositeField getChildFreeingOrderByEnglandAndWalesCourt() {
        final CompositeField childFreeingOrderByEnglandAndWalesCourt = CompositeField.builder()
            .name("child_freeingOrderByEnglandAndWalesCourt")
            .fieldType(FieldTypeEnum.CHECKBOX)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childFreeingOrderByEnglandAndWalesCourt.add(TextField.builder()
                                                        .name("child_freeingOrderByEnglandAndWalesCourtName")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        childFreeingOrderByEnglandAndWalesCourt.add(TextField.builder()
                                                        .name("child_freeingOrderByEnglandAndWalesCaseNumber")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        childFreeingOrderByEnglandAndWalesCourt.add(TextField.builder()
                                                        .name("child_freeingOrderByEnglandAndWalesTypeOfOrder")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        childFreeingOrderByEnglandAndWalesCourt.add(DateField.builder()
                                                        .name("child_freeingOrderByEnglandAndWalesDateOfOrder")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        return childFreeingOrderByEnglandAndWalesCourt;
    }

    private CompositeField getChildPermanenceOrderByScotlandCourt() {
        final CompositeField childPermanenceOrderByScotlandCourt = CompositeField.builder()
            .name("child_permanenceOrderByScotlandCourt")
            .fieldType(FieldTypeEnum.CHECKBOX)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childPermanenceOrderByScotlandCourt.add(TextField.builder()
                                                    .name("child_permanenceOrderByScotlandCourtName")
                                                    .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                    .build());
        childPermanenceOrderByScotlandCourt.add(TextField.builder()
                                                    .name("child_permanenceOrderByScotlandCaseNumber")
                                                    .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                    .build());
        childPermanenceOrderByScotlandCourt.add(TextField.builder()
                                                    .name("child_permanenceOrderByScotlandTypeOfOrder")
                                                    .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                    .build());
        childPermanenceOrderByScotlandCourt.add(DateField.builder()
                                                    .name("child_permanenceOrderByScotlandDateOfOrder")
                                                    .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                    .build());
        return childPermanenceOrderByScotlandCourt;
    }

    private CompositeField getChildFreeingOrderByNorthernIrelandCourt() {
        final CompositeField childFreeingOrderByNorthernIrelandCourt = CompositeField.builder()
            .name("child_freeingOrderByNorthernIrelandCourt")
            .fieldType(FieldTypeEnum.CHECKBOX)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childFreeingOrderByNorthernIrelandCourt.add(TextField.builder()
                                                        .name("child_freeingOrderByNorthernIrelandCourtName")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        childFreeingOrderByNorthernIrelandCourt.add(TextField.builder()
                                                        .name("child_freeingOrderByNorthernIrelandCaseNumber")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        childFreeingOrderByNorthernIrelandCourt.add(TextField.builder()
                                                        .name("child_freeingOrderByNorthernIrelandTypeOfOrder")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        childFreeingOrderByNorthernIrelandCourt.add(DateField.builder()
                                                        .name("child_freeingOrderByNorthernIrelandDateOfOrder")
                                                        .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                        .build());
        return childFreeingOrderByNorthernIrelandCourt;
    }

    private CompositeField getChildPlacementOrderByEnglandAndWalesCourt() {
        final CompositeField childPlacementOrderByEnglandAndWalesCourt = CompositeField.builder()
            .name("child_placementOrderByEnglandAndWalesCourt")
            .fieldType(FieldTypeEnum.CHECKBOX)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childPlacementOrderByEnglandAndWalesCourt.add(TextField.builder()
                                                          .name("child_placmentOrderByEnglandAndWalesCourtName")
                                                          .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                          .build());
        childPlacementOrderByEnglandAndWalesCourt.add(TextField.builder()
                                                          .name("child_placmentOrderByEnglandAndWalesCaseNumber")
                                                          .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                          .build());
        childPlacementOrderByEnglandAndWalesCourt.add(TextField.builder()
                                                          .name("child_placmentOrderByEnglandAndWalesTypeOfOrder")
                                                          .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                          .build());
        childPlacementOrderByEnglandAndWalesCourt.add(DateField.builder()
                                                          .name("child_placmentOrderByEnglandAndWalesDateOfOrder")
                                                          .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                          .build());
        return childPlacementOrderByEnglandAndWalesCourt;
    }

    private EmailField getChildLlaEmail() {
        return EmailField.builder()
            .name("child_laEmail")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private PhoneNumberField getChildLaTelephoneNo() {
        return PhoneNumberField.builder()
            .name("child_laTelephoneNo")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private TextField getChildLaContactName() {
        return TextField.builder()
            .name("child_laContactName")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private DateField getChildLaNotifiedDate() {
        return DateField.builder()
            .name("child_laNotifiedDate")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private TextField getChildLaAddress() {
        return TextField.builder()
            .name("child_laAddress")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private TextField getChildLaName() {
        return TextField.builder()
            .name("child_laName")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private CompositeField getCompositeFieldGroupchildAdoptionAgencyDetailsAvailable() {
        //child is Adoption Agency Involved
        //child is Adoption Agency Details Available
        final CompositeField compositeFieldGroupchildAdoptionAgencyDetailsAvailable =
            CompositeField.builder()
                .selectorType(SelectorEnum.ONE_CHILD_REQUIRED)
                .fieldType(FieldTypeEnum.ROOT)
                .build();

        CompositeField childIsAdoptionAgencyDetailsAvailable =
             CompositeField.builder()
                 .name("child_isAdoptionAgencyDetailsAvailable")
                 .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
                 .fieldType(FieldTypeEnum.CHECKBOX)
                 .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                 .build();
        childIsAdoptionAgencyDetailsAvailable.add(DateField.builder()
                                                      .name("child_adoptionDate")
                                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                      .build());
        childIsAdoptionAgencyDetailsAvailable.add(TextField.builder()
                                                      .name("child_adoptionAgencyName")
                                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                      .build());
        childIsAdoptionAgencyDetailsAvailable.add(TextField.builder()
                                                      .name("child_adoptionAgencyAddress")
                                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                      .build());
        childIsAdoptionAgencyDetailsAvailable.add(TextField.builder()
                                                      .name("child_adoptionAgencyContactName")
                                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                      .build());
        childIsAdoptionAgencyDetailsAvailable.add(PhoneNumberField.builder()
                                                      .name("child_adoptionAgencyTelephoneNo")
                                                      .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                      .build());

        CheckboxField childIsAdoptionAgencyInvolved = CheckboxField.builder()
            .name("child_isAdoptionAgencyInvolved")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();

        compositeFieldGroupchildAdoptionAgencyDetailsAvailable.add(childIsAdoptionAgencyDetailsAvailable);
        compositeFieldGroupchildAdoptionAgencyDetailsAvailable.add(childIsAdoptionAgencyInvolved);
        return compositeFieldGroupchildAdoptionAgencyDetailsAvailable;
    }

    private PhoneNumberField getChildPlacedAdoptionAgencyTelephoneNo() {
        return PhoneNumberField.builder()
            .name("child_placedAdoptionAgencyTelephoneNo")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private TextField getChildPlacedAdoptionAgencyContactName() {
        return TextField.builder()
            .name("child_placedAdoptionAgencyContactName")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private TextField getChildPlacedAdoptionAgencyAddress() {
        return TextField.builder()
            .name("child_placedAdoptionAgencyAddress")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private TextField getChildPlacedAdoptionAgencyName() {
        return TextField.builder()
            .name("child_placedAdoptionAgencyName")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private DateField getChildPlacedAdoptionDate() {
        return DateField.builder()
            .name("child_placedAdoptionDate")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private TextField getChildIsAdoptedByAgency() {
        //child_isAdoptedByAgency
        return TextField.builder()
            .name("child_isAdoptedByAgency")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private DateField getChildLiveAtHomeFrom() {
        // child_liveAtHomeFrom
        return DateField.builder()
            .name("child_liveAtHomeFrom")
            .fieldRequiredType(FieldRequiredTypeEnum.OPTIONAL)
            .build();
    }

    private CheckboxField getChildIsMarried() {
        //child_isMarried
        return CheckboxField.builder()
            .name("child_isMarried")
            .fieldRequiredType(FieldRequiredTypeEnum.OPTIONAL)
            .build();
    }

    private TextField getChildNationality() {
        //child nationality
        return TextField.builder()
            .name("child_nationality")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private CompositeField getCompositeFieldGroupChildDateOfBirth() {
        //Group field child date of birth
        CompositeField compositeFieldGroupChildDateOfBirth = CompositeField.builder()
            .selectorType(SelectorEnum.ONE_CHILD_REQUIRED)
            .fieldType(FieldTypeEnum.ROOT).build();

        CompositeField childApproximateDateOfBirth = CompositeField.builder()
            .name("child_approximateDateOfBirth")
            .fieldType(FieldTypeEnum.DATE)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .build();

        childApproximateDateOfBirth.add(TextField.builder()
                                            .name("child_placeAndCountryOfBirth")
                                            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                            .build());

        DateField childDateOfBirth = DateField.builder()
                                            .name("child_DateOfBirth")
                                            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                            .build();

        compositeFieldGroupChildDateOfBirth.add(childDateOfBirth);
        compositeFieldGroupChildDateOfBirth.add(childApproximateDateOfBirth);
        return compositeFieldGroupChildDateOfBirth;
    }

    private CheckboxField getChildGender() {
        //Group field child gender
        return CheckboxField.builder()
            .name("child_gender")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
    }

    private CompositeField getCompositeFieldGroupChildProceedingDetailsWithRelation() {
        //child no Procceding Details With Relation
        //child proceeding Details With Relation
        //child dont Know Proceeding Details WithRelation
        final CompositeField compositeFieldGroupChildProceedingDetailsWithRelation =
            CompositeField.builder()
                .selectorType(SelectorEnum.ONE_CHILD_REQUIRED)
                .fieldType(FieldTypeEnum.ROOT)
                .build();

        final CompositeField childProceedingDetailsWithRelation = CompositeField.builder()
            .name("child_proceedingDetailsWithRelation")
            .fieldType(FieldTypeEnum.CHECKBOX)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childProceedingDetailsWithRelation.add(TextField.builder()
                                                   .name("child_typeOfOrderRelation")
                                                   .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                   .build());
        childProceedingDetailsWithRelation.add(DateField.builder()
                                                   .name("child_dateOfOrderRelation")
                                                   .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                   .build());
        childProceedingDetailsWithRelation.add(TextField.builder()
                                                   .name("child_nameOfCourtRelation")
                                                   .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                   .build());
        childProceedingDetailsWithRelation.add(TextField.builder()
                                                   .name("child_caseNumberRelation")
                                                   .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                                   .build());

        CheckboxField childNoProccedingDetailsWithRelation = CheckboxField.builder()
            .name("child_noProccedingDetailsWithRelation")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        CheckboxField childDontKnowProceedingDetailsWithRelation = CheckboxField.builder()
            .name("child_dontKnowProceedingDetailsWithRelation")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();

        compositeFieldGroupChildProceedingDetailsWithRelation.add(childProceedingDetailsWithRelation);
        compositeFieldGroupChildProceedingDetailsWithRelation.add(childNoProccedingDetailsWithRelation);
        compositeFieldGroupChildProceedingDetailsWithRelation.add(childDontKnowProceedingDetailsWithRelation);
        return compositeFieldGroupChildProceedingDetailsWithRelation;
    }

    private CompositeField getCompositeFieldGroupChildProceedingDetails() {
        //child no Proceeding Details
        //child proceeding Details
        final CompositeField compositeFieldGroupChildProceedingDetails =
            CompositeField.builder()
                .fieldType(FieldTypeEnum.ROOT)
                .selectorType(SelectorEnum.ONE_CHILD_REQUIRED)
                .build();

        final CompositeField childProceedingDetails = CompositeField.builder()
            .name("child_proceedingDetails")
            .fieldType(FieldTypeEnum.CHECKBOX)
            .selectorType(SelectorEnum.ALL_CHILD_REQUIRED)
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();
        childProceedingDetails.add(TextField.builder()
                                       .name("child_typeOfOrder")
                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                       .build());
        childProceedingDetails.add(DateField.builder()
                                       .name("child_dateOfOrder")
                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                       .build());
        childProceedingDetails.add(TextField.builder()
                                       .name("child_nameOfCourt")
                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                       .build());
        childProceedingDetails.add(DateField.builder()
                                       .name("child_caseNumber")
                                       .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
                                       .build());

        final CheckboxField childNoProceedingDetails = CheckboxField.builder()
            .name("child_noProceedingDetails")
            .fieldRequiredType(FieldRequiredTypeEnum.MANDATORY)
            .build();

        compositeFieldGroupChildProceedingDetails.add(childProceedingDetails);
        compositeFieldGroupChildProceedingDetails.add(childNoProceedingDetails);
        return compositeFieldGroupChildProceedingDetails;
    }
}
