package com.app.badoli.config;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.app.badoli.utilities.LoginPre;

import java.util.Locale;

public class MyApplication extends Application {

    private static MyApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        FontsOverride.setDefaultFont(this, "DEFAULT", "font/segoe_print.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/segoe_print.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "font/segoe_print.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "font/segoe_print.ttf");
        setlocale();
    }

    public static void setlocale() {

        String lang = "en";
        if(LoginPre.getActiveInstance(getInstance()).getLocaleLangua().equalsIgnoreCase("en")){
            lang = "en";
        }

        if(LoginPre.getActiveInstance(getInstance()).getLocaleLangua().equalsIgnoreCase("fr")){
            lang = "fr";
        }

        if(LoginPre.getActiveInstance(getInstance()).getLocaleLangua().equalsIgnoreCase("pt")){
            lang = "pt";
        }

        Locale myLocale = new Locale(lang);
        Resources res = getInstance().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static MyApplication getInstance() {
        return singleton;
    }
}
