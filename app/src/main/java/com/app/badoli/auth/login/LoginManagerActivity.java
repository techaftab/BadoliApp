package com.app.badoli.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.auth.signup.professional.ProfessionalSignup;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityLoginManagerBinding;
import com.app.badoli.viewModels.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class LoginManagerActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginManagerBinding binding;
   // private Integer[] images={R.drawable.img_one,R.drawable.img_two,R.drawable.img_three,R.drawable.img_four};

    String type="";
    private AuthViewModel authViewModel;
    ImageSlideAdapter imageSlideAdapter;
    private List<String> images=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_manager);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        init();
    }

    private void init() {
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
        if (v==binding.txtSignUp){
            Intent intent = new Intent(LoginManagerActivity.this, ProfessionalSignup.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        if (v==binding.imgBack){
            finish();
        }
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
                    images.add(Constant.BASE_URL + bannerModel.getLoginBanner().get(b).getImage());
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
}