package com.webmobril.badoli.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("id")
    @Expose
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    @SerializedName("auth_token")
    @Expose
    public String auth_token;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("country_id")
    @Expose
    public String country_id;

    @SerializedName("country_code")
    @Expose
    public String country_code;

    @SerializedName("wallet_balance")
    @Expose
    public String wallet_balance;

    @SerializedName("device_token")
    @Expose
    public String device_token;

    @SerializedName("user_image")
    @Expose
    public String user_image;

    @SerializedName("qrcode_image")
    @Expose
    public String qrcode_image;

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

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public UserData(String id, String auth_token, String country_code, String country_id,
                    String device_token, String email, String mobile,String name,String wallet_balance,String user_imsage,String qrcode_image) {
        this.id = id;
        this.auth_token = auth_token;
        this.country_code = country_code;
        this.country_id = country_id;
        this.device_token = device_token;
        this.mobile = mobile;
        this.email = email;
        this.name = name;
        this.wallet_balance=wallet_balance;
        this.user_image=user_imsage;
        this.qrcode_image=qrcode_image;
    }
}
