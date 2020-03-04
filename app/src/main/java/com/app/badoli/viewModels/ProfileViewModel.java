package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.ProfileImageResponse;
import com.app.badoli.model.ResetPassword;
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
}
