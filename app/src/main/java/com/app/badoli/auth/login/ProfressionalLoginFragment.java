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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.auth.forget.ForgotPasswordActivity;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.LayoutProfessionalLoginBinding;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;
import com.google.firebase.iid.FirebaseInstanceId;

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
        binding.cardviewStaff.setOnClickListener(this);
        binding.cardViewManager.setOnClickListener(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String token = instanceIdResult.getToken();
            LoginPre.getActiveInstance(getActivity()).setDevice_token(token);
        });
    }

    @Override
    public void onClick(View v) {
        if (v==binding.forgetPassword){
            startActivity(new Intent(getActivity(), ForgotPasswordActivity.class)
                    .putExtra(Constant.ROLE_ID,"4"));
        }
        if (v==binding.cardviewStaff){
            startActivity(new Intent(getActivity(),LoginManagerActivity.class).putExtra(Constant.TYPE_LOGIN,"staff"));
        }
        if (v==binding.cardViewManager){
            startActivity(new Intent(getActivity(),LoginManagerActivity.class).putExtra(Constant.TYPE_LOGIN,"manager"));
         //   loadFragment(new LoginManager(),"manager");
        }
    }

    private void loadFragment(Fragment fragment, String type) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.TYPE_LOGIN,type);
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
        ft.replace(R.id.framelayout, fragment);
        fragment.setArguments(bundle);
        ft.addToBackStack(null);
        ft.commit();
    }
}
