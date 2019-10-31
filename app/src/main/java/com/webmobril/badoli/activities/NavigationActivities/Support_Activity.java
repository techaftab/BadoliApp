package com.webmobril.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivitySupportBinding;

public class Support_Activity extends AppCompatActivity implements View.OnClickListener {

    ActivitySupportBinding supportBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportBinding = DataBindingUtil.setContentView(this, R.layout.activity_support_);
        initView();

    }

    private void initView() {
        supportBinding.commonHeader.rlToolbarHome.setBackgroundColor(getResources().getColor(R.color.text_orange));
        supportBinding.commonHeader.lnPayBalance.setVisibility(View.GONE);
        supportBinding.commonHeader.txtTitlePay.setVisibility(View.VISIBLE);
        supportBinding.commonHeader.txtTitlePay.setText(getResources().getString(R.string.submit_request));
        supportBinding.commonHeader.imgBackPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==supportBinding.commonHeader.imgBackPay){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
