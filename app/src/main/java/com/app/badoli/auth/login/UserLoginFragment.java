package com.app.badoli.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.SplashActivity;
import com.app.badoli.auth.forget.ForgotPasswordActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.LayoutLoginUserBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;

import java.util.Objects;

public class UserLoginFragment extends Fragment implements View.OnClickListener {

    LayoutLoginUserBinding binding;
    private AuthViewModel authViewModel;

    String deviceToken="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_login_user, container, false);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        View view = binding.getRoot();
        init();
        return view;
    }

    private void init() {
        binding.forgetPassword.setOnClickListener(this);
        deviceToken= LoginPre.getActiveInstance(getActivity()).getDevice_token();
        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""))) {
            binding.edPhone.setText(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""));
            binding.edPassword.setText(SplashActivity.getPreferences(Constant.REMEMBER_PASSWORD,""));
            binding.rememberMe.setChecked(true);
        }

        binding.loginButton.setOnClickListener(v -> {
           String phone = Objects.requireNonNull(binding.edPhone.getText()).toString();
           String password = Objects.requireNonNull(binding.edPassword.getText()).toString();
            if (setValidation(phone, password)) {
                getLoginResponse(phone, password);
            }
            if (binding.rememberMe.isChecked()) {
                SplashActivity.savePreferences(Constant.REMEMBER_PHONE,phone);
                SplashActivity.savePreferences(Constant.REMEMBER_PASSWORD,password);
            } else {
                SplashActivity.savePreferences(Constant.REMEMBER_PHONE,"");
                SplashActivity.savePreferences(Constant.REMEMBER_PASSWORD,"");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""))) {
            binding.edPhone.setText(SplashActivity.getPreferences(Constant.REMEMBER_PHONE,""));
            binding.edPassword.setText(SplashActivity.getPreferences(Constant.REMEMBER_PASSWORD,""));
            binding.rememberMe.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v==binding.forgetPassword){
            startActivity(new Intent(getActivity(), ForgotPasswordActivity.class)
            .putExtra(Constant.ROLE_ID,"3"));
        }
    }

    private boolean setValidation(String phone, String password) {
        if (phone.isEmpty()) {
            AppUtils.showSnackbar(getString(R.string.enter_phone), binding.parentLayout);
            return false;
        } else if( phone.length() < 7 || phone.length() > 15) {
            AppUtils.showSnackbar(getString(R.string.Phone_no_length), binding.parentLayout);
            return false;
        }
        if (password.isEmpty()) {
            AppUtils.showSnackbar(getString(R.string.password_empty), binding.parentLayout);
            return false;
        } else if (password.length() < 6 || password.length() > 16) {
            AppUtils.showSnackbar(getString(R.string.password_length), binding.parentLayout);
            return false;
        }
        return true;
    }


    private void getLoginResponse(String phone, String password) {
        ((LoginActivity)requireActivity()).showLoading(getResources().getString(R.string.logging_in));
        authViewModel.getLogin(phone,password,1,deviceToken).observe(getViewLifecycleOwner(), loginResponse -> {
            ((LoginActivity)requireActivity()).dismissLoading();
            if (!loginResponse.error) {
                UserData userData = new UserData(
                        loginResponse.result.user.getId(),
                        loginResponse.result.user.getAuth_token(),
                        loginResponse.result.user.getCountry_code(),
                        loginResponse.result.user.getCountry_id(),
                        loginResponse.result.user.getDevice_token(),
                        loginResponse.result.user.getEmail(),
                        loginResponse.result.user.getMobile(),
                        loginResponse.result.user.getName(),
                        loginResponse.result.user.getWallet_balance(),
                        loginResponse.result.user.getUser_image(),
                        loginResponse.result.user.getQrcode_image());
                PrefManager.getInstance(getActivity()).userLogin(userData);
                LoginPre.getActiveInstance(getActivity()).setIsLoggedIn(true);
                StartActivity();
            } else {
                if (loginResponse.getMessage()!=null) {
                    AppUtils.openPopup(getActivity(), R.style.Dialod_UpDown, "error", loginResponse.getMessage());
                }else {
                    AppUtils.openPopup(getActivity(), R.style.Dialod_UpDown, "error", getResources().getString(R.string.something_wrong));
                }
            }
        });
    }

    private void StartActivity() {
        Intent intent = new Intent(getActivity(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        requireActivity().finish();
    }
}
