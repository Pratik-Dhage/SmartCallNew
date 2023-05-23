package com.example.test.schedule_flow.model;

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
    private Date activityDateTime;
    private Long dataSetId;
    private Long parentActivity;
    private String createdBy;
    private Date createdDateTime;
    private String updatedBy;
    private Date updatedDateTime;
    private Date removeDateTime;
    private Date validFrom;
    private Date validTo;
    private String branchCode;
    private String userId;
    private String activityStatus;
    private String genericString1;
    private String genericString2;
    private String genericString3;
    private String genericString4;
    private String genericString5;
    private String genericString6;
    private String genericString7;
    private String genericString8;
    private String genericString9;
    private String genericString10;
    private Long genericNumber1;
    private Long genericNumber2;
    private Long genericNumber3;
    private Long genericNumber4;
    private Long genericNumber5;
    private Long genericNumber6;
    private Long genericNumber7;
    private Long genericNumber8;
    private Long genericNumber9;
    private Long genericNumber10;
    private BigDecimal genericDecimal1;
    private BigDecimal genericDecimal2;
    private BigDecimal genericDecimal3;
    private BigDecimal genericDecimal4;
    private BigDecimal genericDecimal5;
    private BigDecimal genericDecimal6;
    private BigDecimal genericDecimal7;
    private BigDecimal genericDecimal8;
    private BigDecimal genericDecimal9;
    private BigDecimal genericDecimal10;
    private Date genericDate1;
    private Date genericDate2;
    private Date genericDate3;
    private Date genericDate4;
    private Date genericDate5;
    private Date genericDate6;
    private Date genericDate7;
    private Date genericDate8;
    private Date genericDate9;
    private Date genericDate10;
    private List<ActivityDetail> activityDetails;

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

    public Date getActivityDateTime() {
        return activityDateTime;
    }

    public void setActivityDateTime(Date activityDateTime) {
        this.activityDateTime = activityDateTime;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public Date getRemoveDateTime() {
        return removeDateTime;
    }

    public void setRemoveDateTime(Date removeDateTime) {
        this.removeDateTime = removeDateTime;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
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

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getGenericString1() {
        return genericString1;
    }

    public void setGenericString1(String genericString1) {
        this.genericString1 = genericString1;
    }

    public String getGenericString2() {
        return genericString2;
    }

    public void setGenericString2(String genericString2) {
        this.genericString2 = genericString2;
    }

    public String getGenericString3() {
        return genericString3;
    }

    public void setGenericString3(String genericString3) {
        this.genericString3 = genericString3;
    }

    public String getGenericString4() {
        return genericString4;
    }

    public void setGenericString4(String genericString4) {
        this.genericString4 = genericString4;
    }

    public String getGenericString5() {
        return genericString5;
    }

    public void setGenericString5(String genericString5) {
        this.genericString5 = genericString5;
    }

    public String getGenericString6() {
        return genericString6;
    }

    public void setGenericString6(String genericString6) {
        this.genericString6 = genericString6;
    }

    public String getGenericString7() {
        return genericString7;
    }

    public void setGenericString7(String genericString7) {
        this.genericString7 = genericString7;
    }

    public String getGenericString8() {
        return genericString8;
    }

    public void setGenericString8(String genericString8) {
        this.genericString8 = genericString8;
    }

    public String getGenericString9() {
        return genericString9;
    }

    public void setGenericString9(String genericString9) {
        this.genericString9 = genericString9;
    }

    public String getGenericString10() {
        return genericString10;
    }

    public void setGenericString10(String genericString10) {
        this.genericString10 = genericString10;
    }

    public Long getGenericNumber1() {
        return genericNumber1;
    }

    public void setGenericNumber1(Long genericNumber1) {
        this.genericNumber1 = genericNumber1;
    }

    public Long getGenericNumber2() {
        return genericNumber2;
    }

    public void setGenericNumber2(Long genericNumber2) {
        this.genericNumber2 = genericNumber2;
    }

    public Long getGenericNumber3() {
        return genericNumber3;
    }

    public void setGenericNumber3(Long genericNumber3) {
        this.genericNumber3 = genericNumber3;
    }

    public Long getGenericNumber4() {
        return genericNumber4;
    }

    public void setGenericNumber4(Long genericNumber4) {
        this.genericNumber4 = genericNumber4;
    }

    public Long getGenericNumber5() {
        return genericNumber5;
    }

    public void setGenericNumber5(Long genericNumber5) {
        this.genericNumber5 = genericNumber5;
    }

    public Long getGenericNumber6() {
        return genericNumber6;
    }

    public void setGenericNumber6(Long genericNumber6) {
        this.genericNumber6 = genericNumber6;
    }

    public Long getGenericNumber7() {
        return genericNumber7;
    }

    public void setGenericNumber7(Long genericNumber7) {
        this.genericNumber7 = genericNumber7;
    }

    public Long getGenericNumber8() {
        return genericNumber8;
    }

    public void setGenericNumber8(Long genericNumber8) {
        this.genericNumber8 = genericNumber8;
    }

    public Long getGenericNumber9() {
        return genericNumber9;
    }

    public void setGenericNumber9(Long genericNumber9) {
        this.genericNumber9 = genericNumber9;
    }

    public Long getGenericNumber10() {
        return genericNumber10;
    }

    public void setGenericNumber10(Long genericNumber10) {
        this.genericNumber10 = genericNumber10;
    }

    public BigDecimal getGenericDecimal1() {
        return genericDecimal1;
    }

    public void setGenericDecimal1(BigDecimal genericDecimal1) {
        this.genericDecimal1 = genericDecimal1;
    }

    public BigDecimal getGenericDecimal2() {
        return genericDecimal2;
    }

    public void setGenericDecimal2(BigDecimal genericDecimal2) {
        this.genericDecimal2 = genericDecimal2;
    }

    public BigDecimal getGenericDecimal3() {
        return genericDecimal3;
    }

    public void setGenericDecimal3(BigDecimal genericDecimal3) {
        this.genericDecimal3 = genericDecimal3;
    }

    public BigDecimal getGenericDecimal4() {
        return genericDecimal4;
    }

    public void setGenericDecimal4(BigDecimal genericDecimal4) {
        this.genericDecimal4 = genericDecimal4;
    }

    public BigDecimal getGenericDecimal5() {
        return genericDecimal5;
    }

    public void setGenericDecimal5(BigDecimal genericDecimal5) {
        this.genericDecimal5 = genericDecimal5;
    }

    public BigDecimal getGenericDecimal6() {
        return genericDecimal6;
    }

    public void setGenericDecimal6(BigDecimal genericDecimal6) {
        this.genericDecimal6 = genericDecimal6;
    }

    public BigDecimal getGenericDecimal7() {
        return genericDecimal7;
    }

    public void setGenericDecimal7(BigDecimal genericDecimal7) {
        this.genericDecimal7 = genericDecimal7;
    }

    public BigDecimal getGenericDecimal8() {
        return genericDecimal8;
    }

    public void setGenericDecimal8(BigDecimal genericDecimal8) {
        this.genericDecimal8 = genericDecimal8;
    }

    public BigDecimal getGenericDecimal9() {
        return genericDecimal9;
    }

    public void setGenericDecimal9(BigDecimal genericDecimal9) {
        this.genericDecimal9 = genericDecimal9;
    }

    public BigDecimal getGenericDecimal10() {
        return genericDecimal10;
    }

    public void setGenericDecimal10(BigDecimal genericDecimal10) {
        this.genericDecimal10 = genericDecimal10;
    }

    public Date getGenericDate1() {
        return genericDate1;
    }

    public void setGenericDate1(Date genericDate1) {
        this.genericDate1 = genericDate1;
    }

    public Date getGenericDate2() {
        return genericDate2;
    }

    public void setGenericDate2(Date genericDate2) {
        this.genericDate2 = genericDate2;
    }

    public Date getGenericDate3() {
        return genericDate3;
    }

    public void setGenericDate3(Date genericDate3) {
        this.genericDate3 = genericDate3;
    }

    public Date getGenericDate4() {
        return genericDate4;
    }

    public void setGenericDate4(Date genericDate4) {
        this.genericDate4 = genericDate4;
    }

    public Date getGenericDate5() {
        return genericDate5;
    }

    public void setGenericDate5(Date genericDate5) {
        this.genericDate5 = genericDate5;
    }

    public Date getGenericDate6() {
        return genericDate6;
    }

    public void setGenericDate6(Date genericDate6) {
        this.genericDate6 = genericDate6;
    }

    public Date getGenericDate7() {
        return genericDate7;
    }

    public void setGenericDate7(Date genericDate7) {
        this.genericDate7 = genericDate7;
    }

    public Date getGenericDate8() {
        return genericDate8;
    }

    public void setGenericDate8(Date genericDate8) {
        this.genericDate8 = genericDate8;
    }

    public Date getGenericDate9() {
        return genericDate9;
    }

    public void setGenericDate9(Date genericDate9) {
        this.genericDate9 = genericDate9;
    }

    public Date getGenericDate10() {
        return genericDate10;
    }

    public void setGenericDate10(Date genericDate10) {
        this.genericDate10 = genericDate10;
    }

    public List<ActivityDetail> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(List<ActivityDetail> activityDetails) {
        this.activityDetails = activityDetails;
    }
}