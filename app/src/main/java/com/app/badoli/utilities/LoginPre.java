package com.app.badoli.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.badoli.config.Constant;

public class LoginPre {
    private static final String SHARED_PREF_NAME = Constant.PREFS_NAME;
    @SuppressLint("StaticFieldLeak")
    private static LoginPre preferences = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
 //   private Context context;
    private String isLoggedIn = "isLoggedIn";
    private String otp = "otp";
    private String signup_id = "id";
    private String access_token = "access_token";
    private String device_token= "device_token_firebase";
    private String mobile= "mobile";
    private String password= "password";
    private String access_OTp= "access_OTp";
    private String country_id= "country_id";
    private String country_code= "country_code";
    private String name= "name";
    private String email= "email";
    private String remember_name= "rememberName";
    private String remember_passs= "rememberPass";

    private String LocaleLangua = "locale_language_badoli";

    private LoginPre(Context context) {
      //  this.context = context;
        mPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //setmPreferences(PreferenceManager.SHAr(context));
       // setmPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
        editor.apply();
    }

    public String getOtp() {
        return mPreferences.getString(this.otp, "");
    }

    public void setOtp(String otp) {
        editor = mPreferences.edit();
        editor.putString(this.otp, otp);
        editor.apply();
    }

    public String getLocaleLangua() {
        return mPreferences.getString(this.LocaleLangua, "");
    }

    public void setLocaleLangua(String localeLangua) {
        editor = mPreferences.edit();
        editor.putString(this.LocaleLangua, localeLangua);
        editor.apply();
    }

    public String getDevice_token() {
        return mPreferences.getString(this.device_token, "");
    }

    public void setDevice_token(String device_token) {
        editor = mPreferences.edit();
        editor.putString(this.device_token, device_token);
        editor.apply();
    }

    public String getMobile() {
        return mPreferences.getString(this.mobile, "");
    }

    public void setMobile(String mobile) {
        editor = mPreferences.edit();
        editor.putString(this.mobile, mobile);
        editor.apply();
    }

    public String getPassword() {
        return mPreferences.getString(this.password, "");
    }

    public void setPassword(String password) {
        editor = mPreferences.edit();
        editor.putString(this.password, password);
        editor.apply();
    }

    public String getAccess_OTp() {
        return mPreferences.getString(this.access_OTp, "");
    }

    public void setAccess_OTp(String access_OTp) {
        editor = mPreferences.edit();
        editor.putString(this.access_OTp, access_OTp);
        editor.apply();
    }
    public String getAccess_token() {
        return mPreferences.getString(this.access_token, "");
    }

    public void setAccess_token(String access_token) {
        editor = mPreferences.edit();
        editor.putString(this.access_token, access_token);
        editor.apply();
    }

    public int getSignup_id() {
        return mPreferences.getInt(this.signup_id, 1);
    }

    public void setSignup_id(int signup_id) {
        editor = mPreferences.edit();
        editor.putInt(this.signup_id, signup_id);
        editor.apply();
    }

    public int getCountry_id() {
        return mPreferences.getInt(this.country_id, 1);
    }

    public void setCountry_id(int country_id) {
        editor = mPreferences.edit();
        editor.putInt(this.country_id, country_id);
        editor.apply();
    }
    public int getCountryCode() {
        return mPreferences.getInt(this.country_code, 1);
    }

    public void setCountry_code(int country_code) {
        editor = mPreferences.edit();
        editor.putInt(this.country_code, country_code);
        editor.apply();
    }

    public String getName() {
        return mPreferences.getString(this.name, "");
    }

    public void setName(String name) {
        editor = mPreferences.edit();
        editor.putString(this.name, name);
        editor.apply();
    }

    public String getEmail() {
        return mPreferences.getString(this.email, "");
    }

    public void setEmail(String email) {
        editor = mPreferences.edit();
        editor.putString(this.email, email);
        editor.apply();
    }

    public String getRemember_name() {
        return mPreferences.getString(this.remember_name, "");
    }

    public void setRemember_name(String remember_name) {
        editor = mPreferences.edit();
        editor.putString(this.remember_name, remember_name);
        editor.apply();
    }

    public String getRemember_passs() {
        return mPreferences.getString(this.remember_passs, "");
    }

    public void setRemember_passs(String remember_passs) {
        editor = mPreferences.edit();
        editor.putString(this.remember_passs, remember_passs);
        editor.apply();
    }

}
