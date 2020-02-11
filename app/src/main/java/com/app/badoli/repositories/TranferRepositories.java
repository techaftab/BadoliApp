package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.app.badoli.model.WalletTransfer;
import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranferRepositories {

    private String TAG=TranferRepositories.class.getSimpleName();

    public TranferRepositories() {
    }

    public LiveData<WalletTransfer> transferMobile(String amount, String recieverId, String senderId) {
        MutableLiveData<WalletTransfer> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<WalletTransfer> call = apiService.transferMobile(amount, recieverId, senderId);
        call.enqueue(new Callback<WalletTransfer>() {
            @Override
            public void onResponse(@NonNull Call<WalletTransfer> call, @NonNull Response<WalletTransfer> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                mutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<WalletTransfer> call,@NonNull Throwable t) {

            }
        });
        return mutableLiveData;
    }
}
