package com.webmobril.badoli.activities;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.webmobril.badoli.activities.AccountActivities.LoginActivity;
import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.utilities.LoginPre;

public class SplashActivity extends AppCompatActivity {

    int SPLASH_SCREEN_TIME_IN_MILLIS = 3000;
    Thread thread;
    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
       // if (Build.VERSION.SDK_INT >= M) {
            makesplash();
    //    }
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
