package com.app.badoli.professionalFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.StaffListActivity;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentProfHomeBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.ProfessionalViewModel;

public class ProfessionalHomeFragment extends Fragment {

    private FragmentProfHomeBinding binding;
    private UserData userData;
    private ProfessionalViewModel professionalViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_prof_home,container,false);
        userData = PrefManager.getInstance(requireActivity()).getUserData();
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        View view = binding.getRoot();
        init();
        return  view;
    }

    private void init() {

    }
}
