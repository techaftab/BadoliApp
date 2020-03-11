package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionRepositories {

    private static final String TAG = TransactionRepositories.class.getSimpleName();

    public TransactionRepositories(){}

    public LiveData<TransactionHistory> getHistory(String id) {
        MutableLiveData<TransactionHistory> mutableLiveDataProfile = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<TransactionHistory> call = apiService.getHistory(id);
        call.enqueue(new Callback<TransactionHistory>() {
            @Override
            public void onResponse(@NonNull Call<TransactionHistory> call, @NonNull Response<TransactionHistory> response) {
                Log.e(TAG, new Gson().toJson(response.body()));

                mutableLiveDataProfile.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<TransactionHistory> call, @NonNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getMessage()));
            }
        });
        return mutableLiveDataProfile;
    }
}
