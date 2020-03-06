package com.app.badoli.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.badoli.R;
import com.app.badoli.fragments.PaidTransactionFragment;
import com.app.badoli.fragments.RecievedTransactionFragment;

import org.jetbrains.annotations.NotNull;

public class TransactionPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public TransactionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PaidTransactionFragment();
            case 1:
                return new RecievedTransactionFragment();
            default:
                return new PaidTransactionFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.paid);
            case 1:
                return context.getResources().getString(R.string.recieve);
            default:
                return null;
        }
    }
}
