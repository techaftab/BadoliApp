package com.webmobril.badoli.activities.HomePageActivites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.webmobril.badoli.R;
import com.webmobril.badoli.config.Configuration;
import com.webmobril.badoli.config.Constant;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.config.WebService;
import com.webmobril.badoli.config.updateBalance;
import com.webmobril.badoli.databinding.ActivityAddMoneyBinding;
import com.webmobril.badoli.model.UserData;
import com.webmobril.badoli.viewModels.AddMoneyViewModel;

import xyz.hasnat.sweettoast.SweetToast;

public class AddMoney extends AppCompatActivity implements updateBalance {

    public ActivityAddMoneyBinding addMoney;
    AddMoneyViewModel addMoneyViewModel;
    UserData userData;

    Handler handler=new Handler();

    String mobile,amount,referenceNo;

    WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMoney = DataBindingUtil.setContentView(this, R.layout.activity_add_money);
        userData= PrefManager.getInstance(AddMoney.this).getUserData();
        addMoneyViewModel = ViewModelProviders.of(this).get(AddMoneyViewModel.class);
        init();
    }

    private void init() {
        webService=new WebService(this);
        webService.updateBalance(userData.getId(),userData.getAuth_token());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String balance) {
        addMoney.txtBalanceAddmoney.setText(getResources().getString(R.string.badoli_balance)+" "+balance+" ");
    }

    public void backPressed(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public void proceedAirtel(View view) {
        mobile=addMoney.edittextPhoneMobileAddmoney.getText().toString().trim();
        amount=addMoney.edittextAmountAddMoneyAddmoney.getText().toString().trim();
        if (isValidated(mobile,amount)){
            handler.post(() -> getReference(mobile,amount,userData.getId(),userData.getAuth_token()));
        }
    }

    private boolean isValidated(String mobile, String amount) {
        if (TextUtils.isEmpty(mobile)){
            addMoney.edittextPhoneMobileAddmoney.setError(getResources().getString(R.string.enter_gabon_mobile));
            addMoney.edittextPhoneMobileAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_mobile));
            return false;
        }
        if (mobile.length()>15||mobile.length()<7){
            addMoney.edittextPhoneMobileAddmoney.setError(getResources().getString(R.string.enter_valide_mobile));
            addMoney.edittextPhoneMobileAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_valide_mobile));
            return false;
        }
        if (TextUtils.isEmpty(amount)){
            addMoney.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.enter_amount));
            addMoney.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_amount));
            return false;
        }
        if (Float.valueOf(amount)<100){
            addMoney.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.amount_should_100));
            addMoney.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.amount_should_100));
            return false;
        }
        if (Float.valueOf(amount)>499000){
            addMoney.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.amount_should_499));
            addMoney.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.amount_should_499));
            return false;
        }
        addMoney.edittextAmountAddMoneyAddmoney.setError(null);
        addMoney.edittextPhoneMobileAddmoney.setError(null);
        return  true;
    }

    void showLoading(){
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

    private void goAirtel(String mobile, String amount,String referenceNo) {
        showLoading();
        addMoneyViewModel.goAirtel(mobile,amount,referenceNo,Constant.TEL_MERCHAND,Constant.TOKEN)
                .observe(this, airtelResponse -> {
            if (airtelResponse.response_code==1000) {
                dismissLoading();
                SweetToast.error(AddMoney.this,airtelResponse.getMessage());
                Configuration.openPopupUpDownBack(AddMoney.this,R.style.Dialod_UpDown,
                "main","",airtelResponse.getMessage());
            } else {
                dismissLoading();
                SweetToast.error(AddMoney.this,airtelResponse.getMessage());
            }
        });
    }
}
