package com.app.badoli.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityStaffDetailBinding;
import com.app.badoli.viewModels.ProfessionalViewModel;

public class StaffDetailActivity extends AppCompatActivity {

    private ActivityStaffDetailBinding binding;
    private ProfessionalViewModel professionalViewModel;

    String staffCode,staffPin,merchantId,staffName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staff_detail);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        init();
    }

    private void init() {
        binding.toolbar.imgBack.setOnClickListener(this::back);

        staffCode=getIntent().getStringExtra(Constant.AGENT_CODE);
        staffPin =getIntent().getStringExtra(Constant.AGENT_PIN);
        merchantId=getIntent().getStringExtra(Constant.MERCHANT_ID);
        staffName=getIntent().getStringExtra(Constant.AGENT_NAME);

        binding.toolbar.txtTitle.setText(staffName);
        binding.toolbar.txtTitle.setAllCaps(true);
        binding.txtCode.setText(staffCode);
        binding.txtPinCode.setText(staffPin);

    }

    private void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}