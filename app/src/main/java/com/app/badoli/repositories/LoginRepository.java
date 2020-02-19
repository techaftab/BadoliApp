package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.app.badoli.model.LoginResponse;
import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;
import com.app.badoli.model.ChangePasswordModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private static final String TAG = LoginRepository.class.getSimpleName();
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
               // Log.e(TAG, new Gson().toJson(response.body()));
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call,@NonNull Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<ChangePasswordModel> changePassword(String mobile, String confirmPassword, String roleId) {
        MutableLiveData<ChangePasswordModel> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<ChangePasswordModel> call = apiService.changePassword(mobile,confirmPassword,confirmPassword,roleId);

        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordModel> call,@NonNull Response<ChangePasswordModel> response) {
              //  Log.e(TAG, new Gson().toJson(response.body()));
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordModel> call,@NonNull Throwable t) {

            }
        });
        return mutableLiveData;
    }
}
