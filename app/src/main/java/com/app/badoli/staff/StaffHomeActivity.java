package com.app.badoli.staff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.badoli.R;
import com.app.badoli.activities.NavigationActivities.AboutUsActivity;
import com.app.badoli.activities.NavigationActivities.Support_Activity;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityStaffHomeBinding;
import com.app.badoli.model.StaffData;
import com.app.badoli.switchstaff.StaffSwitchActivity;
import com.app.badoli.utilities.LoginPre;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class StaffHomeActivity extends AppCompatActivity implements updateBalance {

    private ActivityStaffHomeBinding binding;
    WebService webService;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    StaffData staffData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_home);
        staffData=PrefManager.getInstance(StaffHomeActivity.this).getStaffData();
        viewUpdate();
        updateLanguage();
    }

    private void viewUpdate() {
        loadFragment("0");
        init();
    }

    public void loadFragment(String anim) {
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new StaffHomeFragment();
        if (anim.equals("1")){
            ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        }
        ft.replace(R.id.rootLayout, currentFragment);
        ft.commit();
        homeData();
        loadData();
    }
    @SuppressLint("SetTextI18n")
    public void loadData() {
        showBadge(binding.bottomNavigation, R.id.navigation_transaction, "2");
      //  binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //String mobile=userData.getMobile().substring(0,3)+" "+userData.getMobile().substring(3);
        binding.commonHeader.badoliPhoneText.setText(staffData.getStaffName()+" ("+")");
        binding.commonHeader.txtWalletBalance.setText(staffData.getWalletBalance()+" FCFA");
        binding.drawerMenuItems.userName.setText(staffData.getStaffName());
    }

/*
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        dismissLoading();
        switch (item.getItemId()) {
            case R.id.navigation_home:
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
                currentFragment = new ProfessionalHomeFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
                return true;
            case R.id.navigation_profile:
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
                currentFragment = new ProfessionalProfileFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
                return true;
            case R.id.navigation_transaction:
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
                currentFragment = new TransactionFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
                return true;
            case R.id.navigation_help:
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
                currentFragment = new HelpFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
                return true;
        }
        return false;
    };
*/


    public void showBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        //removeBadge(bottomNavigationView, itemId);
        //BottomNavigationItemView itemView = bottomNavigationView.findViewById(R.id.bottom_navigation);
        View v = bottomNavigationView.findViewById(itemId);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badge = LayoutInflater.from(StaffHomeActivity.this).inflate(R.layout.layout_bottom_badge,
                bottomNavigationView, false);
        TextView text = badge.findViewById(R.id.badge_text_view);
       /* text.setText(Html.fromHtml("<font color='#FFFFFF'>"+getResources().getString(R.string.new_request)+"["+"</font>"
                +"<font color='#FF6700'>"+value+"</font>"
                +"<font color='#FFFFFF'>"+"]"+"</font>"));*/
        text.setText("");
        itemView.addView(badge);
    }


    private void init() {
        webService=new WebService(this);
        webService.updateBalance(staffData.getId());

        binding.commonHeader.hamburger.setOnClickListener(this::openDrawer);
        binding.drawerMenuItems.about.setOnClickListener(this::goAbout);
        binding.drawerMenuItems.support.setOnClickListener(this::goSupport);
        binding.drawerMenuItems.layoutLogout.setOnClickListener(this::goLogout);
       // binding.drawerMenuItems.rlProfileInfo.setOnClickListener(this::pro);
    }

    private void goLogout(View view) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        logout();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.want_logout));
        builder.setPositiveButton(getResources().getString(R.string.no), (dialog, id) ->dialog.cancel());
        builder.setNegativeButton(getResources().getString(R.string.yes), (dialog, which) -> {
            showLoading(getResources().getString(R.string.logging_out));
            new Handler().postDelayed(() -> {
                dismissLoading();
                if (staffData.getUserType().equals("5")){
                    PrefManager.getInstance(StaffHomeActivity.this).logout();
                    LoginPre.getActiveInstance(StaffHomeActivity.this).setIsLoggedIn(false);
                    Intent intent1 = new Intent(StaffHomeActivity.this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }else {
                    finish();
                }
                //PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this).edit().clear().apply();
            },200);
        });
        builder.create();
        builder.show();

    }

    private void goSupport(View view) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        Intent support = new Intent(StaffHomeActivity.this, Support_Activity.class);
        startActivity(support);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void goAbout(View view) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        Intent about = new Intent(StaffHomeActivity.this, AboutUsActivity.class);
        startActivity(about);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void openDrawer(View view) {
        if(!binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.openDrawer(GravityCompat.START);
        else binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @SuppressLint("SetTextI18n")
    public void homeData() {
        binding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        binding.bottomNavigation.getMenu().getItem(0).setChecked(true);
        binding.commonHeader.mainLayout.setBackgroundResource(R.drawable.header_background_red);
        binding.commonHeader.hamburger.setVisibility(View.VISIBLE);
        binding.commonHeader.imgBackMain.setVisibility(View.GONE);
        binding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
        binding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        binding.bottomNavigation.setVisibility(View.GONE);
        //String mobile=staffData.getStaffName().substring(0,3)+" "+userData.getMobile().substring(3);
        binding.commonHeader.badoliPhoneText.setText(staffData.getStaffName()+" ("+")");
        binding.commonHeader.txtWalletBalance.setText(staffData.getWalletBalance()+" FCFA");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String balance) {
        PrefManager.getInstance(StaffHomeActivity.this).setWalletBalance(balance);
        binding.commonHeader.txtWalletBalance.setText(balance+" FCFA");
        staffData.setWalletBalance(balance);
        dismissLoading();
    }

    @Override
    public void onConfigurationChanged(@NonNull android.content.res.Configuration newConfig) {
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_home);
        super.onConfigurationChanged(newConfig);
    }
    private void updateLanguage() {
        if (!TextUtils.isEmpty(LoginPre.getActiveInstance(StaffHomeActivity.this).getLocaleLangua())) {
            Locale myLocale = new Locale(LoginPre.getActiveInstance(StaffHomeActivity.this).getLocaleLangua());
            // Locale.setDefault(myLocale);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            // onConfigurationChanged(conf);
        }
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(StaffHomeActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (staffData.getUserType().equals("5")) {
                new AlertDialog.Builder(StaffHomeActivity.this)
                        .setTitle(getResources().getString(R.string.really_exit))
                        .setMessage(getResources().getString(R.string.are_sure_exit))
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            Intent launchNextActivity = new Intent(Intent.ACTION_MAIN);
                            launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                            launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchNextActivity);
                            finish();
                        }).create().show();
            } else {
                Intent intent = new Intent(StaffHomeActivity.this, StaffSwitchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
                finish();
            }
        }
    }
}