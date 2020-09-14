package com.app.badoli.professionalFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.AddMoney;
import com.app.badoli.activities.HomePageActivites.AgentActivity;
import com.app.badoli.activities.ProfessionalActivity;
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
        try {
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){e.printStackTrace();}
        init();
        return  view;
    }

    private void init() {
        ((ProfessionalActivity)requireActivity()).homeData();

        binding.lnCountRefill.setOnClickListener(this::goCountRefill);
        binding.lnTransferAgent.setOnClickListener(this::goTransferAgent);
    }

    private void goTransferAgent(View view) {
        Intent intent = new Intent(getActivity(), AgentActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void goCountRefill(View view) {
        Intent wallet = new Intent(getActivity(), AddMoney.class);
        startActivity(wallet);
        requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }
}
