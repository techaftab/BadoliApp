package com.app.badoli.activities.AccountActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.NavigationActivities.ChangePasswordActivity;
import com.app.badoli.activities.SplashActivity;
import com.app.badoli.adapter.Country_Adapter;
import com.app.badoli.config.Configuration;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivitySignUpBinding;
import com.app.badoli.model.CountryResult;
import com.app.badoli.model.UserData;
import com.app.badoli.utilities.GetMyItem;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.utilities.Validation;
import com.app.badoli.viewModels.SignUpViewModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xyz.hasnat.sweettoast.SweetToast;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,
        GetMyItem,
        Country_Adapter.DMTPayHistoryAdapterListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    static ActivitySignUpBinding signUpBinding;
    String name, phone, password, confirm_password, email,companyName,companyAddress, activitySector;
    SignUpViewModel signUpViewModel;
    String otp;
    boolean checked;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;
    Country_Adapter adapter;
    List<CountryResult> countryResults;
    private List<CountryResult> country;
    int userId=-1;
    String  access_token, device_token;
    private String roleId="";

    String[] language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        activity=SignUpActivity.this;

        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,""))) {
            Log.e("ksjd", "-" + SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,""));
            signUpBinding.tvCountryCode.setText("+".concat(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")));
        }

        init();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    void showLoading(){
        Configuration.hideKeyboardFrom(SignUpActivity.this);
        signUpBinding.signupProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    void dismissLoading(){
        signUpBinding.signupProgressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    @Override
    public void onClick(View v) {
        if (v==signUpBinding.txtResendOtp){
            userId = LoginPre.getActiveInstance(SignUpActivity.this).getSignup_id();
            access_token = LoginPre.getActiveInstance(SignUpActivity.this).getAccess_token();
            if (userId==-1||TextUtils.isEmpty(access_token)){
                Toast.makeText(activity, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }else {
                resendOtp(userId,access_token);
            }
        }
        if (v==signUpBinding.nextButton){
            userId = LoginPre.getActiveInstance(SignUpActivity.this).getSignup_id();
            access_token = LoginPre.getActiveInstance(SignUpActivity.this).getAccess_token();
            String otp=signUpBinding.ed1.getText().toString()
                    +signUpBinding.ed2.getText().toString()
                    +signUpBinding.ed3.getText().toString()
                    +signUpBinding.ed4.getText().toString()
                    +signUpBinding.ed5.getText().toString()
                    +signUpBinding.ed6.getText().toString();
            if (validateOtp()){
                Configuration.hideKeyboardFrom(SignUpActivity.this);
                verifyOtp(otp,userId,access_token);
            }
        }
        if (v==signUpBinding.includedLayout.imgCloseHidden){
            slideClose();
            signUpBinding.includedLayout.editTextSearchLayout.setText("");
        }
        if (v==signUpBinding.tvCountryCode){
            if (Configuration.hasNetworkConnection(SignUpActivity.this)){
                slideUpCountry();
            }else {
                Configuration.openPopupUpDown(SignUpActivity.this, R.style.Dialod_UpDown, "internetError",
                        getResources().getString(R.string.no_internet));
            }
        }
        if (v==signUpBinding.signupButton){
            name = signUpBinding.edFullName.getText().toString();
            phone = signUpBinding.edPhoneNo.getText().toString();
            email = signUpBinding.edEmailId.getText().toString();
            password = signUpBinding.edPassword.getText().toString();
            companyName = signUpBinding.edittextCompanyName.getText().toString();
            companyAddress = signUpBinding.edittextCompanyAddress.getText().toString();
            activitySector = signUpBinding.autocompActivitySctor.getText().toString();
            confirm_password = signUpBinding.edConfirmPassword.getText().toString();
            checked = signUpBinding.checReadAgreements.isChecked();

            if (setValidation(name, phone, email, password, confirm_password, checked,companyName,companyAddress, activitySector)) {
                Configuration.hideKeyboardFrom(SignUpActivity.this);
                getSignupResponse();
            }

        }
        if (v==signUpBinding.rlLogin){
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
            finish();
        }
    }


    private void init() {
        /*if (LoginPre.getActiveInstance(SignUpActivity.this).getLocaleLangua().equals("fr")){
            signUpBinding.spinnerLanguageSignup.setSelection(2);
        }else {
            signUpBinding.spinnerLanguageSignup.setSelection(1);
        }*/
        signUpBinding.radiogroupUsertype.setOnCheckedChangeListener((group, checkedId) -> {
            if (signUpBinding.rbUser.isChecked()){
                roleId="3";
                signUpBinding.lnMerchantDetails.setVisibility(View.GONE);
            }
            if (signUpBinding.rbMerchant.isChecked()){
                signUpBinding.lnMerchantDetails.setVisibility(View.VISIBLE);
                roleId="4";
            }
        });
        signUpBinding.spinnerLanguageSignup.setOnItemSelectedListener(this);
        language=getResources().getStringArray(R.array.select_lang);
        ArrayAdapter aa = new ArrayAdapter<>(this, R.layout.spinner_item, language);
        aa.setDropDownViewResource(R.layout.spinner_item);
        signUpBinding.spinnerLanguageSignup.setAdapter(aa);

        if (LoginPre.getActiveInstance(SignUpActivity.this).getLocaleLangua().equals("fr")){
            signUpBinding.spinnerLanguageSignup.setSelection(2);
        }else {
            signUpBinding.spinnerLanguageSignup.setSelection(1);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            LoginPre.getActiveInstance(SignUpActivity.this).setDevice_token(deviceToken);
            device_token= LoginPre.getActiveInstance(SignUpActivity.this).getDevice_token();
        });
        /*@SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
     //   String uniqueID = UUID.randomUUID().toString();
        Log.e(TAG,"DEVICE_ID--->"+android_id);
        MessageDigest md;
        device_token=null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(android_id.getBytes());
            device_token = new String(Base64.encode(md.digest(), 0));

            // String key = new String(Base64.encodeBytes(md.digest()));
            Log.e("Key Hash=", device_token);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
        signUpBinding.tvCountryCode.setOnClickListener(this);
        signUpBinding.signupButton.setOnClickListener(this);
        signUpBinding.rlLogin.setOnClickListener(this);
        signUpBinding.nextButton.setOnClickListener(this);
        signUpBinding.txtResendOtp.setOnClickListener(this);
        signUpBinding.includedLayout.imgCloseHidden.setOnClickListener(this);
        signUpBinding.ed1.addTextChangedListener(new GenericTextWatcher(signUpBinding.ed1));
        signUpBinding.ed2.addTextChangedListener(new GenericTextWatcher(signUpBinding.ed2));
        signUpBinding.ed3.addTextChangedListener(new GenericTextWatcher(signUpBinding.ed3));
        signUpBinding.ed4.addTextChangedListener(new GenericTextWatcher(signUpBinding.ed4));
        signUpBinding.ed5.addTextChangedListener(new GenericTextWatcher(signUpBinding.ed5));
        signUpBinding.ed6.addTextChangedListener(new GenericTextWatcher(signUpBinding.ed6));
        countryResults=new ArrayList<>();
        country=new ArrayList<>();
        adapter = new Country_Adapter(this, country, this,SignUpActivity.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (language[position].equals("French(fr)")) {
            LoginPre.getActiveInstance(SignUpActivity.this).setLocaleLangua("fr");
            setLocale("fr");
        }else {
            LoginPre.getActiveInstance(SignUpActivity.this).setLocaleLangua("en");
            setLocale("en");
        }
    }

    private void setLocale(String lang) {
        LoginPre.getActiveInstance(SignUpActivity.this).setLocaleLangua(lang);
        Locale myLocale = new Locale(lang);
        // Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(SignUpActivity.this, SignUpActivity.class);
//        startActivity(refresh);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.ed_1:
                    if (text.length() == 1)
                        signUpBinding.ed2.requestFocus();
                    break;

                case R.id.ed_2:
                    if (text.length() == 1)
                        signUpBinding.ed3.requestFocus();
                    else if (text.length() == 0)
                        signUpBinding.ed1.requestFocus();
                    break;

                case R.id.ed_3:
                    if (text.length() == 1)
                        signUpBinding.ed4.requestFocus();
                    else if (text.length() == 0)
                        signUpBinding.ed2.requestFocus();
                    break;

                case R.id.ed_4:
                    if (text.length() == 1)
                        signUpBinding.ed5.requestFocus();
                    else if (text.length() == 0)
                        signUpBinding.ed3.requestFocus();
                    break;

                case R.id.ed_5:
                    if (text.length() == 1)
                        signUpBinding.ed6.requestFocus();
                    else if (text.length() == 0)
                        signUpBinding.ed4.requestFocus();
                    break;

                case R.id.ed_6:
                    if (text.length() == 0)
                        signUpBinding.ed5.requestFocus();
                    break;
            }
        }
    }

    private void resendOtp(int userId, String access_token) {
        showLoading();
        signUpViewModel.resendOtp(userId,access_token).observe(this,
                resendOtpResponse -> {
                    Log.e("responsee", new Gson().toJson(resendOtpResponse.message));
                    dismissLoading();
                    if (!resendOtpResponse.error) {
                        Toast.makeText(SignUpActivity.this, resendOtpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, resendOtpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getSignupResponse() {
        Log.e("Signup", "onclickdata" + name + "\n " + email + "\n " + phone + "\n" + password + "\n" + confirm_password);

        device_token=LoginPre.getActiveInstance(SignUpActivity.this).getDevice_token();
        showLoading();

        signUpViewModel.getSignUp(name, email, phone, password, 1, device_token, confirm_password,
                Integer.valueOf(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")), 1,roleId).observe(this,
                signupResult -> {
                    Log.e("responsee", new Gson().toJson(signupResult.message));
                    dismissLoading();
                    if (!signupResult.error) {

                        Toast.makeText(SignUpActivity.this, signupResult.getMessage(), Toast.LENGTH_SHORT).show();

                        int id = signupResult.result.getId();
                        otp = signupResult.result.getOtp();

                        String token = signupResult.result.getToken();
                        Log.e("id", "" + signupResult.result.id);
                        Log.e("otp", "" + signupResult.result.otp);
                        Log.e("token", "" + signupResult.result.token);

                        LoginPre.getActiveInstance(SignUpActivity.this).setSignup_id(id);
                        LoginPre.getActiveInstance(SignUpActivity.this).setAccess_token(token);
                        fillOtp(otp);
                    } else {
                        Toast.makeText(SignUpActivity.this, signupResult.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fillOtp(String otp) {
        signUpBinding.cardViewSignup.setVisibility(View.GONE);
        signUpBinding.cardViewOtp.setVisibility(View.VISIBLE);
        signUpBinding.rlLogin.setVisibility(View.GONE);
        List<String> strings = new ArrayList<>();
        int index = 0;
        while (index < otp.length()) {
            strings.add(otp.substring(index, Math.min(index + 1, otp.length())));
            index += 1;
        }

        signUpBinding.ed1.setText(strings.get(0));
        signUpBinding.ed2.setText(strings.get(1));
        signUpBinding.ed3.setText(strings.get(2));
        signUpBinding.ed4.setText(strings.get(3));
        signUpBinding.ed5.setText(strings.get(4));
        signUpBinding.ed6.setText(strings.get(5));
    }
    private void verifyOtp(String otp, int userId, String access_token) {
        signUpBinding.otpProgressBar.setVisibility(View.VISIBLE);
        signUpViewModel.getOTP(userId, otp, access_token).observe(this, verifyOtpResponse -> {
            if (!verifyOtpResponse.error) {
                signUpBinding.otpProgressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, verifyOtpResponse.getMessage(), Toast.LENGTH_SHORT).show();

                String id=String.valueOf(verifyOtpResponse.result.getId());
                String countryCode=String.valueOf(verifyOtpResponse.result.getCountryCode());
                String countryId=String.valueOf(verifyOtpResponse.result.getCountryId());
                LoginPre.getActiveInstance(SignUpActivity.this).setIsLoggedIn(true);
                UserData userData = new UserData(
                        id,
                        verifyOtpResponse.result.getAuthToken(),
                        countryCode,
                        countryId,
                        verifyOtpResponse.result.getDeviceToken(),
                        verifyOtpResponse.result.getEmail(),
                        verifyOtpResponse.result.getMobile(),
                        verifyOtpResponse.result.getName(),
                        verifyOtpResponse.result.getWalletBalance(),
                        verifyOtpResponse.result.getUser_image(),
                        verifyOtpResponse.result.getQrcode_image());
                PrefManager.getInstance(SignUpActivity.this).userLogin(userData);
              //  generateQrCode(verifyOtpResponse.result.getMobile(),userId);
           //     new Thread(() ->generateQrCode(verifyOtpResponse.result.getMobile(),userId, this)).start();
                Thread mThread =  new Thread(){
                    @Override
                    public void run(){
                        // Perform thread commands...
                        generateQrCode(verifyOtpResponse.result.getMobile(),userId,this);

                    }
                };
                mThread.start();

            }else {
                Toast.makeText(SignUpActivity.this, verifyOtpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                signUpBinding.otpProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,""))) {
            signUpBinding.tvCountryCode.setText("+".concat(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")));
        }
    }


    private boolean validateOtp() {
        if (TextUtils.isEmpty(signUpBinding.ed1.getText().toString())
        ||TextUtils.isEmpty(signUpBinding.ed2.getText().toString())
        ||TextUtils.isEmpty(signUpBinding.ed3.getText().toString())
        ||TextUtils.isEmpty(signUpBinding.ed4.getText().toString())
        ||TextUtils.isEmpty(signUpBinding.ed4.getText().toString())
        ||TextUtils.isEmpty(signUpBinding.ed6.getText().toString())){
            Toast.makeText(activity, getResources().getString(R.string.enter_otp_sent_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void call() {
        signUpBinding.includedLayout.countryProgress.setVisibility(View.VISIBLE);
        signUpBinding.includedLayout.editTextSearchLayout.setEnabled(false);
        Configuration.hideKeyboardFrom(SignUpActivity.this);
        signUpViewModel.getCountryList().observe(this, response -> {
            signUpBinding.includedLayout.countryProgress.setVisibility(View.INVISIBLE);
            if (!response.error) {
                signUpBinding.includedLayout.editTextSearchLayout.setEnabled(true);
                signUpBinding.includedLayout.countryProgress.setVisibility(View.GONE);
                countryResults=response.getResult();
                Log.e("countryResults", "" + countryResults.size());
                setAdapter(countryResults);
            } else {
                SweetToast.error(SignUpActivity.this,getResources().getString(R.string.something_wrong));
            }
        });
    }

    private void setAdapter(final List<CountryResult> countryResults) {

        country.clear();
        country.addAll(countryResults);
        adapter.notifyDataSetChanged();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        signUpBinding.includedLayout.countryRecyclerview.setLayoutManager(mLayoutManager);
        signUpBinding.includedLayout.countryRecyclerview.setItemAnimator(new DefaultItemAnimator());
        signUpBinding.includedLayout.countryRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        signUpBinding.includedLayout.editTextSearchLayout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
               // if (country!=null) {
                    adapter.getFilter().filter(s);
              //  }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    private void slideUpCountry() {
        if (!isPanelShown()) {
            call();
            Animation bottomUp = AnimationUtils.loadAnimation(SignUpActivity.this,
                    R.anim.slide_up_dialog);
            signUpBinding.includedLayout.hiddenLayoutCountry.startAnimation(bottomUp);
            signUpBinding.includedLayout.hiddenLayoutCountry.setVisibility(View.VISIBLE);
            signUpBinding.scrollviewSignup.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return signUpBinding.includedLayout.hiddenLayoutCountry.getVisibility() == View.VISIBLE;
    }

    public static void slideClose() {
        try{
            Configuration.hideKeyboardFrom(activity);
        }catch (Exception e){
            e.printStackTrace();
        }
        Animation bottomDown = AnimationUtils.loadAnimation(activity,
                R.anim.slide_bottom_dialog);

        signUpBinding.includedLayout.hiddenLayoutCountry.startAnimation(bottomDown);
        signUpBinding.includedLayout.hiddenLayoutCountry.setVisibility(View.GONE);
        signUpBinding.scrollviewSignup.setVisibility(View.VISIBLE);
    }

    private boolean setValidation(String name, String phone, String email, String password, String confirm_password, boolean checked,
                                  String companyName, String companyAddress, String companyNumber) {
        if (name.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.ed_fullName), Toast.LENGTH_LONG).show();
            return false;
        } else if (phone.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.ed_phone), Toast.LENGTH_LONG).show();
            return false;
        } else if (phone.length() < 7 || phone.length() > 15) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.phone_lenght), Toast.LENGTH_LONG).show();
            return false;
        } else if (email.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.ed_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (!Validation.isValidEmaillId(email)) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.ed_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (password.length() < 8) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (confirm_password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.ed_confirm_password), Toast.LENGTH_LONG).show();
            return false;
        } else if (!password.equals(confirm_password)) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.not_match), Toast.LENGTH_LONG).show();
            return false;
        } else if (signUpBinding.radiogroupUsertype.getCheckedRadioButtonId()==-1){
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.select_usertype), Toast.LENGTH_LONG).show();
            return false;
        }  else if (signUpBinding.lnMerchantDetails.isShown()){
            if (TextUtils.isEmpty(companyName)){
                Toast.makeText(this, getResources().getString(R.string.enter_company_name), Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(companyAddress)){
                Toast.makeText(this, getResources().getString(R.string.enter_company_address), Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(companyNumber)){
                Toast.makeText(this, getResources().getString(R.string.select_your_sector), Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if (!checked) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.check_term), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void GetClickedItem(int id, int code) {
        Log.e(TAG, "GetClickedItem position id : " + id);
        Log.e(TAG, "GetClickedItem position code : " + code);

        SplashActivity.savePreferences(Constant.REMEMER_COUNTRY_CODE, String.valueOf(code));
        SplashActivity.savePreferences(Constant.REMEMER_COUNTRY_ID, String.valueOf(id));

       // LoginPre.getActiveInstance(SignUpActivity.this).setCountry_code(code);
       // LoginPre.getActiveInstance(SignUpActivity.this).setCountry_id(id);
        signUpBinding.tvCountryCode.setText("+".concat(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")));
    }

    @Override
    public void onRecyclerViewClickListenerSelected(CountryResult countryResult) {

    }

    @Override
    public void onClick(View v, int pos) {

    }

    @Override
    public void onBackPressed() {
        if (isPanelShown()){
            slideClose();
        }else if (signUpBinding.cardViewOtp.isShown()){
            signUpBinding.cardViewSignup.setVisibility(View.VISIBLE);
            signUpBinding.cardViewOtp.setVisibility(View.GONE);
            signUpBinding.rlLogin.setVisibility(View.VISIBLE);
        }else {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
            finish();
        }
    }
    private void generateQrCode(String mobile, int id, Thread thread) {

        int width=500;
        int height=500 ;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            bitMatrix = new MultiFormatWriter().encode(
                    new String(mobile.getBytes()),
                    BarcodeFormat.QR_CODE, width, height, hintMap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        int bitMatrixWidth = 0;
        if (bitMatrix != null) {
            bitMatrixWidth = bitMatrix.getWidth();
        }

        int bitMatrixHeight = 0;
        if (bitMatrix != null) {
            bitMatrixHeight = bitMatrix.getHeight();
        }

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {

            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.bla_trans) : getResources().getColor(R.color.white);

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.RGB_565);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);

       // BitMatrix finalBitMatrix = bitMatrix;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        //File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        File file  = new File(getCacheDir(), "temporary_file.jpg");
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        runOnUiThread(() -> sendImage(file,id));
        stopThread(thread);
    }
    private synchronized void stopThread(Thread theThread) {
        if (theThread != null) {
           // theThread = null;
            theThread.interrupt();
        }
    }

    private void sendImage(File file, int id) {
        Log.e(TAG,"USER ID--->"+id);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        //RequestBody fileReq = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));

        MultipartBody.Part part = MultipartBody.Part.createFormData("qrcode_image", file.getName(), fileReqBody);

        signUpBinding.includedLayout.countryProgress.setVisibility(View.VISIBLE);
        signUpViewModel.sendQrcode(part,id).observe(this, verifyOtpResponse -> {
            if (!verifyOtpResponse.error) {
                signUpBinding.includedLayout.countryProgress.setVisibility(View.GONE);
                Intent intent = new Intent(SignUpActivity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            } else {
                signUpBinding.includedLayout.countryProgress.setVisibility(View.GONE);
            }
        });
    }
}
