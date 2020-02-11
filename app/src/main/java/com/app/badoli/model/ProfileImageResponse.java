package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImageResponse {
    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("result")
    @Expose
    public VerifyOtpResponse.VerifyOtpResult result;

    public VerifyOtpResponse.VerifyOtpResult getResult() {
        return result;
    }

    public void setResult(VerifyOtpResponse.VerifyOtpResult result) {
        this.result = result;
    }

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

}
