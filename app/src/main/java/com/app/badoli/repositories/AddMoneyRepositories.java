package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.app.badoli.model.AirtelResponse;
import com.app.badoli.model.ReferenceResponse;
import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMoneyRepositories {
    private String TAG=AddMoneyRepositories.class.getSimpleName();

    public AddMoneyRepositories() {}

    public LiveData<ReferenceResponse> getReference(String id, String mobile, String amount, String auth_token, String request_for) {
        MutableLiveData<ReferenceResponse> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<ReferenceResponse> call = apiService.getReference(id, mobile, amount, auth_token,request_for);

        call.enqueue(new Callback<ReferenceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReferenceResponse> call, @NonNull Response<ReferenceResponse> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ReferenceResponse> call,@NonNull Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<AirtelResponse> goAirtel(String mobile, String amount,
                                             String referenceNo, String telMerchand, String token) {
        MutableLiveData<AirtelResponse> mutableLiveData = new MutableLiveData<>();

        Call<AirtelResponse> call = RetrofitConnection.getApiAirtel().goAirtel(mobile, amount,referenceNo,telMerchand,token);

        call.enqueue(new Callback<AirtelResponse>() {
            @Override
            public void onResponse(@NonNull Call<AirtelResponse> call, @NonNull Response<AirtelResponse> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AirtelResponse> call,@NonNull Throwable t) {

            }
        });
        return mutableLiveData;
    }
}
