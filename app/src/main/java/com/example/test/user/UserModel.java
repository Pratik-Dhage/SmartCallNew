package com.example.test.user;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;


public class UserModel {
    @SerializedName("userId")
    private String userId;
    private String userName;
    @SerializedName("password")
    private String password;
    private String mobileNumber;
    private Date dateOfBirth;
    private String nameOfMother;
    private String emailId;
    private Long roleId;
    private String roleDescription;
    private String branchCode;
    private String branchName;
    private String concurrentLoginAllowed;
    private Integer concurrentLimit;
    private Date allowedLoginTimeFrom;
    private Date allowedLoginTimeTo;
    private Integer otpCode;
    private Date validFrom;
    private Date validTo;
    private String createdBy;
    private Date createdDateTime;
    private String updatedBy;
    private Date updatedDateTime;
    private Date removeDatetime;
    private Long companyId;
    private String status;
    private String generic1;
    private String generic2;
    private String generic3;
    private String generic4;
    private String generic5;
    private String generic6;
    private String generic7;
    private String generic8;
    private String generic9;
    private String generic10;
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
    private BigDecimal genericNumber1;
    private BigDecimal genericNumber2;
    private BigDecimal genericNumber3;
    private BigDecimal genericNumber4;
    private BigDecimal genericNumber5;
    private BigDecimal genericNumber6;
    private BigDecimal genericNumber7;
    private BigDecimal genericNumber8;
    private BigDecimal genericNumber9;
    private BigDecimal genericNumber10;
    private String authenticationResult;


  /*  public UserModel(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }*/

    //Login purpose


    //Generate OTP purpose
    public UserModel(String userId) {
        this.userId = userId;
    }

    //Validate OTP
    public UserModel(String userId, Integer otpCode) {
        this.userId = userId;
        this.otpCode = otpCode;
    }

    // This Constructor is also be used for Login / Authentication Purpose
    //Changes (28/04/2023) now use userId and branchCode as parameters to get DashBoard Data
    public UserModel(String userId, String branchCode) {
        this.userId = userId;
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getNameOfMother() {
        return nameOfMother;
    }

    public void setNameOfMother(String nameOfMother) {
        this.nameOfMother = nameOfMother;
    }


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }


    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }


    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }


    public String getConcurrentLoginAllowed() {
        return concurrentLoginAllowed;
    }

    public void setConcurrentLoginAllowed(String concurrentLoginAllowed) {
        this.concurrentLoginAllowed = concurrentLoginAllowed;
    }


    public Integer getConcurrentLimit() {
        return concurrentLimit;
    }

    public void setConcurrentLimit(Integer concurrentLimit) {
        this.concurrentLimit = concurrentLimit;
    }


    public Date getAllowedLoginTimeFrom() {
        return allowedLoginTimeFrom;
    }

    public void setAllowedLoginTimeFrom(Date allowedLoginTimeFrom) {
        this.allowedLoginTimeFrom = allowedLoginTimeFrom;
    }


    public Date getAllowedLoginTimeTo() {
        return allowedLoginTimeTo;
    }

    public void setAllowedLoginTimeTo(Date allowedLoginTimeTo) {
        this.allowedLoginTimeTo = allowedLoginTimeTo;
    }


    public Integer getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(Integer otpCode) {
        this.otpCode = otpCode;
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


    public Date getRemoveDatetime() {
        return removeDatetime;
    }

    public void setRemoveDatetime(Date removeDatetime) {
        this.removeDatetime = removeDatetime;
    }


    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getGeneric1() {
        return generic1;
    }

    public void setGeneric1(String generic1) {
        this.generic1 = generic1;
    }


    public String getGeneric2() {
        return generic2;
    }

    public void setGeneric2(String generic2) {
        this.generic2 = generic2;
    }


    public String getGeneric3() {
        return generic3;
    }

    public void setGeneric3(String generic3) {
        this.generic3 = generic3;
    }


    public String getGeneric4() {
        return generic4;
    }

    public void setGeneric4(String generic4) {
        this.generic4 = generic4;
    }


    public String getGeneric5() {
        return generic5;
    }

    public void setGeneric5(String generic5) {
        this.generic5 = generic5;
    }


    public String getGeneric6() {
        return generic6;
    }

    public void setGeneric6(String generic6) {
        this.generic6 = generic6;
    }


    public String getGeneric7() {
        return generic7;
    }

    public void setGeneric7(String generic7) {
        this.generic7 = generic7;
    }


    public String getGeneric8() {
        return generic8;
    }

    public void setGeneric8(String generic8) {
        this.generic8 = generic8;
    }


    public String getGeneric9() {
        return generic9;
    }

    public void setGeneric9(String generic9) {
        this.generic9 = generic9;
    }


    public String getGeneric10() {
        return generic10;
    }

    public void setGeneric10(String generic10) {
        this.generic10 = generic10;
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


    public BigDecimal getGenericNumber1() {
        return genericNumber1;
    }

    public void setGenericNumber1(BigDecimal genericNumber1) {
        this.genericNumber1 = genericNumber1;
    }


    public BigDecimal getGenericNumber2() {
        return genericNumber2;
    }

    public void setGenericNumber2(BigDecimal genericNumber2) {
        this.genericNumber2 = genericNumber2;
    }


    public BigDecimal getGenericNumber3() {
        return genericNumber3;
    }

    public void setGenericNumber3(BigDecimal genericNumber3) {
        this.genericNumber3 = genericNumber3;
    }


    public BigDecimal getGenericNumber4() {
        return genericNumber4;
    }

    public void setGenericNumber4(BigDecimal genericNumber4) {
        this.genericNumber4 = genericNumber4;
    }


    public BigDecimal getGenericNumber5() {
        return genericNumber5;
    }

    public void setGenericNumber5(BigDecimal genericNumber5) {
        this.genericNumber5 = genericNumber5;
    }


    public BigDecimal getGenericNumber6() {
        return genericNumber6;
    }

    public void setGenericNumber6(BigDecimal genericNumber6) {
        this.genericNumber6 = genericNumber6;
    }


    public BigDecimal getGenericNumber7() {
        return genericNumber7;
    }

    public void setGenericNumber7(BigDecimal genericNumber7) {
        this.genericNumber7 = genericNumber7;
    }


    public BigDecimal getGenericNumber8() {
        return genericNumber8;
    }

    public void setGenericNumber8(BigDecimal genericNumber8) {
        this.genericNumber8 = genericNumber8;
    }


    public BigDecimal getGenericNumber9() {
        return genericNumber9;
    }

    public void setGenericNumber9(BigDecimal genericNumber9) {
        this.genericNumber9 = genericNumber9;
    }


    public BigDecimal getGenericNumber10() {
        return genericNumber10;
    }

    public void setGenericNumber10(BigDecimal genericNumber10) {
        this.genericNumber10 = genericNumber10;
    }


    public String getAuthenticationResult() {
        return authenticationResult;
    }

    public void setAuthenticationResult(String authenticationResult) {
        this.authenticationResult = authenticationResult;
    }
}
