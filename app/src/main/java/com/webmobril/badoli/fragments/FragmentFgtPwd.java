package com.webmobril.badoli.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityForgetPasswordBinding;
import com.webmobril.badoli.databinding.HelpFragmentBinding;

public class FragmentFgtPwd extends Fragment {

    public FragmentFgtPwd () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityForgetPasswordBinding fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.activity_forget_password,container,false);

        View view   = fragmentBinding.getRoot();

        return  view;

    }
}
