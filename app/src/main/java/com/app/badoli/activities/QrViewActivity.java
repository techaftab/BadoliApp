package com.app.badoli.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.databinding.ActivityQrViewBinding;
import com.app.badoli.viewModels.ProfessionalViewModel;

public class QrViewActivity extends AppCompatActivity {

    private ActivityQrViewBinding binding;
    private ProfessionalViewModel professionalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_view);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        init();
    }

    private void init() {
        binding.toolbar.imgBack.setOnClickListener(this::back);
        binding.toolbar.txtTitle.setText(getResources().getString(R.string.badoli_qr));
    }

    private void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}