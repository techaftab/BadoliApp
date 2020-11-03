package com.app.badoli.activities.PaymentActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityConfirmPaymentBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.TranferViewModel;

import xyz.hasnat.sweettoast.SweetToast;

public class ConfirmPaymentActivity extends AppCompatActivity implements updateBalance {

    private ActivityConfirmPaymentBinding binding;

    String requestType,mobile,name,amount;
    private UserData userData;
    private TranferViewModel tranferViewModel;
    private WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ConfirmPaymentActivity.this, R.layout.activity_confirm_payment);
        webService = new WebService(this);
        tranferViewModel = new ViewModelProvider(this).get(TranferViewModel.class);
        userData = PrefManager.getInstance(ConfirmPaymentActivity.this).getUserData();
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        requestType=getIntent().getStringExtra(Constant.TRANSFER_TYPE);
        mobile=getIntent().getStringExtra(Constant.MOBILE_TRANSFER);
        name=getIntent().getStringExtra(Constant.NAME_TRANSFER);
        amount=getIntent().getStringExtra(Constant.TRANSFER_AMOUNT);

        binding.txtMerchantName.setText(name);
        binding.txtMerchantId.setText(mobile);
        binding.txtOtherFees.setText("0.00");
        binding.txtAmountDetaisl.setText(amount+" FCFA");
        binding.txtTotalAmountDetail.setText(amount+" FCFA");
        binding.txtAmount.setText(amount+" FCFA");

        binding.btnConfirmPaymentDetails.setOnClickListener(this::tranferPayment);
        binding.txtBack.setOnClickListener(this::back);
        binding.imgBackPayment.setOnClickListener(this::back);
    }

    private void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void tranferPayment(View view) {
        transferMobile(amount,mobile,userData.getId());
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(ConfirmPaymentActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void transferMobile(String amount, String phone, String userId) {
        showLoading(getResources().getString(R.string.transferring));
        tranferViewModel.transferMobile(amount, phone, userId).observe(this, walletTransfer -> {
            dismissLoading();
            webService.updateBalance(userId);
            if (walletTransfer!=null&&!walletTransfer.error) {
                SweetToast.success(ConfirmPaymentActivity.this,walletTransfer.getMessage());
                AppUtils.openPopupPaymentStatus(ConfirmPaymentActivity.this,true,walletTransfer.getMessage(),amount,phone,userData.getUserType());
            } else {
                if (walletTransfer!=null&&!TextUtils.isEmpty(walletTransfer.getMessage())) {
                    SweetToast.error(ConfirmPaymentActivity.this, walletTransfer.getMessage());
                    AppUtils.openPopupPaymentStatus(ConfirmPaymentActivity.this, false, walletTransfer.getMessage(), amount, phone, userData.getUserType());
                }else {
                    SweetToast.error(ConfirmPaymentActivity.this, getResources().getString(R.string.something_went_wrong));
                }
            }
        });
    }

    @Override
    public void onUpdateBalance(String balance) {

    }
}