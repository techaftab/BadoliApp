package com.app.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivitySupportBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.ProfileViewModel;

import java.util.Objects;

public class Support_Activity extends AppCompatActivity implements View.OnClickListener {

    ActivitySupportBinding supportBinding;
    private UserData userData;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportBinding = DataBindingUtil.setContentView(this, R.layout.activity_support_);
        profileViewModel =new ViewModelProvider(this).get(ProfileViewModel.class);
        userData = PrefManager.getInstance(Support_Activity.this).getUserData();
        initView();

    }

    private void initView() {
        setSupportActionBar(supportBinding.toolbarChangePassword);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        supportBinding.toolbarChangePassword.setTitle("");
        supportBinding.toolbarChangePassword.setNavigationOnClickListener(v -> {
            Log.d("tag", "onClick : navigating back to back activity ");
            finish();
        });
        supportBinding.btnSubmit.setOnClickListener(this::contact);
    }

    private void contact(View view) {
        String subject=supportBinding.edittextSubject.getText().toString();
        String message=supportBinding.edittextMessage.getText().toString();
        if (isValid(subject,message)){
            contactUs(subject,message);
        }
    }

    private boolean isValid(String subject, String message) {

        if (TextUtils.isEmpty(subject)){
            supportBinding.edittextSubject.requestFocus();
            AppUtils.showSnackbar(getString(R.string.please_enter_subject), supportBinding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(message)){
            supportBinding.edittextMessage.requestFocus();
            AppUtils.showSnackbar(getString(R.string.please_enter_message), supportBinding.parentLayout);
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(Support_Activity.this);
        supportBinding.layoutProgress.txtMessage.setText(message);
        supportBinding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        supportBinding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void contactUs(String subject, String message) {
        showLoading(getResources().getString(R.string.please_wait));
        profileViewModel.contactUs(userData.getId(),subject,message)
                .observe(this, searchList -> {
                    dismissLoading();
                    if (!searchList.getError()){
                        AppUtils.openPopup(Support_Activity.this,R.style.Dialod_UpDown,"backActivity",searchList.getMessage());
                     /*   SweetToast.success(ContactUsActivity.this,searchList.getMessage());
                        onBackPressed()xxx;*/
                        supportBinding.edittextMessage.setText("");
                        supportBinding.edittextSubject.setText("");
                    } else {
                        if (searchList.getMessage()!=null){
                            AppUtils.openPopup(Support_Activity.this,R.style.Dialod_UpDown,"error",searchList.getMessage());
                        }else {
                            AppUtils.openPopup(Support_Activity.this,R.style.Dialod_UpDown,"error",getResources().getString(R.string.something_went_wrong));
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
