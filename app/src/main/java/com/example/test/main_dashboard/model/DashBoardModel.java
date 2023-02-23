package com.example.test.main_dashboard.model;

import java.io.Serializable;

public class DashBoardModel implements Serializable {


    private static final long serialVersionUID = 6544756564330484925L;
    private String queueName;
    private Integer pendingCalls;
    private Integer completedCalls;
    private Integer sequence;

    public DashBoardModel(String queueName, Integer pendingCalls, Integer completedCalls, Integer sequence) {
        super();
        this.queueName = queueName;
        this.pendingCalls = pendingCalls;
        this.completedCalls = completedCalls;
        this.sequence = sequence;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Integer getPendingCalls() {
        return pendingCalls;
    }

    public void setPendingCalls(Integer pendingCalls) {
        this.pendingCalls = pendingCalls;
    }

    public Integer getCompletedCalls() {
        return completedCalls;
    }

    public void setCompletedCalls(Integer completedCalls) {
        this.completedCalls = completedCalls;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
