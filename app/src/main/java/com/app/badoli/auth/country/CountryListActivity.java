package com.app.badoli.auth.country;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityCountryListBinding;
import com.app.badoli.model.CountryResponse;
import com.app.badoli.viewModels.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

public class CountryListActivity extends AppCompatActivity implements CountryListAdapter.CountryListAdapterListener, View.OnClickListener {

    ActivityCountryListBinding binding;
    private AuthViewModel authViewModel;
    private List<CountryResponse.CountryResult> countryList=new ArrayList<>();
    private CountryListAdapter countryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country_list);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        init();
    }
    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(CountryListActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void init() {
        binding.imgCloseHidden.setOnClickListener(this);
        countryListAdapter = new CountryListAdapter(CountryListActivity.this, countryList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CountryListActivity.this);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        binding.recyclerview.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();
        if (AppUtils.hasNetworkConnection(CountryListActivity.this)){
            getCountryList();
        }else {
            AppUtils.openPopup(CountryListActivity.this, R.style.Dialod_UpDown, "internetError",
                    getResources().getString(R.string.no_internet));
        }
        binding.editTextSearchLayout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countryListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getCountryList() {
        showLoading(getResources().getString(R.string.please_wait));
        authViewModel.getCountryList()
                .observe(this,
                        countryResponse -> {
                            dismissLoading();
                            if (!countryResponse.error) {
                                countryList.clear();
                                countryList.addAll(countryResponse.result);
                                countryListAdapter.notifyDataSetChanged();
                            } else {
                                if (countryResponse.message!=null){
                                    AppUtils.openPopup(CountryListActivity.this,R.style.Dialod_UpDown,"error",countryResponse.message);
                                }else {
                                    AppUtils.openPopup(CountryListActivity.this,R.style.Dialod_UpDown,"error",getResources().getString(R.string.something_wrong));
                                }
                            }
                        });
    }

    @Override
    public void onCountryClick(int phonecode, String name, int id) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constant.PHONE_CODE,String.valueOf(phonecode));
        returnIntent.putExtra(Constant.COUNTRY_NAME,name);
        returnIntent.putExtra(Constant.COUNTRY_ID,id);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v==binding.imgCloseHidden){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}