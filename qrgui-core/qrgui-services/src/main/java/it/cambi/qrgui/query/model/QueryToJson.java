package it.cambi.qrgui.query.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cambi.qrgui.enums.QueryType;
import it.cambi.qrgui.model.Temi13DtbInf;

import java.util.List;

public class QueryToJson {

  private String statement;
  private List<Attribute> attrs;
  private List<Constraint> constr;
  private List<SelectColumns> querySelectColumns;
  private QueryType queryType;
  private int position;

  /** Campi non più utilizzati dalla 1.0.0 */
  @JsonIgnore private String name;

  @JsonIgnore private Temi13DtbInf temi13DtbInf;
  @JsonIgnore private Long cque;

  public String getStatement() {
    return statement;
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

  public List<Attribute> getAttrs() {
    return attrs;
  }

  public void setAttrs(List<Attribute> attrs) {
    this.attrs = attrs;
  }

  public List<Constraint> getConstr() {
    return constr;
  }

  public void setConstr(List<Constraint> constr) {
    this.constr = constr;
  }

  public List<SelectColumns> getQuerySelectColumns() {
    return querySelectColumns;
  }

  public void setQuerySelectColumns(List<SelectColumns> querySelectColumns) {
    this.querySelectColumns = querySelectColumns;
  }

  public QueryType getQueryType() {
    return queryType;
  }

  public void setQueryType(QueryType queryType) {
    this.queryType = queryType;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Temi13DtbInf getTemi13DtbInf() {
    return temi13DtbInf;
  }

  public void setTemi13DtbInf(Temi13DtbInf temi13DtbInf) {
    this.temi13DtbInf = temi13DtbInf;
  }

  public Long getCque() {
    return cque;
  }

  public void setCque(Long cque) {
    this.cque = cque;
  }
}
