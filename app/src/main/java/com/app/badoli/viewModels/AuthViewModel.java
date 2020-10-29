package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.BannerModel;
import com.app.badoli.model.BussinessList;
import com.app.badoli.model.ChangePasswordModel;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.model.LoginResponse;
import com.app.badoli.model.QRResponse;
import com.app.badoli.model.ResendOtpResponse;
import com.app.badoli.model.StaffLogin;
import com.app.badoli.model.VerifyOtpResponse;
import com.app.badoli.repositories.LoginRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AuthViewModel extends AndroidViewModel {

    LoginRepository loginRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public LiveData<LoginResponse> getLogin(String mobile, String password, int device_type,
                                            String devicetoken, String roleId) {

        return loginRepository.getMutableLiveData(mobile, password, device_type, devicetoken,roleId);
    }

    public LiveData<StaffLogin> loginStaff(String staffCode, String staffPin) {
        return loginRepository.loginStaff(staffCode,staffPin);
    }

    public LiveData<ChangePasswordModel> changePassword(String mobile, String confirmPassword, String roleId) {
        return loginRepository.changePassword(mobile, confirmPassword, roleId);
    }

    public LiveData<CountryResponse> getCountryList() {
        return loginRepository.getCountryList();
    }

    public LiveData<VerifyOtpResponse> verifyOtp(String userId, String otp) {
        return loginRepository.verifyOtp(userId, otp);
    }

    public LiveData<QRResponse> sendQrcode(MultipartBody.Part file, RequestBody id) {
        return loginRepository.sendQrCode(file,id);
    }

    public LiveData<ResendOtpResponse> resendOtp(String userId) {
        return loginRepository.resendOtp(userId);
    }

    public LiveData<BussinessList> getBussinessList() {
        return loginRepository.getBussinessList();
    }

    public LiveData<BannerModel> getBanner() {
        return loginRepository.getBanner();
    }


}
