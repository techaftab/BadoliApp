package com.app.badoli.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.badoli.fragments.PaidFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
//        PaidFragment paidFragment = new PaidFragment();
        return new PaidFragment();
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
