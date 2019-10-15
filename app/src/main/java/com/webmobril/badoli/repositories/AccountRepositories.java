package com.webmobril.badoli.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.webmobril.badoli.model.CountryResponse;
import com.webmobril.badoli.model.SignupResponse;
import com.webmobril.badoli.model.VerifyOtpResponse;
import com.webmobril.badoli.retrofit.ApiInterface;
import com.webmobril.badoli.retrofit.RetrofitConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepositories {

    private MutableLiveData<SignupResponse> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CountryResponse> mutableCountryLiveData = new MutableLiveData<>();

    private MutableLiveData<VerifyOtpResponse> mutableOtpLiveData = new MutableLiveData<>();
    Context context;


    public AccountRepositories() {
    }

    public LiveData<SignupResponse> getMutableLiveData(String name, String email, String mobile, String password, int deviceType,
                                                       String deviceToken, String confirmPassword, int countryId, int agreeterms) {

        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<SignupResponse> call = apiService.getSignup(name, email, mobile, password, deviceType,
                deviceToken, confirmPassword, countryId, agreeterms);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignupResponse> call,@NonNull Response<SignupResponse> response) {
                Log.e("signup_response", new Gson().toJson(response.message()));
                mutableLiveData.setValue(response.body());
                /*if (!response.body().error) {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }*/
            }
            @Override
            public void onFailure(@NonNull Call<SignupResponse> call,@NonNull Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
        return mutableLiveData;
    }


    public LiveData<CountryResponse> getCountryList() {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<CountryResponse> call = apiService.getCountryList();

        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponse> call,@NonNull Response<CountryResponse> response) {
                Log.e("Country_resonse", new Gson().toJson(response.body()));
                mutableCountryLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CountryResponse> call,@NonNull Throwable t) {
                Log.e("Country_resonse", t.getMessage());
            }
        });

        return mutableCountryLiveData;
    }


    public LiveData<VerifyOtpResponse> getOtpLiveData(int userId, String otp, String access_token) {

        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<VerifyOtpResponse> call = apiService.verifyOtp(userId, otp, access_token);

        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                Log.e("otp_response", new Gson().toJson(response));
                mutableOtpLiveData.setValue(response.body());/*
                if (!response.body().error) {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
        return mutableOtpLiveData;
    }
}
