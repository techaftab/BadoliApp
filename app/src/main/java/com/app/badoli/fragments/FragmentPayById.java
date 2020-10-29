package com.app.badoli.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.PaymentActivities.PaymentActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentPayByidBinding;
import com.app.badoli.model.UserData;

import xyz.hasnat.sweettoast.SweetToast;

public class FragmentPayById extends Fragment implements View.OnClickListener {
    FragmentPayByidBinding binding;
    UserData userData;
    FragmentTransaction ft;
    Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_byid,container,false);
        View view   = binding.getRoot();
        userData= PrefManager.getInstance(getActivity()).getUserData();
        listener();
        binding.edMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    if (!editable.toString().equalsIgnoreCase("0")) {
                        binding.edMobile.setText("");
                    }
                }
                if (editable.toString().length() == 2) {
                    if (!editable.toString().equalsIgnoreCase("07")) {
                        String text = binding.edMobile.getText().toString();
                        binding.edMobile.setText(text.substring(0, text.length() - 1));
                        binding.edMobile.setSelection(text.substring(0, text.length() - 1).length());
                    }
                }
                if (editable.toString().length() == 3) {
                    String text = binding.edMobile.getText().toString();
                    if (editable.toString().equalsIgnoreCase("074")||editable.toString().equalsIgnoreCase("079")) {

                    } else {
                        binding.edMobile.setText(text.substring(0, 2));
                        binding.edMobile.setSelection(text.substring(0, text.length() - 1).length());
                    }
                }
            }
        });

        return  view;
    }

    @SuppressLint("SetTextI18n")
    private void listener() {
        binding.rlBack.setOnClickListener(this);
        ((HomePageActivity) requireContext()).updateHeader();
        binding.rlNext.setOnClickListener(this::goTransfer);
    }

    private void goTransfer(View view) {
        String merchantId=binding.edittextOtp.getText().toString();
        String mobile=binding.edMobile.getText().toString();

        if (isValid(merchantId,mobile)){
            if (!TextUtils.isEmpty(merchantId)){
                goActivity(PaymentActivity.class,mobile,"id");
            }else if (!TextUtils.isEmpty(mobile)){
                goActivity(PaymentActivity.class,mobile,"mobile");
            }
        }
    }

    private void goActivity(Class<?> activity, String mobile, String type) {
        Intent intent = new Intent(requireActivity(),activity);
        intent.putExtra(Constant.MOBILE_TRANSFER,mobile);
        intent.putExtra(Constant.TRANSFER_TYPE,type);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private boolean isValid(String merchantId, String mobile) {
        if (TextUtils.isEmpty(merchantId)&&TextUtils.isEmpty(mobile)){
            SweetToast.error(requireActivity(),getResources().getString(R.string.plz_enter_mobile_or_merhcnat_id));
            return false;
        }
        if (!TextUtils.isEmpty(merchantId)&&!TextUtils.isEmpty(mobile)){
            SweetToast.error(requireActivity(),getResources().getString(R.string.plz_enter_either_merchant_or_mobile));
            return false;
        }
        if (!TextUtils.isEmpty(merchantId)){
            if (merchantId.length()<5){
                SweetToast.error(requireActivity(),getResources().getString(R.string.plz_enter_valid_merchant));
                return false;
            }
        }
        if (!TextUtils.isEmpty(mobile)){
            if (mobile.length()<7||mobile.length()>15){
                SweetToast.error(requireActivity(),getResources().getString(R.string.enter_valid_phone));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v==binding.rlBack){
            if (getActivity()!=null) {
                AppUtils.hideKeyboardFrom(getActivity());
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                currentFragment = new FragmentMerchant();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }
}
