package com.app.badoli.model;

public class Country_model {

    String phoneCode;
    String countryName;

    public Country_model(String phoneCode, String countryName) {
        this.phoneCode = phoneCode;
        this.countryName = countryName;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
