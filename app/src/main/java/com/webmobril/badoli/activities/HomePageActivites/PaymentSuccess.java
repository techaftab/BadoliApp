package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityPaymentSuccessBinding;

public class PaymentSuccess extends AppCompatActivity {


    ActivityPaymentSuccessBinding paymentSuccessBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       paymentSuccessBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment_success);
       paymentSuccessBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.dark_pink));
       paymentSuccessBinding.commonHeader.header.setVisibility(View.VISIBLE);
       paymentSuccessBinding.commonHeader.badoliPhoneText.setText("Payment Success");
       paymentSuccessBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
    }
}
