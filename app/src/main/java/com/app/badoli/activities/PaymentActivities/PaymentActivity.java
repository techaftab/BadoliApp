package com.app.badoli.activities.PaymentActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityPaymentBinding;
import com.app.badoli.model.UserData;

import xyz.hasnat.sweettoast.SweetToast;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, updateBalance {
    ActivityPaymentBinding binding;
    String requestType,mobile,name;

    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PaymentActivity.this, R.layout.activity_payment);
        userData = PrefManager.getInstance(PaymentActivity.this).getUserData();
        initView();
    }

    private void initView() {
        requestType=getIntent().getStringExtra(Constant.TRANSFER_TYPE);
        mobile=getIntent().getStringExtra(Constant.MOBILE_TRANSFER);
        name=getIntent().getStringExtra(Constant.NAME_TRANSFER);
        binding.imgBackPayment.setOnClickListener(this);
        binding.btnProceed.setOnClickListener(this::validateDeatils);
        binding.btnConfirm.setOnClickListener(this::validatePin);

        binding.rlAmountPayment.setVisibility(View.VISIBLE);
        binding.rlPinPayment.setVisibility(View.GONE);

        binding.txtName.setText(name);
        binding.txtMobile.setText(mobile);
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(PaymentActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void validatePin(View view) {
        binding.edittextPin.requestFocus();
        String pin=binding.edittextPin.getText().toString();
        String amount=binding.edittextAmount.getText().toString();
        if (isValidPin(pin)){
            goActivity(ConfirmPaymentActivity.class,name,amount,mobile);
        }
    }

    private void goActivity(Class<?> activity,
                            String name, String amount, String mobile) {
        Intent intent = new Intent(PaymentActivity.this,activity);
        intent.putExtra(Constant.MOBILE_TRANSFER,mobile);
        intent.putExtra(Constant.NAME_TRANSFER,name);
        intent.putExtra(Constant.TRANSFER_TYPE,requestType);
        intent.putExtra(Constant.TRANSFER_AMOUNT,amount);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }


    private boolean isValidPin(String pin) {
        if (TextUtils.isEmpty(pin)){
           // binding.edittextPin.setError(getResources().getString(R.string.enter_amount));
            binding.edittextAmount.requestFocus();
            SweetToast.error(PaymentActivity.this,getResources().getString(R.string.enter_your_pin));
            return false;
        }
        if (pin.length()!=4){
            binding.edittextAmount.requestFocus();
            SweetToast.error(PaymentActivity.this,getResources().getString(R.string.enter_valid_pin));
            return false;
        }
        return true;
    }

    private void validateDeatils(View view) {
        String amount=binding.edittextAmount.getText().toString();
        if (isValid(amount)){
            slideUpPin();
        }
    }

    private boolean isValid(String amount) {
        if (TextUtils.isEmpty(amount)){
            binding.edittextAmount.setError(getResources().getString(R.string.enter_amount));
            binding.edittextAmount.requestFocus();
            SweetToast.error(PaymentActivity.this,getResources().getString(R.string.enter_amount));
            return false;
        }
        if (Float.parseFloat(amount)<100){
            binding.edittextAmount.setError(getResources().getString(R.string.amount_should_100));
            binding.edittextAmount.requestFocus();
            SweetToast.error(PaymentActivity.this,getResources().getString(R.string.amount_should_100));
            return false;
        }
        if (Float.parseFloat(amount)>49900){
            binding.edittextAmount.setError(getResources().getString(R.string.amount_should_499));
            binding.edittextAmount.requestFocus();
            SweetToast.error(PaymentActivity.this,getResources().getString(R.string.amount_should_499));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v==binding.imgBackPayment){
            if (isPinPanelShown()){
                slideClosePin();
            }else {
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
    }
    private boolean isPinPanelShown() {
        return binding.rlPinPayment.getVisibility() == View.VISIBLE;
    }

    public void slideUpPin() {
        if (!isPinPanelShown()) {
            Animation bottomUp = AnimationUtils.loadAnimation(PaymentActivity.this,
                    R.anim.slide_up_dialog);
            binding.rlPinPayment.startAnimation(bottomUp);
            binding.rlPinPayment.setVisibility(View.VISIBLE);
            binding.rlAmountPayment.setVisibility(View.GONE);
            binding.lnUserDetails.setVisibility(View.INVISIBLE);
            binding.edittextPin.requestFocus();
        }
    }

    private void slideClosePin() {
        AppUtils.hideKeyboardFrom(PaymentActivity.this);
        Animation bottomDown = AnimationUtils.loadAnimation(PaymentActivity.this,
                R.anim.slide_bottom_dialog);

        binding.rlPinPayment.startAnimation(bottomDown);
        binding.rlPinPayment.setVisibility(View.GONE);
        binding.rlAmountPayment.setVisibility(View.VISIBLE);
        binding.lnUserDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isPinPanelShown()){
            slideClosePin();
        }else {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public void onUpdateBalance(String balance) {

    }
}
