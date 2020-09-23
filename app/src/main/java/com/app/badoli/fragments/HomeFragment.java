package com.app.badoli.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.AddMoney;
import com.app.badoli.activities.HomePageActivites.AgentActivity;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.HomeFragmentBinding;
import com.app.badoli.model.UserData;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public HomeFragment() {
    }

    private Fragment currentFragment;
    private FragmentTransaction ft;
    private HomeFragmentBinding homeFragmentBinding;

    UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){e.printStackTrace();}
        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        View view = homeFragmentBinding.getRoot();
        userData = PrefManager.getInstance(requireActivity()).getUserData();
        //((HomePageActivity) Objects.requireNonNull(getActivity())).headerHome();
        try {
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){e.printStackTrace();}
        setUpListener();
        return view;
    }

    private void setUpListener() {
      /*  if (userData.getUserType().equalsIgnoreCase("4")) {
            ((ProfessionalActivity)requireActivity()).homeData();
        } else {*/
            ((HomePageActivity) requireActivity()).homeData();
       //}
        homeFragmentBinding.payuMerchant.setOnClickListener(this);
        homeFragmentBinding.transferAgent.setOnClickListener(this);
        homeFragmentBinding.rechargeComte.setOnClickListener(this);
        homeFragmentBinding.lnCorporate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payu_merchant:
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
                requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;

            case R.id.recharge_comte:
                Intent wallet = new Intent(getActivity(), AddMoney.class);
                startActivity(wallet);
                requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;

            case R.id.ln_corporate:

                break;

        }
    }
}
