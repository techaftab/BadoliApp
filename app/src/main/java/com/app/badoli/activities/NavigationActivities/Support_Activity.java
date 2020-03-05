package com.app.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.badoli.R;
import com.app.badoli.databinding.ActivitySupportBinding;

import java.util.Objects;

public class Support_Activity extends AppCompatActivity implements View.OnClickListener {

    ActivitySupportBinding supportBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportBinding = DataBindingUtil.setContentView(this, R.layout.activity_support_);
        initView();

    }

    private void initView() {
        setSupportActionBar(supportBinding.toolbarChangePassword);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        supportBinding.toolbarChangePassword.setTitle("");
        supportBinding.toolbarChangePassword.setNavigationOnClickListener(v -> {
            Log.d("tag", "onClick : navigating back to back activity ");
            finish();
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
