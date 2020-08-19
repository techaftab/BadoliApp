package com.app.badoli.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.model.CreateStaff;
import com.app.badoli.model.StaffList;
import com.app.badoli.retrofit.ApiInterface;
import com.app.badoli.retrofit.RetrofitConnection;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfessionalRepositories {

    public String TAG = ProfessionalActivity.class.getSimpleName();

    public LiveData<StaffList> getStaffList(String id) {
        MutableLiveData<StaffList> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<StaffList> call = apiService.getStaffList(id);
        call.enqueue(new Callback<StaffList>() {
            @Override
            public void onResponse(@NonNull Call<StaffList> call, @NonNull Response<StaffList> response) {
                Log.e(TAG, new Gson().toJson(response.message()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    StaffList statusMessageModel=new StaffList();
                    statusMessageModel.setError(true);
                    mutableLiveData.setValue(statusMessageModel);
                }
            }
            @Override
            public void onFailure(@NonNull Call<StaffList> call,@NonNull Throwable t) {
                Log.e("error", Objects.requireNonNull(t.getMessage()));
                Log.e(TAG, new Gson().toJson(t.getCause()));
                StaffList statusMessageModel=new StaffList();
                statusMessageModel.setError(true);
                mutableLiveData.setValue(statusMessageModel);
            }
        });
        return mutableLiveData;
    }

    public LiveData<CreateStaff> createStaff(String id, String name) {
        MutableLiveData<CreateStaff> mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = RetrofitConnection.getInstance().createService();
        Call<CreateStaff> call = apiService.createStaff(id,name);
        call.enqueue(new Callback<CreateStaff>() {
            @Override
            public void onResponse(@NonNull Call<CreateStaff> call, @NonNull Response<CreateStaff> response) {
                Log.e(TAG, new Gson().toJson(response.message()));
                if (response.isSuccessful()) {
                    mutableLiveData.setValue(response.body());
                }else {
                    CreateStaff statusMessageModel=new CreateStaff();
                    statusMessageModel.setError(true);
                    mutableLiveData.setValue(statusMessageModel);
                }
            }
            @Override
            public void onFailure(@NonNull Call<CreateStaff> call,@NonNull Throwable t) {
                Log.e("error", Objects.requireNonNull(t.getMessage()));
                Log.e(TAG, new Gson().toJson(t.getCause()));
                CreateStaff statusMessageModel=new CreateStaff();
                statusMessageModel.setError(true);
                mutableLiveData.setValue(statusMessageModel);
            }
        });
        return mutableLiveData;
    }
}
