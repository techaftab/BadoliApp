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
import com.webmobril.badoli.databinding.FragmentAgentPhoneBinding;

public class FragmentAgentPhone extends Fragment {
    private FragmentAgentPhoneBinding fragmentAgentPhoneBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAgentPhoneBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_agent_phone, container, false);
        View view = fragmentAgentPhoneBinding.getRoot();

        return view;
    }

}
