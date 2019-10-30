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
import com.webmobril.badoli.databinding.FragmentAgentBankBinding;

public class FragmentAgentBank extends Fragment {
    FragmentAgentBankBinding fragmentAgentBankBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAgentBankBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_agent_bank, container, false);
        View view = fragmentAgentBankBinding.getRoot();

        return view;
    }

}
