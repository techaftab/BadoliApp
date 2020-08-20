package com.app.badoli.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.HelpFragmentBinding;
import com.app.badoli.model.UserData;

import java.util.Objects;

public class HelpFragment extends Fragment {

     public HelpFragment () {}

     UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HelpFragmentBinding fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.help_fragment,container,false);
        View view   = fragmentBinding.getRoot();
        userData = PrefManager.getInstance(requireActivity()).getUserData();

        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).hideHeader();
        }else {
            ((ProfessionalActivity) requireActivity()).hideHeader();
        }

        return  view;

    }
}
