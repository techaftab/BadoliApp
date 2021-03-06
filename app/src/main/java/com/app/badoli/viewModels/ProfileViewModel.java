package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.CustomerUserProfile;
import com.app.badoli.model.ProfileImageResponse;
import com.app.badoli.model.ResetPassword;
import com.app.badoli.model.UserProfile;
import com.app.badoli.repositories.AccountRepositories;

import okhttp3.MultipartBody;

public class ProfileViewModel extends AndroidViewModel {

    private AccountRepositories accountRepositories;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        accountRepositories = new AccountRepositories();
    }

    public LiveData<ProfileImageResponse> saveProfileImage(MultipartBody.Part part, int id) {
        return accountRepositories.saveProfileImage(part,id);
    }

    public LiveData<ResetPassword> resetPassword(String userId,String oldPwd, String newPassword, String confirmPwd) {
        return accountRepositories.resetPassword(userId,oldPwd,newPassword,confirmPwd);
    }

    public LiveData<UserProfile> getProfile(String id, String userType) {
        return accountRepositories.getProfile(id,userType);
    }

    public LiveData<CustomerUserProfile> getUserProfile(String id, String userType) {
        return accountRepositories.getUserProfile(id,userType);
    }

    public LiveData<ResetPassword> contactUs(String id, String subject, String message) {
        return accountRepositories.contactUs(id,subject,message);
    }

    public LiveData<ResetPassword> updateProfile(String id, String userType, String name, String email, String dob, String gender) {
        return accountRepositories.updateProfile(id,userType,name,email,dob,gender);
    }

    public LiveData<ResetPassword> updateMerchantProfile(String id, String userType, String name, String address) {
        return accountRepositories.updateMerchantProfile(id,userType,name,address);
    }
}
