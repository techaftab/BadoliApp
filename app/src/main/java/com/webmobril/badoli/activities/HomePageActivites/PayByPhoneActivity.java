package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityPayByPhoneBinding;
import com.webmobril.badoli.model.UserData;

public class PayByPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPayByPhoneBinding payuphoneBinding;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        payuphoneBinding = DataBindingUtil.setContentView(this, R.layout.activity_pay_by_phone);
     /*   payuphoneBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.dark_pink));
        payuphoneBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        payuphoneBinding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        payuphoneBinding.commonHeader.balanceLayout.setBackgroundColor(getResources().getColor(R.color.dark_pink));*/
     userData= PrefManager.getInstance(PayByPhoneActivity.this).getUserData();

        loadView();
    }
    @SuppressLint("SetTextI18n")
    private void loadView() {
        payuphoneBinding.headerPay.imgBackPay.setOnClickListener(this);
        payuphoneBinding.headerPay.txtTitlePay.setText(userData.getName()+" ("+userData.getMobile()+")");
        payuphoneBinding.headerPay.txtTitlePay.setVisibility(View.GONE);
        payuphoneBinding.headerPay.txtPayWalletBalance.setText("$ "+userData.getWallet_balance());
    }

    @Override
    public void onClick(View v) {
        if (v==payuphoneBinding.headerPay.imgBackPay){
            finish();
        }
    }
}
