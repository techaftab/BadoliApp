package com.app.badoli.activities.HomePageActivites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.databinding.ActivityMerchantBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.AddMoneyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import xyz.hasnat.sweettoast.SweetToast;

public class MerchantActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener, updateBalance {
    private static final String TAG = MerchantActivity.class.getSimpleName();
    ActivityMerchantBinding binding;
    UserData userData;
    WebService webService;

    AddMoneyViewModel addMoneyViewModel;

    Handler handler=new Handler();

    String referenceNo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_merchant);
        binding = DataBindingUtil.setContentView(MerchantActivity.this, R.layout.activity_merchant);
        userData= PrefManager.getInstance(MerchantActivity.this).getUserData();

        addMoneyViewModel = new ViewModelProvider(this).get(AddMoneyViewModel.class);

        loadData();
        binding.txtWalletBalancetMerchant.setOnClickListener(this);
        binding.btnProgress.setOnClickListener(this);

        binding.edittextPhone.addTextChangedListener(new TextWatcher() {
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
                        binding.edittextPhone.setText("");
                    }
                }
                if (editable.toString().length() == 2) {
                    if (!editable.toString().equalsIgnoreCase("07")) {
                        String text = binding.edittextPhone.getText().toString();
                        binding.edittextPhone.setText(text.substring(0, text.length() - 1));
                        binding.edittextPhone.setSelection(text.substring(0, text.length() - 1).length());
                    }
                }
                if (editable.toString().length() == 3) {
                    String text = binding.edittextPhone.getText().toString();
                    Log.i(TAG, "afterTextChanged: "+text);
                    if (editable.toString().equalsIgnoreCase("074")||editable.toString().equalsIgnoreCase("079")) {

                    } else {
                        binding.edittextPhone.setText(text.substring(0, 2));
                        binding.edittextPhone.setSelection(text.substring(0, text.length() - 1).length());
                    }
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String balance) {
        PrefManager.getInstance(MerchantActivity.this).setWalletBalance(balance);
        binding.txtWalletBalancetMerchant.setText(balance+" FCFA");
        userData.setWallet_balance(balance);
        if (binding.layoutProgress.lnProgress.isShown()){
            dismissLoading();
        }
    }

    void showLoading(String message){
        AppUtils.hideKeyboardFrom(MerchantActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        webService=new WebService(this);
        webService.updateBalance(userData.getId());
        binding.btnGenCode.setOnClickListener(this);
        binding.hiddenLayout.imgBackMerchant.setOnClickListener(this);
        binding.imgBackMaintMerchant.setOnClickListener(this);
        String mobile=userData.getMobile().substring(0,3)+" "+userData.getMobile().substring(3);
        binding.badoliPhoneTextMerchant.setText(userData.getName()+" ("+mobile+")");
        binding.txtWalletBalancetMerchant.setText(userData.getWallet_balance()+" FCFA");
        binding.radiogroupMerchant.setOnCheckedChangeListener(this);
        binding.rbQrcode.setChecked(true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (binding.rbQrcode.isChecked()){
            binding.rbQrcode.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            binding.rbMobileId.setBackground(null);
            //binding.progressbarRequest.setVisibility(View.VISIBLE);
            binding.rbMobileId.setTextColor(getResources().getColor(R.color.text_orange));
            binding.rbQrcode.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                binding.lnQrCode.setVisibility(View.VISIBLE);
                binding.rlMobileid.setVisibility(View.GONE);
                //binding.progressbarRequest.setVisibility(View.GONE);
            },0);
        }
        if (binding.rbMobileId.isChecked()){
            binding.rbMobileId.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            binding.rbQrcode.setBackground(null);
            //binding.progressbarRequest.setVisibility(View.VISIBLE);
            binding.rbQrcode.setTextColor(getResources().getColor(R.color.text_orange));
            binding.rbMobileId.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                binding.lnQrCode.setVisibility(View.GONE);
                binding.rlMobileid.setVisibility(View.VISIBLE);
                //binding.progressbarRequest.setVisibility(View.GONE);
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
        if (v==binding.imgBackMaintMerchant) {
            finish();
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
        }
        if (v==binding.hiddenLayout.imgBackMerchant) {
            slideClose();
        }
        if (v==binding.btnGenCode) {
            String amount=binding.edittextAmtFcfa.getText().toString();
            if(isValidated(amount)) {
                AppUtils.hideKeyboardFrom(MerchantActivity.this);
                showLoading(getResources().getString(R.string.fetching_code));
                genrateQrCode(amount);
            }
        }
        if (v==binding.txtWalletBalancetMerchant){
            showLoading(getResources().getString(R.string.please_wait));
            webService.updateBalance(userData.getId());
        }
        if (v==binding.btnProgress){
            String mobile=binding.edittextPhone.getText().toString().trim();
            String amount=binding.edittextAmtFcfaIdmobile.getText().toString().trim();
            if (isRequestTrue(mobile,amount)){
                handler.post(() -> getReference(mobile,amount,userData.getId(),userData.getAuth_token()));
            }
        }
    }

    private boolean isValidated(String amount) {
        if (TextUtils.isEmpty(amount)){
            binding.edittextAmtFcfa.setError(getResources().getString(R.string.enter_amount));
            binding.edittextAmtFcfa.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_amount));
            return false;
        }
        if (Float.parseFloat(amount)<100){
            binding.edittextAmtFcfa.setError(getResources().getString(R.string.amount_should_100));
            binding.edittextAmtFcfa.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_100));
            return false;
        }
        if (Float.parseFloat(amount)>49900){
            binding.edittextAmtFcfa.setError(getResources().getString(R.string.amount_should_499));
            binding.edittextAmtFcfa.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_499));
            return false;
        }
        binding.edittextAmtFcfa.setError(null);
        return true;
    }

    private boolean isRequestTrue(String mobile, String amount) {
        if (TextUtils.isEmpty(mobile)){
            binding.edittextPhone.setError(getResources().getString(R.string.enter_gabon_mobile));
            binding.edittextPhone.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_mobile));
            return false;
        }
        if (mobile.length()>15||mobile.length()<7){
            binding.edittextPhone.setError(getResources().getString(R.string.enter_valide_mobile));
            binding.edittextPhone.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_valide_mobile));
            return false;
        }
        if (TextUtils.isEmpty(amount)){
            binding.edittextAmtFcfaIdmobile.setError(getResources().getString(R.string.enter_amount));
            binding.edittextAmtFcfaIdmobile.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.enter_amount));
            return false;
        }
        if (Float.parseFloat(amount)<100){
            binding.edittextAmtFcfaIdmobile.setError(getResources().getString(R.string.amount_should_100));
            binding.edittextAmtFcfaIdmobile.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_100));
            return false;
        }
        if (Float.parseFloat(amount)>49900){
            binding.edittextAmtFcfaIdmobile.setError(getResources().getString(R.string.amount_should_499));
            binding.edittextAmtFcfaIdmobile.requestFocus();
            SweetToast.error(MerchantActivity.this,getResources().getString(R.string.amount_should_499));
            return false;
        }
        binding.edittextPhone.setError(null);
        binding.edittextAmtFcfaIdmobile.setError(null);
        return true;
    }

    private void getReference(String mobile, String amount, String id, String auth_token) {
        showLoading(getResources().getString(R.string.validating));
        addMoneyViewModel.getReference(id, mobile, amount, auth_token, "1").observe(this, referenceResponse -> {
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
        showLoading(getResources().getString(R.string.sending_request));
        addMoneyViewModel.goAirtel(mobile,amount,reference, Constant.TEL_MERCHAND,Constant.TOKEN)
                .observe(this, airtelResponse -> {
                    if (airtelResponse.response_code==1000) {
                        dismissLoading();
                        SweetToast.error(MerchantActivity.this,airtelResponse.getMessage());
                        AppUtils.openPopup(MerchantActivity.this,R.style.Dialod_UpDown,
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
        binding.edittextPhone.setText("");
        binding.edittextAmtFcfaIdmobile.setText("");
        binding.edittextRemarksIdmobile.setText("");
    }

    private boolean isPanelShown() {
        return binding.hiddenLayout.rlHiddenLayout.getVisibility() == View.VISIBLE;
    }

    public void slideUpDown(String amount) {
        if (!isPanelShown()) {
            AppUtils.hideKeyboardFrom(MerchantActivity.this);
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(MerchantActivity.this,
                    R.anim.slide_up_dialog);
            dismissLoading();
            binding.hiddenLayout.rlHiddenLayout.startAnimation(bottomUp);
            binding.hiddenLayout.rlHiddenLayout.setVisibility(View.VISIBLE);
            binding.rlMainMerchant.setVisibility(View.GONE);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy'T'hhmmss");
            String format = simpleDateFormat.format(new Date());
            Thread thread=new Thread(() -> getQrCode(amount,format,userData.getMobile(),userData.getName()));
            handler.post(thread::start);
        }
    }

    @SuppressLint("SetTextI18n")
    private void slideClose() {
        AppUtils.hideKeyboardFrom(MerchantActivity.this);
        Animation bottomDown = AnimationUtils.loadAnimation(MerchantActivity.this,
                R.anim.slide_bottom_dialog);
        binding.hiddenLayout.rlHiddenLayout.startAnimation(bottomDown);
        binding.hiddenLayout.rlHiddenLayout.setVisibility(View.GONE);
        binding.rlMainMerchant.setVisibility(View.VISIBLE);
        binding.hiddenLayout.progressbarMerchant.setVisibility(View.INVISIBLE);
        binding.hiddenLayout.txtNameQrcode.setText(getResources().getString(R.string.to_pay) + " " + userData.getName());
        binding.hiddenLayout.txtMerchantAmt.setText(getResources().getString(R.string.amt_paid) + " ");
        binding.hiddenLayout.imgQrcode.setImageBitmap(null);
        binding.hiddenLayout.imgQrcode.setImageResource(R.drawable.logo);
    }

    @SuppressLint("SetTextI18n")
     private void genrateQrCode(String amount) {
        binding.hiddenLayout.progressbarMerchant.setVisibility(View.VISIBLE);
        slideUpDown(amount);
        binding.edittextAmtFcfa.setText("");
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
        binding.hiddenLayout.progressbarMerchant.setVisibility(View.INVISIBLE);
        binding.hiddenLayout.txtNameQrcode.setText(getResources().getString(R.string.to_pay) + " " + userData.getName());
        binding.hiddenLayout.txtMerchantAmt.setText(getResources().getString(R.string.amt_paid) + " " + amount+" FCFA");
        binding.hiddenLayout.imgQrcode.setImageBitmap(bitmap);
        startTimer(120,bitMatrix);
    }

    private void startTimer(int sec, BitMatrix bitMatrix) {
        CountDownTimer downTimer= new CountDownTimer(1000 * sec, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                @SuppressLint("DefaultLocale") String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                binding.hiddenLayout.txtQrcodeTimer.setText(getResources().getString(R.string.code_expires_in)+" "+v + ":" + String.format("%02d", va)+" "+getResources().getString(R.string.minutes));
            }
            @SuppressLint("SetTextI18n")
            public void onFinish() {
                binding.hiddenLayout.txtQrcodeTimer.setText(getResources().getString(R.string.code_expired));
                binding.hiddenLayout.imgQrcode.setImageBitmap(null);
                bitMatrix.clear();
                handler.postDelayed(() -> slideClose(),1000);
            }
        };
        downTimer.start();
    }
}
