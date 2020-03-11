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
import com.app.badoli.adapter.PaidListAdapter;
import com.app.badoli.config.Configuration;
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
        if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
            getHistory(userData.getId());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentBinding.recyclerviewPaid.setLayoutManager(linearLayoutManager);
        // request_list.setHasFixedSize(true);
        fragmentBinding.recyclerviewPaid.setAdapter(paidListAdapter);
        paidListAdapter.notifyDataSetChanged();
    }

    private void showLoading(){
        Configuration.hideKeyboardFrom(Objects.requireNonNull(getActivity()));
        fragmentBinding.progressbarHistory.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void dismissLoading(){
        fragmentBinding.progressbarHistory.setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void getHistory(String id) {
        showLoading();
        transactionViewModel.getHistory(id).observe(getViewLifecycleOwner(), transactionHistory -> {
            dismissLoading();
            if (!transactionHistory.error) {
               /*for (int j=0;j<transactionHistory.wallethistory.get(i).getWallethistoryinner().size();j++) {
                       if (transactionHistory.wallethistory.get(i).getWallethistoryinner().get(j).type.equals("Debit")){
                       }
                   }*/
                paidList.clear();
                paidList.addAll(transactionHistory.wallethistory);
                paidListAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(getActivity(), transactionHistory.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
