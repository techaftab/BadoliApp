package com.webmobril.badoli.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.webmobril.badoli.fragments.PaidFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

    }

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
