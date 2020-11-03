package com.app.badoli.switchstaff;

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
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentSwitchLoginBinding;
import com.app.badoli.model.StaffData;
import com.app.badoli.model.UserData;
import com.app.badoli.staff.StaffHomeActivity;
import com.app.badoli.viewModels.AuthViewModel;

import xyz.hasnat.sweettoast.SweetToast;

public class SwitchLoginFragment extends Fragment {

    private FragmentSwitchLoginBinding binding;

    String staffName;
    private AuthViewModel authViewModel;
    UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_switch_login,container,false);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userData= PrefManager.getInstance(requireActivity()).getUserData();
        View view   = binding.getRoot();
        ((StaffSwitchActivity)requireActivity()).updateTitle(getResources().getString(R.string.list_availble));
        init();
        return  view;
    }

    private void init() {
        staffName=getArguments().getString(Constant.STAFF_NAME);
        binding.btnStaffContinue.setOnClickListener(this::validateDetails);
        ((StaffSwitchActivity)requireActivity()).updateTitle(staffName/*+"("+userData.getName()+")"*/);
    }

    private void validateDetails(View view) {
        String staffCode=binding.edittextStaffCode.getText().toString();
        String staffPin=binding.edittextPincode.getText().toString();
        if (isStaffValid(staffCode, staffPin)) {
            loginStaff(staffCode,staffPin);
        }
    }
    private boolean isStaffValid(String staffCode, String staffPin) {
        if (TextUtils.isEmpty(staffCode)){
            AppUtils.showSnackbar(getString(R.string.staff_code_empty), binding.parentLayout);
            SweetToast.error(requireActivity(),getString(R.string.staff_code_empty));
            return false;
        }
        if (TextUtils.isEmpty(staffPin)){
            SweetToast.error(requireActivity(),getString(R.string.staff_pin_empty));
            AppUtils.showSnackbar(getString(R.string.staff_pin_empty), binding.parentLayout);
            return false;
        }
        return true;
    }

    private void loginStaff(String staffCode, String staffPin) {
       // AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"underdevelop","Available Soon");
        ((StaffSwitchActivity)requireActivity()).showLoading(getResources().getString(R.string.validating));
        authViewModel.loginStaff(staffCode,staffPin)
                .observe(this, loginResponse -> {
                    ((StaffSwitchActivity)requireActivity()).dismissLoading();
                    if (loginResponse!=null&&!loginResponse.getError()) {
                        SweetToast.success(requireActivity(),loginResponse.getMessage());
                        StaffData userData = new StaffData(
                                String.valueOf(loginResponse.getResult().getId()),
                                loginResponse.getResult().getStaff_name(),
                                loginResponse.getResult().getAgent_code(),
                                loginResponse.getResult().getWallet_balance(),
                                String.valueOf(loginResponse.getResult().getAgent_pin()),
                                "4");
                        PrefManager.getInstance(requireActivity()).staffLogin(userData);
                        Intent intent = new Intent(requireActivity(), StaffHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                       // requireActivity().finish();
                    }else {
                        if (loginResponse!=null&&!TextUtils.isEmpty(loginResponse.getMessage())){
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",loginResponse.getMessage());
                        }else {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",getResources().getString(R.string.something_went_wrong));
                        }
                    }
                });
    }

}
