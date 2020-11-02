package com.app.badoli.activities.PaymentActivities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPaymentBinding binding;
    String transferTo,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PaymentActivity.this, R.layout.activity_payment);
        initView();
    }

    private void initView() {
        transferTo=getIntent().getStringExtra(Constant.MOBILE_TRANSFER);
        type=getIntent().getStringExtra(Constant.TRANSFER_TYPE);
        binding.imgBackPayment.setOnClickListener(this);
        binding.btnProceedPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==binding.imgBackPayment){
            if (isPinPanelShown()){
                slideClosePin();
            }else {
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
        if (v==binding.btnProceedPayment){
            slideUpPin();
        }
    }
    private boolean isPinPanelShown() {
        return binding.rlPinPayment.getVisibility() == View.VISIBLE;
    }

    public void slideUpPin() {
        if (!isPinPanelShown()) {
            Animation bottomUp = AnimationUtils.loadAnimation(PaymentActivity.this,
                    R.anim.slide_up_dialog);

            binding.rlPinPayment.startAnimation(bottomUp);
            binding.rlPinPayment.setVisibility(View.VISIBLE);
            binding.rlAmountPayment.setVisibility(View.GONE);
            binding.lnUserDetails.setVisibility(View.INVISIBLE);
        }
    }
    private void slideClosePin() {
        AppUtils.hideKeyboardFrom(PaymentActivity.this);
        Animation bottomDown = AnimationUtils.loadAnimation(PaymentActivity.this,
                R.anim.slide_bottom_dialog);

        binding.rlPinPayment.startAnimation(bottomDown);
        binding.rlPinPayment.setVisibility(View.GONE);
        binding.rlAmountPayment.setVisibility(View.VISIBLE);
        binding.lnUserDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isPinPanelShown()){
            slideClosePin();
        }else {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }
}
