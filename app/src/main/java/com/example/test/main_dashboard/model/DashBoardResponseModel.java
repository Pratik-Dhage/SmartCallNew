package com.example.test.main_dashboard.model;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

public class DashBoardResponseModel implements Serializable {
  private Integer sequence;

  private String queueName;

  private Integer pendingCalls;

  private Integer completedCalls;

  public Integer getSequence() {
    return this.sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  public String getQueueName() {
    return this.queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public Integer getPendingCalls() {
    return this.pendingCalls;
  }

  public void setPendingCalls(Integer pendingCalls) {
    this.pendingCalls = pendingCalls;
  }

  public Integer getCompletedCalls() {
    return this.completedCalls;
  }

  public void setCompletedCalls(Integer completedCalls) {
    this.completedCalls = completedCalls;
  }
}
