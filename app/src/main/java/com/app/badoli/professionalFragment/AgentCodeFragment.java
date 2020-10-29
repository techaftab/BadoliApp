package com.app.badoli.professionalFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.switchstaff.StaffListActivity;
import com.app.badoli.databinding.FragmentAgentCodeBinding;
import com.app.badoli.viewModels.ProfessionalViewModel;

public class AgentCodeFragment extends Fragment {

    private FragmentAgentCodeBinding binding;
    private ProfessionalViewModel professionalViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_agent_code,container,false);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        View view = binding.getRoot();
        ((StaffListActivity)requireActivity()).updateTitle(getResources().getString(R.string.add_new_agent));
        init();
        return  view;
    }

    private void init() {

    }
}
