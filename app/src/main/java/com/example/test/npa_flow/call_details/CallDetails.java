package com.example.test.npa_flow.call_details;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.util.Date;

public class CallDetails implements Serializable {


    private static final long serialVersionUID = 8756372923692891750L;
    private Integer attemptNo;
    private String callDateTime;
    private Integer callDuration;
    private String notes;
    private byte[] callRecording;
    private String scheduledCallDateTime;
    public Integer getAttemptNo() {
        return attemptNo;
    }
    public void setAttemptNo(Integer attemptNo) {
        this.attemptNo = attemptNo;
    }
    public String getCallDateTime() {
        return callDateTime;
    }
    public void setCallDateTime(String callDateTime) {
        this.callDateTime = callDateTime;
    }
    public Integer getCallDuration() {
        return callDuration;
    }
    public void setCallDuration(Integer callDuration) {
        this.callDuration = callDuration;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public byte[] getCallRecording() {
        return callRecording;
    }
    public void setCallRecording(byte[] callRecording) {
        this.callRecording = callRecording;
    }

    public String getScheduledCallDateTime() {
        return scheduledCallDateTime;
    }

    public void setScheduledCallDateTime(String scheduledCallDateTime) {
        this.scheduledCallDateTime = scheduledCallDateTime;
    }
}