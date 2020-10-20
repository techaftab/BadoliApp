package com.app.badoli.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.adapter.PaidListAdapter;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.WalletHistoryBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.repositories.TransactionHistory;
import com.app.badoli.viewModels.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaidWalletFragment extends Fragment implements PaidListAdapter.PaidListClickListner {

   // private static final String TAG = PaidWalletFragment.class.getSimpleName();
    private WalletHistoryBinding fragmentBinding;
    private UserData userData;
    private TransactionViewModel transactionViewModel;

    private final List<TransactionHistory.WalletHistory> paidList = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static PaidListAdapter paidListAdapter;

    public PaidWalletFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.wallet_history,container,false);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        userData= PrefManager.getInstance(getActivity()).getUserData();
        View view = fragmentBinding.getRoot();

        init();

        return  view;
    }

    private void init() {
        paidListAdapter = new PaidListAdapter(getActivity(), paidList, this);
        if (AppUtils.hasNetworkConnection(requireActivity())){
            getHistory(userData.getId());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentBinding.recyclerviewPaid.setLayoutManager(linearLayoutManager);
        // request_list.setHasFixedSize(true);
        fragmentBinding.recyclerviewPaid.setAdapter(paidListAdapter);
        paidListAdapter.notifyDataSetChanged();
    }

    private void getHistory(String id) {
        showLoading();
        transactionViewModel.getHistory(id,"Debit")
                .observe(getViewLifecycleOwner(), transactionHistory -> {
            dismissLoading();
            if (!transactionHistory.error) {
               if (transactionHistory.wallethistory.isEmpty()){
                   fragmentBinding.layoutNoItem.noItem.setVisibility(View.VISIBLE);
                   fragmentBinding.recyclerviewPaid.setVisibility(View.GONE);
               }else {
                   fragmentBinding.layoutNoItem.noItem.setVisibility(View.GONE);
                   fragmentBinding.recyclerviewPaid.setVisibility(View.VISIBLE);
                   paidList.clear();
                   paidList.addAll(transactionHistory.wallethistory);
                   paidListAdapter.notifyDataSetChanged();
               }
            } else {
                Toast.makeText(getActivity(), transactionHistory.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dismissLoading() {
        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).dismissLoading();
        }else {
            ((ProfessionalActivity) requireActivity()).dismissLoading();
        }
    }

    private void showLoading() {
        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).showLoading(getResources().getString(R.string.please_wait));
        }else {
            ((ProfessionalActivity) requireActivity()).showLoading(getResources().getString(R.string.please_wait));
        }
    }
}
