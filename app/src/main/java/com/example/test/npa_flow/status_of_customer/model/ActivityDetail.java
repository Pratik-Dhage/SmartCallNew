package com.example.test.npa_flow.status_of_customer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ActivityDetail implements Serializable {

    private static final long serialVersionUID = 5750905047142840128L;
    private Long activityId;
    private String activityDate;
    private String activityTime;
    private String activityDay;
    private Integer attemptSequence;
    private String attemptDate;
    private String attemptTime;
    private Integer attemptDuration;
    private String attemptStatus;
    private String attemptFlow;
    private String attemptNotes;
    private String scheduleType;
    private String scheduleDate;
    private String scheduleTime;
    private String status;
    public Long getActivityId() {
        return activityId;
    }
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    public String getActivityDate() {
        return activityDate;
    }
    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }
    public String getActivityTime() {
        return activityTime;
    }
    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }
    public String getActivityDay() {
        return activityDay;
    }
    public void setActivityDay(String activityDay) {
        this.activityDay = activityDay;
    }
    public Integer getAttemptSequence() {
        return attemptSequence;
    }
    public void setAttemptSequence(Integer attemptSequence) {
        this.attemptSequence = attemptSequence;
    }
    public String getAttemptDate() {
        return attemptDate;
    }
    public void setAttemptDate(String attemptDate) {
        this.attemptDate = attemptDate;
    }
    public String getAttemptTime() {
        return attemptTime;
    }
    public void setAttemptTime(String attemptTime) {
        this.attemptTime = attemptTime;
    }
    public Integer getAttemptDuration() {
        return attemptDuration;
    }
    public void setAttemptDuration(Integer attemptDuration) {
        this.attemptDuration = attemptDuration;
    }
    public String getAttemptStatus() {
        return attemptStatus;
    }
    public void setAttemptStatus(String attemptStatus) {
        this.attemptStatus = attemptStatus;
    }
    public String getAttemptFlow() {
        return attemptFlow;
    }
    public void setAttemptFlow(String attemptFlow) {
        this.attemptFlow = attemptFlow;
    }
    public String getAttemptNotes() {
        return attemptNotes;
    }
    public void setAttemptNotes(String attemptNotes) {
        this.attemptNotes = attemptNotes;
    }
    public String getScheduleType() {
        return scheduleType;
    }
    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }
    public String getScheduleDate() {
        return scheduleDate;
    }
    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    public String getScheduleTime() {
        return scheduleTime;
    }
    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}