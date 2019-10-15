package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioGroup;

import com.webmobril.badoli.R;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityMerchantBinding;
import com.webmobril.badoli.model.UserData;

public class MerchantActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    ActivityMerchantBinding merchantBinding;
    UserData userData;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_merchant);
        merchantBinding = DataBindingUtil.setContentView(MerchantActivity.this, R.layout.activity_merchant);
        userData= PrefManager.getInstance(MerchantActivity.this).getUserData();
        handler=new Handler();
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        merchantBinding.imgBackMaintMerchant.setOnClickListener(this);
        merchantBinding.badoliPhoneTextMerchant.setText("Badolipay ("+userData.getMobile()+")");
        merchantBinding.txtWalletBalancetMerchant.setText("$ "+userData.getWallet_balance());
        merchantBinding.radiogroupMerchant.setOnCheckedChangeListener(this);
        merchantBinding.rbQrcode.setChecked(true);
    }

    @Override

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (merchantBinding.rbQrcode.isChecked()){
            merchantBinding.rbQrcode.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            merchantBinding.rbMobileId.setBackground(null);
            merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            merchantBinding.rbMobileId.setTextColor(getResources().getColor(R.color.text_orange));
            merchantBinding.rbQrcode.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                merchantBinding.lnQrCode.setVisibility(View.VISIBLE);
                merchantBinding.rlMobileid.setVisibility(View.GONE);
                merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },300);
        }
        if (merchantBinding.rbMobileId.isChecked()){
            merchantBinding.rbMobileId.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            merchantBinding.rbQrcode.setBackground(null);
            merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            merchantBinding.rbQrcode.setTextColor(getResources().getColor(R.color.text_orange));
            merchantBinding.rbMobileId.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                merchantBinding.lnQrCode.setVisibility(View.GONE);
                merchantBinding.rlMobileid.setVisibility(View.VISIBLE);
                merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },300);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }

    @Override
    public void onClick(View v) {
        if (v==merchantBinding.imgBackMaintMerchant){
            finish();
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
        }
    }
}
