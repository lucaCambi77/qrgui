package it.cambi.qrgui.query.model;

import it.cambi.qrgui.enums.ContstraintType;

public class Constraint {

  private String attrName;
  private String parameters;
  private String message;
  private ContstraintType constrType;
  private Long maxIntervalDays;
  private Long maxIntervalHours;
  private Long maxIntervalMin;
  private Long maxIntervalSec;
  private Long maxIntervalNumber;
  private Long maxInSize;

  public String getAttrName() {
    return attrName;
  }

  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public ContstraintType getConstrType() {
    return constrType;
  }

  public void setConstrType(ContstraintType constrType) {
    this.constrType = constrType;
  }

  public Long getMaxIntervalDays() {
    return maxIntervalDays;
  }

  public void setMaxIntervalDays(Long maxIntervalDays) {
    this.maxIntervalDays = maxIntervalDays;
  }

  public Long getMaxIntervalHours() {
    return maxIntervalHours;
  }

  public void setMaxIntervalHours(Long maxIntervalHours) {
    this.maxIntervalHours = maxIntervalHours;
  }

  public Long getMaxIntervalMin() {
    return maxIntervalMin;
  }

  public void setMaxIntervalMin(Long maxIntervalMin) {
    this.maxIntervalMin = maxIntervalMin;
  }

  public Long getMaxIntervalSec() {
    return maxIntervalSec;
  }

  public void setMaxIntervalSec(Long maxIntervalSec) {
    this.maxIntervalSec = maxIntervalSec;
  }

  public Long getMaxIntervalNumber() {
    return maxIntervalNumber;
  }

  public void setMaxIntervalNumber(Long maxIntervalNumber) {
    this.maxIntervalNumber = maxIntervalNumber;
  }

  public Long getMaxInSize() {
    return maxInSize;
  }

  public void setMaxInSize(Long maxInSize) {
    this.maxInSize = maxInSize;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
