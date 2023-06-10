package com.example.test.npa_flow.loan_collection;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;

public class LoanCollectionListResponseModel implements Serializable {
  private Long dataSetId;

  private BigDecimal distance;

  private String actionStatus;

  private Object scheduleDateTime;

  private BigDecimal lattitute;

  private String memberName;

  private String location;

  private BigDecimal longitute;

  private String mobileNumber;

  private String pinCode;

  public Long getDataSetId() {
    return this.dataSetId;
  }

  public void setDataSetId(Long dataSetId) {
    this.dataSetId = dataSetId;
  }

  public BigDecimal getDistance() {
    return this.distance;
  }

  public void setDistance(BigDecimal distance) {
    this.distance = distance;
  }

  public String getActionStatus() {
    return this.actionStatus;
  }

  public void setActionStatus(String actionStatus) {
    this.actionStatus = actionStatus;
  }

  public Object getScheduleDateTime() {
    return this.scheduleDateTime;
  }

  public void setScheduleDateTime(Object scheduleDateTime) {
    this.scheduleDateTime = scheduleDateTime;
  }

  public BigDecimal getLattitute() {
    return this.lattitute;
  }

  public void setLattitute(BigDecimal lattitute) {
    this.lattitute = lattitute;
  }

  public String getMemberName() {
    return this.memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public BigDecimal getLongitute() {
    return this.longitute;
  }

  public void setLongitute(BigDecimal longitute) {
    this.longitute = longitute;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }
}
