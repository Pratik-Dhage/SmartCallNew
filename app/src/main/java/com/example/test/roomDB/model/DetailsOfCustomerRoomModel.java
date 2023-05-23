package com.example.test.roomDB.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "details_of_customer_table")
public class DetailsOfCustomerRoomModel {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "sequence")
    private int sequence;

    @ColumnInfo(name = "lable")
    private String lable;

    @ColumnInfo(name = "value")
    private Object value;

    @ColumnInfo(name = "editable")
    private String editable;

    @ColumnInfo(name = "editDataType")
    private String editDataType;

    @ColumnInfo(name = "button")
    private String button;

    @ColumnInfo(name = "buttonLable")
    private String buttonLable;

    @ColumnInfo(name = "buttonAction")
    private String buttonAction;

    public DetailsOfCustomerRoomModel(int sequence, String lable, Object value, String editable, String editDataType, String button, String buttonLable, String buttonAction) {
        this.sequence = sequence;
        this.lable = lable;
        this.value = value;
        this.editable = editable;
        this.editDataType = editDataType;
        this.button = button;
        this.buttonLable = buttonLable;
        this.buttonAction = buttonAction;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
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
