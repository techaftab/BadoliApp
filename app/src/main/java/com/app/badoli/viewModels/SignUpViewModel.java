package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.QRResponse;
import com.app.badoli.model.ResendOtpResponse;
import com.app.badoli.model.SignupResponse;
import com.app.badoli.model.VerifyOtpResponse;
import com.app.badoli.repositories.AccountRepositories;
import com.webmobril.badoli.model.BussinessList;

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

    public LiveData<BussinessList> getBussinessList() {
        return signupRepository.getBussinessList();
    }
}
