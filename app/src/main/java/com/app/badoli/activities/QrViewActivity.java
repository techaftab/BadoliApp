package com.app.badoli.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityQrViewBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.professionalFragment.StaffCodeFragment;
import com.app.badoli.switchstaff.StaffListActivity;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import xyz.hasnat.sweettoast.SweetToast;

import static android.content.ContentValues.TAG;

public class QrViewActivity extends AppCompatActivity {

    private ActivityQrViewBinding binding;
    private ProfessionalViewModel professionalViewModel;

    String merchantId,qrCodeImage;

    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_view);
        userData = PrefManager.getInstance(QrViewActivity.this).getUserData();
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        init();
    }

    private void init() {
        merchantId=getIntent().getStringExtra(Constant.MERCHANT_ID);
        qrCodeImage=getIntent().getStringExtra(Constant.MERCHANT_QRCODE);
        binding.toolbar.imgBack.setOnClickListener(this::back);
        binding.toolbar.txtTitle.setText(getResources().getString(R.string.badoli_qr));
        binding.btnSend.setOnClickListener(this::validateEmail);

        Glide.with(this).load(Constant.IMAGE_URL+qrCodeImage)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .thumbnail(0.06f)
                .into(binding.imgQrCode);
    }

    private void validateEmail(View view) {
        String email=binding.edittextEmail.getText().toString();
        if (isValidEmail(email)){
            sendEmail(userData.getId(),email);
        }
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(QrViewActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void sendEmail(String id, String email) {
       showLoading(getResources().getString(R.string.creating_staff));
        professionalViewModel.sendQRtoEmail(id,email).observe(this,
                createStaff -> {
                    Log.e(TAG,"CUSTOMER"+ new Gson().toJson(createStaff));
                   dismissLoading();
                    if (createStaff!=null&&!createStaff.getError()) {
                        binding.edittextEmail.setText("");
                        AppUtils.openPopup(QrViewActivity.this,R.style.Dialod_UpDown,"",
                                createStaff.getMessage());
                        SweetToast.success(QrViewActivity.this,createStaff.getMessage());
                    } else {
                        if (createStaff!=null&&createStaff.getMessage() != null) {
                            AppUtils.openPopup(QrViewActivity.this,R.style.Dialod_UpDown,"error",
                                    createStaff.getMessage());
                        } else {
                            AppUtils.openPopup(QrViewActivity.this,R.style.Dialod_UpDown,"error",
                                    getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            binding.edittextEmail.requestFocus();
            binding.edittextEmail.setError(getResources().getString(R.string.email_empty));
            AppUtils.showSnackbar(getString(R.string.email_empty), binding.parentLayout);
            SweetToast.error(QrViewActivity.this,getString(R.string.email_empty));
            return false;
        }
        if (!AppUtils.isEmailValid(email)) {
            binding.edittextEmail.requestFocus();
            binding.edittextEmail.setError(getResources().getString(R.string.invalid_email));
            SweetToast.error(QrViewActivity.this,getString(R.string.enter_valid_email));
            AppUtils.showSnackbar(getString(R.string.enter_valid_email), binding.parentLayout);
            return false;
        }
        return true;
    }

    private void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}