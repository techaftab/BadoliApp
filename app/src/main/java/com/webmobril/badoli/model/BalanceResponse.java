package com.webmobril.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BalanceResponse {

    public BalanceResponse() {
    }
    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("result")
    @Expose
    public String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
