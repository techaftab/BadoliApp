package com.webmobril.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.webmobril.badoli.model.AirtelResponse;
import com.webmobril.badoli.model.ReferenceResponse;
import com.webmobril.badoli.repositories.AddMoneyRepositories;

public class AddMoneyViewModel extends AndroidViewModel {

    private AddMoneyRepositories addMoneyRepositories;

    public AddMoneyViewModel(@NonNull Application application) {
        super(application);
        addMoneyRepositories = new AddMoneyRepositories();
    }

    public LiveData<ReferenceResponse> getReference(String id, String mobile, String amount, String auth_token) {
        return addMoneyRepositories.getReference(id, mobile, amount, auth_token);
    }

    public LiveData<AirtelResponse> goAirtel(String mobile, String amount,String referenceNo,
                                             String telMerchand, String token) {
        return addMoneyRepositories.goAirtel(mobile, amount,referenceNo,telMerchand,token);
    }
}
