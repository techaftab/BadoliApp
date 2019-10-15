package com.webmobril.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity {
    ActivityAboutUsBinding aboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        aboutUsBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.text_orange));
        aboutUsBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        aboutUsBinding.commonHeader.badoliPhoneText.setText("About Us");
        aboutUsBinding.commonHeader.balanceLayout.setVisibility(View.GONE);

    }
}
