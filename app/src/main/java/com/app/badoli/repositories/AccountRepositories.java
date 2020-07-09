package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.badoli.model.BussinessList;
import com.app.badoli.model.ProfileImageResponse;
import com.app.badoli.model.ResetPassword;
import com.app.badoli.model.SignupResponse;
import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;
import com.google.gson.Gson;

import java.util.Objects;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepositories {

    private String TAG=AccountRepositories.class.getSimpleName();

    private MutableLiveData<SignupResponse> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ProfileImageResponse> mutableLiveDataProfile = new MutableLiveData<>();


    public AccountRepositories() {
    }

    public LiveData<SignupResponse> getMutableLiveData(String name, String email, String mobile, String password, int deviceType,
                                                       String deviceToken, String confirmPassword, int countryId, int agreeterms, String roleId) {

        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<SignupResponse> call = apiService.getSignup(name, email, mobile, password, deviceType,
                deviceToken, confirmPassword, countryId, agreeterms,roleId);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignupResponse> call,@NonNull Response<SignupResponse> response) {
                Log.e(TAG, new Gson().toJson(response.message()));
                mutableLiveData.setValue(response.body());
                /*if (!response.body().error) {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }*/
            }
            @Override
            public void onFailure(@NonNull Call<SignupResponse> call,@NonNull Throwable t) {
                Log.e("error", Objects.requireNonNull(t.getMessage()));
            }
        });
        return mutableLiveData;
    }


    public LiveData<ProfileImageResponse> saveProfileImage(MultipartBody.Part file, int id) {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();

        Call<ProfileImageResponse> call = apiService.saveProfileImage(file, id);

        call.enqueue(new Callback<ProfileImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileImageResponse> call,@NonNull Response<ProfileImageResponse> response) {
                Log.e("profile_image", new Gson().toJson(response.body()));
                mutableLiveDataProfile.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ProfileImageResponse> call, @NonNull Throwable t) {
                Log.e("profile_image error", Objects.requireNonNull(t.getMessage()));
            }
        });
        return mutableLiveDataProfile;
    }


    public LiveData<ResetPassword> resetPassword(String userId, String oldPwd, String newPassword, String confirmPwd) {
        MutableLiveData<ResetPassword> mutableLiveDataProfile = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<ResetPassword> call = apiService.resetPassword(userId,oldPwd,newPassword,confirmPwd);
        call.enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(@NonNull Call<ResetPassword> call,@NonNull Response<ResetPassword> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                mutableLiveDataProfile.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ResetPassword> call, @NonNull Throwable t) {
                Log.e(TAG, "error--->"+ Objects.requireNonNull(t.getMessage()));
            }
        });
        return mutableLiveDataProfile;
    }
}
