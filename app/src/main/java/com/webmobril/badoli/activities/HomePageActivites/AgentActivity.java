package com.webmobril.badoli.activities.HomePageActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.webmobril.badoli.R;
import com.webmobril.badoli.databinding.ActivityAgentBinding;
import com.webmobril.badoli.fragments.FragmentAgentBank;
import com.webmobril.badoli.fragments.FragmentAgentPhone;

public class AgentActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    ActivityAgentBinding activityAgentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAgentBinding = DataBindingUtil.setContentView(AgentActivity.this, R.layout.activity_agent);
        loadData();
        // ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
    }

    private void loadData() {
        activityAgentBinding.radiogroupAgent.setOnCheckedChangeListener(this);
        activityAgentBinding.imgBackAgent.setOnClickListener(this);
        activityAgentBinding.rbPhone.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (activityAgentBinding.rbPhone.isChecked()){
            activityAgentBinding.rbPhone.setBackground(getResources().getDrawable(R.drawable.purple_round_left_layout));
            activityAgentBinding.rbBank.setBackground(null);
            activityAgentBinding.rbBank.setTextColor(getResources().getColor(R.color.text_orange));
            activityAgentBinding.rbPhone.setTextColor(getResources().getColor(R.color.white));
            loadFragment(new FragmentAgentPhone(),R.anim.left_in,R.anim.right_out);
        }
        if (activityAgentBinding.rbBank.isChecked()){
            activityAgentBinding.rbBank.setBackground(getResources().getDrawable(R.drawable.purple_round_right_layout));
            activityAgentBinding.rbPhone.setBackground(null);
            activityAgentBinding.rbPhone.setTextColor(getResources().getColor(R.color.text_orange));
            activityAgentBinding.rbBank.setTextColor(getResources().getColor(R.color.white));
            loadFragment(new FragmentAgentBank(),R.anim.right_in,R.anim.left_out);
        }
    }

    private void loadFragment(Fragment fragment, int left_in, int right_out) {
        if (fragment != null) {
          //  checkView(fragment);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(left_in,right_out)
                    .replace(R.id.framelayout_agent, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v==activityAgentBinding.imgBackAgent){
            finish();
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }
}
