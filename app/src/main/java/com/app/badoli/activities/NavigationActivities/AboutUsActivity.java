package com.app.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.app.badoli.R;
import com.app.badoli.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAboutUsBinding aboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        aboutUsBinding.commonHeader.rlToolbarHome.setBackgroundColor(getResources().getColor(R.color.text_orange));
        aboutUsBinding.commonHeader.txtTitlePay.setVisibility(View.VISIBLE);
        aboutUsBinding.commonHeader.txtTitlePay.setText(getResources().getString(R.string.about_us));
        aboutUsBinding.commonHeader.lnPayBalance.setVisibility(View.GONE);
        aboutUsBinding.commonHeader.imgBackPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==aboutUsBinding.commonHeader.imgBackPay){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
