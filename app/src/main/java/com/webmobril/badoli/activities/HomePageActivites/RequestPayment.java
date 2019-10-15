package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityRequestPaymentBinding;
import com.webmobril.badoli.model.UserData;

public class RequestPayment extends AppCompatActivity implements View.OnClickListener {

    ActivityRequestPaymentBinding requestPaymentBinding ;
    UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_request_payment);
        userData= PrefManager.getInstance(RequestPayment.this).getUserData();
     /*   requestPaymentBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.dark_pink));
        requestPaymentBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        requestPaymentBinding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        requestPaymentBinding.commonHeader.balanceLayout.setBackgroundColor(getResources().getColor(R.color.dark_pink));
        selectLayoutQrCodeInit();
        requestPaymentBinding.selectMobileId.setOnClickListener(this);
        requestPaymentBinding.selectQrCode.setOnClickListener(this);
        requestPaymentBinding.btnProgress.setOnClickListener(this);*/
        loadView();
    }

    @SuppressLint("SetTextI18n")
    private void loadView() {
        requestPaymentBinding.selectMobileId.setOnClickListener(this);
        requestPaymentBinding.selectQrCode.setOnClickListener(this);
        requestPaymentBinding.btnProgress.setOnClickListener(this);
        requestPaymentBinding.headerPay.imgBackPay.setOnClickListener(this);
        requestPaymentBinding.headerPay.txtTitlePay.setText(userData.getName()+" ("+userData.getMobile()+")");
        requestPaymentBinding.headerPay.txtTitlePay.setVisibility(View.GONE);
        requestPaymentBinding.headerPay.txtPayWalletBalance.setText("$ "+userData.getWallet_balance());
    }

    void selectLayoutQrCodeInit(){

        requestPaymentBinding.qrCodeText.setTextColor(getResources().getColor(R.color.white));
        requestPaymentBinding.mobileIdText.setTextColor(getResources().getColor(R.color.text_orange));
        requestPaymentBinding.selectQrCode.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
        requestPaymentBinding.selectMobileId.setBackground(getResources().getDrawable(R.drawable.white_round_right_layout));

    }

    void selectLayoutMobileInit(){

        requestPaymentBinding.qrCodeText.setTextColor(getResources().getColor(R.color.text_orange));
        requestPaymentBinding.mobileIdText.setTextColor(getResources().getColor(R.color.white));
        requestPaymentBinding.selectQrCode.setBackground(getResources().getDrawable(R.drawable.white_round_left_layout));
        requestPaymentBinding.selectMobileId.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));

    }


    @Override
    public void onClick(View v) {
        if (v==requestPaymentBinding.headerPay.imgBackPay){
            finish();
        }
        switch (v.getId()){

            case R.id.select_qr_code:
                requestPaymentBinding.layoutQRCode.setVisibility(View.VISIBLE);
                requestPaymentBinding.layoutMobileId.setVisibility(View.GONE);
                selectLayoutQrCodeInit();
                break;
            case R.id.select_mobile_id:
                requestPaymentBinding.layoutQRCode.setVisibility(View.GONE);
                requestPaymentBinding.layoutMobileId.setVisibility(View.VISIBLE);
                selectLayoutMobileInit();
                break;
            case R.id.btn_progress:
                Intent intent = new Intent(this, EnterAmountActivity.class);
                startActivity(intent);
                break;


        }

    }
}
