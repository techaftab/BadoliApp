package com.webmobril.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivitySupportBinding;

public class Support_Activity extends AppCompatActivity {

    ActivitySupportBinding supportBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportBinding = DataBindingUtil.setContentView(this, R.layout.activity_support_);
        supportBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.text_orange));
        supportBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
        supportBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        supportBinding.commonHeader.badoliPhoneText.setText("Submit Request");
    }
}
