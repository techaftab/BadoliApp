package com.app.badoli.transaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.badoli.R;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.adapter.TransactionPagerAdapter;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ActivityTransactionHistoryBinding;
import com.app.badoli.model.UserData;

public class TransactionHistoryActivity extends AppCompatActivity {
    private ActivityTransactionHistoryBinding binding;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_history);
        userData = PrefManager.getInstance(TransactionHistoryActivity.this).getUserData();
        init();
    }

    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(TransactionHistoryActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        binding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
        binding.commonHeader.balanceLayout.setVisibility(View.VISIBLE);
        binding.commonHeader.hamburger.setVisibility(View.GONE);
        binding.commonHeader.imgBackMain.setVisibility(View.VISIBLE);
        binding.commonHeader.mainLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.commonHeader.badoliPhoneText.setVisibility(View.GONE);
        binding.commonHeader.txtWalletBalance.setText(userData.getWallet_balance()+" FCFA");
        binding.commonHeader.imgBackMain.setOnClickListener(this::back);
        binding.tablayoutTransaction.addTab(binding.tablayoutTransaction.newTab().setText(getResources().getString(R.string.paid)));
        binding.tablayoutTransaction.addTab(binding.tablayoutTransaction.newTab().setText(getResources().getString(R.string.recieve)));
        TransactionPagerAdapter mTabsPagerAdapter = new TransactionPagerAdapter(getSupportFragmentManager(), TransactionHistoryActivity.this);
        binding.viewpagerTransaction.setAdapter(mTabsPagerAdapter);
        //binding.tablayoutTransaction.addOnTabSelectedListener((TabLayout.OnTabSelectedListener) this);
        binding.tablayoutTransaction.setupWithViewPager(binding.viewpagerTransaction);
        binding.tablayoutTransaction.getTabAt(0);
    }

    private void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}