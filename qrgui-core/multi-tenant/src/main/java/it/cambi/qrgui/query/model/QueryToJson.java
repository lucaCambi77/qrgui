package it.cambi.qrgui.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class QueryToJson {
    private String statement;
    private List<Attribute> attrs;
    private List<Constraint> constr;
    private List<SelectColumns> querySelectColumns;
    private int position;
}
