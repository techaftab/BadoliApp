package com.webmobril.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.webmobril.badoli.model.CountryResponse;
import com.webmobril.badoli.model.QRResponse;
import com.webmobril.badoli.model.ResendOtpResponse;
import com.webmobril.badoli.model.SignupResponse;
import com.webmobril.badoli.model.VerifyOtpResponse;
import com.webmobril.badoli.repositories.AccountRepositories;

import java.io.File;

import okhttp3.MultipartBody;


public class SignUpViewModel extends AndroidViewModel {

    private AccountRepositories signupRepository;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signupRepository = new AccountRepositories();
    }

    public LiveData<SignupResponse> getSignUp(String name, String email, String phone, String password, int device_type,
                                              String deviceToken, String confirm_password, int country_id, int agreeterms, String roleId) {

        return signupRepository.getMutableLiveData(name, email, phone, password, device_type, deviceToken,
                confirm_password, country_id, agreeterms,roleId);
    }

    public LiveData<CountryResponse> getCountryList() {
        return signupRepository.getCountryList();
    }

    public LiveData<VerifyOtpResponse> getOTP(int userid, String otp, String access_token) {

        return signupRepository.getOtpLiveData(userid, otp, access_token);
    }

    public LiveData<QRResponse> sendQrcode(MultipartBody.Part file, int id) {
        return signupRepository.sendQrCode(file,id);
    }

    public LiveData<ResendOtpResponse> resendOtp(int userId, String access_token) {
        return signupRepository.resendOtp(userId, access_token);
    }
}
