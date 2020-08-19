package com.app.badoli.professionalFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.StaffListActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentStaffListBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.google.gson.Gson;

import static android.content.ContentValues.TAG;

public class StaffListFragment extends Fragment {

    private FragmentStaffListBinding binding;
    private UserData userData;
    private ProfessionalViewModel professionalViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_staff_list,container,false);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        View view   = binding.getRoot();
        ((StaffListActivity)requireActivity()).updateTitle(getResources().getString(R.string.list_availble));
        init();
        return  view;
    }

    private void init() {
        userData = PrefManager.getInstance(requireActivity()).getUserData();
        binding.btnNewAgent.setOnClickListener(this::addNewAgent);
        if (AppUtils.hasNetworkConnection(requireActivity())){
            getStaffList(userData.getId());
        }else {
            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"internetError",
                    getResources().getString(R.string.no_internet));
        }
    }
    private void addNewAgent(View view) {
        Fragment currentFragment = new AddNewFragment();
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
        ft.replace(R.id.frameLayout, currentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void getStaffList(String id) {
        ((StaffListActivity)requireActivity()).showLoading(getResources().getString(R.string.plz_wait));
        professionalViewModel.getStaffList(id).observe(requireActivity(),
                staffList -> {
                    Log.e(TAG,"CUSTOMER"+ new Gson().toJson(staffList));
                    ((StaffListActivity)requireActivity()).dismissLoading();
                    if (staffList!=null&&!staffList.getError()) {
                        binding.rlNoItem.setVisibility(View.GONE);
                        binding.recyclerview.setVisibility(View.VISIBLE);

                    } else {
                        binding.rlNoItem.setVisibility(View.VISIBLE);
                        binding.recyclerview.setVisibility(View.GONE);
                        if (staffList!=null&&staffList.getMessage() != null) {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    staffList.getMessage());
                        } else {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

}
