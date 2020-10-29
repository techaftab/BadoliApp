package com.app.badoli.switchstaff;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.databinding.ActivityStaffSwitchBinding;
import com.app.badoli.professionalFragment.StaffCodeFragment;
import com.app.badoli.professionalFragment.StaffListFragment;

public class StaffSwitchActivity extends AppCompatActivity {

    private ActivityStaffSwitchBinding binding;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_switch);
        init();
    }

    private void init() {
        binding.layoutToolbar.imgBack.setOnClickListener(this::onBackPressed);
        loadFragment("0");
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (f instanceof StaffCodeFragment){
            loadFragment("0");
        }else if (f instanceof StaffListFragment){
            finish();
        }else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }else {
            finish();
        }
    }

    public void loadFragment(String anim) {
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new SwitchListFragment();
        if (anim.equals("1")){
            ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        }
        ft.replace(R.id.frameLayout, currentFragment);
        ft.commit();
    }


    private void onBackPressed(View view) {
        onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(StaffSwitchActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void updateTitle (String title){
        binding.layoutToolbar.txtTitle.setText(title);
    }

}