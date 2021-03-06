package com.app.badoli.activities.HomePageActivites;

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
import android.widget.Toast;

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
import com.app.badoli.activities.NavigationActivities.ChangePasswordActivity;
import com.app.badoli.activities.NavigationActivities.Support_Activity;
import com.app.badoli.auth.login.LoginActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.config.WebService;
import com.app.badoli.config.updateBalance;
import com.app.badoli.databinding.ActivityHomePageBinding;
import com.app.badoli.fragments.FragmentMerchant;
import com.app.badoli.fragments.FragmentPayById;
import com.app.badoli.fragments.HelpFragment;
import com.app.badoli.fragments.HomeFragment;
import com.app.badoli.fragments.ProfileFragment;
import com.app.badoli.fragments.TransactionFragment;
import com.app.badoli.model.UserData;
import com.app.badoli.utilities.LoginPre;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class HomePageActivity extends AppCompatActivity implements updateBalance {

    public ActivityHomePageBinding homePageBinding;
    // boolean openDrawer = false;
    static UserData userData;
    // HomeViewModel homeViewModel;
    Fragment currentFragment;
    FragmentTransaction ft;
    Handler handler;
    //@SuppressLint("StaticFieldLeak")
    //Activity activity;
    WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePageBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        viewUpdate();
        updateLanguage();
      //  init();
    }

    private void viewUpdate() {
    //    //homeViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(HomeViewModel.class);
      //  homePageBinding.setHandler(homeViewModel);
        //activity=HomePageActivity.this;
        //setupBottomNavigationListener();
        userData= PrefManager.getInstance(HomePageActivity.this).getUserData();
        handler=new Handler();
        loadFragment("0");
        init();
    }

    @Override
    public void onConfigurationChanged(@NonNull android.content.res.Configuration newConfig) {
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
       // homePageBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        //init();
        super.onConfigurationChanged(newConfig);
    }

    private void updateLanguage() {
        if (!TextUtils.isEmpty(LoginPre.getActiveInstance(HomePageActivity.this).getLocaleLangua())) {
            Locale myLocale = new Locale(LoginPre.getActiveInstance(HomePageActivity.this).getLocaleLangua());
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
        PrefManager.getInstance(HomePageActivity.this).setWalletBalance(balance);
        homePageBinding.commonHeader.txtWalletBalance.setText(balance+" FCFA");
        userData.setWallet_balance(balance);
        if (homePageBinding.progressbarMain.isShown()) {
            dismissLoading();
        }
    }

    /*@SuppressLint("SetTextI18n")
    public void updateBalance(String balance) {
        homePageBinding.commonHeader.txtWalletBalance.setText(balance+" FCFA");
        userData.setWallet_balance(balance);
        PrefManager.getInstance(HomePageActivity.this).setWalletBalance(balance);
    }*/

    private void init() {
        webService=new WebService(this);
        webService.updateBalance(userData.getId());
        homePageBinding.commonHeader.hamburger.setOnClickListener(this::openDrawer);
        homePageBinding.commonHeader.imgBackMain.setOnClickListener(this::back);
        homePageBinding.drawerMenuItems.openWallet.setOnClickListener(this::goWallet);
        homePageBinding.drawerMenuItems.about.setOnClickListener(this::goAbout);
        homePageBinding.drawerMenuItems.support.setOnClickListener(this::openSupport);
        homePageBinding.drawerMenuItems.changePassword.setOnClickListener(this::goChangePwd);
        homePageBinding.drawerMenuItems.layoutLogout.setOnClickListener(this::goLogout);
    }

    private void openDrawer(View view) {
      //  homePageBinding.bottomNavigation.requestLayout();
        if(!homePageBinding.drawerLayout.isDrawerOpen(GravityCompat.START))
            homePageBinding.drawerLayout.openDrawer(GravityCompat.START);
        else homePageBinding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void back(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.rootLayout);
        try {
            AppUtils.hideKeyboardFrom(HomePageActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (f instanceof FragmentPayById){
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.left_in,R.anim.right_out);
            currentFragment = new FragmentMerchant();
            ft.replace(R.id.rootLayout, currentFragment);
            ft.addToBackStack(null);
            ft.commit();
        }else if (!(f instanceof HomeFragment)) {
            loadFragment("1");
        }
    }

    private void goWallet(View view) {
        Toast.makeText(this, "gfhdsghf", Toast.LENGTH_SHORT).show();
        homePageBinding.drawerLayout.closeDrawer(GravityCompat.START);
        Intent wallet = new Intent(HomePageActivity.this, AddMoney.class);
        startActivity(wallet);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void goLogout(View view) {
        homePageBinding.drawerLayout.closeDrawer(GravityCompat.START);
        logout();
    }

    private void goChangePwd(View view) {
        homePageBinding.drawerLayout.closeDrawer(GravityCompat.START);
        Intent change = new Intent(HomePageActivity.this, ChangePasswordActivity.class);
        startActivity(change);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void goAbout(View view) {
        homePageBinding.drawerLayout.closeDrawer(GravityCompat.START);
        Intent about = new Intent(HomePageActivity.this, AboutUsActivity.class);
        startActivity(about);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void openSupport(View view) {
        homePageBinding.drawerLayout.closeDrawer(GravityCompat.START);
        Intent support = new Intent(HomePageActivity.this, Support_Activity.class);
        startActivity(support);
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    public void checkBalance(View view) {
        showLoading(getResources().getString(R.string.please_wait));
        webService.updateBalance(userData.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        webService.updateBalance(userData.getId());

    }

    public void loadFragment(String anim) {
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new HomeFragment();
        if (anim.equals("1")){
            ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        }
        ft.replace(R.id.rootLayout, currentFragment);
        ft.commit();
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(HomePageActivity.this);
        homePageBinding.layoutProgress.txtMessage.setText(message);
        homePageBinding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        homePageBinding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @SuppressLint("SetTextI18n")
    public void loadData() {
        showBadge(homePageBinding.bottomNavigation, R.id.navigation_transaction, "2");
        homePageBinding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        String mobile=userData.getMobile().substring(0,3)+" "+userData.getMobile().substring(3);
        homePageBinding.commonHeader.badoliPhoneText.setText(userData.getName()+" ("+mobile+")");
        homePageBinding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
        homePageBinding.drawerMenuItems.userName.setText(userData.getName());

    }

    public void loadFrgament(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        // ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
        ft.replace(R.id.rootLayout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        dismissLoading();
        switch (item.getItemId()) {
            case R.id.navigation_home:
              loadFragment("0");
                return true;
            case R.id.navigation_profile:
                loadFrgament(new ProfileFragment());
               /* ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new ProfileFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();*/
                return true;
            case R.id.navigation_transaction:
                loadFrgament(new TransactionFragment());
               /* ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new TransactionFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();*/
                return true;
            case R.id.navigation_help:
                loadFrgament(new HelpFragment());
               /* ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new HelpFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();*/
                return true;
        }
        return false;
    };

    @Override
    public void onBackPressed() {
        try {
            AppUtils.hideKeyboardFrom(HomePageActivity.this);
        }catch (Exception e) {
            e.printStackTrace();
        }
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.rootLayout);
        homePageBinding.bottomNavigation.getMenu().getItem(0).setChecked(true);
        if (f instanceof FragmentPayById) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.left_in,R.anim.right_out);
            currentFragment = new FragmentMerchant();
            ft.replace(R.id.rootLayout, currentFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (!(f instanceof HomeFragment)) {
            loadFragment("1");
        } else if (homePageBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homePageBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(HomePageActivity.this)
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

    public void showBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        //removeBadge(bottomNavigationView, itemId);
        //BottomNavigationItemView itemView = bottomNavigationView.findViewById(R.id.bottom_navigation);
        View v = bottomNavigationView.findViewById(itemId);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badge = LayoutInflater.from(HomePageActivity.this).inflate(R.layout.layout_bottom_badge,
                bottomNavigationView, false);
        TextView text = badge.findViewById(R.id.badge_text_view);
        /*text.setText(Html.fromHtml("<font color='#FFFFFF'>"+getResources().getString(R.string.new_request)+"["+"</font>"
                +"<font color='#FF6700'>"+value+"</font>"
                +"<font color='#FFFFFF'>"+"]"+"</font>"));*/
        text.setText("");
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        //BottomNavigationItemView itemView = bottomNavigationView.findViewById(R.id.bottom_navigation);
        if (bottomNavigationView.getChildCount() == 3) {
            bottomNavigationView.removeViewAt(2);
        }
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.want_logout));
        builder.setPositiveButton(getResources().getString(R.string.no), (dialog, id) ->dialog.cancel());
        builder.setNegativeButton(getResources().getString(R.string.yes), (dialog, which) -> {
            showLoading(getResources().getString(R.string.logging_out));
            handler.postDelayed(() -> {
                dismissLoading();
                //PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this).edit().clear().apply();
                PrefManager.getInstance(HomePageActivity.this).logout();
                LoginPre.getActiveInstance(HomePageActivity.this).setIsLoggedIn(false);
                Intent intent1 = new Intent(HomePageActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            },300);
        });
        builder.create();
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    public void homeData() {
        loadData();
        homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        homePageBinding.bottomNavigation.getMenu().getItem(0).setChecked(true);
        homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
        homePageBinding.commonHeader.hamburger.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
        homePageBinding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        homePageBinding.bottomNavigation.setVisibility(View.VISIBLE);
       // homePageBinding.drawerLayout.bringToFront();
       // homePageBinding.drawerLayout.requestLayout();
        String mobile=userData.getMobile().substring(0,3)+" "+userData.getMobile().substring(3);
        homePageBinding.commonHeader.badoliPhoneText.setText(userData.getName()+" ("+mobile+")");
        homePageBinding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
    }

    @SuppressLint("SetTextI18n")
    public void updateHeader() {
        homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        homePageBinding.bottomNavigation.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
        homePageBinding.commonHeader.imgBackMain.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.mainLayout.setBackgroundColor(getResources().getColor(R.color.dark_pink));
        String mobile=userData.getMobile().substring(0,3)+" "+userData.getMobile().substring(3);
        homePageBinding.commonHeader.badoliPhoneText.setText(userData.getName()+" ("+mobile+")");
        homePageBinding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
    }

    public void updateToolbar() {
        homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
        homePageBinding.commonHeader.badoliPhoneText.setText("");
        homePageBinding.commonHeader.badoliPhoneText.setText(getResources().getString(R.string.scan_pay));
        homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
        homePageBinding.commonHeader.imgBackMain.setVisibility(View.VISIBLE);
        homePageBinding.bottomNavigation.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void transactionHeader() {
        homePageBinding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
        homePageBinding.bottomNavigation.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
        homePageBinding.commonHeader.imgBackMain.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.mainLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        homePageBinding.commonHeader.badoliPhoneText.setVisibility(View.GONE);
        homePageBinding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
    }

    /*public void headerHome(){
        homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
        homePageBinding.commonHeader.hamburger.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
    }*/

    public void hideHeader(){
        homePageBinding.commonHeader.mainLayout.setVisibility(View.GONE);
    }

}
