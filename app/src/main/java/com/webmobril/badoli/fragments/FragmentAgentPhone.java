package com.webmobril.badoli.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.HomePageActivites.AgentActivity;
import com.webmobril.badoli.config.Configuration;
import com.webmobril.badoli.databinding.FragmentAgentPhoneBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class FragmentAgentPhone extends Fragment implements View.OnClickListener {
    private FragmentAgentPhoneBinding fragmentAgentPhoneBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAgentPhoneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_agent_phone, container, false);
        View view = fragmentAgentPhoneBinding.getRoot();
        init();
        return view;
    }

    private void init() {
        fragmentAgentPhoneBinding.btnContinueProgress.setOnClickListener(this);
        fragmentAgentPhoneBinding.btnProgressPhone.setOnClickListener(this);
        fragmentAgentPhoneBinding.layoutConfrim.txtGobackPayment.setOnClickListener(this);
        fragmentAgentPhoneBinding.layoutConfrim.btnConfirmPayment.setOnClickListener(this);
        fragmentAgentPhoneBinding.layoutConfrim.btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==fragmentAgentPhoneBinding.btnContinueProgress) {
            String amount=fragmentAgentPhoneBinding.edittextAmount.getText().toString().trim();
            String countryCode=fragmentAgentPhoneBinding.autoCountryPhone.getText().toString();
            String phone=fragmentAgentPhoneBinding.edittextPhoneMobile.getText().toString();
            String gsmProvider=fragmentAgentPhoneBinding.autoGsmProvider.getText().toString();
            if (setValidation(amount,countryCode,phone,gsmProvider)) {
                View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm!=null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                continueDetail(amount,countryCode,phone,gsmProvider);
            }
        }

        if (v==fragmentAgentPhoneBinding.btnProgressPhone) {
            String amount = fragmentAgentPhoneBinding.edittextAmount.getText().toString().trim();
            String account = fragmentAgentPhoneBinding.edittextPhoneAccount.getText().toString().trim();
            if (setAccountValidation(amount,account)){
                View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm!=null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                progressPayment(amount,account);
            }
        }

        if (v==fragmentAgentPhoneBinding.layoutConfrim.txtGobackPayment) {
            slideCloseConfirm();
        }

        if (v==fragmentAgentPhoneBinding.layoutConfrim.btnConfirmPayment) {
            //api payment call
            View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm!=null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            slideUpStatus();
        }

        if (v==fragmentAgentPhoneBinding.layoutConfrim.btnDone) {
            slideCloseConfirm();
        }

    }

    private boolean setAccountValidation(String amount, String account) {
        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();
            return false;
        }else if (Float.valueOf(amount)<=0){
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_valid_amount), Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(account)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_reciever_acc), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean setValidation(String amount, String countryCode, String phone, String gsmProvider) {
        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();
            return false;
        }else if (Float.valueOf(amount)<=0){
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_valid_amount), Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(countryCode)){
            Toast.makeText(getActivity(), getResources().getString(R.string.select_country_code), Toast.LENGTH_LONG).show();
            return false;
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_phone), Toast.LENGTH_LONG).show();
            return false;
        }else if (phone.length()<10||phone.length()>15){
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_valid_phone), Toast.LENGTH_LONG).show();
            return false;
        }/*else if (TextUtils.isEmpty(gsmProvider)){
            Toast.makeText(getActivity(), getResources().getString(R.string.select_gsm_provider), Toast.LENGTH_LONG).show();
            return false;
        }*/
        return true;
    }

    private void continueDetail(String amount, String countryCode, String phone, String gsmProvider) {
        Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up_dialog);
        fragmentAgentPhoneBinding.lnPhoneDetails.setVisibility(View.GONE);
        fragmentAgentPhoneBinding.lnPhoneAccount.startAnimation(bottomUp);
        fragmentAgentPhoneBinding.lnPhoneAccount.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void progressPayment(String amount, String account) {
        ((AgentActivity) Objects.requireNonNull(getContext())).updateView();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String format = simpleDateFormat.format(new Date());
        fragmentAgentPhoneBinding.layoutConfrim.txtNameAgent.setText("Name from Api");
        fragmentAgentPhoneBinding.layoutConfrim.txtAmountPayment.setText("$ "+amount);
        fragmentAgentPhoneBinding.layoutConfrim.txtDatePayment.setText("on "+format);
        slideUpConfirm();
    }

    private boolean isPanelShown() {
        return fragmentAgentPhoneBinding.layoutConfrim.rlConfirmPayment.getVisibility() == View.VISIBLE;
    }
    private boolean isStatusShown() {
        return fragmentAgentPhoneBinding.layoutConfrim.rlPaymentNotice.getVisibility() == View.VISIBLE;
    }
    private void slideUpStatus() {
        if (!isStatusShown()) {
            ((AgentActivity) Objects.requireNonNull(getContext())).updateViewStatus();

            Configuration.hideKeyboardFrom(Objects.requireNonNull(getActivity()));

            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up_dialog);
            fragmentAgentPhoneBinding.lnPhoneAgent.setVisibility(View.GONE);
            fragmentAgentPhoneBinding.layoutConfrim.lnConfirmPayment.setVisibility(View.GONE);
            fragmentAgentPhoneBinding.layoutConfrim.rlPaymentNotice.startAnimation(bottomUp);
            fragmentAgentPhoneBinding.layoutConfrim.rlPaymentNotice.setVisibility(View.VISIBLE);
            fragmentAgentPhoneBinding.layoutConfrim.rlConfirmPayment.setVisibility(View.VISIBLE);
        }
    }

    private void slideUpConfirm() {
        if (!isPanelShown()) {
            View view = Objects.requireNonNull(getActivity()).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm!=null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            // Show the panel0
            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up_dialog);
            fragmentAgentPhoneBinding.lnPhoneAgent.setVisibility(View.GONE);
            fragmentAgentPhoneBinding.layoutConfrim.rlPaymentNotice.setVisibility(View.GONE);
            fragmentAgentPhoneBinding.layoutConfrim.rlConfirmPayment.startAnimation(bottomUp);
            fragmentAgentPhoneBinding.layoutConfrim.rlConfirmPayment.setVisibility(View.VISIBLE);
            fragmentAgentPhoneBinding.layoutConfrim.lnConfirmPayment.setVisibility(View.VISIBLE);
        }
    }
    private void slideCloseConfirm() {
        ((AgentActivity) Objects.requireNonNull(getContext())).updateViewNew();
        Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_bottom_dialog);

        fragmentAgentPhoneBinding.layoutConfrim.rlConfirmPayment.startAnimation(bottomUp);
        fragmentAgentPhoneBinding.layoutConfrim.rlConfirmPayment.setVisibility(View.GONE);
        fragmentAgentPhoneBinding.layoutConfrim.lnConfirmPayment.setVisibility(View.GONE);
        fragmentAgentPhoneBinding.layoutConfrim.rlPaymentNotice.setVisibility(View.GONE);
        fragmentAgentPhoneBinding.lnPhoneAgent.setVisibility(View.VISIBLE);
    }
}