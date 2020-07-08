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
import com.app.badoli.model.BussinessList;

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

    public LiveData<BussinessList> getBussinessList() {
        return signupRepository.getBussinessList();
    }
}
