package com.app.badoli.auth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.badoli.R;
import com.app.badoli.auth.signup.professional.ProfessionalSignup;
import com.app.badoli.databinding.ActivityLoginManagerBinding;

public class LoginManagerActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_manager);
        init();
    }

    private void init() {
        binding.txtSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==binding.txtSignUp){
            Intent intent = new Intent(LoginManagerActivity.this, ProfessionalSignup.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }
}