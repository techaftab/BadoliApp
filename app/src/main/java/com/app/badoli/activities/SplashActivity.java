package com.app.badoli.activities;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.utilities.LoginPre;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    int SPLASH_SCREEN_TIME_IN_MILLIS = 3000;
    Thread thread;
    static SharedPreferences sharedPreferences;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        if (!TextUtils.isEmpty(LoginPre.getActiveInstance(SplashActivity.this).getLocaleLangua())) {
            setLocale(LoginPre.getActiveInstance(SplashActivity.this).getLocaleLangua());
            Log.e("language11", LoginPre.getActiveInstance(SplashActivity.this).getLocaleLangua());
        }
       // if (Build.VERSION.SDK_INT >= M) {
            makesplash();
    //    }

    }
    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void makesplash() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_SCREEN_TIME_IN_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (LoginPre.getActiveInstance(SplashActivity.this).getIsLoggedIn()) {
                        Intent i = new Intent(SplashActivity.this, HomePageActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public static void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPreferences(String key, String val) {
        return sharedPreferences.getString(key, val);
    }
}
