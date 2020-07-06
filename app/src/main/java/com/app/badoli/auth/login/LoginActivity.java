package com.app.badoli.auth.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.badoli.R;
import com.app.badoli.auth.signup.SignUpActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.databinding.ActivityLoginBinding;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

  //  private static final String TAG = LoginActivity.class.getSimpleName();
    AuthViewModel authViewModel;
    ActivityLoginBinding loginBinding;
    String phone, password,device_token;
    Fragment currentFragment;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        viewUpdate();
        init();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void viewUpdate() {


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        loginBinding.txtSignUp.setOnClickListener(this);
      //  loginBinding.txtChangeLang.setOnClickListener(this);
        loginBinding.lnSignup.setOnClickListener(this);
        loginBinding.radioGroup.setOnCheckedChangeListener(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            LoginPre.getActiveInstance(LoginActivity.this).setDevice_token(deviceToken);
            device_token= LoginPre.getActiveInstance(LoginActivity.this).getDevice_token();
        });
        String selectedLan = LoginPre.getActiveInstance(LoginActivity.this).getLocaleLangua();
        if (selectedLan.equalsIgnoreCase("Fr (French)")) {
            loginBinding.autoLang.setText("Fr");
        } else {
            loginBinding.autoLang.setText("En");
        }
        String[] height=getResources().getStringArray(R.array.select_lang);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(LoginActivity.this,R.layout.spinner_layout,R.id.spinner_text, height);
        loginBinding.autoLang.setAdapter(adapter);
        loginBinding.autoLang.setThreshold(1);
        loginBinding.autoLang.setOnFocusChangeListener((v15, hasFocus) -> {
            if (hasFocus) {
                loginBinding.autoLang.showDropDown();
            }
        });
        loginBinding.autoLang.setOnTouchListener((paramView, paramMotionEvent) -> {
            // TODO Auto-generated method stub
            loginBinding.autoLang.showDropDown();
            loginBinding.autoLang.requestFocus();
            return false;
        });
        loginBinding.autoLang.setOnItemClickListener((parent, view, position, id) -> {
            String lang =parent.getItemAtPosition(position).toString();
            LoginPre.getActiveInstance(LoginActivity.this).setLocaleLangua(lang);
            if (lang.equalsIgnoreCase("Fr (French)")){
                setLocale("fr");
            }else {
                setLocale("en");
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (loginBinding.rbParticular.isChecked()){
            loginBinding.rbParticular.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            loginBinding.rbProfessional.setBackground(null);
            loginBinding.rbProfessional.setTextColor(getResources().getColor(R.color.text_orange));
            loginBinding.rbParticular.setTextColor(getResources().getColor(R.color.white));
            loadFragment(new UserLoginFragment(),R.anim.left_in,R.anim.right_out);
        }
        if (loginBinding.rbProfessional.isChecked()){
            loginBinding.rbProfessional.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            loginBinding.rbParticular.setBackground(null);
            loginBinding.rbParticular.setTextColor(getResources().getColor(R.color.text_orange));
            loginBinding.rbProfessional.setTextColor(getResources().getColor(R.color.white));
            loadFragment(new ProfressionalLoginFragment(),R.anim.right_in,R.anim.left_out);
        }
    }

    private void loadFragment(Fragment fragment, int left_in, int right_out) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(left_in,right_out)
                    .replace(R.id.framelayout, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void showLoading(){
        AppUtils.hideKeyboardFrom(LoginActivity.this);
        loginBinding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        loginBinding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        switch (LoginPre.getActiveInstance(LoginActivity.this).getLocaleLangua()) {
            case "En (English)":
                setLocale("en");
                break;
            case "Fr (French)":
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
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewUpdate();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        if (v==loginBinding.lnSignup) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(getResources().getString(R.string.really_exit))
                .setMessage(getResources().getString(R.string.are_sure_exit))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    Intent launchNextActivity = new Intent(Intent.ACTION_MAIN);
                    launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                    launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchNextActivity);
                    finish();
                }).create().show();
    }

    public void updateView() {
        loginBinding.framelayoutLogin.setVisibility(View.GONE);
        loginBinding.scrollviewLogin.setVisibility(View.VISIBLE);
    }
}
