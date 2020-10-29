package com.app.badoli.switchstaff;

import android.os.Bundle;
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
import com.app.badoli.databinding.ActivityStaffDetailBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import xyz.hasnat.sweettoast.SweetToast;

import static android.content.ContentValues.TAG;

public class StaffDetailActivity extends AppCompatActivity {

    private ActivityStaffDetailBinding binding;
    private ProfessionalViewModel professionalViewModel;

    UserData userData;

    String staffId,staffCode,staffPin,merchantId,staffName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_detail);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        userData = PrefManager.getInstance(StaffDetailActivity.this).getUserData();
        init();
    }

    private void init() {
        binding.toolbar.imgBack.setOnClickListener(this::back);
        binding.btnRenew.setOnClickListener(this::renewCode);
        binding.btnDelete.setOnClickListener(this::deleteCode);

        staffId=getIntent().getStringExtra(Constant.STAFF_ID);
        staffCode=getIntent().getStringExtra(Constant.AGENT_CODE);
        staffPin =getIntent().getStringExtra(Constant.AGENT_PIN);
        merchantId=getIntent().getStringExtra(Constant.MERCHANT_ID);
        staffName=getIntent().getStringExtra(Constant.AGENT_NAME);

        binding.toolbar.txtTitle.setText(staffName);
        binding.toolbar.txtTitle.setAllCaps(true);
        binding.txtCode.setText(staffCode);
        binding.txtPinCode.setText(staffPin);
        Glide.with(this).load(Constant.IMAGE_URL+userData.getQrcode_image())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .thumbnail(0.06f)
                .into(binding.imgQrCode);
    }
    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(StaffDetailActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void deleteCode(View view) {
        showLoading(getResources().getString(R.string.deleting_code));
        professionalViewModel.deleteStaffCode(staffId).observe(StaffDetailActivity.this,
                responseList -> {
                    Log.e(TAG,"DELETE STAFF"+ new Gson().toJson(responseList));
                    dismissLoading();
                    if (responseList!=null&&!responseList.getError()) {
                        AppUtils.openPopup(StaffDetailActivity.this,R.style.Dialod_UpDown,"backActivity",responseList.getMessage());
                       // onBackPressed();
                    } else {
                        if (responseList!=null&&responseList.getMessage() != null) {
                            SweetToast.error(StaffDetailActivity.this,responseList.getMessage());
                           /* AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    responseList.getMessage());*/
                        } else {
                            AppUtils.openPopup(StaffDetailActivity.this,R.style.Dialod_UpDown,"error",
                                    getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

    private void renewCode(View view) {
        showLoading(getResources().getString(R.string.regenerating_code));
        professionalViewModel.renewCode(userData.getId(),staffId).observe(StaffDetailActivity.this,
                responseList -> {
                    Log.e(TAG,"RENEW STAFF"+ new Gson().toJson(responseList));
                    dismissLoading();
                    if (responseList!=null&&!responseList.getError()) {
                        binding.txtCode.setText(responseList.getResult().getAgent_code());
                        binding.txtPinCode.setText(responseList.getResult().getAgent_pin());
                        AppUtils.openPopup(StaffDetailActivity.this,R.style.Dialod_UpDown,"",responseList.getMessage());
                        // onBackPressed();
                    } else {
                        if (responseList!=null&&responseList.getMessage() != null) {
                            SweetToast.error(StaffDetailActivity.this,responseList.getMessage());
                           /* AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    responseList.getMessage());*/
                        } else {
                            AppUtils.openPopup(StaffDetailActivity.this,R.style.Dialod_UpDown,"error",
                                    getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

    private void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}