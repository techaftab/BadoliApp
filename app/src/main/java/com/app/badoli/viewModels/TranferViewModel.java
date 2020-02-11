package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.WalletTransfer;
import com.app.badoli.repositories.TranferRepositories;

public class TranferViewModel extends AndroidViewModel {

    private TranferRepositories tranferRepositories;

    public TranferViewModel(@NonNull Application application) {
        super(application);
        tranferRepositories = new TranferRepositories();
    }

    public LiveData<WalletTransfer> transferMobile(String amount, String recieverId, String senderId) {
        return tranferRepositories.transferMobile(amount, recieverId, senderId);
    }
}
