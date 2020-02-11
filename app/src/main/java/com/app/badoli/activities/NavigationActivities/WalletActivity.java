package com.app.badoli.activities.NavigationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.app.badoli.adapter.ViewPagerAdapter;
import com.app.badoli.R;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityWalletBinding;
import com.app.badoli.model.UserData;

public class WalletActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener, View.OnClickListener {
    ActivityWalletBinding walletBinding;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    int position;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
    /*  walletBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.text_orange));
        walletBinding.commonHeader.badoliPhoneText.setVisibility(View.VISIBLE);
        walletBinding.commonHeader.badoliPhoneText.setText("Wallet");
        walletBinding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        walletBinding.commonHeader.balanceLayout.setBackgroundColor(getResources().getColor(R.color.text_orange));*/
        userData= PrefManager.getInstance(WalletActivity.this).getUserData();


        walletBinding.tabPaymentStatus.addOnTabSelectedListener(this);

        walletBinding.tabPaymentStatus.addTab(walletBinding.tabPaymentStatus.newTab().setText("Paid"));
        walletBinding.tabPaymentStatus.addTab(walletBinding.tabPaymentStatus.newTab().setText("Receive"));
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), walletBinding.tabPaymentStatus.getTabCount());
        walletBinding.walletViewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();
        loadView();

        walletBinding.walletViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(walletBinding.tabPaymentStatus));
    }

    @SuppressLint("SetTextI18n")
    private void loadView() {
            walletBinding.headerPay.imgBackPay.setOnClickListener(this);
            walletBinding.headerPay.txtTitlePay.setVisibility(View.GONE);
            walletBinding.headerPay.txtPayWalletBalance.setText("$ "+userData.getWallet_balance());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        position = tab.parent.getSelectedTabPosition();
        walletBinding.walletViewPager.setCurrentItem(position);
        Log.e("position", "" + position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        if (v==walletBinding.headerPay.imgBackPay){
            finish();
        }
    }
}
