package com.app.badoli.professionalFragment;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentAddStaffBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.google.gson.Gson;

import static android.content.ContentValues.TAG;

public class AddNewFragment extends Fragment {

    private FragmentAddStaffBinding binding;
    private ProfessionalViewModel professionalViewModel;
    UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_staff,container,false);
        userData = PrefManager.getInstance(requireActivity()).getUserData();
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        View view = binding.getRoot();
        ((StaffListActivity)requireActivity()).updateTitle(getResources().getString(R.string.add_new_agent));
        init();
        return  view;
    }

    private void init() {
        binding.btnNext.setOnClickListener(this::next);
    }

    private void next(View view) {
        String name = binding.edittextName.getText().toString();
        if (TextUtils.isEmpty(name)){
            binding.edittextName.requestFocus();
            AppUtils.showSnackbar(getString(R.string.name_field_empty), binding.parentLayout);
        } else {
            createStaff(userData.getId(),name);
        }
    }

    private void createStaff(String id, String name) {
        ((StaffListActivity)requireActivity()).showLoading(getResources().getString(R.string.creating_staff));
        professionalViewModel.createStaff(id,name).observe(requireActivity(),
                createStaff -> {
                    Log.e(TAG,"CUSTOMER"+ new Gson().toJson(createStaff));
                    ((StaffListActivity)requireActivity()).dismissLoading();
                    if (createStaff!=null&&!createStaff.getError()) {
                        loadFragment(new StaffCodeFragment(),createStaff.getResult().getAgent_code(),
                                createStaff.getResult().getStaff_name());
                    } else {
                        if (createStaff!=null&&createStaff.getMessage() != null) {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    createStaff.getMessage());
                        } else {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

    private void loadFragment(Fragment fragment, String agent_code, String staff_name) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.AGENT_NAME,staff_name);
        bundle.putString(Constant.AGENT_CODE,agent_code);
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.frameLayout, fragment);
        fragment.setArguments(bundle);
        ft.addToBackStack(null);
        ft.commit();
    }
}
