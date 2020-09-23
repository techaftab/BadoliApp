package com.app.badoli.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.auth.otp.VerifyOtpActivity;
import com.app.badoli.auth.signup.professional.ProfessionalSignup;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityLoginManagerBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class LoginManagerActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginManagerBinding binding;
   // private Integer[] images={R.drawable.img_one,R.drawable.img_two,R.drawable.img_three,R.drawable.img_four};
    String type="";
    private AuthViewModel authViewModel;
    ImageSlideAdapter imageSlideAdapter;
    private List<String> images=new ArrayList<>();
    private String deviceToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_manager);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        init();
    }

    private void init() {
        deviceToken= LoginPre.getActiveInstance(LoginManagerActivity.this).getDevice_token();

        type=getIntent().getStringExtra(Constant.TYPE_LOGIN);
        if (type != null) {
            if (type.equals("staff")){
                binding.lnStaff.setVisibility(View.VISIBLE);
                binding.lnMerchant.setVisibility(View.GONE);
            }else {
                binding.lnStaff.setVisibility(View.GONE);
                binding.lnMerchant.setVisibility(View.VISIBLE);
            }
        }
        binding.txtSignUp.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.layoutLogin.loginButton.setOnClickListener(this);
        binding.btnStaffContinue.setOnClickListener(this);

        imageSlideAdapter = new ImageSlideAdapter(LoginManagerActivity.this, images);
        binding.viewPager.setAdapter(imageSlideAdapter);
        imageSlideAdapter.notifyDataSetChanged();
        binding.viewPager.setInterval(3000);
        binding.viewPager.setDirection(AutoScrollViewPager.Direction.RIGHT);
        binding.viewPager.setCycle(true);
        binding.viewPager.setBorderAnimation(true);
        binding.viewPager.setSlideBorderMode(AutoScrollViewPager.SlideBorderMode.TO_PARENT);
        binding.viewPager.startAutoScroll();
        binding.wormDotsIndicator.setViewPager(binding.viewPager);

        if (AppUtils.hasNetworkConnection(LoginManagerActivity.this)){
            getBanner();
        }else {
            AppUtils.openPopup(LoginManagerActivity.this,R.style.Dialod_UpDown,"internetError",getResources().getString(R.string.no_internet));
        }
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(LoginManagerActivity.this);
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
        if (v==binding.btnStaffContinue){
            String staffCode=binding.edittextStaffCode.getText().toString();
            String staffPin=binding.edittextPincode.getText().toString();
            if (isStaffValid(staffCode, staffPin)) {
                loginStaff(staffCode,staffPin);
            }
        }
        if (v==binding.txtSignUp){
            Intent intent = new Intent(LoginManagerActivity.this, ProfessionalSignup.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        if (v==binding.layoutLogin.loginButton){
            String phone = Objects.requireNonNull(binding.layoutLogin.edPhone.getText()).toString();
            String password = Objects.requireNonNull(binding.layoutLogin.edPassword.getText()).toString();
            if (setValidation(phone, password)) {
                loginMerchant(phone, password);
            }
            if (binding.layoutLogin.rememberMe.isChecked()) {
                PrefManager.getInstance(LoginManagerActivity.this).setPhoneNumber(phone);
                PrefManager.getInstance(LoginManagerActivity.this).setPassword(password);
            } else {
                PrefManager.getInstance(LoginManagerActivity.this).setPhoneNumber("");
                PrefManager.getInstance(LoginManagerActivity.this).setPassword("");
            }
        }
        if (v==binding.imgBack){
            finish();
        }
    }

    private void loginStaff(String staffCode, String staffPin) {
        AppUtils.openPopup(LoginManagerActivity.this,R.style.Dialod_UpDown,"underdevelop","Available Soon");
    }

    private boolean isStaffValid(String staffCode, String staffPin) {
        if (TextUtils.isEmpty(staffCode)){
            AppUtils.showSnackbar(getString(R.string.staff_code_empty), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(staffPin)){
            AppUtils.showSnackbar(getString(R.string.staff_pin_empty), binding.parentLayout);
            return false;
        }
        return true;
    }

    private boolean setValidation(String phone, String password) {
        if (phone.isEmpty()) {
            AppUtils.showSnackbar(getString(R.string.enter_phone), binding.parentLayout);
            return false;
        } else if( phone.length() < 7 || phone.length() > 15) {
            AppUtils.showSnackbar(getString(R.string.Phone_no_length), binding.parentLayout);
            return false;
        }
        if (password.isEmpty()) {
            AppUtils.showSnackbar(getString(R.string.password_empty), binding.parentLayout);
            return false;
        } else if (password.length()!=4) {
            AppUtils.showSnackbar(getString(R.string.password_length), binding.parentLayout);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getBanner() {
        showLoading(getResources().getString(R.string.please_wait));
        authViewModel.getBanner().observe(this, bannerModel -> {
            dismissLoading();
            if (!bannerModel.getError()) {
                images.clear();
                for (int b=0;b<bannerModel.getLoginBanner().size();b++) {
                    images.add(Constant.IMAGE_URL + bannerModel.getLoginBanner().get(b).getImage());
                }
                imageSlideAdapter.notifyDataSetChanged();
            } else {
             /*   if (bannerModel.getMessage()!=null) {
                    AppUtils.openPopup(LoginManagerActivity.this, R.style.Dialod_UpDown, "error",
                            bannerModel.getMessage());
                }else {*/
                    AppUtils.openPopup(LoginManagerActivity.this, R.style.Dialod_UpDown, "error",
                            getResources().getString(R.string.something_wrong));
               // }
            }
        });
    }

    private void loginMerchant(String phone, String password) {
        showLoading(getResources().getString(R.string.logging_in));
        authViewModel.getLogin(phone,password,1,deviceToken,"4")
                .observe(this, loginResponse -> {
            dismissLoading();
            if (loginResponse!=null&&!loginResponse.error) {
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
                        loginResponse.result.user.getQrcode_image(),
                        "4");
                PrefManager.getInstance(LoginManagerActivity.this).userLogin(userData);
                LoginPre.getActiveInstance(LoginManagerActivity.this).setIsLoggedIn(true);
                StartActivity();
            } else {
                if (loginResponse!=null&&loginResponse.getMessage()!=null) {
                    if (loginResponse.getMobile_verify()==2){
                        String otp=loginResponse.getOtp();
                        String id = String.valueOf(loginResponse.getId());
                        LoginPre.getActiveInstance(LoginManagerActivity.this).setSignup_id(id);
                        Intent intent = new Intent(LoginManagerActivity.this, VerifyOtpActivity.class);
                        intent.putExtra(Constant.VERIFY_OTP,otp);
                        intent.putExtra(Constant.MOBILE,phone);
                        intent.putExtra(Constant.ROLES_ID,"4");
                        startActivity(intent);
                    }else {
                        AppUtils.openPopup(LoginManagerActivity.this, R.style.Dialod_UpDown, "error", loginResponse.getMessage());
                    }
                }else {
                    AppUtils.openPopup(LoginManagerActivity.this, R.style.Dialod_UpDown, "error", getResources().getString(R.string.something_wrong));
                }
            }
        });
    }

    private void StartActivity() {
        Intent intent = new Intent(LoginManagerActivity.this, ProfessionalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}