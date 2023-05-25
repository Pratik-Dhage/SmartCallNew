package com.example.test.schedule_flow.model;

public class ScheduleVisit_Details {

    private static final long serialVersionUID = -6563689854953117512L;
    private String scheduledDate;
    private String scheduledTime;
    private String memberName;
    private String queue;
    public String getScheduledDate() {
        return scheduledDate;
    }
    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    public String getScheduledTime() {
        return scheduledTime;
    }
    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public String getQueue() {
        return queue;
    }
    public void setQueue(String queue) {
        this.queue = queue;
    }
}
