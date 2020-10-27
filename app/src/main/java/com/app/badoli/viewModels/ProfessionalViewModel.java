package com.app.badoli.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.badoli.model.CreateStaff;
import com.app.badoli.model.StaffList;
import com.app.badoli.repositories.ProfessionalRepositories;

public class ProfessionalViewModel extends AndroidViewModel {

    private ProfessionalRepositories professionalRepositories;

    public ProfessionalViewModel(@NonNull Application application) {
        super(application);
        professionalRepositories = new ProfessionalRepositories();
    }

    public LiveData<StaffList> getStaffList(String id) {
        return professionalRepositories.getStaffList(id);
    }

    public LiveData<CreateStaff> createStaff(String id, String name) {
        return professionalRepositories.createStaff(id,name);
    }

    public LiveData<CreateStaff> deleteStaffCode(String staffId) {
        return professionalRepositories.deleteStaffCode(staffId);
    }

    public LiveData<CreateStaff> renewCode(String id, String staffId) {
        return professionalRepositories.renewCode(id,staffId);
    }
}
