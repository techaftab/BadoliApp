package com.app.badoli.activities.NavigationActivities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.AccountActivities.LoginActivity;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityChangePasswordBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.ProfileViewModel;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = ChangePasswordActivity.class.getSimpleName();
    ActivityChangePasswordBinding changePasswordBinding;
    private ProfileViewModel profileViewModel;
    private Handler handler=new Handler();
    String lang="";
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        profileViewModel =new ViewModelProvider(this).get(ProfileViewModel.class);
        userData=PrefManager.getInstance(ChangePasswordActivity.this).getUserData();
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

    public void changePassword(View view) {
        String oldPwd=changePasswordBinding.edittextOldPassword.getText().toString();
        String newPwd=changePasswordBinding.edittextNewPassword.getText().toString();
        String confirmPwd=changePasswordBinding.edittextConfirmPassword.getText().toString();
        if (validatepassword(oldPwd,newPwd,confirmPwd)){
            resetPassword(userData.getId(),oldPwd,confirmPwd);
        }
    }

    void showLoading(){
        com.app.badoli.config.Configuration.hideKeyboardFrom(ChangePasswordActivity.this);
        changePasswordBinding.progressbarChangepwd.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    void dismissLoading(){
        changePasswordBinding.progressbarChangepwd.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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

    private boolean validatepassword(String oldPwd, String newPwd, String confirmPwd) {
        if (TextUtils.isEmpty(oldPwd)){
            changePasswordBinding.edittextOldPassword.requestFocus();
            changePasswordBinding.edittextOldPassword.setError(getResources().getString(R.string.enter_old_password));
            Toast.makeText(this, getResources().getString(R.string.enter_old_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(newPwd)){
            changePasswordBinding.edittextNewPassword.requestFocus();
            changePasswordBinding.edittextNewPassword.setError(getResources().getString(R.string.enter_new_password));
            Toast.makeText(this, getResources().getString(R.string.enter_new_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(confirmPwd)){
            changePasswordBinding.edittextConfirmPassword.requestFocus();
            changePasswordBinding.edittextConfirmPassword.setError(getResources().getString(R.string.ed_confirm_password));
            Toast.makeText(this, getResources().getString(R.string.ed_confirm_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtils.equals(newPwd,confirmPwd)){
            changePasswordBinding.edittextConfirmPassword.requestFocus();
            changePasswordBinding.edittextConfirmPassword.setError(getResources().getString(R.string.password_does_not_match));
            Toast.makeText(this, getResources().getString(R.string.password_does_not_match), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validate(String lang) {
        if (TextUtils.isEmpty(lang)||changePasswordBinding.radiogroupLanguage.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, getResources().getString(R.string.select_language), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetPassword(String userId,String oldPwd, String confirmPwd) {
        showLoading();
        profileViewModel.resetPassword(userId,oldPwd,confirmPwd,confirmPwd).observe(this, resetPassword -> {
            dismissLoading();
            if (!resetPassword.error) {
                Log.e(TAG, new Gson().toJson(resetPassword));
                Toast.makeText(this, resetPassword.getMessage(), Toast.LENGTH_SHORT).show();
                handler.postDelayed(() -> {
                    dismissLoading();
                    //PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this).edit().clear().apply();
                    PrefManager.getInstance(ChangePasswordActivity.this).logout();
                    LoginPre.getActiveInstance(ChangePasswordActivity.this).setIsLoggedIn(false);
                    Intent intent1 = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    finish();
                },300);
            } else {
                Toast.makeText(this, resetPassword.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
