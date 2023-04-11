package com.example.test.npa_flow.dpd;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

public class DPD_ResponseModel implements Serializable {
  private String dpdQueueName;

  private Integer pending;

  private Integer noOfMembers;

  private Integer completed;

  public String getDpdQueueName() {
    return this.dpdQueueName;
  }

  public void setDpdQueueName(String dpdQueueName) {
    this.dpdQueueName = dpdQueueName;
  }

  public Integer getPending() {
    return this.pending;
  }

  public void setPending(Integer pending) {
    this.pending = pending;
  }

  public Integer getNoOfMembers() {
    return this.noOfMembers;
  }

  public void setNoOfMembers(Integer noOfMembers) {
    this.noOfMembers = noOfMembers;
  }

  public Integer getCompleted() {
    return this.completed;
  }

  public void setCompleted(Integer completed) {
    this.completed = completed;
  }
}
