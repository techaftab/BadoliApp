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
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentStaffCodeBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.bumptech.glide.Glide;

public class StaffCodeFragment extends Fragment {

    private FragmentStaffCodeBinding binding;
    private ProfessionalViewModel professionalViewModel;
    UserData userData;

    String name,agentCode;

      /*webmobril@gmail.com
      !@#$%^&*WebmobrilGmai*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_staff_code,container,false);
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        userData= PrefManager.getInstance(requireActivity()).getUserData();
        View view   = binding.getRoot();
        //((StaffListActivity)requireActivity()).updateTitle(getResources().getString(R.string.list_availble));
        init();
        return  view;
    }

    private void init() {
        name=getArguments().getString(Constant.AGENT_NAME);
        agentCode=getArguments().getString(Constant.AGENT_CODE);
        ((StaffListActivity)requireActivity()).updateTitle(name);
        binding.txtCode.setText(agentCode);
        if (userData.getQrcode_image()!=null){
            Glide.with(this).load(Constant.IMAGE_URL+userData.getQrcode_image())
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .thumbnail(0.06f)
                    .into(binding.imgQrCode);
        }
    }
}
