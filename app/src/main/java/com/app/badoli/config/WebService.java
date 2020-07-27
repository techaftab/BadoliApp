package com.app.badoli.config;

import android.util.Log;

import androidx.annotation.NonNull;

import com.app.badoli.activities.SplashActivity;
import com.app.badoli.model.BalanceResponse;
import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebService {

    private String TAG=WebService.class.getSimpleName();

    private updateBalance Balance;

    public WebService(updateBalance callback) {
        Balance = callback;
    }

    public void updateBalance(String id) {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<BalanceResponse> call = apiService.getCurrentBalance(id);

        call.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<BalanceResponse> call, @NonNull Response<BalanceResponse> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                if (response.body() != null) {
                    Balance.onUpdateBalance(response.body().getResult().replace(",",""));
                }
            }
            @Override
            public void onFailure(@NonNull Call<BalanceResponse> call,@NonNull Throwable t) {

            }
        });
    }
}
