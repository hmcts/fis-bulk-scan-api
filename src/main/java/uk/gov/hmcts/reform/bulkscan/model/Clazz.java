package uk.gov.hmcts.reform.bulkscan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clazz {

    private String name;

    private List<Field> fieldList;
}
