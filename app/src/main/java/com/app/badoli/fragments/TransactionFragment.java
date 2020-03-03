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
import com.app.badoli.adapter.TransactionPagerAdapter;
import com.app.badoli.databinding.TransactionFragmentBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class TransactionFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private TransactionFragmentBinding fragmentBinding;

    public   TransactionFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_fragment,container,false);
        View view = fragmentBinding.getRoot();
        ((HomePageActivity) Objects.requireNonNull(getActivity())).transactionHeader();
        try {
          if (getActivity()!=null) {
              getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
              getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        return  view;
    }

    private void init() {
        fragmentBinding.tablayoutTransaction.setTabGravity(TabLayout.GRAVITY_FILL);

        TransactionPagerAdapter mTabsPagerAdapter = new TransactionPagerAdapter(getChildFragmentManager(), getActivity());
        fragmentBinding.viewpagerTransaction.setAdapter(mTabsPagerAdapter);

        fragmentBinding.tablayoutTransaction.addOnTabSelectedListener(this);
        fragmentBinding.tablayoutTransaction.setupWithViewPager(fragmentBinding.viewpagerTransaction);
        fragmentBinding.tablayoutTransaction.getTabAt(0);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        fragmentBinding.viewpagerTransaction.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
