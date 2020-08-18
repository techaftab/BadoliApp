package com.app.badoli.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.badoli.model.UserData;


/**
 * Created by aftab on 4/30/2018.
 */

public class PrefManager {

    private static final String SHARED_PREF_NAME = Constant.PREFS_NAME;
    private static final String KEY_USERID="keyid";
    private static final String KEY_TOKEN="keytoken";
    private static final String KEY_COUNTRY_CODE = "keycountrycode";
    private static final String KEY_COUNTRY_ID = "keycountryid";
    private static final String KEY_DEVICE_TOKEN = "keydob";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_MOBILE = "keymobile";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_WALLET_BALANCE = "keywalletbalance";
    private static final String KEY_USER_IMAGE = "keyuserimage";
    private static final String KEY_QRCODE_IMAGE = "keyqrcodeimage";
    private static final String TAG_TOKEN = Constant.TAG_TOKEN;
    private static final String PHONE_CODE = "keyphonecode";
    private static final String COUNTRY_CODE = "keycountrycode";
    private static final String PHONE = "keyphone";
    private static final String PASSWORD = "keypassword";
    private static final String KEY_USER_TYPE = "keyusertype";

    @SuppressLint("StaticFieldLeak")
    private static PrefManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    private SharedPreferences prefs;
   // public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    private static final String SET_NOTIFY = "set_notify";
    private SharedPreferences pref;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private SharedPreferences.Editor editor;
    private String KEY_IS_LOGGEDIN="isLoggedIn";


    public PrefManager(Context context) {
        mCtx = context;
        pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLogin(boolean isLoggedIn) {
        editor=pref.edit();
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(UserData userData) {
        editor=pref.edit();
        editor.putString(KEY_USERID,userData.getId());
        editor.putString(KEY_TOKEN, userData.getAuth_token());
        editor.putString(KEY_COUNTRY_CODE, userData.getCountry_code());
        editor.putString(KEY_COUNTRY_ID, userData.getCountry_id());
        editor.putString(KEY_DEVICE_TOKEN,userData.getDevice_token());
        editor.putString(KEY_EMAIL, userData.getEmail());
        editor.putString(KEY_MOBILE, userData.getMobile());
        editor.putString(KEY_NAME, userData.getName());
        editor.putString(KEY_WALLET_BALANCE,userData.getWallet_balance());
        editor.putString(KEY_USER_IMAGE,userData.getUser_image());
        editor.putString(KEY_QRCODE_IMAGE,userData.getQrcode_image());
        editor.putString(KEY_USER_TYPE,userData.getUserType());
        editor.apply();
    }

    public void setWalletBalance(String balance){
        editor=pref.edit();
        editor.putString(KEY_WALLET_BALANCE,balance);
        editor.apply();
    }

    public String getWalletBalance() {
        return pref.getString(KEY_WALLET_BALANCE,"");
    }

    public UserData getUserData() {
        return new UserData(
                pref.getString(KEY_USERID, null),
                pref.getString(KEY_TOKEN,null),
                pref.getString(KEY_COUNTRY_CODE,null),
                pref.getString(KEY_COUNTRY_ID,null),
                pref.getString(KEY_DEVICE_TOKEN,null),
                pref.getString(KEY_EMAIL,null),
                pref.getString(KEY_MOBILE,null),
                pref.getString(KEY_NAME,null),
                pref.getString(KEY_WALLET_BALANCE,null),
                pref.getString(KEY_USER_IMAGE,null),
                pref.getString(KEY_QRCODE_IMAGE,null),
                pref.getString(KEY_USER_TYPE,null));
    }
    public void setPhoneCode(String phoneCode) {
        editor=pref.edit();
        editor.putString(this.PHONE_CODE,phoneCode);
        editor.apply();
    }
    public String getPhoneCode() {
        return pref.getString(PHONE_CODE,"");
    }


    public void setCountryCode(String countryCode) {
        editor=pref.edit();
        editor.putString(this.COUNTRY_CODE,countryCode);
        editor.apply();
    }

    public void setPhoneNumber(String phone) {
        editor=pref.edit();
        editor.putString(this.PHONE,phone);
        editor.apply();
    }

    public String getPHONE() {
        return pref.getString(PHONE,"");
    }

    public void setPassword(String password) {
        editor=pref.edit();
        editor.putString(this.PASSWORD,password);
        editor.apply();
    }

    public String getPassword() {
        return pref.getString(PASSWORD,"");
    }

    public String getCountryCode() {
        return pref.getString(COUNTRY_CODE,"");
    }

    public void logout() {
        editor=pref.edit();
        editor.clear();
        editor.apply();
        //mCtx.startActivity(new Intent(mCtx, SplashMainActivity.class));
    }
    public void saveNotificationSubscription(boolean value){
        editor=pref.edit();
        editor.putBoolean(SET_NOTIFY, value);
        editor.apply();
    }
    public boolean hasUserSubscribeToNotification(){
        return prefs.getBoolean(SET_NOTIFY, false);
    }
    public void clearAllSubscriptions(){
        prefs.edit().clear().apply();
    }

    public boolean saveDeviceToken(String token){
        editor=pref.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        return  pref.getString(TAG_TOKEN, null);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor=pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }
}
