package com.webmobril.badoli.model;

import java.util.List;

public class ResendOtpResponse {

    public Boolean error;

    public String message;

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
