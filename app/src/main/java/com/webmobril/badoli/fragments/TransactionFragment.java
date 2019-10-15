package com.webmobril.badoli.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.TransactionFragmentBinding;

public class TransactionFragment extends Fragment {

    public   TransactionFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TransactionFragmentBinding  fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment,container,false);
        View view = fragmentBinding.getRoot();
        try {
          /*  Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
          if (getActivity()!=null) {
              getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
              getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
          }
        }catch (Exception e){e.printStackTrace();}
        return  view;
    }
}
