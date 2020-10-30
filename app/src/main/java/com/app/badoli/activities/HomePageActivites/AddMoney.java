package com.app.badoli.activities.HomePageActivites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityAddMoneyBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.AddMoneyViewModel;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xyz.hasnat.sweettoast.SweetToast;

public class AddMoney extends AppCompatActivity implements updateBalance, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = AddMoney.class.getSimpleName();
    private static final int PAYPAL_REQUEST_CODE = 123;
    public ActivityAddMoneyBinding binding;
    AddMoneyViewModel bindingViewModel;
    UserData userData;
    String bindingFrom="";

    Handler handler=new Handler();

    String mobile,amount,referenceNo;

    WebService webService;
    private static PayPalConfiguration config;
    String paypal_token,paypal_payer_id,transactionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_money);
        userData= PrefManager.getInstance(AddMoney.this).getUserData();
        bindingViewModel = new ViewModelProvider(this).get(AddMoneyViewModel.class);

       /* config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);*/

        binding.edittextPhoneMobileAddmoney.addTextChangedListener(new TextWatcher() {
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
                        binding.edittextPhoneMobileAddmoney.setText("");
                    }
                }
                if (editable.toString().length() == 2) {
                    if (!editable.toString().equalsIgnoreCase("07")) {
                        String text = binding.edittextPhoneMobileAddmoney.getText().toString();
                        binding.edittextPhoneMobileAddmoney.setText(text.substring(0, text.length() - 1));
                        binding.edittextPhoneMobileAddmoney.setSelection(text.substring(0, text.length() - 1).length());
                    }
                }
                if (editable.toString().length() == 3) {
                    String text = binding.edittextPhoneMobileAddmoney.getText().toString();
                    if (editable.toString().equalsIgnoreCase("074")||editable.toString().equalsIgnoreCase("079")) {

                    } else {
                        binding.edittextPhoneMobileAddmoney.setText(text.substring(0, 2));
                        binding.edittextPhoneMobileAddmoney.setSelection(text.substring(0, text.length() - 1).length());
                    }
                }
            }
        });


        init();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void init() {
        webService=new WebService(this);
        webService.updateBalance(userData.getId());
        showLoading(getResources().getString(R.string.please_wait));
        handler.postDelayed(() -> webService.updateBalance(userData.getId()),10000);
        binding.radiogrouAddmoney.setOnCheckedChangeListener(this);
    }

    public void checkBalanceAdd(View view) {
        showLoading(getResources().getString(R.string.please_wait));
        webService.updateBalance(userData.getId());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String balance) {
        userData.setWallet_balance(balance);
        PrefManager.getInstance(AddMoney.this).setWalletBalance(balance);
        dismissLoading();
        if (!TextUtils.isEmpty(binding.txtBalanceAddmoney.getText().toString())) {
            Float bal = Float.valueOf(binding.txtBalanceAddmoney.getText().toString()
                    .replace(getResources().getString(R.string.badoli_balance), "")
                    .replace(" ", "")
                    .replace("FCFA",""));
           //Log.e(TAG,"WALLET--->"+bal+"\n"+balance);
        } else {
            binding.txtBalanceAddmoney.setText(getResources().getString(R.string.badoli_balance) + " " + balance + " FCFA");
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
        if (binding.rbAirtelMoney.isChecked()) {
           bindingFrom="AIRTEL";
        }
        if (binding.rbPaypal.isChecked()) {
            bindingFrom="PAYPAL";
        }
    }

    public void addBalanceMoney(View view) {
        amount=binding.edittextAmountAddMoneyAddmoney.getText().toString();
        mobile=binding.edittextPhoneMobileAddmoney.getText().toString();
        if (isValidated(amount,mobile)){
            if (binding.rbAirtelMoney.isChecked()) {
                proceedAirtel();
            } else {
              //  proceedPaypal();
                SweetToast.error(AddMoney.this,"Available soon");
            }
        }
    }

    private void proceedPaypal() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),
                "USD", getResources().getString(R.string.adding_money_to_wallet),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, AddMoney.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    public void proceedAirtel() {
        handler.post(() -> getReference(mobile,amount,userData.getId(),userData.getAuth_token()));
    }

    private boolean isValidated(String amount,String mobile) {
        if (TextUtils.isEmpty(amount)){
            binding.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.enter_amount));
            binding.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_amount));
            return false;
        }

        if (Float.parseFloat(amount)<100){
            binding.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.amount_should_100));
            binding.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.amount_should_100));
            return false;
        }

        if (Float.parseFloat(amount)>49900){
            binding.edittextAmountAddMoneyAddmoney.setError(getResources().getString(R.string.amount_should_499));
            binding.edittextAmountAddMoneyAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.amount_should_499));
            return false;
        }

        if (TextUtils.isEmpty(mobile)){
            binding.edittextPhoneMobileAddmoney.setError(getResources().getString(R.string.enter_gabon_mobile));
            binding.edittextPhoneMobileAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_mobile));
            return false;
        }

        if (mobile.length()<7||mobile.length()>15){
            binding.edittextPhoneMobileAddmoney.setError(getResources().getString(R.string.enter_valide_mobile));
            binding.edittextPhoneMobileAddmoney.requestFocus();
            SweetToast.error(AddMoney.this,getResources().getString(R.string.enter_valide_mobile));
            return false;
        }

        if (binding.radiogrouAddmoney.getCheckedRadioButtonId()==-1){
            SweetToast.error(AddMoney.this,getResources().getString(R.string.select_option_to_add));
            return false;
        }

        binding.edittextAmountAddMoneyAddmoney.setError(null);
        binding.edittextPhoneMobileAddmoney.setError(null);

        return  true;
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(AddMoney.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    //https://mypvit.com/mypvitapi.kk
    private void getReference(String mobile, String amount, String id, String auth_token) {
        showLoading(getResources().getString(R.string.please_wait));
        bindingViewModel.getReference(id, mobile, amount, auth_token,"1").observe(this, referenceResponse -> {
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
        showLoading(getResources().getString(R.string.requesting));
        bindingViewModel.goAirtel(mobile,amount,reference,Constant.TEL_MERCHAND,Constant.TOKEN)
                .observe(this, airtelResponse -> {
                    dismissLoading();
                    webService.updateBalance(userData.getId());
                    if (airtelResponse.response_code==1000) {
                        SweetToast.error(AddMoney.this,airtelResponse.getMessage());
                        binding.edittextPhoneMobileAddmoney.setText("");
                        binding.edittextAmountAddMoneyAddmoney.setText("");
                        referenceNo="";
                        goActivity();

                    } else {
                        SweetToast.error(AddMoney.this,airtelResponse.getMessage());
                    }
                });
    }

    private void goActivity() {
        if (userData.getUserType().equalsIgnoreCase("3")){
            Intent intent = new Intent(AddMoney.this, ProfessionalActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }else {
            Intent intent = new Intent(AddMoney.this, HomePageActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            PayPalAuthorization auth =
                    data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    String paymentDetails = confirm.toJSONObject().toString(10);
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.  PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    //                PayPalAuthorization auth =
                    //                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                    Log.i("paymentExample", paymentDetails);
                    Log.e(TAG,"1-->"+ confirm.toJSONObject().toString(4));
                    Log.e(TAG, "2--->"+confirm.getPayment().toJSONObject().toString(4));
                    Log.e(TAG, "3--->"+ confirm.getEnvironment());
                    Log.e(TAG, "4--->"+confirm.getProofOfPayment().toJSONObject().toString(4));
                    if (auth != null) {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));
                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);
                    }
                    sendPayment(confirm.toJSONObject());
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    SweetToast.error(AddMoney.this,getResources().getString(R.string.transaction_failed));
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
            SweetToast.error(AddMoney.this,getResources().getString(R.string.payment_cancelled));
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            SweetToast.error(AddMoney.this,getResources().getString(R.string.something_went_wrong));
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private void sendPayment(JSONObject jsonObject) throws JSONException {
        JSONObject response=jsonObject.getJSONObject("response");
        paypal_token=response.getString("id");
        String create_time=response.getString("create_time");
        //paypal_payer_id=getResources().getString(R.string.paypal_client_id);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat newFormat=new SimpleDateFormat("yyyyMMddTHHmmss", Locale.getDefault());
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String paymentDate="";
        try {
            Date date = sdf.parse(create_time);
          //  Date payDate = sdf.parse(create_time);
            paymentDate=format.format(date);
            if (date != null) {
                transactionId = newFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        payment(userData.getId(),paypal_token,amount,paymentDate);
    }

    private void payment(String id, String paypal_token, String amount, String paymentDate) {
       // /
    }

}

