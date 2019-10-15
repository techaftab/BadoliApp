package com.webmobril.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.webmobril.badoli.model.CountryResponse;
import com.webmobril.badoli.model.SignupResponse;
import com.webmobril.badoli.model.VerifyOtpResponse;
import com.webmobril.badoli.repositories.AccountRepositories;



public class SignUpViewModel extends AndroidViewModel {

    private AccountRepositories signupRepository;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signupRepository = new AccountRepositories();
    }

    public LiveData<SignupResponse> getSignUp(String name, String email, String phone, String password, int device_type,
                                              String deviceToken, String confirm_password, int country_id, int agreeterms) {

        return signupRepository.getMutableLiveData(name, email, phone, password, device_type, deviceToken,
                confirm_password, country_id, agreeterms);
    }

    public LiveData<CountryResponse> getCountryList() {
        return signupRepository.getCountryList();
    }

    public LiveData<VerifyOtpResponse> getOTP(int userid, String otp, String access_token) {

        return signupRepository.getOtpLiveData(userid, otp, access_token);
    }
}
