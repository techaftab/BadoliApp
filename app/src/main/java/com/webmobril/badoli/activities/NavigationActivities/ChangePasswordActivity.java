package com.webmobril.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding changePasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        changePasswordBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.text_orange));
        changePasswordBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        changePasswordBinding.commonHeader.badoliPhoneText.setText("Change Password");
        changePasswordBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
    }
}
