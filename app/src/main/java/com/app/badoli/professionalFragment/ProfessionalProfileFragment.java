package com.app.badoli.professionalFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ProfessionalProfileFragmentBinding;
import com.app.badoli.model.UserData;

import java.util.Objects;

public class ProfessionalProfileFragment extends Fragment {

    private ProfessionalProfileFragmentBinding binding;
    private UserData userData;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.professional_profile_fragment,container,false);
        View view = binding.getRoot();
        try {
            if (getActivity()!=null) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
            requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }catch (Exception e){
            e.printStackTrace();
        }
        userData= PrefManager.getInstance(getActivity()).getUserData();
        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).hideHeader();
        }else {
            ((ProfessionalActivity) requireActivity()).hideHeader();
        }
        init();
        return  view;
    }

    private void init() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarProfile);
        Objects.requireNonNull(Objects.requireNonNull((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(null);

        binding.toolbarProfile.setTitle("");
        binding.toolbarProfile.setNavigationOnClickListener(v -> {
            Log.d("tag", "onClick : navigating back to back activity ");
            //finish();
            if (getActivity()!=null) {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                currentFragment = new ProfessionalHomeFragment();
                ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
            }
        });

        binding.btnView.setOnClickListener(this::viewQr);
    }

    private void viewQr(View view) {
        Fragment currentFragment = new ProfQrViewFragment();
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.frameLayout, currentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
