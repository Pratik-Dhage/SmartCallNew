package com.example.test.main_dashboard.model;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

public class DashBoardScheduleForTheDayModel implements Serializable {
  private Integer pending;

  private Integer complete;

  private String queue;

  public Integer getPending() {
    return this.pending;
  }

  public void setPending(Integer pending) {
    this.pending = pending;
  }

  public Integer getComplete() {
    return this.complete;
  }

  public void setComplete(Integer complete) {
    this.complete = complete;
  }

  public String getQueue() {
    return this.queue;
  }

  public void setQueue(String queue) {
    this.queue = queue;
  }
}
