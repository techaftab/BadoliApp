package com.webmobril.badoli.model;

import java.util.List;

public class ResendOtpResponse {

    private String error;

    private List<String> message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
