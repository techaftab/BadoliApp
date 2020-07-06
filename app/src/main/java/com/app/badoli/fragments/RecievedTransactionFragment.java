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
import com.app.badoli.adapter.ReceivedListAdapter;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.WalletHistoryBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.repositories.TransactionHistory;
import com.app.badoli.viewModels.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecievedTransactionFragment extends Fragment implements ReceivedListAdapter.ReceivedListClickListner {

    private WalletHistoryBinding fragmentBinding;
    private UserData userData;

    private TransactionViewModel transactionViewModel;

    private final List<TransactionHistory.WalletHistory> receivedList = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static ReceivedListAdapter receivedListAdapter;

    public RecievedTransactionFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.wallet_history,container,false);
        View view = fragmentBinding.getRoot();
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        userData= PrefManager.getInstance(getActivity()).getUserData();
        init();

        return  view;
    }
    private void init() {
        receivedListAdapter = new ReceivedListAdapter(getActivity(), receivedList, this);
        if (AppUtils.hasNetworkConnection(requireActivity())){
            getHistory(userData.getId());
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentBinding.recyclerviewPaid.setLayoutManager(linearLayoutManager);
        // request_list.setHasFixedSize(true);
        fragmentBinding.recyclerviewPaid.setAdapter(receivedListAdapter);
        receivedListAdapter.notifyDataSetChanged();
    }
    private void showLoading(){
        AppUtils.hideKeyboardFrom(requireActivity());
        fragmentBinding.progressbarHistory.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void dismissLoading(){
        fragmentBinding.progressbarHistory.setVisibility(View.INVISIBLE);
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void getHistory(String id) {
        showLoading();
        transactionViewModel.getHistory(id).observe(getViewLifecycleOwner(), transactionHistory -> {
            dismissLoading();
            if (!transactionHistory.error) {
               /* for (int j=0;j<transactionHistory.wallethistory.get(i).getWallethistoryinner().size();j++) {
                       if (transactionHistory.wallethistory.get(i).getWallethistoryinner().get(j).type.equals("Debit")){

                       }
                   }*/
                receivedList.clear();
                receivedList.addAll(transactionHistory.wallethistory);
                receivedListAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(getActivity(), transactionHistory.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
