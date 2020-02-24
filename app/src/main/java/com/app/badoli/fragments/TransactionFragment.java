package com.app.badoli.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.databinding.TransactionFragmentBinding;

import java.util.Objects;

public class TransactionFragment extends Fragment {

    public   TransactionFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TransactionFragmentBinding  fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment,container,false);
        View view = fragmentBinding.getRoot();
        ((HomePageActivity) Objects.requireNonNull(getActivity())).hideHeader();
        try {
          if (getActivity()!=null) {
              getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
              getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
          }
        }catch (Exception e){e.printStackTrace();}
        return  view;
    }
}