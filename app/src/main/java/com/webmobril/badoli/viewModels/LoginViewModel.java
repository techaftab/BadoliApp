package com.webmobril.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.webmobril.badoli.model.LoginResponse;
import com.webmobril.badoli.repositories.LoginRepository;


public class LoginViewModel extends AndroidViewModel {

    LoginRepository loginRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public LiveData<LoginResponse> getLogin(String mobile, String password, int device_type,
                                            String devicetoken, String accesstoken) {

        return loginRepository.getMutableLiveData(mobile, password, device_type, devicetoken, accesstoken);
    }

}
