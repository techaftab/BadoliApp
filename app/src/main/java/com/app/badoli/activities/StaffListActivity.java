package com.app.badoli.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityStaffListBinding;
import com.app.badoli.fragments.HomeFragment;
import com.app.badoli.model.UserData;
import com.app.badoli.professionalFragment.StaffListFragment;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.google.gson.Gson;

public class StaffListActivity extends AppCompatActivity {

    private static final String TAG = StaffListActivity.class.getSimpleName();
    private ActivityStaffListBinding binding;

    UserData userData;
    private ProfessionalViewModel professionalViewModel;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_list);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        init();
    }

    private void init() {
        userData = PrefManager.getInstance(StaffListActivity.this).getUserData();
        loadFragment("0");
    }

    public void loadFragment(String anim) {
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new StaffListFragment();
        if (anim.equals("1")){
            ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        }
        ft.replace(R.id.frameLayout, currentFragment);
        ft.commit();
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(StaffListActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void updateTitle(String title) {
        binding.layoutToolbar.txtTitle.setText(title);
    }
}