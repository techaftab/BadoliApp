package com.app.badoli.auth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.badoli.R;
import com.app.badoli.auth.signup.professional.ProfessionalSignup;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityLoginManagerBinding;

import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class LoginManagerActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginManagerBinding binding;
    private Integer[] images={R.drawable.img_one,R.drawable.img_two,R.drawable.img_three,R.drawable.img_four};

    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_manager);
        init();
    }

    private void init() {
        type=getIntent().getStringExtra(Constant.TYPE_LOGIN);
        if (type.equals("staff")){
            binding.lnStaff.setVisibility(View.VISIBLE);
            binding.lnMerchant.setVisibility(View.GONE);
        }else {
            binding.lnStaff.setVisibility(View.GONE);
            binding.lnMerchant.setVisibility(View.VISIBLE);
        }
        binding.txtSignUp.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);

        ImageSlideAdapter imageSlideAdapter = new ImageSlideAdapter(LoginManagerActivity.this, images);
        binding.viewPager.setAdapter(imageSlideAdapter);
        imageSlideAdapter.notifyDataSetChanged();
        binding.viewPager.setInterval(3000);
        binding.viewPager.setDirection(AutoScrollViewPager.Direction.RIGHT);
        binding.viewPager.setCycle(true);
        binding.viewPager.setBorderAnimation(true);
        binding.viewPager.setSlideBorderMode(AutoScrollViewPager.SlideBorderMode.TO_PARENT);
        binding.viewPager.startAutoScroll();
        binding.wormDotsIndicator.setViewPager(binding.viewPager);
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
}