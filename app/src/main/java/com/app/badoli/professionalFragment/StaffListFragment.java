package com.app.badoli.professionalFragment;

import android.content.Intent;
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
import com.app.badoli.activities.StaffDetailActivity;
import com.app.badoli.activities.StaffListActivity;
import com.app.badoli.adapter.StaffListAdapter;
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

public class StaffListFragment extends Fragment implements StaffListAdapter.StaffListAdapterListner {

    private FragmentStaffListBinding binding;
    private UserData userData;
    private ProfessionalViewModel professionalViewModel;

    boolean status=false;
    String message="";

    private final List<StaffList.Result> staffList = new ArrayList<>();
    private StaffListAdapter staffListAdapter;

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


        staffListAdapter = new StaffListAdapter(getActivity(), staffList, this);
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

    private void addNewAgent(View view) {
    /*    if (!status){
            if (!TextUtils.isEmpty(message)) {
                AppUtils.openPopup(requireActivity(), R.style.Dialod_UpDown, "error",
                        message);
            }else {
                AppUtils.openPopup(requireActivity(), R.style.Dialod_UpDown, "error",
                        getResources().getString(R.string.something_wrong));
            }
        } else {*/
            Fragment currentFragment = new AddNewFragment();
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
            ft.replace(R.id.frameLayout, currentFragment);
            ft.addToBackStack(null);
            ft.commit();
      //  }
    }

    private void getStaffList(String id) {
        ((StaffListActivity)requireActivity()).showLoading(getResources().getString(R.string.plz_wait));
        professionalViewModel.getStaffList(id).observe(requireActivity(),
                responseList -> {
                    Log.e(TAG,"CUSTOMER"+ new Gson().toJson(responseList));
                    ((StaffListActivity)requireActivity()).dismissLoading();
                    if (responseList!=null&&!responseList.getError()) {
                        status = true;
                        binding.rlNoItem.setVisibility(View.GONE);
                        binding.recyclerview.setVisibility(View.VISIBLE);

                        staffList.clear();
                        staffList.addAll(responseList.getResult());
                        staffListAdapter.notifyDataSetChanged();

                    } else {
                        status = false;
                        binding.rlNoItem.setVisibility(View.VISIBLE);
                        binding.recyclerview.setVisibility(View.GONE);
                        if (responseList!=null&&responseList.getMessage() != null) {
                            message=responseList.getMessage();
                            SweetToast.error(requireActivity(),message);
                           /* AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    responseList.getMessage());*/
                        } else {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",
                                    getResources().getString(R.string.something_wrong));
                        }
                    }
                });
    }

    @Override
    public void onStaffClick(String agent_code, int agent_pin, int merchant_id, String staff_name, int id) {
        Intent intent = new Intent(requireActivity(), StaffDetailActivity.class);
        intent.putExtra(Constant.AGENT_CODE,agent_code);
        intent.putExtra(Constant.STAFF_ID,String.valueOf(id));
        intent.putExtra(Constant.AGENT_PIN,String.valueOf(agent_pin));
        intent.putExtra(Constant.MERCHANT_ID,String.valueOf(merchant_id));
        intent.putExtra(Constant.AGENT_NAME,String.valueOf(staff_name));
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }
}
