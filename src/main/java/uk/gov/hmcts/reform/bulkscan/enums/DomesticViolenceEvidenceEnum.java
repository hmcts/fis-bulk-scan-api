package uk.gov.hmcts.reform.bulkscan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DomesticViolenceEvidenceEnum {

    ARRESTED_FOR_DOMESTIC_VIOLENCE_OFFENCE("Arrested for relevant domestic violence offence"),

    POLICE_CAUTION_FOR_DOMESTIC_VIOLENCE_OFFENCE("Evidence of a relevant police caution for a domestic violence offence"),

    CRIMINAL_PROCEEDINGS_FOR_DOMESTIC_VIOLENCE_NOT_CONCLUDED("Evidence of relevant criminal proceedings for a domestic violence offence which have not concluded"),

    CONVICTION_FOR_DOMESTIC_VIOLENCE_OFFENCE("Evidence of a relevant conviction for a domestic violence offence"),

    COURT_ORDER_BINDING_PARTY_IN_CONNECTION_WITH_DOMESTIC_VIOLENCE("A court order binding a prospective party over in connection with a domestic violence offence"),

    DOMESTIC_VIOLENCE_PROTECTION_NOTICE_ISSUED_S24_CSA("A domestic violence protection notice issued under section 24 of the Crime and Security Act 2010 against a prospective party"),

    PROTECTIVE_INJUNCTION("A relevant protective injunction"),

    UNDERTAKING_PROVIDED_CROSS_UNDERTAKING_RELATED_TO_DOMESTIC_VIOLENCE_NOT_GIVEN_BY_OTHER("An undertaking given in England and Wales under section 46 or 63E of the Family Law Act 1996 (or given in Scotland or Northern Ireland in place of a protective injunction) by a prospective party, provided that a cross- undertaking relating to domestic violence was not given by another prospective party"),
    COPY_OF_FINDING_OF_FACT_IN_UK_OF_DOMESTIC_VIOLENCE("A copy of a finding of fact, made in proceedings in the United Kingdom, that there has been domestic violence by a prospective party"),

    EXPERT_REPORT_ASSESSED_AS_A_VICTIM("An expert report produced as evidence in proceedings in the United Kingdom for the benefit of a court or tribunal confirming that a person with whom a prospective party is or was in a family relationship, was assessed as being, or at risk of being, a victim of domestic violence by that prospective party"),

    HEALTH_PROFESSIONAL_REPORT_ON_DOMESTIC_ABUSE_INJURIES("A letter or report from an appropriate health professional confirming that- \\r\\n(i)  that professional,or another appropriate health professional, has examined a prospective party in person; and (ii)  in the reasonable professional judgment of the author or the examining appropriate health professional, that prospective party has, or has had, injuries or a condition consistent with being a victim of domestic violence"),

    REFERRAL_HEALTH_REPORT("A letter or report from- \\r\\n(i) the appropriate health professional who made the referral described below; \\r\\n(ii) an appropriate health professional who has access to the medical records of the prospective party referred to below; or \\r\\n(iii) the person to whom the referral described below was made; \\r\\nconfirming that there was a referral by an appropriate health professional of a prospective party to a person who provides specialist support or assistance for victims of, or those at risk of, domestic violence"),

    MULTI_AGENCY_RISK_ASSESSMENT_CONFERENCE_LETTER("A letter from any person who is a member of a multi-agency risk assessment conference (or other suitable local safeguarding forum) confirming that a prospective party, or a person with whom that prospective party is in a family relationship, is or has been at risk of harm from domestic violence by another prospective party"),

    DOMESTIC_VIOLENCE_ADVISOR("A letter from an independent domestic violence advisor confirming that they are providing support to a prospective party"),

    INDEPENDENT_SEXUAL_VIOLENCE_ADVISOR("A letter from an independent sexual violence advisor confirming that they are providing support to a prospective party relating to sexual violence by another prospective party"),

    LETTER_LOCAL_AUTHORITY("A letter from an officer employed by a local authority or housing association (or their equivalent in Scotland or Northern Ireland) for the purpose of supporting tenants containing- \\r\\n(i)  a statement to the effect that, in their reasonable professional judgment, a person with whom a prospective party is or has been in a family relationship is, or is at risk of being, a victim of domestic violence by that prospective party; (ii)  a description of the specific matters relied upon to support that judgment; and (iii) a description of the support they provided to the victim of domestic violence or the person at risk of domestic violence by that prospective party"),

    DOMESTIC_VIOLENCE_SUPPORT_CHARITY("A letter which- \\r\\n(i) is from an organisation providing domestic violence support services, or a registered charity, which letter confirms that it- \\r\\n(a) is situated in England and Wales, \\r\\n(b) has been operating for an uninterrupted period of six months or more; and \\r\\n(c) provided a prospective party with support in relation to that person’s needs as a victim, or a person at risk, of domestic violence; and \\r\\n(ii) contains- \\r\\n(a) a statement to the effect that, in the reasonable professional judgment of the author of the letter, the prospective party is, or is at risk of being, a victim of domestic violence; \\r\\n(b) a description of the specific matters relied upon to support that judgment; \\r\\n(c) a description of the support provided to the prospective party; and \\r\\n(d) a statement of the reasons why the prospective party needed that support"),

    DOMESTIC_VIOLENCE_SUPPORT_CHARITY_REFUGE_LETTER("A letter or report from an organisation providing domestic violence support services in the United Kingdom confirming- \\r\\n(i) that a person with whom a prospective party is or was in a family relationship was refused admission to a refuge; \\r\\n(ii) the date on which they were refused admission to the refuge; and \\r\\n(iii)they sought admission to the refuge because of allegations of domestic violence by the prospective party referred to in paragraph (i)"),

    PUBLIC_AUTHORITY_CONFIRMATION_LETTER("A letter from a public authority confirming that a person with whom a prospective party is or was in a family relationship, was assessed as being, or at risk of being, a victim of domestic violence by that prospective party (or a copy of that assessment);"),

    SECRETARY_OF_STATE_LETTER("A letter from the Secretary of State for the Home Department confirming that a prospective party has been granted leave to remain in the United Kingdom under paragraph 289B of the Rules made by the Home Secretary under section 3(2) of the Immigration Act 1971, which can be found at https://www. gov.uk/guidance/immigration-rules/immigration-rules-index;"),

    EVIDENCE_OF_FINANCIAL_MATTERS("Evidence which demonstrates that a prospective party has been, or is at risk of being, the victim of domestic violence by another prospective party in the form of abuse which relates to financial matters.");


    private final String name;
}
