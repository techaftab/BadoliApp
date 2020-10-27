package com.app.badoli.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityQrViewBinding;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.bumptech.glide.Glide;

public class QrViewActivity extends AppCompatActivity {

    private ActivityQrViewBinding binding;
    private ProfessionalViewModel professionalViewModel;

    String merchantId,qrCodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_view);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        init();
    }

    private void init() {
        merchantId=getIntent().getStringExtra(Constant.MERCHANT_ID);
        qrCodeImage=getIntent().getStringExtra(Constant.MERCHANT_QRCODE);
        binding.toolbar.imgBack.setOnClickListener(this::back);
        binding.toolbar.txtTitle.setText(getResources().getString(R.string.badoli_qr));

        Glide.with(this).load(Constant.IMAGE_URL+qrCodeImage)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .thumbnail(0.06f)
                .into(binding.imgQrCode);
    }

    private void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}