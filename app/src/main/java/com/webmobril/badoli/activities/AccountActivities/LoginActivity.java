package com.webmobril.badoli.activities.AccountActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.activities.SplashActivity;
import com.webmobril.badoli.config.Configuration;
import com.webmobril.badoli.config.Constant;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityLoginBinding;
import com.webmobril.badoli.fragments.FragmentFgtPwd;
import com.webmobril.badoli.model.UserData;
import com.webmobril.badoli.utilities.LoginPre;
import com.webmobril.badoli.viewModels.LoginViewModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    LoginViewModel loginViewModel;
    ActivityLoginBinding loginBinding;
    String phone, password,device_token;
    Fragment currentFragment;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        /*Everything is fine*/
        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""))) {
            loginBinding.edPhone.setText(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""));
            loginBinding.edPassword.setText(SplashActivity.getPreferences(Constant.REMEMBER_PASSWORD,""));
            loginBinding.rememberMe.setChecked(true);
        }

        loginBinding.loginButton.setOnClickListener(v -> {
            phone = Objects.requireNonNull(loginBinding.edPhone.getText()).toString();
            password = Objects.requireNonNull(loginBinding.edPassword.getText()).toString();
            if (setValidation(phone, password)) {
                getLoginResponse(phone, password);
            }
            if (loginBinding.rememberMe.isChecked()) {
                SplashActivity.savePreferences(Constant.REMEMBER_PHONE,phone);
                SplashActivity.savePreferences(Constant.REMEMBER_PASSWORD,password);
            } else {
                SplashActivity.savePreferences(Constant.REMEMBER_PHONE,"");
                SplashActivity.savePreferences(Constant.REMEMBER_PASSWORD,"");
            }
        });



        loginBinding.forgetPassword.setOnClickListener(this);
        loginBinding.txtSignUp.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""))) {
            loginBinding.edPhone.setText(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""));
            loginBinding.edPassword.setText(SplashActivity.getPreferences(Constant.REMEMBER_PASSWORD,""));
            loginBinding.rememberMe.setChecked(true);
        }
    }

    void showLoading(){
        Configuration.hideKeyboardFrom(LoginActivity.this);
        loginBinding.loginProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    void dismissLoading(){
        loginBinding.loginProgressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void init() {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        //String uniqueID = UUID.randomUUID().toString();
        Log.e(TAG,"DEVICE_ID--->"+android_id);
        MessageDigest md;
       // device_token;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(android_id.getBytes());
            device_token = new String(Base64.encode(md.digest(), 0));
            // String key = new String(Base64.encodeBytes(md.digest()));
            Log.e("Key Hash=", device_token);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private boolean setValidation(String phone, String password) {
        if (phone.isEmpty()) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.ed_phone), Toast.LENGTH_LONG).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.ed_password), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void getLoginResponse(String phone, String password) {
        showLoading();
        loginViewModel.getLogin(phone, password, 1, device_token).observe(this, loginResponse -> {
            dismissLoading();
            if (!loginResponse.error) {
                UserData userData = new UserData(
                        loginResponse.result.user.getId(),
                        loginResponse.result.user.getAuth_token(),
                        loginResponse.result.user.getCountry_code(),
                        loginResponse.result.user.getCountry_id(),
                        loginResponse.result.user.getDevice_token(),
                        loginResponse.result.user.getEmail(),
                        loginResponse.result.user.getMobile(),
                        loginResponse.result.user.getName(),
                        loginResponse.result.user.getWallet_balance(),
                        loginResponse.result.user.getUser_image(),
                        loginResponse.result.user.getQrcode_image());
                PrefManager.getInstance(LoginActivity.this).userLogin(userData);
               // PrefManager.getInstance(LoginActivity.this).setLogin(true);
                Toast.makeText(getApplicationContext(), loginResponse.message, Toast.LENGTH_SHORT).show();
                LoginPre.getActiveInstance(LoginActivity.this).setIsLoggedIn(true);
                StartActivity();
            } else {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void StartActivity() {
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v==loginBinding.forgetPassword){
            loginBinding.framelayoutLogin.setVisibility(View.VISIBLE);
            loginBinding.scrollviewLogin.setVisibility(View.GONE);
            ft = getSupportFragmentManager().beginTransaction();
            currentFragment = new FragmentFgtPwd();
            ft.replace(R.id.framelayout_login, currentFragment);
            ft.commit();
        }
        if (v==loginBinding.txtSignUp){
            Context context = getApplicationContext();
            Intent intent = new Intent(context, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (loginBinding.framelayoutLogin.isShown()){
            loginBinding.framelayoutLogin.setVisibility(View.GONE);
            loginBinding.scrollviewLogin.setVisibility(View.VISIBLE);
        }else {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                        Intent launchNextActivity = new Intent(Intent.ACTION_MAIN);
                        launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                        launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchNextActivity);
                        finish();
                    }).create().show();
        }
    }

    public void updateView() {
        loginBinding.framelayoutLogin.setVisibility(View.GONE);
        loginBinding.scrollviewLogin.setVisibility(View.VISIBLE);
    }
}
