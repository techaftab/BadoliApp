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
import com.app.badoli.auth.forget.ForgotPasswordActivity;
import com.app.badoli.auth.otp.VerifyOtpActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.LayoutLoginUserBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;
import com.google.firebase.iid.FirebaseInstanceId;

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
        try {
            if (!TextUtils.isEmpty(PrefManager.getInstance(getActivity()).getPHONE())) {
                binding.edPhone.setText(PrefManager.getInstance(getActivity()).getPHONE());
                binding.edPassword.setText(PrefManager.getInstance(getActivity()).getPassword());
                binding.rememberMe.setChecked(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String token = instanceIdResult.getToken();
            LoginPre.getActiveInstance(getActivity()).setDevice_token(token);
            deviceToken= LoginPre.getActiveInstance(getActivity()).getDevice_token();
        });

        binding.loginButton.setOnClickListener(v -> {
           String phone = Objects.requireNonNull(binding.edPhone.getText()).toString();
           String password = Objects.requireNonNull(binding.edPassword.getText()).toString();
            if (setValidation(phone, password)) {
                getLoginResponse(phone, password);
            }
            if (binding.rememberMe.isChecked()) {
                PrefManager.getInstance(getActivity()).setPhoneNumber(phone);
                PrefManager.getInstance(getActivity()).setPassword(password);
            } else {
                PrefManager.getInstance(getActivity()).setPhoneNumber("");
                PrefManager.getInstance(getActivity()).setPassword("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(PrefManager.getInstance(getActivity()).getPHONE())) {
            binding.edPhone.setText(PrefManager.getInstance(getActivity()).getPHONE());
            binding.edPassword.setText(PrefManager.getInstance(getActivity()).getPassword());
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
        } else if (password.length() !=4) {
            AppUtils.showSnackbar(getString(R.string.password_length), binding.parentLayout);
            return false;
        }
        return true;
    }

    private void getLoginResponse(String phone, String password) {
        ((LoginActivity)requireActivity()).showLoading(getResources().getString(R.string.logging_in));
        authViewModel.getLogin(phone,password,1,deviceToken,"3").observe(getViewLifecycleOwner(), loginResponse -> {
            ((LoginActivity)requireActivity()).dismissLoading();
            if (loginResponse!=null&&!loginResponse.error) {
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
                        loginResponse.result.user.getQrcode_image(),
                        "3");
                PrefManager.getInstance(getActivity()).userLogin(userData);
                LoginPre.getActiveInstance(getActivity()).setIsLoggedIn(true);
                LoginPre.getActiveInstance(getActivity()).setLoginType("3");
                StartActivity();
            } else {
                if (loginResponse!=null&&loginResponse.getMessage()!=null) {
                    if (loginResponse.getMobile_verify()==2){
                        String otp=loginResponse.getOtp();
                        String id = String.valueOf(loginResponse.getId());
                        LoginPre.getActiveInstance(getActivity()).setSignup_id(id);
                        Intent intent = new Intent(getActivity(), VerifyOtpActivity.class);
                        intent.putExtra(Constant.VERIFY_OTP,otp);
                        intent.putExtra(Constant.MOBILE,phone);
                        intent.putExtra(Constant.ROLES_ID,"3");
                        startActivity(intent);
                    }else {
                        AppUtils.openPopup(getActivity(), R.style.Dialod_UpDown, "error", loginResponse.getMessage());
                    }
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
