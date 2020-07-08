package com.app.badoli.auth.signup.professional;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.badoli.R;
import com.app.badoli.activities.SplashActivity;
import com.app.badoli.auth.country.CountryListActivity;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityProfessionalSignupBinding;
import com.app.badoli.utilities.LoginPre;

import java.util.Locale;

public class ProfessionalSignup extends AppCompatActivity implements View.OnClickListener {

    private static final int COUNTRY_CODE = 123;
    ActivityProfessionalSignupBinding binding;
    private String countryId;
    private String phoneCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_professional_signup);
        viewUpdate();
        init();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void viewUpdate() {
        binding.tvCountryCode.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);
        String selectedLan = LoginPre.getActiveInstance(ProfessionalSignup.this).getLocaleLangua();
        if (selectedLan.equalsIgnoreCase("Fr (French)")) {
            binding.autoLang.setText("Fr");
        } else {
            binding.autoLang.setText("En");
        }
        String[] height=getResources().getStringArray(R.array.select_lang);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(ProfessionalSignup.this,R.layout.spinner_layout,R.id.spinner_text, height);
        binding.autoLang.setAdapter(adapter);
        binding.autoLang.setThreshold(1);
        binding.autoLang.setOnFocusChangeListener((v15, hasFocus) -> {
            if (hasFocus) {
                binding.autoLang.showDropDown();
            }
        });
        binding.autoLang.setOnTouchListener((paramView, paramMotionEvent) -> {
            // TODO Auto-generated method stub
            binding.autoLang.showDropDown();
            binding.autoLang.requestFocus();
            return false;
        });
        binding.autoLang.setOnItemClickListener((parent, view, position, id) -> {
            String lang =parent.getItemAtPosition(position).toString();
            LoginPre.getActiveInstance(ProfessionalSignup.this).setLocaleLangua(lang);
            if (lang.equalsIgnoreCase("Fr (French)")){
                setLocale("fr");
            }else {
                setLocale("en");
            }
        });
    }

    private void init() {
        switch (LoginPre.getActiveInstance(ProfessionalSignup.this).getLocaleLangua()) {
            case "en":
                setLocale("en");
                break;
            case "fr":
                setLocale("fr");
                break;
        }
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        // Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
        //  restartActivity();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_professional_signup);
        viewUpdate();
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onClick(View v) {
        if (v==binding.tvCountryCode){
            Intent intent = new Intent(ProfessionalSignup.this, CountryListActivity.class);
            startActivityForResult(intent,COUNTRY_CODE);
        }
        if (v==binding.txtLogin){
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COUNTRY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                if (data != null) {
                    String id=data.getStringExtra(Constant.COUNTRY_ID);
                    String code=data.getStringExtra(Constant.PHONE_CODE);
                    countryId=id;
                    phoneCode=code;
                    SplashActivity.savePreferences(Constant.PHONE_CODE,code);
                    SplashActivity.savePreferences(Constant.COUNTRY_ID,id);
                    binding.tvCountryCode.setText("+"+phoneCode);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}