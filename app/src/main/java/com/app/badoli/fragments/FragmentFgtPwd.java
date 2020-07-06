package com.app.badoli.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.databinding.ActivityForgetPasswordBinding;
import com.app.badoli.viewModels.AuthViewModel;

import java.util.Objects;

import xyz.hasnat.sweettoast.SweetToast;

public class FragmentFgtPwd extends Fragment implements View.OnClickListener {

    private String roleId;

    public FragmentFgtPwd () {

    }

    private AuthViewModel authViewModel;
    private ActivityForgetPasswordBinding forgotfragmentBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        forgotfragmentBinding = DataBindingUtil.inflate(inflater, R.layout.activity_forget_password,container,false);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        View view   = forgotfragmentBinding.getRoot();

        init();

        return  view;
    }

    private void init() {
        forgotfragmentBinding.btnFgtPassword.setOnClickListener(this);
        forgotfragmentBinding.imgBackForgotPassword.setOnClickListener(this);
        forgotfragmentBinding.txtBackFgt.setOnClickListener(this);
        forgotfragmentBinding.radiogroupUsertypeFgtpassword.setOnCheckedChangeListener((group, checkedId) -> {
            if (forgotfragmentBinding.rbUserFgtpassword.isChecked()){
                roleId="3";
            }
            if (forgotfragmentBinding.rbMerchantFgtpassword.isChecked()){
                roleId="4";
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==forgotfragmentBinding.btnFgtPassword){
            String mobile= Objects.requireNonNull(forgotfragmentBinding.edittextMobileFgt.getText()).toString();
            String newPassword= Objects.requireNonNull(forgotfragmentBinding.edittextNewpasswordFgt.getText()).toString();
            String confirmPassword= Objects.requireNonNull(forgotfragmentBinding.edittextConfirmpassFgt.getText()).toString();
            if (validatedetail(mobile,newPassword,confirmPassword)){
                changePassword(mobile,confirmPassword,roleId);
            }
        }
        if (v==forgotfragmentBinding.imgBackForgotPassword){
            ((LoginActivity) requireActivity()).updateView();
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            if (getActivity()!=null) {
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        }
        if (v==forgotfragmentBinding.txtBackFgt){
            ((LoginActivity) Objects.requireNonNull(getActivity())).updateView();
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            if (getActivity()!=null) {
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        }
    }

    private void showLoading(){
        AppUtils.hideKeyboardFrom(requireActivity());
        forgotfragmentBinding.fgtProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void dismissLoading(){
        forgotfragmentBinding.fgtProgressBar.setVisibility(View.INVISIBLE);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private boolean validatedetail(String mobile, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(mobile)){
            forgotfragmentBinding.edittextMobileFgt.requestFocus();
            forgotfragmentBinding.edittextMobileFgt.setError(getResources().getString(R.string.enter_mobile));
            SweetToast.error(getActivity(),getResources().getString(R.string.enter_mobile));
            return false;
        }
        if (TextUtils.isEmpty(roleId)||forgotfragmentBinding.radiogroupUsertypeFgtpassword.getCheckedRadioButtonId()==-1){
            SweetToast.error(getActivity(),getResources().getString(R.string.select_usertype));
            return false;
        }
        if (TextUtils.isEmpty(newPassword)){
            forgotfragmentBinding.edittextNewpasswordFgt.requestFocus();
            forgotfragmentBinding.edittextNewpasswordFgt.setError(getResources().getString(R.string.enter_new_password));
            SweetToast.error(getActivity(),getResources().getString(R.string.enter_new_password));
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            forgotfragmentBinding.edittextConfirmpassFgt.requestFocus();
            forgotfragmentBinding.edittextConfirmpassFgt.setError(getResources().getString(R.string.ed_confirm_password));
            SweetToast.error(getActivity(),getResources().getString(R.string.ed_confirm_password));
            return false;
        }
        if (!TextUtils.equals(newPassword,confirmPassword)){
            SweetToast.error(getActivity(),getResources().getString(R.string.password_does_not_match));
            return false;
        }
        return true;
    }

    private void changePassword(String mobile, String confirmPassword, String rolId) {
        showLoading();
        authViewModel.changePassword(mobile,confirmPassword,rolId).observe(this, changePasswordModel -> {
            dismissLoading();
            if (!changePasswordModel.error) {
                SweetToast.success(getActivity(),changePasswordModel.getMessage());
                forgotfragmentBinding.edittextMobileFgt.setText("");
                forgotfragmentBinding.edittextNewpasswordFgt.setText("");
                forgotfragmentBinding.edittextConfirmpassFgt.setText("");
                roleId="";
                forgotfragmentBinding.radiogroupUsertypeFgtpassword.clearCheck();
            } else {
                SweetToast.error(getActivity(),changePasswordModel.getMessage());
            }
        });
    }

}
