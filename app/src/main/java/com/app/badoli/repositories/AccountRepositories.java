package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.badoli.model.ProfileImageResponse;
import com.app.badoli.model.ResetPassword;
import com.app.badoli.model.SignupResponse;
import com.app.badoli.model.UserSignUpResponse;
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


    private MutableLiveData<ProfileImageResponse> mutableLiveDataProfile = new MutableLiveData<>();


    public AccountRepositories() {
    }

    public LiveData<UserSignUpResponse> getMutableLiveData(String name, String email, String mobile, String password, int deviceType,
                                                           String deviceToken, String confirmPassword, int countryId, int agreeterms, String roleId) {
        final MutableLiveData<UserSignUpResponse> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<UserSignUpResponse> call = apiService.getSignup(name, email, mobile, password, deviceType, deviceToken,
                confirmPassword, countryId, agreeterms,roleId);
        call.enqueue(new Callback<UserSignUpResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserSignUpResponse> call, @NonNull Response<UserSignUpResponse> response) {
                Log.e("responsee", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    UserSignUpResponse signInResponse = new UserSignUpResponse();
                    signInResponse.setError(true);
                    signInResponse.setMessage(signInResponse.getMessage());
                    mutableLiveData.setValue(signInResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserSignUpResponse> call, @NonNull Throwable t) {
                UserSignUpResponse signInResponse = new UserSignUpResponse();
                signInResponse.setError(true);
                signInResponse.setMessage("");
                mutableLiveData.setValue(signInResponse);
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

    public LiveData<SignupResponse> signUpProfessional(int deviceType, String device_token, String companyName, String companyAddress,
                                                       String businessId, String countryId, String phone, String email, String password,
                                                       String confirm_password, String nameDirector, int agreeTerms, String roleId) {
        final MutableLiveData<SignupResponse> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<SignupResponse> call = apiService.signUpProfessional(deviceType, device_token,companyName,companyAddress,businessId,
                countryId,phone,email,password,confirm_password,nameDirector,agreeTerms,roleId);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignupResponse> call, @NonNull Response<SignupResponse> response) {
                Log.e("responsee", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    SignupResponse signInResponse = new SignupResponse();
                    signInResponse.setError(true);
                    signInResponse.setMessage(signInResponse.message);
                    mutableLiveData.setValue(signInResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignupResponse> call, @NonNull Throwable t) {
                SignupResponse signInResponse = new SignupResponse();
                signInResponse.setError(true);
                signInResponse.setMessage(signInResponse.message);
                mutableLiveData.setValue(signInResponse);
            }
        });
        return mutableLiveData;
    }
}
