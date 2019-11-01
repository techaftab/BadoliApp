package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.AccountActivities.LoginActivity;
import com.webmobril.badoli.config.Configuration;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityMerchantBinding;
import com.webmobril.badoli.model.UserData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MerchantActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    ActivityMerchantBinding merchantBinding;
    UserData userData;

   // Bitmap bitmap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_merchant);
        merchantBinding = DataBindingUtil.setContentView(MerchantActivity.this, R.layout.activity_merchant);
        userData= PrefManager.getInstance(MerchantActivity.this).getUserData();

        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        merchantBinding.btnGenCode.setOnClickListener(this);
        merchantBinding.hiddenLayout.imgBackMerchant.setOnClickListener(this);
        merchantBinding.imgBackMaintMerchant.setOnClickListener(this);
        merchantBinding.badoliPhoneTextMerchant.setText("Badolipay ("+userData.getMobile()+")");
        merchantBinding.txtWalletBalancetMerchant.setText("$ "+userData.getWallet_balance());
        merchantBinding.radiogroupMerchant.setOnCheckedChangeListener(this);
        merchantBinding.rbQrcode.setChecked(true);
    }

    @Override

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Handler handler=new Handler();
        if (merchantBinding.rbQrcode.isChecked()){
            merchantBinding.rbQrcode.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            merchantBinding.rbMobileId.setBackground(null);
            merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            merchantBinding.rbMobileId.setTextColor(getResources().getColor(R.color.text_orange));
            merchantBinding.rbQrcode.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                merchantBinding.lnQrCode.setVisibility(View.VISIBLE);
                merchantBinding.rlMobileid.setVisibility(View.GONE);
                merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },100);
        }
        if (merchantBinding.rbMobileId.isChecked()){
            merchantBinding.rbMobileId.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            merchantBinding.rbQrcode.setBackground(null);
            merchantBinding.progressbarRequest.setVisibility(View.VISIBLE);
            merchantBinding.rbQrcode.setTextColor(getResources().getColor(R.color.text_orange));
            merchantBinding.rbMobileId.setTextColor(getResources().getColor(R.color.white));
            handler.postDelayed(() -> {
                merchantBinding.lnQrCode.setVisibility(View.GONE);
                merchantBinding.rlMobileid.setVisibility(View.VISIBLE);
                merchantBinding.progressbarRequest.setVisibility(View.GONE);
            },100);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }

    @Override
    public void onClick(View v) {
        if (v==merchantBinding.imgBackMaintMerchant){
            finish();
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
        }
        if (v==merchantBinding.hiddenLayout.imgBackMerchant){
            slideClose();
        }
        if (v==merchantBinding.btnGenCode){
            String amount=merchantBinding.edittextAmtFcfa.getText().toString();
            if(isValidated(amount)){
                merchantBinding.hiddenLayout.progressbarMerchant.setVisibility(View.VISIBLE);
                genrateQrCode(amount);
            }

        }
    }

    private boolean isPanelShown() {
        return merchantBinding.hiddenLayout.rlHiddenLayout.getVisibility() == View.VISIBLE;
    }

    public void slideUpDown(String amount) {
        if (!isPanelShown()) {

            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(MerchantActivity.this,
                    R.anim.slide_up_dialog);

            merchantBinding.hiddenLayout.rlHiddenLayout.startAnimation(bottomUp);
            merchantBinding.hiddenLayout.rlHiddenLayout.setVisibility(View.VISIBLE);
            merchantBinding.hiddenLayout.progressbarMerchant.setVisibility(View.VISIBLE);
            merchantBinding.rlMainMerchant.setVisibility(View.GONE);
            Configuration.hideKeyboardFrom(MerchantActivity.this);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy'T'hhmmss");
            String format = simpleDateFormat.format(new Date());
            new Thread(() -> getQrCode(amount,format,userData.getMobile(),userData.getName())).start();

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
        merchantBinding.hiddenLayout.progressbarMerchant.setVisibility(View.GONE);
        merchantBinding.hiddenLayout.txtNameQrcode.setText(getResources().getString(R.string.to_pay) + " " + userData.getName());
        merchantBinding.hiddenLayout.txtMerchantAmt.setText(getResources().getString(R.string.amt_paid) + " " + "");
        merchantBinding.hiddenLayout.imgQrcode.setImageBitmap(null);
        merchantBinding.hiddenLayout.imgQrcode.setImageResource(R.drawable.logo);

    }


    @SuppressLint("SetTextI18n")
    private void genrateQrCode(String amount) {
        slideUpDown(amount);
    }

    @SuppressLint("SetTextI18n")
    private void getQrCode(String amount, String format, String mobile, String name) {
        String value="Amount: "+amount+"\n"+"UserMobile: "+mobile+"\n"+"Name: "+name+"\n"+format;

        int width=500;
        int height=500 ;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            bitMatrix = new MultiFormatWriter().encode(
                    new String(value.getBytes()),
                    BarcodeFormat.QR_CODE, width, height, hintMap);

        } catch (IllegalArgumentException Illegalargumentexception) {

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

    }

    @SuppressLint("SetTextI18n")
    private void showImage(String amount, Bitmap bitmap, BitMatrix bitMatrix) {
        merchantBinding.hiddenLayout.progressbarMerchant.setVisibility(View.GONE);
        merchantBinding.hiddenLayout.txtNameQrcode.setText(getResources().getString(R.string.to_pay) + " " + userData.getName());
        merchantBinding.hiddenLayout.txtMerchantAmt.setText(getResources().getString(R.string.amt_paid) + " " + amount);
        merchantBinding.hiddenLayout.imgQrcode.setImageBitmap(bitmap);
        startTimer(120,bitMatrix);
    }


    private void startTimer(int sec, BitMatrix bitMatrix) {

        CountDownTimer downTimer= new CountDownTimer(1000 * sec, 1000) {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {


                @SuppressLint("DefaultLocale") String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                merchantBinding.hiddenLayout.txtQrcodeTimer.setText("This code will expire in: "+v + ":" + String.format("%02d", va)+" minutes");
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                merchantBinding.hiddenLayout.txtQrcodeTimer.setText("This code is expired");
                slideClose();
                merchantBinding.hiddenLayout.imgQrcode.setImageBitmap(null);
                bitMatrix.clear();
            }
        };
        downTimer.start();
    }

    private boolean isValidated(String amount) {
        if (amount.isEmpty()) {
            Toast.makeText(MerchantActivity.this, getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
