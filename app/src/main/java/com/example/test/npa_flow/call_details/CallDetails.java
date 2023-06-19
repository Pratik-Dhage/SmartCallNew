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
    private String dateOfVisitPromised;
    private String foName;
    private String relativeName;
    private String relativeContact;
    private String amountCollected;
    private String chequeDate;
    private String chequeNumber;
    private String chequeAmount;
    private String bankName;
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

    public String getDateOfVisitPromised() {
        return dateOfVisitPromised;
    }

    public void setDateOfVisitPromised(String dateOfVisitPromised) {
        this.dateOfVisitPromised = dateOfVisitPromised;
    }

    public String getFoName() {
        return foName;
    }

    public void setFoName(String foName) {
        this.foName = foName;
    }

    public String getRelativeName() {
        return relativeName;
    }

    public void setRelativeName(String relativeName) {
        this.relativeName = relativeName;
    }

    public String getRelativeContact() {
        return relativeContact;
    }

    public void setRelativeContact(String relativeContact) {
        this.relativeContact = relativeContact;
    }

    public String getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        this.amountCollected = amountCollected;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getChequeAmount() {
        return chequeAmount;
    }

    public void setChequeAmount(String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}