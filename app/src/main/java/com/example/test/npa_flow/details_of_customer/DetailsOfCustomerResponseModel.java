package com.example.test.npa_flow.details_of_customer;

import java.io.Serializable;

public class DetailsOfCustomerResponseModel implements Serializable {


    private static final long serialVersionUID = 7012246381672161019L;
    private Integer sequence;
    private String lable;
    private Object value;
    private String editable;
    private String editDataType;
    private String button;
    private String buttonLable;
    private String buttonAction;
    public Integer getSequence() {
        return sequence;
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
    public String getLable() {
        return lable;
    }
    public void setLable(String lable) {
        this.lable = lable;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public String getEditable() {
        return editable;
    }
    public void setEditable(String editable) {
        this.editable = editable;
    }
    public String getEditDataType() {
        return editDataType;
    }
    public void setEditDataType(String editDataType) {
        this.editDataType = editDataType;
    }
    public String getButton() {
        return button;
    }
    public void setButton(String button) {
        this.button = button;
    }
    public String getButtonLable() {
        return buttonLable;
    }
    public void setButtonLable(String buttonLable) {
        this.buttonLable = buttonLable;
    }
    public String getButtonAction() {
        return buttonAction;
    }
    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }



}
