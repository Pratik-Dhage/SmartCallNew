package com.example.test.npa_flow.loan_collection;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.math.BigDecimal;

public class LoanCollectionListResponseModel implements Serializable {
  private Integer dataSetId;

  private BigDecimal distance;

  private Object scheduleDateTime;

  private String memberName;

  private String location;

  public Integer getDataSetId() {
    return this.dataSetId;
  }

  public void setDataSetId(Integer dataSetId) {
    this.dataSetId = dataSetId;
  }

  public BigDecimal getDistance() {
    return this.distance;
  }

  public void setDistance(BigDecimal distance) {
    this.distance = distance;
  }

  public Object getScheduleDateTime() {
    return this.scheduleDateTime;
  }

  public void setScheduleDateTime(Object scheduleDateTime) {
    this.scheduleDateTime = scheduleDateTime;
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
}
