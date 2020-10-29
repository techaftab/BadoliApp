package com.app.badoli.staff;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.badoli.R;
import com.app.badoli.databinding.FragmentStaffHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StaffHomeFragment extends Fragment {

    private FragmentStaffHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_staff_home,container,false);
        View view = binding.getRoot();
        try {
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){e.printStackTrace();}
        init();
        return  view;
    }

    private void init() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        String formattedDate = df.format(c);
        SpannableString content = new SpannableString(getResources().getString(R.string.today)+", "+formattedDate);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        binding.txtDate.setText(content);
    }
}
