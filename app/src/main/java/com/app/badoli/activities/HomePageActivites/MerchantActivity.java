package com.app.badoli.activities.HomePageActivites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.app.badoli.R;
import com.app.badoli.config.Configuration;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityMerchantBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.AddMoneyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import xyz.hasnat.sweettoast.SweetToast;

public class MerchantActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, updateBalance {
    private static final String TAG = MerchantActivity.class.getSimpleName();
    ActivityMerchantBinding merchantBinding;
    UserData userData;
    WebService webService;

    AddMoneyViewModel addMoneyViewModel;

    Handler handler=new Handler();

    String referenceNo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_merchant);
        merchantBinding = DataBindingUtil.setContentView(MerchantActivity.this, R.layout.activity_merchant);
        userData= PrefManager.getInstance(MerchantActivity.this).getUserData();

        addMoneyViewModel = new ViewModelProvider(this).get(AddMoneyViewModel.class);

        loadData();
        merchantBinding.txtWalletBalancetMerchant.setOnClickListener(this);
        merchantBinding.btnProgress.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String balance) {
        PrefManager.getInstance(MerchantActivity.this).setWalletBalance(balance);
        merchantBinding.txtWalletBalancetMerchant.setText(balance+" FCFA");
        userData.setWallet_balance(balance);
        if (merchantBinding.progressbarRequest!=null&&merchantBinding.progressbarRequest.isShown()){
            dismissLoading();
        }
    }

    void showLoading(){
        Configuration.hideKeyboardFrom(MerchantActivity.this);
        merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void dismissLoading(){
        merchantBinding.progressbarRequest.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        webService=new WebService(this);
        webService.updateBalance(userData.getId());
        merchantBinding.btnGenCode.setOnClickListener(this);
        merchantBinding.hiddenLayout.imgBackMerchant.setOnClickListener(this);
        merchantBinding.imgBackMaintMerchant.setOnClickListener(this);
        merchantBinding.badoliPhoneTextMerchant.setText("Badolipay ("+userData.getMobile()+")");
        merchantBinding.txtWalletBalancetMerchant.setText(userData.getWallet_balance()+" FCFA");
        merchantBinding.radiogroupMerchant.setOnCheckedChangeListener(this);
        merchantBinding.rbQrcode.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (merchantBinding.rbQrcode.isChecked()){
            merchantBinding.rbQrcode.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            merchantBinding.rbMobileId.setBackground(null);
            //merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            merchantBinding.rbMobileId.setTextColor(getResources().getColor(R.color.text_orange));
            merchantBinding.rbQrcode.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                merchantBinding.lnQrCode.setVisibility(View.VISIBLE);
                merchantBinding.rlMobileid.setVisibility(View.GONE);
                //merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },0);
        }
        if (merchantBinding.rbMobileId.isChecked()){
            merchantBinding.rbMobileId.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            merchantBinding.rbQrcode.setBackground(null);
            //merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            merchantBinding.rbQrcode.setTextColor(getResources().getColor(R.color.text_orange));
            merchantBinding.rbMobileId.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                merchantBinding.lnQrCode.setVisibility(View.GONE);
                merchantBinding.rlMobileid.setVisibility(View.VISIBLE);
                //merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },0);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }

    @Override
    public void onClick(View v) {
        if (v==merchantBinding.imgBackMaintMerchant) {
            finish();
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
        }
        if (v==merchantBinding.hiddenLayout.imgBackMerchant) {
            slideClose();
        }
        if (v==merchantBinding.btnGenCode) {
            String amount=merchantBinding.edittextAmtFcfa.getText().toString();
            if(isValidated(amount)) {
                Configuration.hideKeyboardFrom(MerchantActivity.this);
                showLoading();
                genrateQrCode(amount);
            }
        }
        if (v==merchantBinding.txtWalletBalancetMerchant){
            showLoading();
            webService.updateBalance(userData.getId());
        }
        if (v==merchantBinding.btnProgress){
            String mobile=merchantBinding.edittextIdmobileNumber.getText().toString().trim();
            String amount=merchantBinding.edittextAmtFcfaIdmobile.getText().toString().trim();
            if (isRequestTrue(mobile,amount)){
                handler.post(() -> getReference(mobile,amount,userData.getId(),userData.getAuth_token()));
            }
        }
    }

    private boolean isValidated(String amount) {
        if (TextUtils.isEmpty(amount)){
            merchantBinding.edittextAmtFcfa.setError(getResources().getString(R.string.enter_amount));
            merchantBinding.edittextAmtFcfa.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_amount));
            return false;
        }
        if (Float.valueOf(amount)<100){
            merchantBinding.edittextAmtFcfa.setError(getResources().getString(R.string.amount_should_100));
            merchantBinding.edittextAmtFcfa.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_100));
            return false;
        }
        if (Float.valueOf(amount)>49900){
            merchantBinding.edittextAmtFcfa.setError(getResources().getString(R.string.amount_should_499));
            merchantBinding.edittextAmtFcfa.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_499));
            return false;
        }
        merchantBinding.edittextAmtFcfa.setError(null);
        return true;
    }

    private boolean isRequestTrue(String mobile, String amount) {
        if (TextUtils.isEmpty(mobile)){
            merchantBinding.edittextIdmobileNumber.setError(getResources().getString(R.string.enter_gabon_mobile));
            merchantBinding.edittextIdmobileNumber.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_mobile));
            return false;
        }
        if (mobile.length()>15||mobile.length()<7){
            merchantBinding.edittextIdmobileNumber.setError(getResources().getString(R.string.enter_valide_mobile));
            merchantBinding.edittextIdmobileNumber.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_valide_mobile));
            return false;
        }
        if (TextUtils.isEmpty(amount)){
            merchantBinding.edittextAmtFcfaIdmobile.setError(getResources().getString(R.string.enter_amount));
            merchantBinding.edittextAmtFcfaIdmobile.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_amount));
            return false;
        }
        if (Float.valueOf(amount)<100){
            merchantBinding.edittextAmtFcfaIdmobile.setError(getResources().getString(R.string.amount_should_100));
            merchantBinding.edittextAmtFcfaIdmobile.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_100));
            return false;
        }
        if (Float.valueOf(amount)>49900){
            merchantBinding.edittextAmtFcfaIdmobile.setError(getResources().getString(R.string.amount_should_499));
            merchantBinding.edittextAmtFcfaIdmobile.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_499));
            return false;
        }
        merchantBinding.edittextIdmobileNumber.setError(null);
        merchantBinding.edittextAmtFcfaIdmobile.setError(null);
        return true;
    }

    private void getReference(String mobile, String amount, String id, String auth_token) {
        showLoading();
        addMoneyViewModel.getReference(id, mobile, amount, auth_token).observe(this, referenceResponse -> {
            if (!referenceResponse.error) {
                dismissLoading();
                referenceNo = referenceResponse.getRef();
                goAirtel(mobile,amount,referenceNo);
            } else {
                dismissLoading();
                SweetToast.error(MerchantActivity.this,referenceResponse.getMessage());
            }
        });
    }

    private void goAirtel(String mobile, String amount,String reference) {
        showLoading();
        addMoneyViewModel.goAirtel(mobile,amount,reference, Constant.TEL_MERCHAND,Constant.TOKEN)
                .observe(this, airtelResponse -> {
                    if (airtelResponse.response_code==1000) {
                        dismissLoading();
                        SweetToast.error(MerchantActivity.this,airtelResponse.getMessage());
                        Configuration.openPopupUpDown(MerchantActivity.this,R.style.Dialod_UpDown,
                                "",airtelResponse.getMessage());
                        updateValue();
                    } else {
                        dismissLoading();
                        SweetToast.error(MerchantActivity.this,airtelResponse.getMessage());
                    }
                });
    }

    private void updateValue() {
        webService.updateBalance(userData.getId());
        referenceNo="";
        merchantBinding.edittextIdmobileNumber.setText("");
        merchantBinding.edittextAmtFcfaIdmobile.setText("");
        merchantBinding.edittextRemarksIdmobile.setText("");
    }

    private boolean isPanelShown() {
        return merchantBinding.hiddenLayout.rlHiddenLayout.getVisibility() == View.VISIBLE;
    }

    public void slideUpDown(String amount) {
        if (!isPanelShown()) {
            Configuration.hideKeyboardFrom(MerchantActivity.this);
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(MerchantActivity.this,
                    R.anim.slide_up_dialog);
            dismissLoading();
            merchantBinding.hiddenLayout.rlHiddenLayout.startAnimation(bottomUp);
            merchantBinding.hiddenLayout.rlHiddenLayout.setVisibility(View.VISIBLE);
            merchantBinding.rlMainMerchant.setVisibility(View.GONE);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy'T'hhmmss");
            String format = simpleDateFormat.format(new Date());
            Thread thread=new Thread(() -> getQrCode(amount,format,userData.getMobile(),userData.getName()));
            handler.post(thread::start);
        }
    }

    @SuppressLint("SetTextI18n")
    private void slideClose() {
        Configuration.hideKeyboardFrom(MerchantActivity.this);
        Animation bottomDown = AnimationUtils.loadAnimation(MerchantActivity.this,
                R.anim.slide_bottom_dialog);
        merchantBinding.hiddenLayout.rlHiddenLayout.startAnimation(bottomDown);
        merchantBinding.hiddenLayout.rlHiddenLayout.setVisibility(View.GONE);
        merchantBinding.rlMainMerchant.setVisibility(View.VISIBLE);
        merchantBinding.hiddenLayout.progressbarMerchant.setVisibility(View.INVISIBLE);
        merchantBinding.hiddenLayout.txtNameQrcode.setText(getResources().getString(R.string.to_pay) + " " + userData.getName());
        merchantBinding.hiddenLayout.txtMerchantAmt.setText(getResources().getString(R.string.amt_paid) + " ");
        merchantBinding.hiddenLayout.imgQrcode.setImageBitmap(null);
        merchantBinding.hiddenLayout.imgQrcode.setImageResource(R.drawable.logo);
    }

    @SuppressLint("SetTextI18n")
     private void genrateQrCode(String amount) {
        merchantBinding.hiddenLayout.progressbarMerchant.setVisibility(View.VISIBLE);
        slideUpDown(amount);
        merchantBinding.edittextAmtFcfa.setText("");
    }

    @SuppressLint("SetTextI18n")
    private void getQrCode(String amount, String format, String mobile, String name) {
       //String value="Amount: "+amount+"\n"+"UserMobile: "+mobile+"\n"+"Name: "+name+"\n"+format;
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("mobile",mobile);
            jsonObject.put("amount",amount);
            jsonObject.put("userId",userData.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"JSON QR--->"+jsonObject);

        Thread thread=new Thread(() -> {
            int width=500;
            int height=500 ;
            BitMatrix bitMatrix = null;
            try {
                Map<EncodeHintType, Object> hintMap = new HashMap<>();
                hintMap.put(EncodeHintType.MARGIN, 1);
                bitMatrix = new MultiFormatWriter().encode(
                        new String(jsonObject.toString().getBytes()),
                        BarcodeFormat.QR_CODE, width, height, hintMap);

            } catch (IllegalArgumentException ignored) {
            } catch (WriterException e) {
                e.printStackTrace();
            }

            int bitMatrixWidth = bitMatrix.getWidth();

            int bitMatrixHeight = bitMatrix.getHeight();

            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;
                for (int x = 0; x < bitMatrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.bla_trans) : getResources().getColor(R.color.white);
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.RGB_565);

            bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
            BitMatrix finalBitMatrix = bitMatrix;
            runOnUiThread(() -> showImage(amount,bitmap, finalBitMatrix));
        });
        handler.post(thread::start);
    }

    @SuppressLint("SetTextI18n")
    private void showImage(String amount, Bitmap bitmap, BitMatrix bitMatrix) {
        merchantBinding.hiddenLayout.progressbarMerchant.setVisibility(View.INVISIBLE);
        merchantBinding.hiddenLayout.txtNameQrcode.setText(getResources().getString(R.string.to_pay) + " " + userData.getName());
        merchantBinding.hiddenLayout.txtMerchantAmt.setText(getResources().getString(R.string.amt_paid) + " " + amount+" FCFA");
        merchantBinding.hiddenLayout.imgQrcode.setImageBitmap(bitmap);
        startTimer(120,bitMatrix);
    }

    private void startTimer(int sec, BitMatrix bitMatrix) {
        CountDownTimer downTimer= new CountDownTimer(1000 * sec, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                @SuppressLint("DefaultLocale") String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                merchantBinding.hiddenLayout.txtQrcodeTimer.setText(getResources().getString(R.string.code_expires_in)+" "+v + ":" + String.format("%02d", va)+" "+getResources().getString(R.string.minutes));
            }
            @SuppressLint("SetTextI18n")
            public void onFinish() {
                merchantBinding.hiddenLayout.txtQrcodeTimer.setText(getResources().getString(R.string.code_expired));
                merchantBinding.hiddenLayout.imgQrcode.setImageBitmap(null);
                bitMatrix.clear();
                handler.postDelayed(() -> slideClose(),1000);
            }
        };
        downTimer.start();
    }
}
