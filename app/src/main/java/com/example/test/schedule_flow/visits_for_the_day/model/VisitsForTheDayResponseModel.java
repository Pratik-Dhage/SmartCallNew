package com.example.test.schedule_flow.visits_for_the_day.model;

import java.io.Serializable;

public class VisitsForTheDayResponseModel implements Serializable {
    private Integer dataSetId;

    private Object distance;

    private String actionStatus;

    private Object scheduleDateTime;

    private Object lattitute;

    private String memberName;

    private String location;

    private Object longitute;

    public Integer getDataSetId() {
        return this.dataSetId;
    }

    public void setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
    }

    public Object getDistance() {
        return this.distance;
    }

    public void setDistance(Object distance) {
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

    public Object getLattitute() {
        return lattitute;
    }

    public void setLattitute(Object lattitute) {
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

    public Object getLongitute() {
        return longitute;
    }

    public void setLongitute(Object longitute) {
        this.longitute = longitute;
    }
}
