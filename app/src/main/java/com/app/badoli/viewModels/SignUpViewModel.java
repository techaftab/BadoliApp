package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.config.Constant;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.QRResponse;
import com.app.badoli.model.ResendOtpResponse;
import com.app.badoli.model.SignupResponse;
import com.app.badoli.model.UserSignUpResponse;
import com.app.badoli.model.VerifyOtpResponse;
import com.app.badoli.repositories.AccountRepositories;
import com.app.badoli.model.BussinessList;

import okhttp3.MultipartBody;


public class SignUpViewModel extends AndroidViewModel {

    private AccountRepositories signupRepository;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signupRepository = new AccountRepositories();
    }

    public LiveData<UserSignUpResponse> getSignUp(String name, String email, String phone, String password, int device_type,
                                                  String deviceToken, String confirm_password, int country_id, int agreeterms, String roleId) {

        return signupRepository.getMutableLiveData(name, email, phone, password, device_type, deviceToken,
                confirm_password, country_id, agreeterms,roleId);
    }

    public LiveData<SignupResponse> signUpProfessional(int deviceType, String device_token, String companyName, String companyAddress,
                                                       String businessId, String countryId, String phone, String email, String password,
                                                       String confirm_password, String nameDirector, int agreeTerms, String roleId) {
        return signupRepository.signUpProfessional(deviceType, device_token,companyName,companyAddress,businessId,
                countryId,phone,email,password,confirm_password,nameDirector,agreeTerms,roleId);
    }
}
