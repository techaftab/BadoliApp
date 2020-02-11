package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpResponse {

    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("result")
    @Expose
    public VerifyOtpResult result;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VerifyOtpResult getResult() {
        return result;
    }

    public void setResult(VerifyOtpResult result) {
        this.result = result;
    }

    public class VerifyOtpResult {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("user_image")
        @Expose
        public String user_image;

        @SerializedName("qrcode_image")
        @Expose
        public String qrcode_image;
        @SerializedName("email_verified_at")
        @Expose
        public Object emailVerifiedAt;
        @SerializedName("txtpassword")
        @Expose
        public String txtpassword;
        @SerializedName("country_id")
        @Expose
        public Integer countryId;
        @SerializedName("country_code")
        @Expose
        public Integer countryCode;
        @SerializedName("state_id")
        @Expose
        public Integer stateId;
        @SerializedName("city_id")
        @Expose
        public Integer cityId;
        @SerializedName("roles_id")
        @Expose
        public Integer rolesId;
        @SerializedName("wallet_balance")
        @Expose
        public String walletBalance;

        @SerializedName("device_type")
        @Expose
        public Integer deviceType;
        @SerializedName("device_token")
        @Expose
        public String deviceToken;
        @SerializedName("email_otp_token")
        @Expose
        public Object emailOtpToken;
        @SerializedName("mobile_otp_token")
        @Expose
        public String mobileOtpToken;
        @SerializedName("email_verify")
        @Expose
        public Integer emailVerify;
        @SerializedName("mobile_verify")
        @Expose
        public String mobileVerify;
        @SerializedName("agree_terms")
        @Expose
        public Integer agreeTerms;
        @SerializedName("auth_token")
        @Expose
        public String authToken;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("last_login")
        @Expose
        public Object lastLogin;
        @SerializedName("record_created_by")
        @Expose
        public Object recordCreatedBy;
        @SerializedName("record_updated_by")
        @Expose
        public Object recordUpdatedBy;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("roles")
        @Expose
        public VerfyOtpRoles roles;
        @SerializedName("city")
        @Expose
        public Object city;
        @SerializedName("state")
        @Expose
        public Object state;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public String getTxtpassword() {
            return txtpassword;
        }

        public void setTxtpassword(String txtpassword) {
            this.txtpassword = txtpassword;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public Integer getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(Integer countryCode) {
            this.countryCode = countryCode;
        }

        public Integer getStateId() {
            return stateId;
        }

        public void setStateId(Integer stateId) {
            this.stateId = stateId;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public Integer getRolesId() {
            return rolesId;
        }

        public void setRolesId(Integer rolesId) {
            this.rolesId = rolesId;
        }

        public String getWalletBalance() {
            return walletBalance;
        }

        public void setWalletBalance(String walletBalance) {
            this.walletBalance = walletBalance;
        }

        public Integer getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(Integer deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public Object getEmailOtpToken() {
            return emailOtpToken;
        }

        public void setEmailOtpToken(Object emailOtpToken) {
            this.emailOtpToken = emailOtpToken;
        }

        public String getMobileOtpToken() {
            return mobileOtpToken;
        }

        public void setMobileOtpToken(String mobileOtpToken) {
            this.mobileOtpToken = mobileOtpToken;
        }

        public Integer getEmailVerify() {
            return emailVerify;
        }

        public void setEmailVerify(Integer emailVerify) {
            this.emailVerify = emailVerify;
        }

        public String getMobileVerify() {
            return mobileVerify;
        }

        public void setMobileVerify(String mobileVerify) {
            this.mobileVerify = mobileVerify;
        }

        public Integer getAgreeTerms() {
            return agreeTerms;
        }

        public void setAgreeTerms(Integer agreeTerms) {
            this.agreeTerms = agreeTerms;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Object getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(Object lastLogin) {
            this.lastLogin = lastLogin;
        }

        public Object getRecordCreatedBy() {
            return recordCreatedBy;
        }

        public void setRecordCreatedBy(Object recordCreatedBy) {
            this.recordCreatedBy = recordCreatedBy;
        }

        public Object getRecordUpdatedBy() {
            return recordUpdatedBy;
        }

        public void setRecordUpdatedBy(Object recordUpdatedBy) {
            this.recordUpdatedBy = recordUpdatedBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public VerfyOtpRoles getRoles() {
            return roles;
        }

        public void setRoles(VerfyOtpRoles roles) {
            this.roles = roles;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getState() {
            return state;
        }

        public void setState(Object state) {
            this.state = state;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getQrcode_image() {
            return qrcode_image;
        }

        public void setQrcode_image(String qrcode_image) {
            this.qrcode_image = qrcode_image;
        }
    }
}
