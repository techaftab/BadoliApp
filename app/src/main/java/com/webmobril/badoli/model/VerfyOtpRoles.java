package com.webmobril.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class VerfyOtpRoles {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("role_text")
    @Expose
    public String roleText;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
}
