package com.webmobril.badoli.activities.HomePageActivites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.AccountActivities.LoginActivity;
import com.webmobril.badoli.activities.NavigationActivities.AboutUsActivity;
import com.webmobril.badoli.activities.NavigationActivities.ChangePasswordActivity;
import com.webmobril.badoli.activities.NavigationActivities.Support_Activity;
import com.webmobril.badoli.activities.NavigationActivities.WalletActivity;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ActivityHomePageBinding;
import com.webmobril.badoli.fragments.FragmentMerchant;
import com.webmobril.badoli.fragments.FragmentPayById;
import com.webmobril.badoli.fragments.HelpFragment;
import com.webmobril.badoli.fragments.HomeFragment;
import com.webmobril.badoli.fragments.ProfileFragment;
import com.webmobril.badoli.fragments.TransactionFragment;
import com.webmobril.badoli.model.UserData;
import com.webmobril.badoli.utilities.LoginPre;
import com.webmobril.badoli.viewModels.HomeViewModel;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    public static ActivityHomePageBinding homePageBinding;
    boolean openDrawer = false;
    static UserData userData;
    HomeViewModel homeViewModel;
    static Fragment currentFragment;
    static FragmentTransaction ft;
    Handler handler;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homePageBinding = DataBindingUtil.setContentView(HomePageActivity.this, R.layout.activity_home_page);
        homeViewModel= ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(HomeViewModel.class);
        homePageBinding.setHandler(homeViewModel);
        activity=HomePageActivity.this;
      //setupBottomNavigationListener();
        userData= PrefManager.getInstance(HomePageActivity.this).getUserData();
        handler=new Handler();
        loadData();
        loadFragment("0");
        init();
    }

    private void init() {
        homePageBinding.commonHeader.hamburger.setOnClickListener(this);
        homePageBinding.commonHeader.imgBackMain.setOnClickListener(this);
        homePageBinding.drawerMenuItems.openWallet.setOnClickListener(this);
        homePageBinding.drawerMenuItems.about.setOnClickListener(this);
        homePageBinding.drawerMenuItems.support.setOnClickListener(this);
        homePageBinding.drawerMenuItems.changePassword.setOnClickListener(this);
        homePageBinding.drawerMenuItems.layoutLogout.setOnClickListener(this);

    }

    public void loadFragment(String anim) {
        ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new HomeFragment();
        if (anim.equals("1")){
            ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        }
        ft.replace(R.id.rootLayout, currentFragment);
        ft.commit();
        homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
        homePageBinding.commonHeader.hamburger.setVisibility(View.VISIBLE);
        homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
        homePageBinding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        showBadge(homePageBinding.bottomNavigation, R.id.navigation_transaction, "2");
        homePageBinding.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        homePageBinding.commonHeader.badoliPhoneText.setText("Badolipay ("+userData.getMobile()+")");
        homePageBinding.commonHeader.txtWalletBalance.setText("$ "+userData.getWallet_balance());
        homePageBinding.drawerMenuItems.userName.setText(userData.getName());
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new HomeFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
                homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
                homePageBinding.commonHeader.hamburger.setVisibility(View.VISIBLE);
                homePageBinding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
                homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
                return true;
            case R.id.navigation_profile:
                ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new ProfileFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
              //  homePageBinding.commonHeader.mainLayout.setBackground(null);
              //  homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
                homePageBinding.commonHeader.mainLayout.setVisibility(View.GONE);
             //   homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
                return true;
            case R.id.navigation_transaction:
                ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new TransactionFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
              //  homePageBinding.commonHeader.mainLayout.setBackground(null);
              //  homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
                homePageBinding.commonHeader.mainLayout.setVisibility(View.GONE);
              //  homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
                return true;
            case R.id.navigation_help:
                ft = getSupportFragmentManager().beginTransaction();
                currentFragment = new HelpFragment();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
              //  homePageBinding.commonHeader.mainLayout.setBackground(null);
               // homePageBinding.commonHeader.hamburger.setVisibility(View.GONE);
                homePageBinding.commonHeader.mainLayout.setVisibility(View.GONE);
               // homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
                return true;
        }
        return false;
    };


    @Override
    public void onClick(View v) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.rootLayout);
        if (v==homePageBinding.drawerMenuItems.openWallet){
            openDrawer = false;
            homePageBinding.drawerLayout.closeDrawer(Gravity.LEFT);
            Intent wallet = new Intent(HomePageActivity.this, WalletActivity.class);
            startActivity(wallet);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==homePageBinding.commonHeader.imgBackMain){
            if (f instanceof FragmentPayById){
               /* homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
                homePageBinding.commonHeader.hamburger.setVisibility(View.VISIBLE);
                homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
                homePageBinding.commonHeader.mainLayout.setVisibility(View.VISIBLE);*/
               // loadData();
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
        if (v==homePageBinding.drawerMenuItems.about){
            openDrawer = false;
            homePageBinding.drawerLayout.closeDrawer(Gravity.LEFT);
            Intent about = new Intent(HomePageActivity.this, AboutUsActivity.class);
            startActivity(about);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==homePageBinding.drawerMenuItems.support){
            openDrawer = false;
            homePageBinding.drawerLayout.closeDrawer(Gravity.LEFT);
            Intent support = new Intent(HomePageActivity.this, Support_Activity.class);
            startActivity(support);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==homePageBinding.drawerMenuItems.changePassword){
            openDrawer = false;
            homePageBinding.drawerLayout.closeDrawer(Gravity.LEFT);
            Intent change = new Intent(HomePageActivity.this, ChangePasswordActivity.class);
            startActivity(change);
            overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }
        if (v==homePageBinding.drawerMenuItems.layoutLogout){
            openDrawer = false;
            homePageBinding.drawerLayout.closeDrawer(Gravity.LEFT);
            logout();
        }
        switch (v.getId()) {
            case R.id.hamburger:
                if (!openDrawer) {
                    homePageBinding.drawerLayout.openDrawer(Gravity.LEFT);
                    openDrawer = true;
                } else {
                    openDrawer = false;
                    homePageBinding.drawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.rootLayout);
        homePageBinding.bottomNavigation.getMenu().getItem(0).setChecked(true);
        if (f instanceof FragmentPayById) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.left_in,R.anim.right_out);
            currentFragment = new FragmentMerchant();
            ft.replace(R.id.rootLayout, currentFragment);
            ft.addToBackStack("home");
            ft.commit();
        } else if (!(f instanceof HomeFragment)) {
            loadFragment("1");
        } else if (openDrawer) {
            openDrawer = false;
            homePageBinding.drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            new AlertDialog.Builder(HomePageActivity.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
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
        View badge = LayoutInflater.from(HomePageActivity.this).inflate(R.layout.layout_bottom_badge, bottomNavigationView, false);
        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(Html.fromHtml("<font color='#FFFFFF'>"+"New request["+"</font>"
                +"<font color='#FF6700'>"+value+"</font>"
                +"<font color='#FFFFFF'>"+"]"+"</font>"));
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        //  BottomNavigationItemView itemView = bottomNavigationView.findViewById(R.id.bottom_navigation);
        if (bottomNavigationView.getChildCount() == 3) {
            bottomNavigationView.removeViewAt(2);
        }
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure want to logout?");
        builder.setPositiveButton("No", (dialog, id) -> dialog.cancel());
        builder.setNegativeButton("Yes", (dialog, which) -> {
            homePageBinding.progressbarMain.setVisibility(View.VISIBLE);
            handler.postDelayed(() -> {
                homePageBinding.progressbarMain.setVisibility(View.GONE);
                PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this).edit().clear().apply();
                PrefManager.getInstance(HomePageActivity.this).logout();
                LoginPre.getActiveInstance(HomePageActivity.this).setIsLoggedIn(false);
                Intent intent1 = new Intent(HomePageActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
            },300);
        });
        builder.create();
        builder.show();
    }
}
