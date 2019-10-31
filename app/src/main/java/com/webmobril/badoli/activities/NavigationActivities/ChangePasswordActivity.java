package com.webmobril.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityChangePasswordBinding changePasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        init();

    }

    private void init() {
        changePasswordBinding.commonHeader.rlToolbarHome.setBackgroundColor(getResources().getColor(R.color.text_orange));
        changePasswordBinding.commonHeader.txtTitlePay.setVisibility(View.VISIBLE);
        changePasswordBinding.commonHeader.txtTitlePay.setText(getResources().getString(R.string.change_pwd));
        changePasswordBinding.commonHeader.lnPayBalance.setVisibility(View.GONE);
        changePasswordBinding.commonHeader.imgBackPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==changePasswordBinding.commonHeader.imgBackPay){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
