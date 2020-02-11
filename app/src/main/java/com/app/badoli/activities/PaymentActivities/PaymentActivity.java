package com.app.badoli.activities.PaymentActivities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.badoli.R;
import com.app.badoli.config.Configuration;
import com.app.badoli.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPaymentBinding activityPaymentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPaymentBinding = DataBindingUtil.setContentView(PaymentActivity.this, R.layout.activity_payment);
        initView();
    }

    private void initView() {
        activityPaymentBinding.imgBackPayment.setOnClickListener(this);
        activityPaymentBinding.btnProceedPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==activityPaymentBinding.imgBackPayment){
            if (isPinPanelShown()){
                slideClosePin();
            }else {
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
        if (v==activityPaymentBinding.btnProceedPayment){
            slideUpPin();
        }
    }
    private boolean isPinPanelShown() {
        return activityPaymentBinding.rlPinPayment.getVisibility() == View.VISIBLE;
    }

    public void slideUpPin() {
        if (!isPinPanelShown()) {
            Animation bottomUp = AnimationUtils.loadAnimation(PaymentActivity.this,
                    R.anim.slide_up_dialog);

            activityPaymentBinding.rlPinPayment.startAnimation(bottomUp);
            activityPaymentBinding.rlPinPayment.setVisibility(View.VISIBLE);
            activityPaymentBinding.rlAmountPayment.setVisibility(View.GONE);
            activityPaymentBinding.lnUserDetails.setVisibility(View.INVISIBLE);
        }
    }
    private void slideClosePin() {
        Configuration.hideKeyboardFrom(PaymentActivity.this);
        Animation bottomDown = AnimationUtils.loadAnimation(PaymentActivity.this,
                R.anim.slide_bottom_dialog);

        activityPaymentBinding.rlPinPayment.startAnimation(bottomDown);
        activityPaymentBinding.rlPinPayment.setVisibility(View.GONE);
        activityPaymentBinding.rlAmountPayment.setVisibility(View.VISIBLE);
        activityPaymentBinding.lnUserDetails.setVisibility(View.VISIBLE);

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
