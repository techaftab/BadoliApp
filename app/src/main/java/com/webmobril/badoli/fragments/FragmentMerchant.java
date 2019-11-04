package com.webmobril.badoli.fragments;

import android.annotation.SuppressLint;
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

import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.activities.HomePageActivites.MerchantActivity;
import com.webmobril.badoli.databinding.FragmentMerchantBinding;

import java.util.Objects;

public class FragmentMerchant extends Fragment implements View.OnClickListener {

    FragmentMerchantBinding fragmentBinding;
    FragmentTransaction ft;
    Fragment currentFragment;
  //  UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_merchant,container,false);
        View view   = fragmentBinding.getRoot();

        listener();

        return  view;
    }

    @SuppressLint("SetTextI18n")
    private void listener() {
        ((HomePageActivity) Objects.requireNonNull(getContext())).updateHeader();
        fragmentBinding.payById.setOnClickListener(this);
        fragmentBinding.requestPay.setOnClickListener(this);
        fragmentBinding.payByQr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==fragmentBinding.payById){
            if (getActivity()!=null) {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                currentFragment = new FragmentPayById();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }

        if (v==fragmentBinding.requestPay){
            Intent intent=new Intent(getActivity(), MerchantActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.right_in,R.anim.left_out);
        }

        if (v==fragmentBinding.payByQr){
            if (getActivity()!=null) {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                currentFragment = new FragmentQrPay();
                ft.replace(R.id.rootLayout, currentFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }

    }
}
