package com.app.badoli.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.HelpFragmentBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.professionalFragment.ProfessionalHomeFragment;
import com.app.badoli.viewModels.ProfileViewModel;

import java.util.Objects;

public class HelpFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentTransaction ft;
    private Fragment currentFragment;

    public HelpFragment () {}

    HelpFragmentBinding fragmentBinding;

     UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            if (getActivity()!=null) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
            requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }catch (Exception e){
            e.printStackTrace();
        }
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.help_fragment,container,false);
        profileViewModel =new ViewModelProvider(this).get(ProfileViewModel.class);
        View view   = fragmentBinding.getRoot();
        userData = PrefManager.getInstance(requireActivity()).getUserData();

        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).hideHeader();
        }else {
            ((ProfessionalActivity) requireActivity()).hideHeader();
        }

        init();

        return  view;

    }

    private void init() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(fragmentBinding.toolbarChangePassword);
        Objects.requireNonNull(Objects.requireNonNull((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(null);
        //Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setHomeButtonEnabled(true);
        //Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayShowHomeEnabled(true);
        fragmentBinding.toolbarChangePassword.setTitle("");
        fragmentBinding.toolbarChangePassword.setNavigationOnClickListener(v -> {
            Log.d("tag", "onClick : navigating back to back activity ");
            //finish();
            if (getActivity()!=null) {
                if (userData.getUserType().equalsIgnoreCase("3")) {
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    currentFragment = new HomeFragment();
                    ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                    ft.replace(R.id.rootLayout, currentFragment);
                    ft.commit();
                }else {
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    currentFragment = new ProfessionalHomeFragment();
                    ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                    ft.replace(R.id.rootLayout, currentFragment);
                    ft.commit();
                }

              //  loadData();
            }
        });
        fragmentBinding.btnSubmit.setOnClickListener(this::contact);
    }

    private void contact(View view) {
        String subject=fragmentBinding.edittextSubject.getText().toString();
        String message=fragmentBinding.edittextMessage.getText().toString();
        if (isValid(subject,message)){
            contactUs(subject,message);
        }
    }

    private boolean isValid(String subject, String message) {

        if (TextUtils.isEmpty(subject)){
            fragmentBinding.edittextSubject.requestFocus();
            AppUtils.showSnackbar(getString(R.string.please_enter_subject), fragmentBinding.parentLayout);
            return false;
        }
        if (TextUtils.isEmpty(message)){
            fragmentBinding.edittextMessage.requestFocus();
            AppUtils.showSnackbar(getString(R.string.please_enter_message), fragmentBinding.parentLayout);
            return false;
        }

        return true;
    }

    private void contactUs(String subject, String message) {
        showLoading();
        profileViewModel.contactUs(userData.getId(),subject,message)
                .observe(this, searchList -> {
                    dismissLoading();
                    if (!searchList.getError()){
                        AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"",searchList.getMessage());
                     /*   SweetToast.success(ContactUsActivity.this,searchList.getMessage());
                        onBackPressed()xxx;*/
                     fragmentBinding.edittextMessage.setText("");
                     fragmentBinding.edittextSubject.setText("");
                    } else {
                        if (searchList.getMessage()!=null){
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",searchList.getMessage());
                        }else {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",getResources().getString(R.string.something_went_wrong));
                        }
                    }
                });
    }

    private void dismissLoading() {
        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).dismissLoading();
        }else {
            ((ProfessionalActivity) requireActivity()).dismissLoading();
        }
    }

    private void showLoading() {
        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).showLoading(getResources().getString(R.string.please_wait));
        }else {
            ((ProfessionalActivity) requireActivity()).showLoading(getResources().getString(R.string.please_wait));
        }
    }
}
