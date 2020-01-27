package com.webmobril.badoli.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.webmobril.badoli.model.UserData;


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

    private static PrefManager mInstance;
    private static Context mCtx;
    private SharedPreferences prefs;
    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";
    SharedPreferences pref;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences.Editor editor;
    private String KEY_IS_LOGGEDIN="isLoggedIn";


    public PrefManager(Context context) {
        mCtx = context;
      //  prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();
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
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
        editor.apply();
    }


    public UserData getUserData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserData(
                sharedPreferences.getString(KEY_USERID, null),
                sharedPreferences.getString(KEY_TOKEN,null),
                sharedPreferences.getString(KEY_COUNTRY_CODE,null),
                sharedPreferences.getString(KEY_COUNTRY_ID,null),
                sharedPreferences.getString(KEY_DEVICE_TOKEN,null),
                sharedPreferences.getString(KEY_EMAIL,null),
                sharedPreferences.getString(KEY_MOBILE,null),
                sharedPreferences.getString(KEY_NAME,null),
                sharedPreferences.getString(KEY_WALLET_BALANCE,null),
                sharedPreferences.getString(KEY_USER_IMAGE,null),
                sharedPreferences.getString(KEY_QRCODE_IMAGE,null));
    }


    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        // mCtx.startActivity(new Intent(mCtx, SplashMainActivity.class));
    }
    public void saveNotificationSubscription(boolean value){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putBoolean(SET_NOTIFY, value);
        edits.apply();
    }
    public boolean hasUserSubscribeToNotification(){
        return prefs.getBoolean(SET_NOTIFY, false);
    }
    public void clearAllSubscriptions(){
        prefs.edit().clear().apply();
    }

    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

}
