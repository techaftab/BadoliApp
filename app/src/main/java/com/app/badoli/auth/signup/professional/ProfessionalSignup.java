package com.app.badoli.auth.signup.professional;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.LocationActivity;
import com.app.badoli.auth.country.CountryListActivity;
import com.app.badoli.auth.otp.VerifyOtpActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityProfessionalSignupBinding;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;
import com.app.badoli.viewModels.SignUpViewModel;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Locale;

public class ProfessionalSignup extends AppCompatActivity implements View.OnClickListener {

    private static final int COUNTRY_CODE = 123;
    private static final int GET_LOCATION = 258;
    private static final String TAG = ProfessionalSignup.class.getSimpleName();
    ActivityProfessionalSignupBinding binding;
    private String countryId;
    private String phoneCode;
    private AuthViewModel authViewModel;

    ArrayList<String> activityName = new ArrayList<>();
    ArrayList<String> activityId = new ArrayList<>();
    String businessId = "";
    private String device_token;
    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_professional_signup);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        viewUpdate();
        init();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void viewUpdate() {
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        countryId = "79";
        phoneCode = "241";
        binding.tvCountryCode.setText("+" + 241);
      /*  if (!TextUtils.isEmpty(PrefManager.getInstance(ProfessionalSignup.this).getPhoneCode())) {
            Log.e("ksjd", "-" + PrefManager.getInstance(ProfessionalSignup.this).getPhoneCode());
            binding.tvCountryCode.setText("+"+PrefManager.getInstance(ProfessionalSignup.this).getPhoneCode());
            countryId=PrefManager.getInstance(ProfessionalSignup.this).getCountryCode();
            phoneCode=PrefManager.getInstance(ProfessionalSignup.this).getPhoneCode();
        }*/
        //   binding.tvCountryCode.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);
        binding.imgLocation.setOnClickListener(this);
        String selectedLan = LoginPre.getActiveInstance(ProfessionalSignup.this).getLocaleLangua();
        if (selectedLan.equalsIgnoreCase("Fr (French)")) {
            binding.autoLang.setText("Fr");
        } else {
            binding.autoLang.setText("En");
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            LoginPre.getActiveInstance(ProfessionalSignup.this).setDevice_token(deviceToken);
            device_token = LoginPre.getActiveInstance(ProfessionalSignup.this).getDevice_token();
        });
        String[] height = getResources().getStringArray(R.array.select_lang);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfessionalSignup.this, R.layout.spinner_layout, R.id.spinner_text, height);
        binding.autoLang.setAdapter(adapter);
        binding.autoLang.setThreshold(1);
        binding.autoLang.setOnFocusChangeListener((v15, hasFocus) -> {
            if (hasFocus) {
                binding.autoLang.requestFocus();
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
            String lang = parent.getItemAtPosition(position).toString();
            LoginPre.getActiveInstance(ProfessionalSignup.this).setLocaleLangua(lang);
            if (lang.equalsIgnoreCase("Fr (French)")) {
                setLocale("fr");
            } else {
                setLocale("en");
            }
        });

        if (AppUtils.hasNetworkConnection(ProfessionalSignup.this)) {
            getBussinessList();
        } else {
            AppUtils.openPopup(ProfessionalSignup.this, R.style.Dialod_UpDown, "internetError", getResources().getString(R.string.no_internet));
        }

        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(ProfessionalSignup.this, R.layout.spinner_layout, R.id.spinner_text,
                activityName);
        binding.autoCompleteText.setAdapter(activityAdapter);
        binding.autoCompleteText.setOnFocusChangeListener((v15, hasFocus) -> {
            if (hasFocus) {
                binding.autoCompleteText.showDropDown();
            }
        });
        binding.autoCompleteText.setOnTouchListener((v, event) -> {
            binding.autoCompleteText.showDropDown();
            binding.autoCompleteText.requestFocus();
            return false;
        });
        binding.autoCompleteText.setOnItemClickListener((parent, view, position, id) -> {
            String activity = parent.getItemAtPosition(position).toString();
            for (int i = 0; i < activityName.size(); i++) {
                if (activity.equalsIgnoreCase(activityName.get(i))) {
                    businessId = activityId.get(i);
                }
            }
            Log.e(TAG, "ACTIVITY Name-->" + activity + ", Id-->" + businessId);
        });

        binding.edPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //String strChar=editable.toString();
              /*  if (editable.toString().length()<=3){

                    if (!binding.edPhoneNo.getText().toString().equals("0")){
                        binding.edPhoneNo.setText("");
                    }else if(binding.edPhoneNo.getText().toString().equals("074")
                            ||binding.edPhoneNo.getText().toString().equals("079"))
                    {
                        //binding.edPhoneNo.setText(editable.toString());
                    }
                    else
                    {
                        binding.edPhoneNo.setText("");
                    }
                }
                else if(editable.toString().length()>3)
                {
                    //binding.edPhoneNo.setText(editable.toString());
                }*/


                if (editable.toString().length() == 1) {
                    if (!editable.toString().equalsIgnoreCase("0")) {
                        binding.edPhoneNo.setText("");
                    }
                }
                if (editable.toString().length() == 2) {
                    if (!editable.toString().equalsIgnoreCase("07")) {
                        String text = binding.edPhoneNo.getText().toString();
                        binding.edPhoneNo.setText(text.substring(0, text.length() - 1));
                        binding.edPhoneNo.setSelection(text.substring(0, text.length() - 1).length());
                    }
                }
                if (editable.toString().length() == 3) {
                    String text = binding.edPhoneNo.getText().toString();
                    Log.i(TAG, "afterTextChanged: "+text);
                    if (editable.toString().equalsIgnoreCase("074")||editable.toString().equalsIgnoreCase("079")) {

                    } else {
                        binding.edPhoneNo.setText(text.substring(0, 2));
                        binding.edPhoneNo.setSelection(text.substring(0, text.length() - 1).length());

                    }
                }
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

    public void showLoading(String message) {
        AppUtils.hideKeyboardFrom(ProfessionalSignup.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading() {
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onClick(View v) {
        /*if (v==binding.tvCountryCode){
            Intent intent = new Intent(ProfessionalSignup.this, CountryListActivity.class);
            startActivityForResult(intent,COUNTRY_CODE);
        }*/

        if (v == binding.txtLogin || v == binding.imgBack) {
            finish();
        }

        if (v == binding.imgLocation) {
            Intent intent = new Intent(ProfessionalSignup.this, LocationActivity.class);
            startActivityForResult(intent, GET_LOCATION);
        }

        if (v == binding.btnSignup) {
            String companyName = binding.edittextCompanyName.getText().toString();
            String companyAddress = binding.edittextCompanyAddress.getText().toString();
            String activitySector = binding.autoCompleteText.getText().toString();
            String phone = binding.edPhoneNo.getText().toString();
            String email = binding.edEmailId.getText().toString();
            String password = binding.edPassword.getText().toString();
            String confirm_password = binding.edConfirmPassword.getText().toString();
            String nameDirector = binding.edittextDirectorName.getText().toString();
            if (isValid(companyName, companyAddress, businessId, phone, email, password, confirm_password, nameDirector)) {
                signUp(companyName, companyAddress, businessId, phone, email, password, confirm_password, nameDirector);
            }
        }
    }

    private void signUp(String companyName, String companyAddress, String businessId, String phone,
                        String email, String password, String confirm_password, String nameDirector) {
        device_token = LoginPre.getActiveInstance(ProfessionalSignup.this).getDevice_token();
        showLoading(getResources().getString(R.string.signing_in));
        signUpViewModel.signUpProfessional(Constant.DEVICE_TYPE, device_token, companyName, companyAddress, businessId,
                countryId, phone, email, password, confirm_password, nameDirector, 1, "4")
                .observe(this,
                        signupResult -> {
                            dismissLoading();
                            if (signupResult != null && !signupResult.error) {
                                int id = signupResult.result.getId();
                                String otp = signupResult.result.getOtp();
                                String token = signupResult.result.getToken();
                                LoginPre.getActiveInstance(ProfessionalSignup.this).setSignup_id(String.valueOf(id));
                                Intent intent = new Intent(ProfessionalSignup.this, VerifyOtpActivity.class);
                                intent.putExtra(Constant.VERIFY_OTP, otp);
                                intent.putExtra(Constant.MOBILE, phone);
                                intent.putExtra(Constant.ROLES_ID, "4");
                                startActivity(intent);
                            } else {
                                if (signupResult != null && signupResult.getMessage() != null) {
                                    AppUtils.openPopup(ProfessionalSignup.this, R.style.Dialod_UpDown, "error", signupResult.getMessage());
                                } else {
                                    AppUtils.openPopup(ProfessionalSignup.this, R.style.Dialod_UpDown, "error", getResources().getString(R.string.something_wrong));
                                }
                            }
                        });
    }

    private boolean isValid(String companyName, String companyAddress, String businessId, String phone, String email,
                            String password, String confirm_password, String nameDirector) {
        if (TextUtils.isEmpty(companyName)) {
            binding.edittextCompanyName.requestFocus();
            binding.edittextCompanyName.setError(getResources().getString(R.string.company_field_empty));
            AppUtils.showSnackbar(getString(R.string.plz_enter_company), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(companyAddress)) {
            binding.edittextCompanyAddress.requestFocus();
            binding.edittextCompanyAddress.setError(getResources().getString(R.string.company_add_field_empty));
            AppUtils.showSnackbar(getString(R.string.plz_enter_company_address), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(businessId) || binding.autoCompleteText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.select_your_sector))) {
            //  binding.autoCompleteText.requestFocus();
            //binding.autoCompleteText.setError(getResources().getString(R.string.select_activity_sector));
            AppUtils.showSnackbar(getString(R.string.plz_select_activity_sector), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            binding.edPhoneNo.requestFocus();
            binding.edPhoneNo.setError(getResources().getString(R.string.phone_no_empty));
            AppUtils.showSnackbar(getString(R.string.phone_no_empty), binding.parentLayout);
            return false;
        }
       /* if (!phone.startsWith("07")){
            binding.edPhoneNo.requestFocus();
            binding.edPhoneNo.setError(getResources().getString(R.string.enter_valid_phone));
            AppUtils.showSnackbar(getString(R.string.enter_valid_phone), binding.parentLayout);
            return false;

        }
        if (phone.startsWith("07")){
            if (!phone.startsWith("079")){
                binding.edPhoneNo.requestFocus();
                binding.edPhoneNo.setError(getResources().getString(R.string.enter_valid_phone));
                AppUtils.showSnackbar(getString(R.string.enter_valid_phone), binding.parentLayout);
                return false;
            } else if (!phone.startsWith("074")){
                binding.edPhoneNo.requestFocus();
                binding.edPhoneNo.setError(getResources().getString(R.string.enter_valid_phone));
                AppUtils.showSnackbar(getString(R.string.enter_valid_phone), binding.parentLayout);
                return false;
            }
        }*/

        if (TextUtils.isEmpty(email)) {
            binding.edEmailId.requestFocus();
            binding.edEmailId.setError(getResources().getString(R.string.email_empty));
            AppUtils.showSnackbar(getString(R.string.email_empty), binding.parentLayout);
            return false;
        }
        if (!AppUtils.isEmailValid(email)) {
            binding.edEmailId.requestFocus();
            binding.edEmailId.setError(getResources().getString(R.string.invalid_email));
            AppUtils.showSnackbar(getString(R.string.enter_valid_email), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            binding.edPassword.requestFocus();
            binding.edPassword.setError(getResources().getString(R.string.password_empty));
            AppUtils.showSnackbar(getString(R.string.password_empty), binding.parentLayout);
            return false;
        }
        if (password.length() != 4) {
            binding.edPassword.requestFocus();
            binding.edPassword.setError(getResources().getString(R.string.password_length));
            AppUtils.showSnackbar(getString(R.string.password_length), binding.parentLayout);
            return false;
        }
        if (confirm_password.isEmpty()) {
            binding.edConfirmPassword.requestFocus();
            binding.edConfirmPassword.setError(getResources().getString(R.string.ed_confirm_password));
            AppUtils.showSnackbar(getString(R.string.confirm_password_empty), binding.parentLayout);
            return false;
        }
        if (confirm_password.length() != 4) {
            binding.edConfirmPassword.requestFocus();
            binding.edConfirmPassword.setError(getResources().getString(R.string.password_length));
            AppUtils.showSnackbar(getString(R.string.password_length), binding.parentLayout);
            return false;
        }
        if (!TextUtils.equals(password, confirm_password)) {
            AppUtils.showSnackbar(getString(R.string.password_does_not_match), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(nameDirector)) {
            binding.edittextDirectorName.requestFocus();
            binding.edittextDirectorName.setError(getResources().getString(R.string.director_name_empty));
            AppUtils.showSnackbar(getString(R.string.please_enter_director_name_company), binding.parentLayout);
            return false;
        }
        if (!binding.checkTerms.isChecked()) {
            AppUtils.showSnackbar(getString(R.string.check_term), binding.parentLayout);
            return false;
        }
        return true;
    }

    private void getBussinessList() {
        showLoading(getResources().getString(R.string.please_wait));
        authViewModel.getBussinessList().observe(this, bussinessList -> {
            dismissLoading();
            if (!bussinessList.error) {
                activityName.clear();
                activityId.clear();
                activityName.add(0, getResources().getString(R.string.select_activity_sector));
                activityId.add(0, "0");
                for (int i = 0; i < bussinessList.getResult().size(); i++) {
                    activityName.add(bussinessList.getResult().get(i).getName());
                    activityId.add(String.valueOf(bussinessList.getResult().get(i).getId()));
                }
            } else {
                if (bussinessList.getMessage() != null) {
                    AppUtils.openPopup(ProfessionalSignup.this, R.style.Dialod_UpDown,
                            "error", bussinessList.getMessage());
                } else {
                    AppUtils.openPopup(ProfessionalSignup.this, R.style.Dialod_UpDown,
                            "error", getResources().getString(R.string.something_wrong));
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                // latitude = extras.getString(Constant.LATTITUDE);
                // longitude=extras.getString(Constant.LONGITUDE);
                String address = extras.getString(Constant.ADDRESS_USER);
                if (address != null && !TextUtils.isEmpty(address)) {
                    binding.edittextCompanyAddress.setText(address);
                }
            }
        }
        if (requestCode == COUNTRY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String id = data.getStringExtra(Constant.COUNTRY_ID);
                    String code = data.getStringExtra(Constant.PHONE_CODE);
                    countryId = id;
                    phoneCode = code;
                    binding.tvCountryCode.setText("+" + phoneCode);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}