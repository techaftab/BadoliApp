package com.app.badoli.switchstaff;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.badoli.R;
import com.app.badoli.adapter.SwitchStaffListAdapter;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentStaffListBinding;
import com.app.badoli.model.StaffList;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import xyz.hasnat.sweettoast.SweetToast;

import static android.content.ContentValues.TAG;

public class SwitchListFragment extends Fragment implements SwitchStaffListAdapter.SwitchStaffListAdapterListner {

    private FragmentStaffListBinding binding;
    private ProfessionalViewModel professionalViewModel;
    private UserData userData;

    private final List<StaffList.Result> staffList = new ArrayList<>();
    private SwitchStaffListAdapter staffListAdapter;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_staff_list,container,false);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        View view   = binding.getRoot();
        ((StaffSwitchActivity)requireActivity()).updateTitle(getResources().getString(R.string.list_availble));
        init();
        return  view;
    }

    private void init() {
        userData = PrefManager.getInstance(requireActivity()).getUserData();
        binding.btnNewAgent.setVisibility(View.GONE);


        staffListAdapter = new SwitchStaffListAdapter(getActivity(), staffList, this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        binding.recyclerview.setLayoutManager(linearLayoutManager1);
        binding.recyclerview.setAdapter(staffListAdapter);
        staffListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppUtils.hasNetworkConnection(requireActivity())){
            getStaffList(userData.getId());
        }else {
            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"internetError",
                    getResources().getString(R.string.no_internet));
        }
    }

    private void getStaffList(String id) {
        ((StaffSwitchActivity)requireActivity()).showLoading(getResources().getString(R.string.plz_wait));
        professionalViewModel.getStaffList(id).observe(requireActivity(),
                responseList -> {
                    Log.e(TAG,"CUSTOMER"+ new Gson().toJson(responseList));
                    ((StaffSwitchActivity)requireActivity()).dismissLoading();
                    if (responseList!=null&&!responseList.getError()) {
                        binding.rlNoItem.setVisibility(View.GONE);
                        binding.recyclerview.setVisibility(View.VISIBLE);

                        staffList.clear();
                        staffList.addAll(responseList.getResult());
                        staffListAdapter.notifyDataSetChanged();

                    } else {
                        binding.rlNoItem.setVisibility(View.VISIBLE);
                        binding.recyclerview.setVisibility(View.GONE);
                        if (responseList!=null&&responseList.getMessage() != null) {
                            String message=responseList.getMessage();
                            SweetToast.error(requireActivity(),message);
                        } else {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

    @Override
    public void onStaffClick(String agent_code, int agent_pin, int merchant_id, String staff_name, int id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.STAFF_NAME,staff_name);
        ft = requireActivity().getSupportFragmentManager().beginTransaction();
        currentFragment = new SwitchLoginFragment();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        currentFragment.setArguments(bundle);
        ft.replace(R.id.frameLayout, currentFragment);
        ft.commit();
    }
}
