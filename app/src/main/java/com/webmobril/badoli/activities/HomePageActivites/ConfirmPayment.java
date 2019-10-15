package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityConfirmPaymentBinding;

public class ConfirmPayment extends AppCompatActivity implements View.OnClickListener {

    ActivityConfirmPaymentBinding confirmPaymentBinding ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        confirmPaymentBinding = DataBindingUtil.setContentView(this,R.layout.activity_confirm_payment);
        confirmPaymentBinding.commonHeader.header.setVisibility(View.VISIBLE);
        confirmPaymentBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.dark_pink));
        confirmPaymentBinding.commonHeader.badoliPhoneText.setText("Confirm Payment");
        confirmPaymentBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
        confirmPaymentBinding.btnConfirmPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm_payment:
                Intent intent = new Intent(this, PaymentSuccess.class);
                startActivity(intent);
                break;
        }
    }
}
