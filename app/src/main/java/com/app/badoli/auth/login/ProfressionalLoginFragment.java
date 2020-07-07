package com.app.badoli.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.auth.forget.ForgotPasswordActivity;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.LayoutProfessionalLoginBinding;
import com.app.badoli.viewModels.AuthViewModel;

public class ProfressionalLoginFragment extends Fragment implements View.OnClickListener {

    private LayoutProfessionalLoginBinding binding;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_professional_login, container, false);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        View view = binding.getRoot();
        init();
        return view;
    }

    private void init() {
        binding.forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==binding.forgetPassword){
            startActivity(new Intent(getActivity(), ForgotPasswordActivity.class)
                    .putExtra(Constant.ROLE_ID,"4"));
        }
    }
}
