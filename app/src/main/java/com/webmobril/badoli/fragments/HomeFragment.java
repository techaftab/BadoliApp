package com.webmobril.badoli.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.webmobril.badoli.activities.HomePageActivites.AgentActivity;
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.activities.HomePageActivites.PayuActivity;
import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.HomeFragmentBinding;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public HomeFragment() {

    }
    private Fragment currentFragment;
    private FragmentTransaction ft;

    boolean openDrawer = false;
    private HomeFragmentBinding homeFragmentBinding;
    AlertDialog alertDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        View view = homeFragmentBinding.getRoot();
        try {
            Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){e.printStackTrace();}
        setUpListener(homeFragmentBinding);
        return view;
    }

    private void setUpListener(HomeFragmentBinding homeFragmentBinding) {
        //homeFragmentBinding.homeMain.hamburger.setOnClickListener(this);
       // homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
        homeFragmentBinding.payuMerchant.setOnClickListener(this);
        homeFragmentBinding.transferAgent.setOnClickListener(this);
        homeFragmentBinding.rechargeComte.setOnClickListener(this);
        homeFragmentBinding.lnCorporate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payu_merchant:
                /*Intent intent = new Intent(getActivity(), PayuActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
                if (getActivity()!=null) {
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    currentFragment = new FragmentMerchant();
                    ft.replace(R.id.rootLayout, currentFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;

            case R.id.transfer_agent:
                Intent intent = new Intent(getActivity(), AgentActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.right_in,R.anim.left_out);
                /*if (getActivity()!=null) {
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    currentFragment = new FragmentAgent();
                    ft.replace(R.id.rootLayout, currentFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }*/
                break;

            case R.id.recharge_comte:

                break;
            case R.id.ln_corporate:

                break;

        }

    }

}
