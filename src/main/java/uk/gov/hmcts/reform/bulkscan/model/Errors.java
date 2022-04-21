package uk.gov.hmcts.reform.bulkscan.model;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public class Errors {
    public List<String> items;
}

