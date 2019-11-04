package com.webmobril.badoli.activities.PaymentActivities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityPaymentBinding;

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
    }

    @Override
    public void onClick(View v) {
        if (v==activityPaymentBinding.imgBackPayment){
            finish();
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }
}
