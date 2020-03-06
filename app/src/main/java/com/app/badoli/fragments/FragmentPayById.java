package com.app.badoli.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.config.Configuration;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentPayByidBinding;
import com.app.badoli.model.UserData;

import java.util.Objects;

public class FragmentPayById extends Fragment implements View.OnClickListener {
    FragmentPayByidBinding fragmentBinding;
    UserData userData;
    FragmentTransaction ft;
    Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_byid,container,false);
        View view   = fragmentBinding.getRoot();
        userData= PrefManager.getInstance(getActivity()).getUserData();
        listener();

        return  view;
    }

    @SuppressLint("SetTextI18n")
    private void listener() {
        fragmentBinding.rlBack.setOnClickListener(this);
        ((HomePageActivity) Objects.requireNonNull(getContext())).updateHeader();
    }

    @Override
    public void onClick(View v) {
        if (v==fragmentBinding.rlBack){
            if (getActivity()!=null) {
                Configuration.hideKeyboardFrom(getActivity());
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                currentFragment = new FragmentMerchant();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }
}
