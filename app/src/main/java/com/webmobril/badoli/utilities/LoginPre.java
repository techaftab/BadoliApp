package com.webmobril.badoli.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginPre {
    private static LoginPre preferences = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private String isLoggedIn = "isLoggedIn";
    private String otp = "otp";
    private String signup_id = "id";
    private String access_token = "access_token";
    private String device_token= "device_token";
    private String mobile= "mobile";
    private String password= "password";
    private String access_OTp= "access_OTp";
    private String country_id= "country_id";
    private String country_code= "country_code";
    private String name= "name";
    private String email= "email";
    private String remember_name= "rememberName";
    private String remember_passs= "rememberPass";

    private LoginPre(Context context) {
        this.context = context;
        setmPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public void setmPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }

    public static LoginPre getActiveInstance(Context context) {
        if (preferences == null) {
            preferences = new LoginPre(context);
        }
        return preferences;
    }

    public boolean getIsLoggedIn() {
        return mPreferences.getBoolean(this.isLoggedIn, false);
    }

    public void setIsLoggedIn(boolean isLoggedin) {
        editor = mPreferences.edit();
        editor.putBoolean(this.isLoggedIn, isLoggedin);
        editor.commit();
    }

    public String getOtp() {
        return mPreferences.getString(this.otp, "");
    }

    public void setOtp(String otp) {
        editor = mPreferences.edit();
        editor.putString(this.otp, otp);
        editor.commit();
    }

    public String getDevice_token() {
        return mPreferences.getString(this.device_token, "");
    }

    public void setDevice_token(String device_token) {
        editor = mPreferences.edit();
        editor.putString(this.device_token, device_token);
        editor.commit();
    }

    public String getMobile() {
        return mPreferences.getString(this.mobile, "");
    }

    public void setMobile(String mobile) {
        editor = mPreferences.edit();
        editor.putString(this.mobile, mobile);
        editor.commit();
    }

    public String getPassword() {
        return mPreferences.getString(this.password, "");
    }

    public void setPassword(String password) {
        editor = mPreferences.edit();
        editor.putString(this.password, password);
        editor.commit();
    }

    public String getAccess_OTp() {
        return mPreferences.getString(this.access_OTp, "");
    }

    public void setAccess_OTp(String access_OTp) {
        editor = mPreferences.edit();
        editor.putString(this.access_OTp, access_OTp);
        editor.commit();
    }
    public String getAccess_token() {
        return mPreferences.getString(this.access_token, "");
    }

    public void setAccess_token(String access_token) {
        editor = mPreferences.edit();
        editor.putString(this.access_token, access_token);
        editor.commit();
    }

    public int getSignup_id() {
        return mPreferences.getInt(this.signup_id, 1);
    }

    public void setSignup_id(int signup_id) {
        editor = mPreferences.edit();
        editor.putInt(this.signup_id, signup_id);
        editor.commit();
    }

    public int getCountry_id() {
        return mPreferences.getInt(this.country_id, 1);
    }

    public void setCountry_id(int country_id) {
        editor = mPreferences.edit();
        editor.putInt(this.country_id, country_id);
        editor.commit();
    }
    public int getCountryCode() {
        return mPreferences.getInt(this.country_code, 1);
    }

    public void setCountry_code(int country_code) {
        editor = mPreferences.edit();
        editor.putInt(this.country_code, country_code);
        editor.commit();
    }

    public String getName() {
        return mPreferences.getString(this.name, "");
    }

    public void setName(String name) {
        editor = mPreferences.edit();
        editor.putString(this.name, name);
        editor.commit();
    }

    public String getEmail() {
        return mPreferences.getString(this.email, "");
    }

    public void setEmail(String email) {
        editor = mPreferences.edit();
        editor.putString(this.email, email);
        editor.commit();
    }

    public String getRemember_name() {
        return mPreferences.getString(this.remember_name, "");
    }

    public void setRemember_name(String remember_name) {
        editor = mPreferences.edit();
        editor.putString(this.remember_name, remember_name);
        editor.commit();
    }

    public String getRemember_passs() {
        return mPreferences.getString(this.remember_passs, "");
    }

    public void setRemember_passs(String remember_passs) {
        editor = mPreferences.edit();
        editor.putString(this.remember_passs, remember_passs);
        editor.commit();
    }

}
