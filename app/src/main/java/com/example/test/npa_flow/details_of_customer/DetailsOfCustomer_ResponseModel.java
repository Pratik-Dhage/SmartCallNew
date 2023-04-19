package com.example.test.npa_flow.details_of_customer;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;

public class DetailsOfCustomer_ResponseModel implements Serializable {
  private Object button;

  private Integer sequence;

  private Object editDataType;

  private String editable;

  private String lable;

  private Object buttonLable;

  private Object buttonAction;

  private String value;

  public Object getButton() {
    return this.button;
  }

  public void setButton(Object button) {
    this.button = button;
  }

  public Integer getSequence() {
    return this.sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  public Object getEditDataType() {
    return this.editDataType;
  }

  public void setEditDataType(Object editDataType) {
    this.editDataType = editDataType;
  }

  public String getEditable() {
    return this.editable;
  }

  public void setEditable(String editable) {
    this.editable = editable;
  }

  public String getLable() {
    return this.lable;
  }

  public void setLable(String lable) {
    this.lable = lable;
  }

  public Object getButtonLable() {
    return this.buttonLable;
  }

  public void setButtonLable(Object buttonLable) {
    this.buttonLable = buttonLable;
  }

  public Object getButtonAction() {
    return this.buttonAction;
  }

  public void setButtonAction(Object buttonAction) {
    this.buttonAction = buttonAction;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
