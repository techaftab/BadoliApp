package com.app.badoli.activities.HomePageActivites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.config.Configuration;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityAddMoneyBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.AddMoneyViewModel;

import xyz.hasnat.sweettoast.SweetToast;

public class AddMoney extends AppCompatActivity implements updateBalance, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = AddMoney.class.getSimpleName();
    public ActivityAddMoneyBinding addMoney;
    AddMoneyViewModel addMoneyViewModel;
    UserData userData;
    String addMoneyFrom="";

    Handler handler=new Handler();

    String mobile,amount,referenceNo;

    WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMoney = DataBindingUtil.setContentView(this, R.layout.activity_add_money);
        userData= PrefManager.getInstance(AddMoney.this).getUserData();
        addMoneyViewModel = new ViewModelProvider(this).get(AddMoneyViewModel.class);
        init();
    }

    private void init() {
        webService=new WebService(this);
        webService.updateBalance(userData.getId());
        showLoading();
        handler.postDelayed(() -> webService.updateBalance(userData.getId()),10000);
        addMoney.radiogrouAddmoney.setOnCheckedChangeListener(this);
    }

    public void checkBalanceAdd(View view) {
        showLoading();
        webService.updateBalance(userData.getId());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String balance) {
        userData.setWallet_balance(balance);
        PrefManager.getInstance(AddMoney.this).setWalletBalance(balance);
        if (addMoney.progressAddmoney.isShown()) {
            dismissLoading();
        }
        if (!TextUtils.isEmpty(addMoney.txtBalanceAddmoney.getText().toString())) {
            Float bal = Float.valueOf(addMoney.txtBalanceAddmoney.getText().toString()
                    .replace(getResources().getString(R.string.badoli_balance), "")
                    .replace(" ", "")
                    .replace("FCFA",""));
            Log.e(TAG,"WALLET--->"+bal+"\n"+balance);
        } else {
            addMoney.txtBalanceAddmoney.setText(getResources().getString(R.string.badoli_balance) + " " + balance + " FCFA");
            userData.setWallet_balance(balance);
        }
    }

    public void backPressed(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (addMoney.rbAirtelMoney.isChecked()) {
           addMoneyFrom="AIRTEL";
        }
        if (addMoney.rbPaypal.isChecked()) {
            addMoneyFrom="PAYPAL";
        }
    }

    public void addBalanceMoney(View view) {
        amount=addMoney.edittextAmountAddMoneyAddmoney.getText().toString();
        mobile=addMoney.edittextPhoneMobileAddmoney.getText().toString();
        if (isValidated(amount,mobile)){
            if (addMoney.rbAirtelMoney.isChecked()) {
                proceedAirtel();
            } else {
                SweetToast.error(AddMoney.this,"Available soon");
            }
        }
    }

    public void proceedAirtel() {
        handler.post(() -> getReference(mobile,amount,userData.getId(),userData.getAuth_token()));
    }

    private boolean isValidated(String amount,String mobile) {
        if (TextUtils.isEmpty(amount)){
            addMoney.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.enter_amount));
            addMoney.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_amount));
            return false;
        }

        if (Float.parseFloat(amount)<100){
            addMoney.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.amount_should_100));
            addMoney.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.amount_should_100));
            return false;
        }

        if (Float.parseFloat(amount)>49900){
            addMoney.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.amount_should_499));
            addMoney.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.amount_should_499));
            return false;
        }

        if (TextUtils.isEmpty(mobile)){
            addMoney.edittextPhoneMobileAddmoney.setError(getResources().getString(R.string.enter_gabon_mobile));
            addMoney.edittextPhoneMobileAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_mobile));
            return false;
        }

        if (mobile.length()<7||mobile.length()>15){
            addMoney.edittextPhoneMobileAddmoney.setError(getResources().getString(R.string.enter_valide_mobile));
            addMoney.edittextPhoneMobileAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_valide_mobile));
            return false;
        }

        if (addMoney.radiogrouAddmoney.getCheckedRadioButtonId()==-1){
            SweetToast.error(AddMoney.this,getResources().getString(R.string.select_option_to_add));
            return false;
        }

        addMoney.edittextAmountAddMoneyAddmoney.setError(null);
        addMoney.edittextPhoneMobileAddmoney.setError(null);

        return  true;
    }

    void showLoading(){
        Configuration.hideKeyboardFrom(AddMoney.this);
        addMoney.progressAddmoney.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void dismissLoading(){
        addMoney.progressAddmoney.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    //https://mypvit.com/mypvitapi.kk
    private void getReference(String mobile, String amount, String id, String auth_token) {
        showLoading();
        addMoneyViewModel.getReference(id, mobile, amount, auth_token).observe(this, referenceResponse -> {
            if (!referenceResponse.error) {
                dismissLoading();
                referenceNo=referenceResponse.getRef();
                goAirtel(mobile,amount,referenceNo);
            } else {
                dismissLoading();
                SweetToast.error(AddMoney.this,referenceResponse.getMessage());
            }
        });
    }

    private void goAirtel(String mobile, String amount,String reference) {
        showLoading();
        addMoneyViewModel.goAirtel(mobile,amount,reference,Constant.TEL_MERCHAND,Constant.TOKEN)
                .observe(this, airtelResponse -> {
                    dismissLoading();
                    webService.updateBalance(userData.getId());
                    if (airtelResponse.response_code==1000) {
                        SweetToast.error(AddMoney.this,airtelResponse.getMessage());
                        addMoney.edittextPhoneMobileAddmoney.setText("");
                        addMoney.edittextAmountAddMoneyAddmoney.setText("");
                        referenceNo="";
                        Intent intent = new Intent(AddMoney.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    } else {
                        SweetToast.error(AddMoney.this,airtelResponse.getMessage());
                    }
                });
    }

}

