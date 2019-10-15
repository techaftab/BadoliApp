package com.webmobril.badoli.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.webmobril.badoli.R;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ProfileFragmentBinding;
import com.webmobril.badoli.model.UserData;

import java.util.Objects;

import static com.webmobril.badoli.activities.HomePageActivites.HomePageActivity.homePageBinding;


public class ProfileFragment extends Fragment implements View.OnClickListener{

    private UserData userData;
    private ProfileFragmentBinding profileFragmentBinding;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment,container,false);
        View view = profileFragmentBinding.getRoot();
        userData= PrefManager.getInstance(getActivity()).getUserData();

        try { if (getActivity()!=null) { getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); } }catch (Exception e){e.printStackTrace();}

        loadData();

        return  view;
    }

    private void loadData() {
        profileFragmentBinding.imgbackProfile.setOnClickListener(this);
        profileFragmentBinding.userName.setText(userData.getName());
        profileFragmentBinding.userEmail.setText(userData.getEmail());
        profileFragmentBinding.userMobile.setText(userData.getMobile());
        profileFragmentBinding.txtNameProfile.setText(userData.getName());
    }

    @Override
    public void onClick(View v) {
        //Fragment f = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.rootLayout);
        if (v==profileFragmentBinding.imgbackProfile){
            if (getActivity()!=null)
            ft = getActivity().getSupportFragmentManager().beginTransaction();
            currentFragment = new HomeFragment();
                ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
            ft.replace(R.id.rootLayout, currentFragment);
            ft.commit();
            homePageBinding.commonHeader.mainLayout.setBackgroundResource(R.mipmap.home_header_bgg);
            homePageBinding.commonHeader.hamburger.setVisibility(View.VISIBLE);
            homePageBinding.commonHeader.imgBackMain.setVisibility(View.GONE);
            homePageBinding.commonHeader.mainLayout.setVisibility(View.VISIBLE);
            homePageBinding.bottomNavigation.getMenu().getItem(0).setChecked(true);
            loadData();
        }
    }
}
