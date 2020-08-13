package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.badoli.model.BannerModel;
import com.app.badoli.model.BussinessList;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.QRResponse;
import com.app.badoli.model.ResendOtpResponse;
import com.app.badoli.model.UserSignUpResponse;
import com.app.badoli.model.VerifyOtpResponse;
import com.google.gson.Gson;
import com.app.badoli.model.LoginResponse;
import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;
import com.app.badoli.model.ChangePasswordModel;

import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private static final String TAG = LoginRepository.class.getSimpleName();
    private MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();

    public LoginRepository() {
    }

    public LiveData<LoginResponse> getMutableLiveData(String mobile, String password, int device_type,
                                                      String devicetoken, String roleId) {
        final MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<LoginResponse> call = apiService.getlogin(mobile, password, device_type, devicetoken,roleId);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Log.e("responsee", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                } else {
                    LoginResponse signInResponse = new LoginResponse();
                    signInResponse.setError(true);
                    signInResponse.setMessage(signInResponse.getMessage());
                    mutableLiveData.setValue(signInResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                LoginResponse signInResponse = new LoginResponse();
                signInResponse.setError(true);
                signInResponse.setMessage("");
                mutableLiveData.setValue(signInResponse);
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

    public LiveData<CountryResponse> getCountryList() {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        MutableLiveData<CountryResponse> mutableLiveData = new MutableLiveData<>();
        Call<CountryResponse> call = apiService.getCountryList();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponse> call, @NonNull Response<CountryResponse> response) {
                Log.e(TAG, new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    CountryResponse countryResponse = new CountryResponse();
                    countryResponse.error=true;
                    mutableLiveData.setValue(countryResponse);
                }
            }
            @Override
            public void onFailure(@NonNull Call<CountryResponse> call,@NonNull Throwable t) {
                CountryResponse countryResponse = new CountryResponse();
                countryResponse.error=true;
                mutableLiveData.setValue(countryResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<VerifyOtpResponse> verifyOtp(String userId, String otp) {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        MutableLiveData<VerifyOtpResponse> mutableLiveData = new MutableLiveData<>();
        Call<VerifyOtpResponse> call = apiService.verifyOtp(userId, otp);
        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtpResponse> call,@NonNull Response<VerifyOtpResponse> response) {
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    VerifyOtpResponse verifyOtpResponse=new VerifyOtpResponse();
                    verifyOtpResponse.error=true;
                    mutableLiveData.setValue(verifyOtpResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtpResponse> call, @NonNull Throwable t) {
                Log.e("error", Objects.requireNonNull(t.getMessage()));
                VerifyOtpResponse verifyOtpResponse=new VerifyOtpResponse();
                verifyOtpResponse.error=true;
                mutableLiveData.setValue(verifyOtpResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<QRResponse> sendQrCode(MultipartBody.Part file, RequestBody id) {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        MutableLiveData<QRResponse> mutableLiveData = new MutableLiveData<>();
        Call<QRResponse> call = apiService.sendQrCode(file, id);

        call.enqueue(new Callback<QRResponse>() {
            @Override
            public void onResponse(@NonNull Call<QRResponse> call,@NonNull Response<QRResponse> response) {
                Log.e("signup_response", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    QRResponse verifyOtpResponse=new QRResponse();
                    verifyOtpResponse.error=true;
                    mutableLiveData.setValue(verifyOtpResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<QRResponse> call, @NonNull Throwable t) {
                Log.e("error", Objects.requireNonNull(t.getMessage()));
                QRResponse verifyOtpResponse=new QRResponse();
                verifyOtpResponse.error=true;
                mutableLiveData.setValue(verifyOtpResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<ResendOtpResponse> resendOtp(String userId) {
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        MutableLiveData<ResendOtpResponse> mutableLiveData = new MutableLiveData<>();
        Call<ResendOtpResponse> call = apiService.resendMobileOtp(userId);

        call.enqueue(new Callback<ResendOtpResponse>() {
            @Override
            public void onResponse(@NonNull Call<ResendOtpResponse> call,@NonNull Response<ResendOtpResponse> response) {
                Log.e("signup_response", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    ResendOtpResponse verifyOtpResponse=new ResendOtpResponse();
                    verifyOtpResponse.error=true;
                    mutableLiveData.setValue(verifyOtpResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResendOtpResponse> call, @NonNull Throwable t) {
                Log.e("error", Objects.requireNonNull(t.getMessage()));
                ResendOtpResponse verifyOtpResponse=new ResendOtpResponse();
                verifyOtpResponse.error=true;
                mutableLiveData.setValue(verifyOtpResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<BussinessList> getBussinessList() {
        MutableLiveData<BussinessList> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<BussinessList> call = apiService.getBussinessList();
        call.enqueue(new Callback<BussinessList>() {
            @Override
            public void onResponse(@NonNull Call<BussinessList> call,@NonNull Response<BussinessList> response) {
                Log.e("profile_image", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    BussinessList verifyOtpResponse=new BussinessList();
                    verifyOtpResponse.error=true;
                    mutableLiveData.setValue(verifyOtpResponse);
                }
            }
            @Override
            public void onFailure(@NonNull Call<BussinessList> call, @NonNull Throwable t) {
                Log.e("profile_image error", Objects.requireNonNull(t.getMessage()));
                BussinessList verifyOtpResponse=new BussinessList();
                verifyOtpResponse.error=true;
                mutableLiveData.setValue(verifyOtpResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<BannerModel> getBanner() {
        MutableLiveData<BannerModel> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<BannerModel> call = apiService.getBanner();
        call.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(@NonNull Call<BannerModel> call,@NonNull Response<BannerModel> response) {
                Log.e("profile_image", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    BannerModel verifyOtpResponse=new BannerModel();
                    verifyOtpResponse.setError(true);
                    mutableLiveData.setValue(verifyOtpResponse);
                }
            }
            @Override
            public void onFailure(@NonNull Call<BannerModel> call, @NonNull Throwable t) {
                Log.e("profile_image error", Objects.requireNonNull(t.getMessage()));
                BannerModel verifyOtpResponse=new BannerModel();
                verifyOtpResponse.setError(true);
                mutableLiveData.setValue(verifyOtpResponse);
            }
        });
        return mutableLiveData;
    }
}
