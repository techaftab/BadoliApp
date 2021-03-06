package com.app.badoli.fragments;

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
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.FragmentAgentPhoneBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.TranferViewModel;

import xyz.hasnat.sweettoast.SweetToast;

public class FragmentAgentPhone extends Fragment implements View.OnClickListener, updateBalance {
    private FragmentAgentPhoneBinding fragmentAgentPhoneBinding;
    private UserData userData;

    private TranferViewModel tranferViewModel;

    private WebService webService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAgentPhoneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_agent_phone, container, false);
        View view = fragmentAgentPhoneBinding.getRoot();
        webService = new WebService(this);
        userData = PrefManager.getInstance(getActivity()).getUserData();
        tranferViewModel = new ViewModelProvider(this).get(TranferViewModel.class);
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
            String phone=fragmentAgentPhoneBinding.edittextPhoneMobile.getText().toString();
            if (setValidation(amount,phone)) {
                AppUtils.hideKeyboardFrom(requireActivity());
                transferMobile(amount,phone,userData.getId());
            }
        }
       /* if (v==fragmentAgentPhoneBinding.btnContinueProgress) {
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
            ((AgentActivity) Objects.requireNonNull(getContext())).loadFragment(new FragmentAgentPhone(),R.anim.left_in,R.anim.right_out);
        }*/
    }

  /*private boolean setAccountValidation(String amount, String account) {
        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();
            return false;
        } else if (Float.valueOf(amount)<=0){
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_valid_amount), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(account)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_reciever_acc), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }*/

    private boolean setValidation(String amount, String phone) {
        float balance= Float.parseFloat(PrefManager.getInstance(getActivity()).getWalletBalance());
        if (TextUtils.isEmpty(amount)) {
            fragmentAgentPhoneBinding.edittextAmount.requestFocus();
            SweetToast.error(getActivity(),getResources().getString(R.string.enter_amount));
            fragmentAgentPhoneBinding.edittextAmount.setError(getResources().getString(R.string.enter_amount));
            return false;
        }else if (Float.parseFloat(amount)<=0){
            fragmentAgentPhoneBinding.edittextAmount.requestFocus();
            SweetToast.error(getActivity(),getResources().getString(R.string.enter_valid_amount));
            fragmentAgentPhoneBinding.edittextAmount.setError(getResources().getString(R.string.enter_valid_amount));
            return false;
        }else if (Float.parseFloat(amount)<10){
            fragmentAgentPhoneBinding.edittextAmount.requestFocus();
            SweetToast.error(getActivity(),getResources().getString(R.string.amount_should_10));
            fragmentAgentPhoneBinding.edittextAmount.setError(getResources().getString(R.string.amount_should_10));
            return false;
        }else if (Float.parseFloat(amount)>balance){
            fragmentAgentPhoneBinding.edittextAmount.requestFocus();
            SweetToast.error(getActivity(),getResources().getString(R.string.wallet_balance_low));
            fragmentAgentPhoneBinding.edittextAmount.setError(getResources().getString(R.string.wallet_balance_low));
            return false;
        }else if (TextUtils.isEmpty(phone)){
            fragmentAgentPhoneBinding.edittextPhoneMobile.requestFocus();
            SweetToast.error(getActivity(),getResources().getString(R.string.enter_phone));
            fragmentAgentPhoneBinding.edittextPhoneMobile.setError(getResources().getString(R.string.enter_phone));
            return false;
        }else if (phone.length()<7||phone.length()>15){
            fragmentAgentPhoneBinding.edittextPhoneMobile.requestFocus();
            SweetToast.error(getActivity(),getResources().getString(R.string.enter_valid_phone));
            fragmentAgentPhoneBinding.edittextPhoneMobile.setError(getResources().getString(R.string.enter_valid_phone));
            return false;
        }
        return true;
    }

    private void showLoading(){
        AppUtils.hideKeyboardFrom(requireActivity());
        fragmentAgentPhoneBinding.progressWalletTransfer.setVisibility(View.VISIBLE);
        if (getActivity()!=null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void dismissLoading(){
        fragmentAgentPhoneBinding.progressWalletTransfer.setVisibility(View.INVISIBLE);
        if (getActivity()!=null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void transferMobile(String amount, String phone, String userId) {
        showLoading();
        tranferViewModel.transferMobile(amount, phone, userId).observe(this, walletTransfer -> {
            dismissLoading();
            webService.updateBalance(userId);
            if (!walletTransfer.error) {
                fragmentAgentPhoneBinding.edittextPhoneAccount.setText("");
                fragmentAgentPhoneBinding.edittextPhoneMobile.setText("");
                fragmentAgentPhoneBinding.edittextAmount.setText("");
                SweetToast.success(getActivity(),walletTransfer.getMessage());
                AppUtils.openPopupPaymentStatus(getActivity(),true,walletTransfer.getMessage(),amount,phone, userData.getUserType());
            } else {
                SweetToast.error(getActivity(),walletTransfer.getMessage());
                AppUtils.openPopupPaymentStatus(getActivity(),false,walletTransfer.getMessage(),amount,phone, userData.getUserType());
            }
        });
    }


 /*   private void continueDetail(String amount, String phone) {
        Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up_dialog);
        fragmentAgentPhoneBinding.lnPhoneDetails.setVisibility(View.GONE);
        fragmentAgentPhoneBinding.lnPhoneAccount.startAnimation(bottomUp);
        fragmentAgentPhoneBinding.lnPhoneAccount.setVisibility(View.VISIBLE);
    }*/

   /* @SuppressLint("SetTextI18n")
    private void progressPayment(String amount, String account) {
        ((AgentActivity) Objects.requireNonNull(getContext())).updateView();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String format = simpleDateFormat.format(new Date());
        fragmentAgentPhoneBinding.layoutConfrim.txtNameAgent.setText("Name from Api");
        fragmentAgentPhoneBinding.layoutConfrim.txtAmountPayment.setText("$ "+amount);
        fragmentAgentPhoneBinding.layoutConfrim.txtDatePayment.setText("on "+format);
        slideUpConfirm();
    }*/

   /* private boolean isPanelShown() {
        return fragmentAgentPhoneBinding.layoutConfrim.rlConfirmPayment.getVisibility() == View.VISIBLE;
    }

    private boolean isStatusShown() {
        return fragmentAgentPhoneBinding.layoutConfrim.rlPaymentNotice.getVisibility() == View.VISIBLE;
    }*/

   /* private void slideUpStatus() {
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
            //Show the panel
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
    }*/

    @Override
    public void onUpdateBalance(String balance) {
       // ((HomePageActivity) Objects.requireNonNull(getActivity())).updateBalance(balance);
    }
}
