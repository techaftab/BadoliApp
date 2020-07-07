package com.app.badoli.auth.signup.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.SplashActivity;
import com.app.badoli.adapter.Country_Adapter;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivitySignUpBinding;
import com.app.badoli.model.CountryResponse;
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
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xyz.hasnat.sweettoast.SweetToast;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,
        GetMyItem,
        Country_Adapter.DMTPayHistoryAdapterListener{
    private static final String TAG = SignUpActivity.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    ActivitySignUpBinding binding;
    String name, phone, password, confirm_password, email;
    SignUpViewModel signUpViewModel;
    String otp;
    boolean checked;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;
    Country_Adapter adapter;
    List<CountryResponse.CountryResult> countryResults;
   // private List<CountryResult> country;
    int userId=-1;
    String  access_token, device_token;


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

        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,""))) {
            Log.e("ksjd", "-" + SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,""));
            binding.tvCountryCode.setText("+".concat(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")));
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            LoginPre.getActiveInstance(SignUpActivity.this).setDevice_token(deviceToken);
            device_token= LoginPre.getActiveInstance(SignUpActivity.this).getDevice_token();
        });

        binding.tvCountryCode.setOnClickListener(this);
        binding.signupButton.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);
        binding.nextButton.setOnClickListener(this);
        binding.txtResendOtp.setOnClickListener(this);
        //signUpBinding.txtChangeLang.setOnClickListener(this);
        binding.includedLayout.imgCloseHidden.setOnClickListener(this);
        binding.ed1.addTextChangedListener(new GenericTextWatcher(binding.ed1));
        binding.ed2.addTextChangedListener(new GenericTextWatcher(binding.ed2));
        binding.ed3.addTextChangedListener(new GenericTextWatcher(binding.ed3));
        binding.ed4.addTextChangedListener(new GenericTextWatcher(binding.ed4));
        binding.ed5.addTextChangedListener(new GenericTextWatcher(binding.ed5));
        binding.ed6.addTextChangedListener(new GenericTextWatcher(binding.ed6));
        countryResults=new ArrayList<>();
       // country=new ArrayList<>();
        adapter = new Country_Adapter(this, countryResults, this,SignUpActivity.this);

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
            // TODO Auto-generated method stub
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
        if (v==binding.txtResendOtp){
            userId = LoginPre.getActiveInstance(SignUpActivity.this).getSignup_id();
            access_token = LoginPre.getActiveInstance(SignUpActivity.this).getAccess_token();
            if (userId==-1||TextUtils.isEmpty(access_token)){
                Toast.makeText(activity, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }else {
                resendOtp(userId,access_token);
            }
        }
        if (v==binding.nextButton){
            userId = LoginPre.getActiveInstance(SignUpActivity.this).getSignup_id();
            access_token = LoginPre.getActiveInstance(SignUpActivity.this).getAccess_token();
            String otp=binding.ed1.getText().toString()
                    +binding.ed2.getText().toString()
                    +binding.ed3.getText().toString()
                    +binding.ed4.getText().toString()
                    +binding.ed5.getText().toString()
                    +binding.ed6.getText().toString();
            if (validateOtp()){
                AppUtils.hideKeyboardFrom(SignUpActivity.this);
                verifyOtp(otp,userId,access_token);
            }
        }
        if (v==binding.includedLayout.imgCloseHidden){
            slideClose();
            binding.includedLayout.editTextSearchLayout.setText("");
        }
        if (v==binding.tvCountryCode){
            if (AppUtils.hasNetworkConnection(SignUpActivity.this)){
                slideUpCountry();
            }else {
                AppUtils.openPopup(SignUpActivity.this, R.style.Dialod_UpDown, "internetError",
                        getResources().getString(R.string.no_internet));
            }
        }
        if (v==binding.signupButton){
            name = binding.edFullName.getText().toString();
            phone = binding.edPhoneNo.getText().toString();
            email = binding.edEmailId.getText().toString();
            password = binding.edPassword.getText().toString();
            confirm_password = binding.edConfirmPassword.getText().toString();
            checked = binding.checReadAgreements.isChecked();

            if (setValidation(name, phone, email, password, confirm_password)) {
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
                        binding.ed2.requestFocus();
                    break;

                case R.id.ed_2:
                    if (text.length() == 1)
                        binding.ed3.requestFocus();
                    else if (text.length() == 0)
                        binding.ed1.requestFocus();
                    break;

                case R.id.ed_3:
                    if (text.length() == 1)
                        binding.ed4.requestFocus();
                    else if (text.length() == 0)
                        binding.ed2.requestFocus();
                    break;

                case R.id.ed_4:
                    if (text.length() == 1)
                        binding.ed5.requestFocus();
                    else if (text.length() == 0)
                        binding.ed3.requestFocus();
                    break;

                case R.id.ed_5:
                    if (text.length() == 1)
                        binding.ed6.requestFocus();
                    else if (text.length() == 0)
                        binding.ed4.requestFocus();
                    break;

                case R.id.ed_6:
                    if (text.length() == 0)
                        binding.ed5.requestFocus();
                    break;
            }
        }
    }

    private void resendOtp(int userId, String access_token) {
        showLoading(getResources().getString(R.string.resending_otp));
        signUpViewModel.resendOtp(userId,access_token).observe(this,
                resendOtpResponse -> {
                    Log.e("responsee", new Gson().toJson(resendOtpResponse.message));
                    dismissLoading();
                    if (!resendOtpResponse.error) {
                        AppUtils.openPopup(SignUpActivity.this,R.style.Dialod_UpDown,"",resendOtpResponse.getMessage());
                    } else {
                        AppUtils.openPopup(SignUpActivity.this,R.style.Dialod_UpDown,"error",resendOtpResponse.getMessage());
                    }
                });
    }

    private void getSignupResponse() {

        device_token=LoginPre.getActiveInstance(SignUpActivity.this).getDevice_token();
        showLoading(getResources().getString(R.string.signing_in));

        String roleId = "3";
        signUpViewModel.getSignUp(name, email, phone, password, 1, device_token, confirm_password,
                Integer.parseInt(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")), 1, roleId).observe(this,
                signupResult -> {
                    Log.e("responsee", new Gson().toJson(signupResult.message));
                    dismissLoading();
                    if (!signupResult.error) {

                        Toast.makeText(SignUpActivity.this, signupResult.getMessage(), Toast.LENGTH_SHORT).show();

                        int id = signupResult.result.getId();
                        otp = signupResult.result.getOtp();

                        String token = signupResult.result.getToken();

                        LoginPre.getActiveInstance(SignUpActivity.this).setSignup_id(id);
                        LoginPre.getActiveInstance(SignUpActivity.this).setAccess_token(token);
                        fillOtp(otp);
                    } else {
                        Toast.makeText(SignUpActivity.this, signupResult.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fillOtp(String otp) {
        binding.cardViewSignup.setVisibility(View.GONE);
        binding.cardViewOtp.setVisibility(View.VISIBLE);
        binding.rlLogin.setVisibility(View.GONE);
        List<String> strings = new ArrayList<>();
        int index = 0;
        while (index < otp.length()) {
            strings.add(otp.substring(index, Math.min(index + 1, otp.length())));
            index += 1;
        }

        binding.ed1.setText(strings.get(0));
        binding.ed2.setText(strings.get(1));
        binding.ed3.setText(strings.get(2));
        binding.ed4.setText(strings.get(3));
        binding.ed5.setText(strings.get(4));
        binding.ed6.setText(strings.get(5));
    }
    private void verifyOtp(String otp, int userId, String access_token) {
        showLoading(getResources().getString(R.string.verifying_otp));
        signUpViewModel.getOTP(userId, otp, access_token).observe(this, verifyOtpResponse -> {
            dismissLoading();
            if (!verifyOtpResponse.error) {
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
                Thread mThread =  new Thread(){
                    @Override
                    public void run(){
                        generateQrCode(verifyOtpResponse.result.getMobile(),userId,this);
                    }
                };
                mThread.start();

            }else {
                if (verifyOtpResponse.getMessage()!=null) {
                    AppUtils.openPopup(SignUpActivity.this, R.style.Dialod_UpDown, "error", verifyOtpResponse.getMessage());
                }else {
                    AppUtils.openPopup(SignUpActivity.this, R.style.Dialod_UpDown, "error", getResources().getString(R.string.something_wrong));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,""))) {
            binding.tvCountryCode.setText("+".concat(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")));
        }
    }


    private boolean validateOtp() {
        if (TextUtils.isEmpty(binding.ed1.getText().toString())
        ||TextUtils.isEmpty(binding.ed2.getText().toString())
        ||TextUtils.isEmpty(binding.ed3.getText().toString())
        ||TextUtils.isEmpty(binding.ed4.getText().toString())
        ||TextUtils.isEmpty(binding.ed4.getText().toString())
        ||TextUtils.isEmpty(binding.ed6.getText().toString())){
            Toast.makeText(activity, getResources().getString(R.string.enter_otp_sent_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void call() {
        binding.includedLayout.countryProgress.setVisibility(View.VISIBLE);
        binding.includedLayout.editTextSearchLayout.setEnabled(false);
        AppUtils.hideKeyboardFrom(SignUpActivity.this);
        signUpViewModel.getCountryList().observe(this, response -> {
            dismissLoading();
            binding.includedLayout.countryProgress.setVisibility(View.INVISIBLE);
            if (response!=null&&!response.error) {
                binding.includedLayout.editTextSearchLayout.setEnabled(true);
                binding.includedLayout.countryProgress.setVisibility(View.GONE);
                countryResults.addAll(response.result);
                Log.e("countryResults", "" + countryResults.size());
                setAdapter();
                if (!isPanelShown()) {
                    Animation bottomUp = AnimationUtils.loadAnimation(SignUpActivity.this,
                            R.anim.slide_up_dialog);
                    binding.includedLayout.hiddenLayoutCountry.startAnimation(bottomUp);
                    binding.includedLayout.hiddenLayoutCountry.setVisibility(View.VISIBLE);
                    binding.scrollviewSignup.setVisibility(View.GONE);
                }
            } else {
                SweetToast.error(SignUpActivity.this,getResources().getString(R.string.something_wrong));
            }
        });
    }

    private void setAdapter() {
        adapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.includedLayout.countryRecyclerview.setLayoutManager(mLayoutManager);
        binding.includedLayout.countryRecyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.includedLayout.countryRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        binding.includedLayout.editTextSearchLayout.addTextChangedListener(new TextWatcher() {
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
        showLoading(getResources().getString(R.string.please_wait));
        call();
    }

    private boolean isPanelShown() {
        return binding.includedLayout.hiddenLayoutCountry.getVisibility() == View.VISIBLE;
    }

    public void slideClose() {
        try{
            AppUtils.hideKeyboardFrom(activity);
        }catch (Exception e){
            e.printStackTrace();
        }
        Animation bottomDown = AnimationUtils.loadAnimation(activity,
                R.anim.slide_bottom_dialog);

        binding.includedLayout.hiddenLayoutCountry.startAnimation(bottomDown);
        binding.includedLayout.hiddenLayoutCountry.setVisibility(View.GONE);
        binding.scrollviewSignup.setVisibility(View.VISIBLE);
    }

    private boolean setValidation(String name, String phone, String email, String password, String confirm_password) {
        if (name.isEmpty()) {
            binding.edFullName.requestFocus();
            binding.edFullName.setError(getResources().getString(R.string.ed_fullName));
            AppUtils.showSnackbar(getString(R.string.name_field_empty), binding.parentLayout);
            return false;
        } else if (phone.isEmpty()) {
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
        } else if (password.length() < 8) {
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
    public void GetClickedItem(int id, int code) {
        Log.e(TAG, "GetClickedItem position id : " + id);
        Log.e(TAG, "GetClickedItem position code : " + code);

        SplashActivity.savePreferences(Constant.REMEMER_COUNTRY_CODE, String.valueOf(code));
        SplashActivity.savePreferences(Constant.REMEMER_COUNTRY_ID, String.valueOf(id));
       // LoginPre.getActiveInstance(SignUpActivity.this).setCountry_code(code);
       // LoginPre.getActiveInstance(SignUpActivity.this).setCountry_id(id);
        binding.tvCountryCode.setText("+".concat(SplashActivity.getPreferences(Constant.REMEMER_COUNTRY_CODE,"")));
        slideClose();
    }

    @Override
    public void onRecyclerViewClickListenerSelected(CountryResponse.CountryResult countryResult) {

    }

    @Override
    public void onClick(View v, int pos) {

    }

    @Override
    public void onBackPressed() {
        if (isPanelShown()){
            slideClose();
        }else if (binding.cardViewOtp.isShown()){
            binding.cardViewSignup.setVisibility(View.VISIBLE);
            binding.cardViewOtp.setVisibility(View.GONE);
            binding.rlLogin.setVisibility(View.VISIBLE);
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
        RequestBody fileReqBody = RequestBody.create(file, Objects.requireNonNull(MediaType.parse("image/*")));
        //RequestBody fileReq = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));

        MultipartBody.Part part = MultipartBody.Part.createFormData("qrcode_image", file.getName(), fileReqBody);

        binding.includedLayout.countryProgress.setVisibility(View.VISIBLE);
        signUpViewModel.sendQrcode(part,id).observe(this, verifyOtpResponse -> {
            if (!verifyOtpResponse.error) {
                binding.includedLayout.countryProgress.setVisibility(View.GONE);
                Intent intent = new Intent(SignUpActivity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
            } else {
                binding.includedLayout.countryProgress.setVisibility(View.GONE);
            }
        });
    }
}
