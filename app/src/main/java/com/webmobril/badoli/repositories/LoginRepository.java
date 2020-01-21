package com.webmobril.badoli.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.webmobril.badoli.model.LoginResponse;
import com.webmobril.badoli.retrofit.ApiInterface;
import com.webmobril.badoli.retrofit.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();

    public LoginRepository() {
    }

    public LiveData<LoginResponse> getMutableLiveData(String mobile, String password, int device_type,
                                                      String devicetoken) {

        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<LoginResponse> call = apiService.getlogin(mobile, password, device_type, devicetoken);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call,@NonNull Response<LoginResponse> response) {
                Log.e("login_response", new Gson().toJson(response.body()));
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call,@NonNull Throwable t) {

            }
        });
        return mutableLiveData;
    }

}
