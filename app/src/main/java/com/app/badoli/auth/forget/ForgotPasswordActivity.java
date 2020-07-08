package com.app.badoli.auth.forget;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityForgotPasswordBinding;
import com.app.badoli.viewModels.AuthViewModel;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityForgotPasswordBinding binding;

    private String roleId;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        init();
    }

    private void init() {
        roleId=getIntent().getStringExtra(Constant.ROLE_ID);
        binding.btnFgtPassword.setOnClickListener(this);
        binding.imgBackForgotPassword.setOnClickListener(this);
        binding.txtBackFgt.setOnClickListener(this);
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(ForgotPasswordActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onClick(View v) {
        if (v==binding.btnFgtPassword){
            String mobile= Objects.requireNonNull(binding.edittextMobileFgt.getText()).toString();
            String newPassword= Objects.requireNonNull(binding.edittextNewpasswordFgt.getText()).toString();
            String confirmPassword= Objects.requireNonNull(binding.edittextConfirmpassFgt.getText()).toString();
            if (validatedetail(mobile,newPassword,confirmPassword)){
                changePassword(mobile,confirmPassword,roleId);
            }
        }
        if (v==binding.imgBackForgotPassword||v==binding.txtBackFgt){
           finish();
        }
    }

    private boolean validatedetail(String mobile, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(mobile)){
            binding.edittextMobileFgt.requestFocus();
            binding.edittextMobileFgt.setError(getResources().getString(R.string.enter_mobile));
            AppUtils.showSnackbar(getString(R.string.phone_no_empty), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(newPassword)){
            binding.edittextNewpasswordFgt.requestFocus();
            binding.edittextNewpasswordFgt.setError(getResources().getString(R.string.enter_new_password));
            AppUtils.showSnackbar(getString(R.string.new_password_empty), binding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            binding.edittextConfirmpassFgt.requestFocus();
            binding.edittextConfirmpassFgt.setError(getResources().getString(R.string.ed_confirm_password));
            AppUtils.showSnackbar(getString(R.string.confirm_password_empty), binding.parentLayout);
            return false;
        }
        if (!TextUtils.equals(newPassword,confirmPassword)){
            AppUtils.showSnackbar(getString(R.string.password_does_not_match), binding.parentLayout);
            return false;
        }
        return true;
    }

    private void changePassword(String mobile, String confirmPassword, String rolId) {
        showLoading(getResources().getString(R.string.please_wait));
        authViewModel.changePassword(mobile,confirmPassword,rolId).observe(this, changePasswordModel -> {
            dismissLoading();
            if (!changePasswordModel.error) {
                AppUtils.openPopup(ForgotPasswordActivity.this, R.style.Dialod_UpDown, "back",
                        changePasswordModel.getMessage());
                Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
               /* bin.edittextMobileFgt.setText("");
                forgotfragmentBinding.edittextNewpasswordFgt.setText("");
                forgotfragmentBinding.edittextConfirmpassFgt.setText("");
                roleId="";
                forgotfragmentBinding.radiogroupUsertypeFgtpassword.clearCheck();*/
            } else {
                if (changePasswordModel.getMessage()!=null) {
                    AppUtils.openPopup(ForgotPasswordActivity.this, R.style.Dialod_UpDown, "error", changePasswordModel.getMessage());
                }else {
                    AppUtils.openPopup(ForgotPasswordActivity.this, R.style.Dialod_UpDown, "error",
                            getResources().getString(R.string.something_wrong));
                }
            }
        });
    }

}