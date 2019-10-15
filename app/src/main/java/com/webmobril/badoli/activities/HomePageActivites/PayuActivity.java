package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityPayuBinding;
import com.webmobril.badoli.model.UserData;

public class  PayuActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPayuBinding payuBinding;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        payuBinding = DataBindingUtil.setContentView(this, R.layout.activity_payu);
        setUpListener(payuBinding);
        userData= PrefManager.getInstance(PayuActivity.this).getUserData();

        loadView();

    }

    @SuppressLint("SetTextI18n")
    private void loadView() {
        payuBinding.headerPay.imgBackPay.setOnClickListener(this);
        payuBinding.headerPay.txtTitlePay.setText(userData.getName()+" ("+userData.getMobile()+")");
        payuBinding.headerPay.txtPayWalletBalance.setText("$ "+userData.getWallet_balance());
    }

    private void setUpListener(ActivityPayuBinding payuBinding) {
        payuBinding.payById.setOnClickListener(this);
        payuBinding.userRequestPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==payuBinding.headerPay.imgBackPay){
            finish();
          //  overridePendingTransition(R.anim.);
        }
        switch (v.getId()) {
            case R.id.pay_by_id:
                Intent intent = new Intent(PayuActivity.this, PayByPhoneActivity.class);
                startActivity(intent);
                break;

            case R.id.user_request_phone:
                Intent request = new Intent(PayuActivity.this, RequestPayment.class);
                startActivity(request);
                break;
        }
    }
}
