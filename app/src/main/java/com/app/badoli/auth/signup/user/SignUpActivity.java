package com.app.badoli.auth.signup.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.auth.country.CountryListActivity;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.auth.otp.VerifyOtpActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivitySignUpBinding;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.utilities.Validation;
import com.app.badoli.viewModels.SignUpViewModel;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int COUNTRY_CODE = 123;
    @SuppressLint("StaticFieldLeak")
    ActivitySignUpBinding binding;
    String name, phone, password, confirm_password, email;
    SignUpViewModel signUpViewModel;
    String otp;
    boolean checked;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;

    String device_token;
    String countryId="",phoneCode="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        viewUpdate();
        init();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void viewUpdate() {
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        activity=SignUpActivity.this;

        if (!TextUtils.isEmpty(PrefManager.getInstance(SignUpActivity.this).getPhoneCode())) {
            Log.e("ksjd", "-" + PrefManager.getInstance(SignUpActivity.this).getPhoneCode());
            binding.tvCountryCode.setText("+"+PrefManager.getInstance(SignUpActivity.this).getPhoneCode());
            countryId=PrefManager.getInstance(SignUpActivity.this).getCountryCode();
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            LoginPre.getActiveInstance(SignUpActivity.this).setDevice_token(deviceToken);
            device_token= LoginPre.getActiveInstance(SignUpActivity.this).getDevice_token();
        });

        binding.signupButton.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);

        binding.tvCountryCode.setOnClickListener(this);

        String selectedLan = LoginPre.getActiveInstance(SignUpActivity.this).getLocaleLangua();
        if (selectedLan.equalsIgnoreCase("Fr (French)")) {
            binding.autoLang.setText("Fr");
        } else {
            binding.autoLang.setText("En");
        }
        String[] height=getResources().getStringArray(R.array.select_lang);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(SignUpActivity.this,R.layout.spinner_layout,R.id.spinner_text, height);
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
            LoginPre.getActiveInstance(SignUpActivity.this).setLocaleLangua(lang);
            if (lang.equalsIgnoreCase("Fr (French)")){
                setLocale("fr");
            }else {
                setLocale("en");
            }
        });
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(SignUpActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    @Override
    public void onClick(View v) {
        if (v==binding.tvCountryCode){
            Intent intent = new Intent(SignUpActivity.this, CountryListActivity.class);
            startActivityForResult(intent,COUNTRY_CODE);
        }

        if (v==binding.signupButton){
            name = binding.edFullName.getText().toString();
            phone = binding.edPhoneNo.getText().toString();
            email = binding.edEmailId.getText().toString();
            password = binding.edPassword.getText().toString();
            confirm_password = binding.edConfirmPassword.getText().toString();
            checked = binding.checReadAgreements.isChecked();

            if (setValidation(name, phone, email, password, confirm_password,countryId)) {
                AppUtils.hideKeyboardFrom(SignUpActivity.this);
                getSignupResponse();
            }

        }
        if (v==binding.txtLogin){
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
            finish();
        }
    }

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        viewUpdate();
        super.onConfigurationChanged(newConfig);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        switch (LoginPre.getActiveInstance(SignUpActivity.this).getLocaleLangua()) {
            case "en":
                setLocale("en");
                break;
            case "fr":
                setLocale("fr");
                break;
        }
    }



    private void getSignupResponse() {
        device_token=LoginPre.getActiveInstance(SignUpActivity.this).getDevice_token();
        showLoading(getResources().getString(R.string.signing_in));
        signUpViewModel.getSignUp(name, email, phone, password, Constant.DEVICE_TYPE, device_token, confirm_password,
                Integer.parseInt(countryId), 1, "3")
                .observe(this,
                signupResult -> {
                    dismissLoading();
                    if (signupResult!=null&&!signupResult.getError()) {
                       //     Toast.makeText(SignUpActivity.this, signupResult.getMessage(), Toast.LENGTH_SHORT).show();
                        int id = signupResult.getResult().getId();
                        otp = signupResult.getResult().getOtp();
                      //  String token = signupResult.result.getToken();
                        LoginPre.getActiveInstance(SignUpActivity.this).setSignup_id(String.valueOf(id));
                       // LoginPre.getActiveInstance(SignUpActivity.this).setAccess_token(token);
                        Intent intent = new Intent(SignUpActivity.this, VerifyOtpActivity.class);
                        intent.putExtra(Constant.VERIFY_OTP,otp);
                        intent.putExtra(Constant.MOBILE,phone);
                        intent.putExtra(Constant.ROLES_ID,"3");
                        startActivity(intent);
                    } else {
                        if (signupResult!=null&&signupResult.getMessage() != null){
                            AppUtils.openPopup(SignUpActivity.this,R.style.Dialod_UpDown,"error",signupResult.getMessage());
                        }else {
                            AppUtils.openPopup(SignUpActivity.this,R.style.Dialod_UpDown,"error",getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(PrefManager.getInstance(SignUpActivity.this).getPhoneCode())) {
            binding.tvCountryCode.setText("+".concat(PrefManager.getInstance(SignUpActivity.this).getPhoneCode()));
        }
    }

    private boolean setValidation(String name, String phone, String email, String password, String confirm_password, String countryId) {
        if (name.isEmpty()) {
            binding.edFullName.requestFocus();
            binding.edFullName.setError(getResources().getString(R.string.ed_fullName));
            AppUtils.showSnackbar(getString(R.string.name_field_empty), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(countryId)){
            binding.edPhoneNo.requestFocus();
            binding.edPhoneNo.setError(getResources().getString(R.string.country_no_empty));
            AppUtils.showSnackbar(getString(R.string.country_no_empty), binding.parentLayout);
            return false;
        }
        if (phone.isEmpty()) {
            binding.edPhoneNo.requestFocus();
            binding.edPhoneNo.setError(getResources().getString(R.string.ed_phone));
            AppUtils.showSnackbar(getString(R.string.phone_no_empty), binding.parentLayout);
            return false;
        } else if (phone.length() < 7 || phone.length() > 15) {
            binding.edPhoneNo.requestFocus();
            binding.edPhoneNo.setError(getResources().getString(R.string.phone_lenght));
            AppUtils.showSnackbar(getString(R.string.Phone_no_length), binding.parentLayout);
            return false;
        } else if (email.isEmpty()) {
            binding.edEmailId.requestFocus();
            binding.edEmailId.setError(getResources().getString(R.string.ed_email));
            AppUtils.showSnackbar(getString(R.string.email_empty), binding.parentLayout);
            return false;
        } else if (!Validation.isValidEmaillId(email)) {
            binding.edEmailId.requestFocus();
            binding.edEmailId.setError(getResources().getString(R.string.invalid_email));
            AppUtils.showSnackbar(getString(R.string.enter_valid_email), binding.parentLayout);
            return false;
        } else if (password.isEmpty()) {
            binding.edPassword.requestFocus();
            binding.edPassword.setError(getResources().getString(R.string.enter_new_password));
            AppUtils.showSnackbar(getString(R.string.new_password_empty), binding.parentLayout);
            return false;
        } else if (password.length() != 4) {
            binding.edPassword.requestFocus();
            binding.edPassword.setError(getResources().getString(R.string.password_length));
            AppUtils.showSnackbar(getString(R.string.invalid_password), binding.parentLayout);
            return false;
        } else if (confirm_password.isEmpty()) {
            binding.edConfirmPassword.requestFocus();
            binding.edConfirmPassword.setError(getResources().getString(R.string.ed_confirm_password));
            AppUtils.showSnackbar(getString(R.string.confirm_password_empty), binding.parentLayout);
            return false;
        } else if (!password.equals(confirm_password)) {
            AppUtils.showSnackbar(getString(R.string.password_does_not_match), binding.parentLayout);
            return false;
        } else if (!binding.checReadAgreements.isChecked()) {
            AppUtils.showSnackbar(getString(R.string.check_term), binding.parentLayout);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
        finish();
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
                    binding.tvCountryCode.setText("+"+phoneCode);
                }
            }
        }
    }
}
