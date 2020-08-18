package com.app.badoli.auth.otp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.auth.signup.professional.ProfessionalSignup;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityVerifyOtpBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.aabhasjindal.otptextview.OTPListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = VerifyOtpActivity.class.getSimpleName();
    ActivityVerifyOtpBinding binding;
    private AuthViewModel authViewModel;

    String otp, mobile, finalOtp, userId,roleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        userId = String.valueOf(LoginPre.getActiveInstance(VerifyOtpActivity.this).getSignup_id());
        mobile = getIntent().getStringExtra(Constant.MOBILE);
        roleId = getIntent().getStringExtra(Constant.ROLES_ID);
        otp = getIntent().getStringExtra(Constant.VERIFY_OTP);
        binding.otpView.setOTP(otp);
        binding.otpView.requestFocusOTP();
        // Toast.makeText(this, ""+otp+"\n"+userId, Toast.LENGTH_SHORT).show();
        binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                finalOtp = otp;
                // Toast.makeText(VerifyOtpActivity.this, "The OTP is " + otp,  Toast.LENGTH_SHORT).show();
            }
        });
        finalOtp = binding.otpView.getOTP();
        setTime(120);

        binding.txtMessage.setText(getResources().getString(R.string.an_otp_sent_to_mobile) + " " + mobile + ".\n" + getResources().getString(R.string.plz_enter_otp_continue));

        binding.btnVerify.setOnClickListener(this);
        binding.txtResendOtp.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);

    }

    private void setTime(int time) {
        binding.txtTime.setVisibility(View.VISIBLE);
        new CountDownTimer(time * 1000, 1000) {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {
                long remainedSecs = millisUntilFinished / 1000;
                binding.txtResendOtp.setEnabled(false);
                binding.txtResendOtp.setTextColor(getResources().getColor(R.color.gray));

                binding.txtTime.setText(getResources().getString(R.string.resend_otp_in) + " "
                        + (remainedSecs / 60) + ":" + String.format("%02d", (remainedSecs % 60)) + " " + getResources().getString(R.string.minutes));
            }

            public void onFinish() {
                binding.txtResendOtp.setEnabled(true);
                binding.txtResendOtp.setTextColor(getResources().getColor(R.color.colorBlue));
                binding.txtTime.setVisibility(View.GONE);
                /*forgotpasswordBinding.txtotptime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTime(time);
                    }
                });*/
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.imgBack) {
            onBackPressed();
        }
        if (v == binding.btnVerify) {
            if (validateOtp(finalOtp)) {
                verifyOtp(finalOtp, userId);
            }
        }
        if (v == binding.txtResendOtp) {
            userId = String.valueOf(LoginPre.getActiveInstance(VerifyOtpActivity.this).getSignup_id());
            if (TextUtils.isEmpty(userId)) {
                AppUtils.showSnackbar(getResources().getString(R.string.user_id_empty), binding.parentLayout);
            } else {
                resendOtp(userId);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showLoading(String message) {
        AppUtils.hideKeyboardFrom(VerifyOtpActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading() {
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void resendOtp(String userId) {
        showLoading(getResources().getString(R.string.resending_otp));
        authViewModel.resendOtp(userId).observe(this,
                resendOtpResponse -> {
                    Log.e("responsee", new Gson().toJson(resendOtpResponse.message));
                    dismissLoading();
                    if (!resendOtpResponse.error) {
                        setTime(120);
                        AppUtils.openPopup(VerifyOtpActivity.this, R.style.Dialod_UpDown, "", resendOtpResponse.getMessage());
                    } else {
                        AppUtils.openPopup(VerifyOtpActivity.this, R.style.Dialod_UpDown, "error", resendOtpResponse.getMessage());
                    }
                });
    }


    private boolean validateOtp(String finalOtp) {
        if (TextUtils.isEmpty(finalOtp)) {
            AppUtils.showSnackbar(getResources().getString(R.string.enter_otp), binding.parentLayout);
            return false;
        }
        if (finalOtp.length() < 6) {
            AppUtils.showSnackbar(getResources().getString(R.string.enter_valid_otp), binding.parentLayout);
            return false;
        }
        return true;
    }

    private void verifyOtp(String otp, String userId) {
        showLoading(getResources().getString(R.string.verifying_otp));
        authViewModel.verifyOtp(userId, otp).observe(this, verifyOtpResponse -> {
            if (verifyOtpResponse != null && !verifyOtpResponse.error) {
                String id = String.valueOf(verifyOtpResponse.result.getId());
                String countryCode = String.valueOf(verifyOtpResponse.result.getCountryCode());
                String countryId = String.valueOf(verifyOtpResponse.result.getCountryId());
                UserData userData = new UserData(
                        id,
                        verifyOtpResponse.result.getAuthToken(),
                        countryCode,
                        countryId,
                        verifyOtpResponse.result.getDeviceToken(),
                        verifyOtpResponse.result.getEmail(),
                        verifyOtpResponse.result.getMobile(),
                        verifyOtpResponse.result.getName(),
                        verifyOtpResponse.result.getWalletBalance(),
                        verifyOtpResponse.result.getUser_image(),
                        verifyOtpResponse.result.getQrcode_image(),
                        roleId);
                PrefManager.getInstance(VerifyOtpActivity.this).userLogin(userData);
                if (roleId.equalsIgnoreCase("3")) {
                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            generateQrCode(verifyOtpResponse.result.getMobile(), userId, this);
                        }
                    };
                    mThread.start();
                }else {
                    dismissLoading();
                    LoginPre.getActiveInstance(VerifyOtpActivity.this).setIsLoggedIn(true);
                    Intent intent = new Intent(VerifyOtpActivity.this, ProfessionalActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            } else {
                if (verifyOtpResponse != null && verifyOtpResponse.getMessage() != null) {
                    AppUtils.openPopup(VerifyOtpActivity.this, R.style.Dialod_UpDown, "error", verifyOtpResponse.getMessage());
                } else {
                    AppUtils.openPopup(VerifyOtpActivity.this, R.style.Dialod_UpDown, "error", getResources().getString(R.string.something_wrong));
                }
            }
        });
    }

    private void generateQrCode(String mobile, String id, Thread thread) {
        int width = 500;
        int height = 500;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            bitMatrix = new MultiFormatWriter().encode(
                    new String(mobile.getBytes()),
                    BarcodeFormat.QR_CODE, width, height, hintMap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        int bitMatrixWidth = 0;
        if (bitMatrix != null) {
            bitMatrixWidth = bitMatrix.getWidth();
        }

        int bitMatrixHeight = 0;
        if (bitMatrix != null) {
            bitMatrixHeight = bitMatrix.getHeight();
        }

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {

            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.bla_trans) : getResources().getColor(R.color.white);

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.RGB_565);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);

        // BitMatrix finalBitMatrix = bitMatrix;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        //File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        File file = new File(getCacheDir(), "temporary_file.jpg");
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        runOnUiThread(() -> sendImage(file, id));
        stopThread(thread);
    }

    private synchronized void stopThread(Thread theThread) {
        if (theThread != null) {
            // theThread = null;
            theThread.interrupt();
        }
    }

    private void sendImage(File file, String id) {
        Log.e(TAG, "USER ID--->" + id);
        RequestBody fileReqBody = RequestBody.create(file, Objects.requireNonNull(MediaType.parse("image/*")));
        MultipartBody.Part part = MultipartBody.Part.createFormData("qrcode_image", file.getName(), fileReqBody);
        RequestBody userId = RequestBody.create(id, MediaType.parse("multipart/form-data"));
        authViewModel.sendQrcode(part, userId).observe(this, verifyOtpResponse -> {
            dismissLoading();
            if (verifyOtpResponse != null && !verifyOtpResponse.error) {
                LoginPre.getActiveInstance(VerifyOtpActivity.this).setIsLoggedIn(true);

                Intent intent = new Intent(VerifyOtpActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            } else {
                if (verifyOtpResponse != null && verifyOtpResponse.getMessage() != null) {
                    AppUtils.openPopup(VerifyOtpActivity.this, R.style.Dialod_UpDown, "error",verifyOtpResponse.getMessage());
                } else {
                    AppUtils.openPopup(VerifyOtpActivity.this, R.style.Dialod_UpDown, "error",getResources().getString(R.string.something_wrong));
                }
            }
        });
    }
}