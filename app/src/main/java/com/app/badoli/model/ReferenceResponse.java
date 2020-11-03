package com.app.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferenceResponse {

    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("user")
    public User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @SerializedName("ref")
    @Expose
    public String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public class User{
        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("mobile")
        @Expose
        public String mobile;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
