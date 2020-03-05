package com.app.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.badoli.R;
import com.app.badoli.databinding.ActivityAboutUsBinding;

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAboutUsBinding aboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        setSupportActionBar(aboutUsBinding.toolbarChangePassword);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        aboutUsBinding.toolbarChangePassword.setTitle("");
        aboutUsBinding.toolbarChangePassword.setNavigationOnClickListener(v -> {
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
