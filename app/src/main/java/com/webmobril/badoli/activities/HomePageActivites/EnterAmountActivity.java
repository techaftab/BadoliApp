package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityEnterAmountBinding;

public class EnterAmountActivity extends AppCompatActivity implements  View.OnClickListener{

    ActivityEnterAmountBinding enterAmountBinding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enterAmountBinding = DataBindingUtil.setContentView(this,R.layout.activity_enter_amount);
        enterAmountBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
        enterAmountBinding.commonHeader.header.setVisibility(View.VISIBLE);
        enterAmountBinding.commonHeader.badoliPhoneText.setText("Enter Amount");
        enterAmountBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.dark_pink));
        enterAmountBinding.btnProceed.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_proceed:
                Intent intent = new Intent(this, EnterCodeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
