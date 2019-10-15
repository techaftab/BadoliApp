package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityEnterCodeBinding;

public class EnterCodeActivity extends AppCompatActivity  implements View.OnClickListener{

    ActivityEnterCodeBinding codeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       codeBinding = DataBindingUtil.setContentView(this,R.layout.activity_enter_code);
       codeBinding.commonHeader.header.setVisibility(View.VISIBLE);
       codeBinding.commonHeader.header.setBackgroundColor(getResources().getColor(R.color.dark_pink));
       codeBinding.commonHeader.badoliPhoneText.setText("Enter Code");
       codeBinding.commonHeader.balanceLayout.setVisibility(View.GONE);
       codeBinding.btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                Intent intent = new Intent(this, ConfirmPayment.class);
                startActivity(intent);
            break;
        }
    }
}
