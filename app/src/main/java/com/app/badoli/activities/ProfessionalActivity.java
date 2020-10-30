package com.app.badoli.activities;

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
import com.app.badoli.switchstaff.StaffListActivity;
import com.app.badoli.switchstaff.StaffSwitchActivity;
import com.app.badoli.activities.HomePageActivites.AddMoney;
import com.app.badoli.activities.NavigationActivities.AboutUsActivity;
import com.app.badoli.activities.NavigationActivities.ChangePasswordActivity;
import com.app.badoli.activities.NavigationActivities.Support_Activity;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityProfessionalBinding;
import com.app.badoli.fragments.FragmentMerchant;
import com.app.badoli.fragments.FragmentPayById;
import com.app.badoli.fragments.HelpFragment;
import com.app.badoli.fragments.HomeFragment;
import com.app.badoli.fragments.TransactionFragment;
import com.app.badoli.model.UserData;
import com.app.badoli.professionalFragment.ProfessionalHomeFragment;
import com.app.badoli.professionalFragment.ProfessionalProfileFragment;
import com.app.badoli.utilities.LoginPre;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class ProfessionalActivity extends AppCompatActivity implements updateBalance, View.OnClickListener {

    private ActivityProfessionalBinding binding;
    private UserData userData;

    WebService webService;
    private FragmentTransaction ft;
    private Fragment currentFragment;

   // boolean openDrawer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_professional);
        viewUpdate();
        updateLanguage();
    }

    private void viewUpdate() {
        userData= PrefManager.getInstance(ProfessionalActivity.this).getUserData();
        loadFragment("0");
        init();
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(ProfessionalActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void checkBalance(View view) {
        showLoading(getResources().getString(R.string.plz_wait));
        webService.updateBalance(userData.getId());
    }

    @Override
    public void onConfigurationChanged(@NonNull android.content.res.Configuration newConfig) {
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        super.onConfigurationChanged(newConfig);
    }

    private void updateLanguage() {
        if (!TextUtils.isEmpty(LoginPre.getActiveInstance(ProfessionalActivity.this).getLocaleLangua())) {
            Locale myLocale = new Locale(LoginPre.getActiveInstance(ProfessionalActivity.this).getLocaleLangua());
            // Locale.setDefault(myLocale);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            // onConfigurationChanged(conf);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String balance) {
        PrefManager.getInstance(ProfessionalActivity.this).setWalletBalance(balance);
        binding.commonHeader.txtWalletBalance.setText(balance+" FCFA");
        userData.setWallet_balance(balance);
        dismissLoading();
    }

    private void init() {
        webService=new WebService(this);
        webService.updateBalance(userData.getId());

        binding.commonHeader.hamburger.setOnClickListener(this);
        binding.commonHeader.imgBackMain.setOnClickListener(this);
        binding.drawerMenuItems.openWallet.setOnClickListener(this);
        binding.drawerMenuItems.about.setOnClickListener(this);
        binding.drawerMenuItems.support.setOnClickListener(this);
        binding.drawerMenuItems.changePassword.setOnClickListener(this);
        binding.drawerMenuItems.layoutLogout.setOnClickListener(this);
        binding.drawerMenuItems.rlStaff.setOnClickListener(this);
        binding.drawerMenuItems.rlSwitch.setOnClickListener(this);
        binding.drawerMenuItems.rlBussinessInfo.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webService.updateBalance(userData.getId());
    }

    public void loadFragment(String anim) {
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new ProfessionalHomeFragment();
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
        binding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        String mobile=userData.getMobile().substring(0,3)+" "+userData.getMobile().substring(3);
        binding.commonHeader.badoliPhoneText.setText(userData.getName()+" ("+mobile+")");
        binding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
        binding.drawerMenuItems.userName.setText(userData.getName());
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
        binding.bottomNavigation.setVisibility(View.VISIBLE);
        String mobile=userData.getMobile().substring(0,3)+" "+userData.getMobile().substring(3);
        binding.commonHeader.badoliPhoneText.setText(userData.getName()+" ("+mobile+")");
        binding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
    }

    public void showBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        //removeBadge(bottomNavigationView, itemId);
        //BottomNavigationItemView itemView = bottomNavigationView.findViewById(R.id.bottom_navigation);
        View v = bottomNavigationView.findViewById(itemId);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badge = LayoutInflater.from(ProfessionalActivity.this).inflate(R.layout.layout_bottom_badge,
                bottomNavigationView, false);
        TextView text = badge.findViewById(R.id.badge_text_view);
       /*text.setText(Html.fromHtml("<font color='#FFFFFF'>"+getResources().getString(R.string.new_request)+"["+"</font>"
                +"<font color='#FF6700'>"+value+"</font>"
                +"<font color='#FFFFFF'>"+"]"+"</font>"));*/
       text.setText("");
       itemView.addView(badge);
    }

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


    @Override
    public void onClick(View v) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.rootLayout);
        if (v==binding.drawerMenuItems.openWallet){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Intent wallet = new Intent(ProfessionalActivity.this, AddMoney.class);
            startActivity(wallet);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==binding.commonHeader.imgBackMain){
            if (f instanceof FragmentPayById){
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_in,R.anim.right_out);
                currentFragment = new FragmentMerchant();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.addToBackStack("home");
                ft.commit();
            }else if (!(f instanceof HomeFragment)) {
                loadFragment("1");
            }
        }
        if (v==binding.drawerMenuItems.about){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Intent about = new Intent(ProfessionalActivity.this, AboutUsActivity.class);
            startActivity(about);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==binding.drawerMenuItems.support){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Intent support = new Intent(ProfessionalActivity.this, Support_Activity.class);
            startActivity(support);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==binding.drawerMenuItems.changePassword){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Intent change = new Intent(ProfessionalActivity.this, ChangePasswordActivity.class);
            startActivity(change);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==binding.drawerMenuItems.layoutLogout){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            logout();
        }

        if (v==binding.drawerMenuItems.rlStaff){
           // openDrawer = false;
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(ProfessionalActivity.this, StaffListActivity.class);
            startActivity(intent);
           // binding.drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (v==binding.drawerMenuItems.rlSwitch){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(ProfessionalActivity.this, StaffSwitchActivity.class);
            startActivity(intent);
        }

        if (v==binding.drawerMenuItems.rlBussinessInfo){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.bottomNavigation.getMenu().getItem(1).setChecked(true);
            goToFragment(new ProfessionalProfileFragment());
        }

        if (v==binding.commonHeader.hamburger) {
            if(!binding.drawerLayout.isDrawerOpen(GravityCompat.START))
                binding.drawerLayout.openDrawer(GravityCompat.START);
            else binding.drawerLayout.closeDrawer(GravityCompat.START);
           /* if (!openDrawer) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
                openDrawer = true;
            } else {
                openDrawer = false;

            }*/
        }

        //
    }

    private void goToFragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
        ft.replace(R.id.rootLayout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.want_logout));
        builder.setPositiveButton(getResources().getString(R.string.no), (dialog, id) ->dialog.cancel());
        builder.setNegativeButton(getResources().getString(R.string.yes), (dialog, which) -> {
            showLoading(getResources().getString(R.string.logging_out));
            new Handler().postDelayed(() -> {
                dismissLoading();
                //PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this).edit().clear().apply();
                PrefManager.getInstance(ProfessionalActivity.this).logout();
                LoginPre.getActiveInstance(ProfessionalActivity.this).setIsLoggedIn(false);
                Intent intent1 = new Intent(ProfessionalActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            },200);
        });
        builder.create();
        builder.show();
    }

    public void hideHeader(){
        binding.commonHeader.mainLayout.setVisibility(View.GONE);
    }
    @SuppressLint("SetTextI18n")
    public void transactionHeader() {
        binding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
        binding.bottomNavigation.setVisibility(View.VISIBLE);
        binding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        binding.commonHeader.hamburger.setVisibility(View.GONE);
        binding.commonHeader.imgBackMain.setVisibility(View.VISIBLE);
        binding.commonHeader.mainLayout.setBackgroundColor(getResources().getColor(R.color.app_red));
        binding.commonHeader.badoliPhoneText.setVisibility(View.GONE);
        binding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
    }

    public void setBottomBar(int pos){
        binding.bottomNavigation.getMenu().getItem(pos).setChecked(true);
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
        currentFragment = new TransactionFragment();
        ft.replace(R.id.rootLayout, currentFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(ProfessionalActivity.this)
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
        }
    }
}