package com.app.badoli.viewModels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.repositories.TransactionHistory;
import com.app.badoli.repositories.TransactionRepositories;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepositories transactionRepositories;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        transactionRepositories = new TransactionRepositories();
    }

    public LiveData<TransactionHistory> getHistory(String id) {
        return transactionRepositories.getHistory(id);
    }
}
