package com.app.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.databinding.ActivityChangePasswordBinding;
import com.app.badoli.utilities.LoginPre;

import java.util.Locale;
import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    ActivityChangePasswordBinding changePasswordBinding;
    private Handler handler=new Handler();
    String lang="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        init();

    }

    private void init() {
        if (LoginPre.getActiveInstance(ChangePasswordActivity.this).getLocaleLangua().equals("fr")){
            changePasswordBinding.rbFrench.setChecked(true);
        }else {
            changePasswordBinding.rbEnglish.setChecked(true);
        }
        setSupportActionBar(changePasswordBinding.toolbarChangePassword);
      //  Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        changePasswordBinding.toolbarChangePassword.setTitle("");
        changePasswordBinding.toolbarChangePassword.setNavigationOnClickListener(v -> {
            Log.d("tag", "onClick : navigating back to back activity ");
            finish();
        });
        changePasswordBinding.radiogroupChngPwdLang.setOnCheckedChangeListener(this);
        changePasswordBinding.radiogroupLanguage.setOnCheckedChangeListener(this);
        changePasswordBinding.rbChangePass.setChecked(true);
    }

    public void changeLanguage(View view) {
        if (validate(lang)){
            setLocale(lang);
        }
    }

    private void setLocale(String lang) {
        LoginPre.getActiveInstance(ChangePasswordActivity.this).setLocaleLangua(lang);
        Locale myLocale = new Locale(lang);
        // Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(ChangePasswordActivity.this, HomePageActivity.class);
        startActivity(refresh);
    }

    private boolean validate(String lang) {
        if (TextUtils.isEmpty(lang)||changePasswordBinding.radiogroupLanguage.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Please select language", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (changePasswordBinding.rbChangePass.isChecked()){
            changePasswordBinding.rbChangePass.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            changePasswordBinding.rbLang.setBackground(null);
            //merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            changePasswordBinding.rbLang.setTextColor(getResources().getColor(R.color.text_orange));
            changePasswordBinding.rbChangePass.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                changePasswordBinding.lnChangePassword.setVisibility(View.VISIBLE);
                changePasswordBinding.lnChangeLanguage.setVisibility(View.GONE);
                //merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },0);
        }
        if (changePasswordBinding.rbLang.isChecked()){
            changePasswordBinding.rbLang.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            changePasswordBinding.rbChangePass.setBackground(null);
            //merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            changePasswordBinding.rbChangePass.setTextColor(getResources().getColor(R.color.text_orange));
            changePasswordBinding.rbLang.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                changePasswordBinding.lnChangePassword.setVisibility(View.GONE);
                changePasswordBinding.lnChangeLanguage.setVisibility(View.VISIBLE);
                //merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },0);
        }
        if (changePasswordBinding.rbEnglish.isChecked()){
            lang="en";
        }
        if (changePasswordBinding.rbFrench.isChecked()){
            lang="fr";
        }
    }
}
