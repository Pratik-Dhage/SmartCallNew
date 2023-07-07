package com.example.test.npa_flow.status_of_customer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Activity implements Serializable {

    private static final long serialVersionUID = 4543453568333371880L;
    private Long activityId;
    private Long companyId;
    private Long activityType;
    private String activityDescription;
    private String activityDate;
    private String activityTime;
    private String day;
    private Long dataSetId;
    private Long parentActivity;
    private String branchCode;
    private String userId;
    private String userName;
    private String activityStatus;
    private String scheduleDate;
    private String scheduleTime;
    private String amountCollected;
    private List<ActivityDetail> activityDetails;
    private Activity childActivity;
    public Long getActivityId() {
        return activityId;
    }
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    public Long getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
    public Long getActivityType() {
        return activityType;
    }
    public void setActivityType(Long activityType) {
        this.activityType = activityType;
    }
    public String getActivityDescription() {
        return activityDescription;
    }
    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
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
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public Long getDataSetId() {
        return dataSetId;
    }
    public void setDataSetId(Long dataSetId) {
        this.dataSetId = dataSetId;
    }
    public Long getParentActivity() {
        return parentActivity;
    }
    public void setParentActivity(Long parentActivity) {
        this.parentActivity = parentActivity;
    }
    public String getBranchCode() {
        return branchCode;
    }
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getActivityStatus() {
        return activityStatus;
    }
    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }
    public List<ActivityDetail> getActivityDetails() {
        return activityDetails;
    }
    public void setActivityDetails(List<ActivityDetail> activityDetails) {
        this.activityDetails = activityDetails;
    }
    public Activity getChildActivity() {
        return childActivity;
    }
    public void setChildActivity(Activity childActivity) {
        this.childActivity = childActivity;
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

    public String getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        this.amountCollected = amountCollected;
    }
}