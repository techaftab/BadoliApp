package com.webmobril.badoli.activities.AccountActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.adapter.Country_Adapter;
import com.webmobril.badoli.config.Configuration;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivitySignUpBinding;
import com.webmobril.badoli.model.CountryResult;
import com.webmobril.badoli.model.UserData;
import com.webmobril.badoli.utilities.GetMyItem;
import com.webmobril.badoli.utilities.LoginPre;
import com.webmobril.badoli.utilities.Validation;
import com.webmobril.badoli.viewModels.SignUpViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,
        GetMyItem,
        Country_Adapter.DMTPayHistoryAdapterListener {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    static ActivitySignUpBinding signUpBinding;
    String name, phone, password, confirm_password, email;
    SignUpViewModel signUpViewModel;
    String otp;
    private boolean checked;
    @SuppressLint("StaticFieldLeak")
    static Activity activity;
    Country_Adapter adapter;
    List<CountryResult> countryResults;
    private List<CountryResult> country;
    int userId;
    String  access_token, device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        activity=SignUpActivity.this;

        if (!TextUtils.isEmpty(String.valueOf(LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode()))) {

            Log.e("ksjd", "-" + LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode());
            signUpBinding.tvCountryCode.setText("+".concat(String.valueOf(LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode())));
        }


        if (!TextUtils.isEmpty(String.valueOf(LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode()))) {
            Log.e("ksjd", "" + LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode());
            signUpBinding.tvCountryCode.setText("+".concat(String.valueOf(LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode())));
        }
        init();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void init() {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
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
        }
        signUpBinding.tvCountryCode.setOnClickListener(this);
        signUpBinding.signupButton.setOnClickListener(this);
        signUpBinding.rlLogin.setOnClickListener(this);
        signUpBinding.nextButton.setOnClickListener(this);
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

    private void getSignupResponse() {
        Log.e("Signup", "onclickdata" + name + "\n " + email + "\n " + phone + "\n" + password + "\n" + confirm_password);

        signUpBinding.signupProgressBar.setVisibility(View.VISIBLE);

        signUpViewModel.getSignUp(name, email, phone, password, 1, device_token, confirm_password,
                LoginPre.getActiveInstance(SignUpActivity.this).getCountry_id(), 1).observe(this,
                signupResult -> {
                    Log.e("responsee", new Gson().toJson(signupResult.message));
                    if (!signupResult.error) {

                        signUpBinding.signupProgressBar.setVisibility(View.GONE);

                        Toast.makeText(SignUpActivity.this, signupResult.getMessage(), Toast.LENGTH_SHORT).show();

                        int id = signupResult.result.getId();
                        otp = signupResult.result.getOtp();

                        String token = signupResult.result.getToken();
                        Log.e("id", "" + signupResult.result.id);
                        Log.e("otp", "" + signupResult.result.otp);
                        Log.e("token", "" + signupResult.result.token);

                        LoginPre.getActiveInstance(SignUpActivity.this).setSignup_id(id);
                        LoginPre.getActiveInstance(SignUpActivity.this).setOtp(otp);
                        LoginPre.getActiveInstance(SignUpActivity.this).setAccess_token(token);

                        StartActivity();
                        OTP_Response();

                    } else {
                        Toast.makeText(SignUpActivity.this, signupResult.getMessage(), Toast.LENGTH_SHORT).show();
                        signUpBinding.signupProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(String.valueOf(LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode()))) {
            signUpBinding.tvCountryCode.setText("+".concat(String.valueOf(LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode())));
        }
    }

    @Override
    public void onClick(View v) {
        if (v==signUpBinding.nextButton){
            Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
        if (v==signUpBinding.includedLayout.imgCloseHidden){
            slideClose();
            signUpBinding.includedLayout.editTextSearchLayout.setText("");
        }
        if (v==signUpBinding.tvCountryCode){
            if (Configuration.hasNetworkConnection(SignUpActivity.this)){
                call();
                slideUpCountry();
            }else {
                Configuration.openPopupUpDown(SignUpActivity.this, R.style.Dialod_UpDown, "internetError",
                        "No Internet Connectivity" +
                                ", Thanks");
            }
        }
        if (v==signUpBinding.signupButton){
            name = signUpBinding.edFullName.getText().toString();
            phone = signUpBinding.edPhoneNo.getText().toString();
            email = signUpBinding.edEmailId.getText().toString();
            password = signUpBinding.edPassword.getText().toString();
            confirm_password = signUpBinding.edConfirmPassword.getText().toString();
            checked = signUpBinding.checReadAgreements.isChecked();

            if (setValidation(name, phone, email, password, confirm_password, checked)) {
                getSignupResponse();
            }

        }
        if (v==signUpBinding.rlLogin){
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void OTP_Response() {
        userId = LoginPre.getActiveInstance(SignUpActivity.this).getSignup_id();
        access_token = LoginPre.getActiveInstance(SignUpActivity.this).getAccess_token();
        otp = LoginPre.getActiveInstance(SignUpActivity.this).getOtp();

        signUpBinding.otpProgressBar.setVisibility(View.VISIBLE);
        signUpViewModel.getOTP(userId, otp, access_token).observe(this, verifyOtpResponse -> {
            if (!verifyOtpResponse.error) {
                signUpBinding.otpProgressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, verifyOtpResponse.getMessage(), Toast.LENGTH_SHORT).show();

                String num = LoginPre.getActiveInstance(SignUpActivity.this).getOtp();

                List<String> strings = new ArrayList<>();
                int index = 0;
                while (index < num.length()) {
                    strings.add(num.substring(index, Math.min(index + 1, num.length())));
                    index += 1;
                }

                signUpBinding.ed1.setText(strings.get(0));
                signUpBinding.ed2.setText(strings.get(1));
                signUpBinding.ed3.setText(strings.get(2));
                signUpBinding.ed4.setText(strings.get(3));
                signUpBinding.ed5.setText(strings.get(4));
                signUpBinding.ed6.setText(strings.get(5));

               /* String mobile = verifyOtpResponse.result.getMobile();
                String password = verifyOtpResponse.result.getTxtpassword();
             //   String device_token = verifyOtpResponse.result.getDeviceToken();
                String access_token = verifyOtpResponse.result.getAuthToken();
                String name= verifyOtpResponse.result.getName();
                String email_id= verifyOtpResponse.result.getEmail();*/
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
                        verifyOtpResponse.result.getWalletBalance());
                PrefManager.getInstance(SignUpActivity.this).userLogin(userData);

                generateQrCode(verifyOtpResponse.result.getMobile(),id);

              /*LoginPre.getActiveInstance(SignUpActivity.this).setMobile(mobile);
                LoginPre.getActiveInstance(SignUpActivity.this).setPassword(password);
                LoginPre.getActiveInstance(SignUpActivity.this).setDevice_token(device_token);
                LoginPre.getActiveInstance(SignUpActivity.this).setAccess_OTp(access_token);
                LoginPre.getActiveInstance(SignUpActivity.this).setName(name);
                LoginPre.getActiveInstance(SignUpActivity.this).setEmail(email_id);*/

            }else if (verifyOtpResponse.getMessage().equals("Mobile Already Registered")){
               // generateQrCode(verifyOtpResponse.result.getMobile(),id);
            }else {
                Toast.makeText(SignUpActivity.this, verifyOtpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                signUpBinding.otpProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void call() {
        signUpBinding.includedLayout.countryProgress.setVisibility(View.VISIBLE);
        signUpViewModel.getCountryList().observe(this, response -> {
            if (!response.error) {
                signUpBinding.includedLayout.countryProgress.setVisibility(View.GONE);
                countryResults=response.getResult();
                Log.e("countryResults", "" + countryResults.size());
                setAdapter(countryResults);
            } else {
                signUpBinding.includedLayout.countryProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAdapter(final List<CountryResult> countryResults) {


        /*final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        signUpBinding.includedLayout.countryRecyclerview.setLayoutManager(layoutManager);
        signUpBinding.includedLayout.countryRecyclerview.setAdapter(adapter);*/
        String array=new Gson().toJson(countryResults.toArray());

        Log.e(TAG,"ARRAY--->"+array);

        List<CountryResult> items = new Gson().fromJson(array,
                new TypeToken<List<CountryResult>>() {
                }.getType());

        country.clear();
        country.addAll(items);
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

    private boolean setValidation(String name, String phone, String email, String password, String confirm_password, boolean checked) {
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
        } else if (!checked) {
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.check_term), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void StartActivity() {

        signUpBinding.cardViewSignup.setVisibility(View.GONE);
        signUpBinding.cardViewOtp.setVisibility(View.VISIBLE);
        signUpBinding.rlLogin.setVisibility(View.GONE);
       /* Intent intent = new Intent(getApplicationContext(), OTP_Screen.class);
        startActivity(intent);*/
    }


    @Override
    public void GetClickedItem(int id, int code) {
        Log.e(TAG, "GetClickedItem position id : " + id);
        Log.e(TAG, "GetClickedItem position code : " + code);

        LoginPre.getActiveInstance(SignUpActivity.this).setCountry_code(code);
        LoginPre.getActiveInstance(SignUpActivity.this).setCountry_id(id);
        signUpBinding.tvCountryCode.setText("+".concat(String.valueOf(LoginPre.getActiveInstance(SignUpActivity.this).getCountryCode())));
      //  finish();
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
            finish();
        }
    }
    private void generateQrCode(String mobile, String id) {

        int width=500;
        int height=500 ;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            bitMatrix = new MultiFormatWriter().encode(
                    new String(mobile.getBytes()),
                    BarcodeFormat.QR_CODE, width, height, hintMap);

        } catch (IllegalArgumentException Illegalargumentexception) {

        } catch (WriterException e) {
            e.printStackTrace();
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

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
    }

    private void sendImage(File file, String id) {
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
        //Create request body with text description and text media type
       // RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        signUpBinding.includedLayout.countryProgress.setVisibility(View.VISIBLE);
        signUpViewModel.sendQrcode(part,id).observe(this, verifyOtpResponse -> {
            if (!verifyOtpResponse.error) {
                signUpBinding.includedLayout.countryProgress.setVisibility(View.GONE);
            } else {
                signUpBinding.includedLayout.countryProgress.setVisibility(View.GONE);
            }
        });
    }
}
