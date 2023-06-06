package com.example.test.schedule_flow.visits_for_the_day.model;

import java.io.Serializable;

public class VisitsForTheDayResponseModel implements Serializable {
    private Object parentActivity;

    private Integer attemptSequence;

    private Object attemptFlow;

    private String activityDateTime;

    private Object userId;

    private Object branchCode;

    private Integer activityId;

    private Object companyId;

    private Object dataSetId;

    private String attemptStatus;

    private String scheduleType;

    private String scheduleDateTime;

    private Object attemptNotes;

    private Object attemptDateTime;

    private String activityDescription;

    private Integer attemptDuration;

    private Integer activityType;

    private Object status;

    public Object getParentActivity() {
        return this.parentActivity;
    }

    public void setParentActivity(Object parentActivity) {
        this.parentActivity = parentActivity;
    }

    public Integer getAttemptSequence() {
        return this.attemptSequence;
    }

    public void setAttemptSequence(Integer attemptSequence) {
        this.attemptSequence = attemptSequence;
    }

    public Object getAttemptFlow() {
        return this.attemptFlow;
    }

    public void setAttemptFlow(Object attemptFlow) {
        this.attemptFlow = attemptFlow;
    }

    public String getActivityDateTime() {
        return this.activityDateTime;
    }

    public void setActivityDateTime(String activityDateTime) {
        this.activityDateTime = activityDateTime;
    }

    public Object getUserId() {
        return this.userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(Object branchCode) {
        this.branchCode = branchCode;
    }

    public Integer getActivityId() {
        return this.activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Object getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Object companyId) {
        this.companyId = companyId;
    }

    public Object getDataSetId() {
        return this.dataSetId;
    }

    public void setDataSetId(Object dataSetId) {
        this.dataSetId = dataSetId;
    }

    public String getAttemptStatus() {
        return this.attemptStatus;
    }

    public void setAttemptStatus(String attemptStatus) {
        this.attemptStatus = attemptStatus;
    }

    public String getScheduleType() {
        return this.scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleDateTime() {
        return this.scheduleDateTime;
    }

    public void setScheduleDateTime(String scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
    }

    public Object getAttemptNotes() {
        return this.attemptNotes;
    }

    public void setAttemptNotes(Object attemptNotes) {
        this.attemptNotes = attemptNotes;
    }

    public Object getAttemptDateTime() {
        return this.attemptDateTime;
    }

    public void setAttemptDateTime(Object attemptDateTime) {
        this.attemptDateTime = attemptDateTime;
    }

    public String getActivityDescription() {
        return this.activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public Integer getAttemptDuration() {
        return this.attemptDuration;
    }

    public void setAttemptDuration(Integer attemptDuration) {
        this.attemptDuration = attemptDuration;
    }

    public Integer getActivityType() {
        return this.activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public Object getStatus() {
        return this.status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
