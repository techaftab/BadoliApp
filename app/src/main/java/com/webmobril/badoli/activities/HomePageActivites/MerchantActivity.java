package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.AccountActivities.LoginActivity;
import com.webmobril.badoli.config.Configuration;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityMerchantBinding;
import com.webmobril.badoli.model.UserData;

import java.util.Objects;

public class MerchantActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    ActivityMerchantBinding merchantBinding;
    UserData userData;
    Handler handler;
    Thread thread ;
    public final int QRcodeWidth = 500 ;
    Bitmap bitmap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_merchant);
        merchantBinding = DataBindingUtil.setContentView(MerchantActivity.this, R.layout.activity_merchant);
        userData= PrefManager.getInstance(MerchantActivity.this).getUserData();
        handler=new Handler();
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        merchantBinding.btnGenCode.setOnClickListener(this);
        merchantBinding.imgBackMerchant.setOnClickListener(this);
        merchantBinding.imgBackMaintMerchant.setOnClickListener(this);
        merchantBinding.badoliPhoneTextMerchant.setText("Badolipay ("+userData.getMobile()+")");
        merchantBinding.txtWalletBalancetMerchant.setText("$ "+userData.getWallet_balance());
        merchantBinding.radiogroupMerchant.setOnCheckedChangeListener(this);
        merchantBinding.rbQrcode.setChecked(true);
    }

    @Override

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
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
            },300);
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
            },300);
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
        if (v==merchantBinding.imgBackMerchant){
            slideClose();
        }
        if (v==merchantBinding.btnGenCode){
            String amount=merchantBinding.edittextAmtFcfa.getText().toString();
            if(isValidated(amount)){
                merchantBinding.progressbarMerchant.setVisibility(View.VISIBLE);
                genrateQrCode(amount);
            }

        }
    }

    private boolean isPanelShown() {
        return merchantBinding.rlHiddenLayout.getVisibility() == View.VISIBLE;
    }

    public void slideUpDown() {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(MerchantActivity.this,
                    R.anim.slide_up_dialog);

            merchantBinding.rlHiddenLayout.startAnimation(bottomUp);
            merchantBinding.rlHiddenLayout.setVisibility(View.VISIBLE);
            merchantBinding.rlMainMerchant.setVisibility(View.GONE);

        }
    }

    private void slideClose() {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.hideKeyboardFrom(MerchantActivity.this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Animation bottomDown = AnimationUtils.loadAnimation(MerchantActivity.this,
                R.anim.slide_bottom_dialog);

        merchantBinding.rlHiddenLayout.startAnimation(bottomDown);
        merchantBinding.rlHiddenLayout.setVisibility(View.GONE);
        merchantBinding.rlMainMerchant.setVisibility(View.VISIBLE);
    }


    @SuppressLint("SetTextI18n")
    private void genrateQrCode(String amount) {
        slideUpDown();
        try {
            bitmap = TextToImageEncode(amount,userData.getMobile(),userData.getName());

            merchantBinding.imgQrcode.setImageBitmap(bitmap);

            merchantBinding.progressbarMerchant.setVisibility(View.GONE);
            merchantBinding.txtNameQrcode.setText(getResources().getString(R.string.to_pay)+" "+userData.getName());
            merchantBinding.txtMerchantAmt.setText(getResources().getString(R.string.amt_paid)+ amount);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidated(String amount) {
        if (amount.isEmpty()) {
            Toast.makeText(MerchantActivity.this, getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    Bitmap TextToImageEncode(String amount,String mobile,String name) throws WriterException {
        String value="Amount: "+amount+"\n"+"UserMobile: "+mobile+"\n"+"Name: "+name;
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth,QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.bla_trans):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
