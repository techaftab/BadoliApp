package com.app.badoli.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.PreferenceManager;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivitySplashBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.staff.StaffHomeActivity;
import com.app.badoli.utilities.LoginPre;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    int SPLASH_SCREEN_TIME_IN_MILLIS = 3000;
    Thread thread;
    Locale myLocale;
    private ActivitySplashBinding binding;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        userData = PrefManager.getInstance(SplashActivity.this).getUserData();
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
                        if (LoginPre.getActiveInstance(SplashActivity.this).getLoginType().equalsIgnoreCase("3")) {
                            Intent i = new Intent(SplashActivity.this, HomePageActivity.class);
                            startActivity(i);
                            finish();
                        }else if (LoginPre.getActiveInstance(SplashActivity.this).getLoginType().equals("5")){
                            Intent i = new Intent(SplashActivity.this, StaffHomeActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Intent i = new Intent(SplashActivity.this, ProfessionalActivity.class);
                            startActivity(i);
                            finish();
                        }
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
}
