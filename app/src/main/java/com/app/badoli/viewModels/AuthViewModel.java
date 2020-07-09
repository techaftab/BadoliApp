package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.BussinessList;
import com.app.badoli.model.ChangePasswordModel;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.LoginResponse;
import com.app.badoli.model.QRResponse;
import com.app.badoli.model.ResendOtpResponse;
import com.app.badoli.model.VerifyOtpResponse;
import com.app.badoli.repositories.LoginRepository;

import okhttp3.MultipartBody;


public class AuthViewModel extends AndroidViewModel {

    LoginRepository loginRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public LiveData<LoginResponse> getLogin(String mobile, String password, int device_type,
                                            String devicetoken) {

        return loginRepository.getMutableLiveData(mobile, password, device_type, devicetoken);
    }

    public LiveData<ChangePasswordModel> changePassword(String mobile, String confirmPassword, String roleId) {
        return loginRepository.changePassword(mobile, confirmPassword, roleId);
    }

    public LiveData<CountryResponse> getCountryList() {
        return loginRepository.getCountryList();
    }

    public LiveData<VerifyOtpResponse> verifyOtp(String userId, String otp, String access_token) {
        return loginRepository.verifyOtp(userId, otp, access_token);
    }

    public LiveData<QRResponse> sendQrcode(MultipartBody.Part file, String id) {
        return loginRepository.sendQrCode(file,id);
    }

    public LiveData<ResendOtpResponse> resendOtp(String userId, String access_token) {
        return loginRepository.resendOtp(userId, access_token);
    }

    public LiveData<BussinessList> getBussinessList() {
        return loginRepository.getBussinessList();
    }
}
