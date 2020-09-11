package com.app.badoli.auth.login;

import android.annotation.SuppressLint;
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

import com.app.badoli.R;
import com.app.badoli.auth.signup.user.SignUpActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.databinding.ActivityLoginBinding;
import com.app.badoli.utilities.LoginPre;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewUpdate();
        init();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void viewUpdate() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.txtSignUp.setOnClickListener(this);
        binding.radioGroup.setOnCheckedChangeListener(this);
        loadFragment(new UserLoginFragment(),R.anim.left_in,R.anim.right_out);
        binding.rbParticular.setChecked(true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            LoginPre.getActiveInstance(LoginActivity.this).setDevice_token(deviceToken);
        });

        String selectedLan = LoginPre.getActiveInstance(LoginActivity.this).getLocaleLangua();
        if (selectedLan.equalsIgnoreCase("Fr (French)")) {
            binding.autoLang.setText("Fr");
        } else {
            binding.autoLang.setText("En");
        }

        String[] height=getResources().getStringArray(R.array.select_lang);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(LoginActivity.this,R.layout.spinner_layout,R.id.spinner_text, height);
        binding.autoLang.setAdapter(adapter);
        binding.autoLang.setThreshold(1);
        binding.autoLang.setOnFocusChangeListener((v15, hasFocus) -> {
            if (hasFocus) {
                binding.autoLang.showDropDown();
            }
        });
        binding.autoLang.setOnTouchListener((paramView, paramMotionEvent) -> {
            binding.autoLang.showDropDown();
            binding.autoLang.requestFocus();
            return false;
        });
        binding.autoLang.setOnItemClickListener((parent, view, position, id) -> {
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
        if (binding.rbParticular.isChecked()){
            binding.rbParticular.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            binding.rbProfessional.setBackground(null);
            binding.rbProfessional.setTextColor(getResources().getColor(R.color.text_orange));
            binding.rbParticular.setTextColor(getResources().getColor(R.color.white));
            loadFragment(new UserLoginFragment(),R.anim.left_in,R.anim.right_out);
            binding.lnSignup.setVisibility(View.VISIBLE);
            binding.txtSignUp.setText(getResources().getString(R.string.sign_up));
        }

        if (binding.rbProfessional.isChecked()){
            binding.rbProfessional.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            binding.rbParticular.setBackground(null);
            binding.rbParticular.setTextColor(getResources().getColor(R.color.text_orange));
            binding.rbProfessional.setTextColor(getResources().getColor(R.color.white));
            loadFragment(new ProfressionalLoginFragment(),R.anim.right_in,R.anim.left_out);
            binding.lnSignup.setVisibility(View.GONE);
           // binding.txtSignUp.setText(getResources().getString(R.string.sign_up_as_manager));

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

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(LoginActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewUpdate();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
       // Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (v==binding.txtSignUp) {
          //  if (f instanceof UserLoginFragment) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
          /*  }else {
                Intent intent = new Intent(LoginActivity.this, ProfessionalSignup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }*/
        }
    }

    @Override
    public void onBackPressed() {
      //  Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
       /* if (f instanceof LoginManager){
            loadFragment(new ProfressionalLoginFragment(),R.anim.left_in,R.anim.right_out);
        }else {*/
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
    //    }
    }

}
