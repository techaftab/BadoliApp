package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.AirtelResponse;
import com.app.badoli.model.ReferenceResponse;
import com.app.badoli.repositories.AddMoneyRepositories;

public class AddMoneyViewModel extends AndroidViewModel {

    private AddMoneyRepositories addMoneyRepositories;

    public AddMoneyViewModel(@NonNull Application application) {
        super(application);
        addMoneyRepositories = new AddMoneyRepositories();
    }

    public LiveData<ReferenceResponse> getReference(String id, String mobile, String amount, String auth_token, String request_for) {
        return addMoneyRepositories.getReference(id, mobile, amount, auth_token,request_for);
    }

    public LiveData<AirtelResponse> goAirtel(String mobile, String amount,String referenceNo,
                                             String telMerchand, String token) {
        return addMoneyRepositories.goAirtel(mobile, amount,referenceNo,telMerchand,token);
    }
}
