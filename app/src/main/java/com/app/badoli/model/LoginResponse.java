package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("mobile_verify")
    @Expose
    public int mobile_verify;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("otp")
    @Expose
    public String otp;

    @SerializedName("result")
    @Expose
    public LoginResult result;


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

    public int getMobile_verify() {
        return mobile_verify;
    }

    public void setMobile_verify(int mobile_verify) {
        this.mobile_verify = mobile_verify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LoginResult getResult() {
        return result;
    }

    public void setResult(LoginResult result) {
        this.result = result;
    }

    public class LoginResult {

        @SerializedName("id")
        @Expose
        public Integer id;

        @SerializedName("token")
        @Expose
        public String token;

        @SerializedName("user")
        @Expose
        public UserData user;

        public UserData getUser() {
            return user;
        }

        public void setUser(UserData user) {
            this.user = user;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
