package com.webmobril.badoli.config;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.webmobril.badoli.model.BalanceResponse;
import com.webmobril.badoli.retrofit.ApiInterface;
import com.webmobril.badoli.retrofit.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebService {

    private String TAG=WebService.class.getSimpleName();

    private Context mContext;

    private updateBalance Balance;

    public WebService(updateBalance callback) {
        Balance = callback;
    }

    public void updateBalance(String id, String auth_token) {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<BalanceResponse> call = apiService.getCurrentBalance(id);

        call.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<BalanceResponse> call, @NonNull Response<BalanceResponse> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
            }
            @Override
            public void onFailure(@NonNull Call<BalanceResponse> call,@NonNull Throwable t) {

            }
        });
    }
}
