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
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.databinding.HelpFragmentBinding;

import java.util.Objects;

public class HelpFragment extends Fragment {

     public HelpFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HelpFragmentBinding fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.help_fragment,container,false);

        View view   = fragmentBinding.getRoot();
        ((HomePageActivity) Objects.requireNonNull(getActivity())).hideHeader();

        return  view;

    }
}
