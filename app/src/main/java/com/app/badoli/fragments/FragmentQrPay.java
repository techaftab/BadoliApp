package com.app.badoli.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.Result;
import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.SplashActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentQrPayBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.viewModels.TranferViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import xyz.hasnat.sweettoast.SweetToast;

import static android.app.Activity.RESULT_OK;

public class FragmentQrPay extends Fragment implements View.OnClickListener,ZXingScannerView.ResultHandler {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final String TAG = FragmentQrPay.class.getSimpleName();
    private FragmentQrPayBinding fragmentBinding;
    private ZXingScannerView zXingScannerView;
    private boolean isPermitted;

    private TranferViewModel tranferViewModel;

    private UserData userData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_pay,container,false);
        tranferViewModel =new ViewModelProvider(this).get(TranferViewModel.class);
        userData= PrefManager.getInstance(getActivity()).getUserData();
        View view   = fragmentBinding.getRoot();
        checkRunTimePermission();
        listener();

        return  view;
    }
    private void listener() {
        ((HomePageActivity) requireContext()).updateToolbar();
        zXingScannerView=new ZXingScannerView(getActivity());
        //zXingScannerView.setFlash(true);
        fragmentBinding.imgFlash.setOnClickListener(this);

    }
    private void checkRunTimePermission() {

        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            //if already permition granted
            //PUT YOUR ACTION (Like Open cemara etc..)
            Scanqr();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       // boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            alertView();
                        }
                    }
                }
            }

            if (isPermitted) {
                //  if (isPermissionFromGallery)
                Scanqr();
            }
        }
    }
    private void alertView() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(getResources().getString(R.string.perm_denied))
                .setInverseBackgroundForced(true)
                //.setIcon(R.drawable.ic_info_black_24dp)
                .setMessage(getResources().getString(R.string.without_perm_not_allowed))

                .setNegativeButton(getResources().getString(R.string.m_sure), (dialoginterface, i) -> dialoginterface.dismiss())

                .setPositiveButton(getResources().getString(R.string.retry), (dialoginterface, i) -> {
                    dialoginterface.dismiss();
                    checkRunTimePermission();
                }).show();
    }


    @Override
    public void onClick(View v) {
        if (v==fragmentBinding.imgFlash){
            zXingScannerView.setFlash(!zXingScannerView.getFlash());
            if (zXingScannerView.getFlash()) {
                fragmentBinding.imgFlash.setImageResource(R.drawable.flash_on);
                fragmentBinding.txtFlash.setText(getResources().getString(R.string.on));
                fragmentBinding.txtFlash.setTextColor(getResources().getColor(R.color.yellow));
            } else {
                fragmentBinding.imgFlash.setImageResource(R.drawable.flash_off);
                fragmentBinding.txtFlash.setText(getResources().getString(R.string.off));
                fragmentBinding.txtFlash.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private void Scanqr() {
        zXingScannerView=new ZXingScannerView(requireActivity().getApplicationContext());
        fragmentBinding.frameZxingCamera.addView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
        //  this.finish();
        //zXingScannerView.getFlash();
    }
    @Override
    public void handleResult(Result result) {
        zXingScannerView.stopCamera();
        Log.d(TAG,"Qrcode data:"+result.toString());
        //Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
        if (isJson(result.toString())) {
            try {
                if (validateResponse(result.toString())) {
                    getResponse(result.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            SweetToast.error(getActivity(),getResources().getString(R.string.invalid_code));
            Scanqr();
        }
        /*try {

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
       /* Intent intent = new Intent(getActivity(), PaymentActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.left_in,R.anim.right_out);*/
    }

    private static boolean isJson(String Json) {
        try {
            new JSONObject(Json);
        } catch (JSONException ex) {
            try {
                new JSONArray(Json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    private boolean validateResponse(String string) throws JSONException {
        JSONObject jsonObject=new JSONObject(string);
        if (jsonObject.length()>3||jsonObject.length()<3){
            SweetToast.error(getActivity(),getResources().getString(R.string.invalid_code));
            return false;
        }
        if (!jsonObject.has("userId")||!jsonObject.has("mobile")||!jsonObject.has("amount")){
            SweetToast.error(getActivity(),getResources().getString(R.string.invalid_code));
            return false;
        }
        return true;
    }

    private void getResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            String userId = jsonObject.getString("userId");
            String mobile = jsonObject.getString("mobile");
            String amount = jsonObject.getString("amount");
            Log.e(TAG,"USERID--->"+userId);
            Log.e(TAG,"MOBILE--->"+mobile);
            Log.e(TAG,"AMOUNT--->"+amount);
            if (setValidation(amount,mobile)) {
                AppUtils.hideKeyboardFrom(requireActivity());
                transferMobile(amount,mobile,userData.getId());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showLoading(){
        fragmentBinding.progressbarScanqr.setVisibility(View.VISIBLE);
        if (getActivity()!=null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    private void dismissLoading(){
        fragmentBinding.progressbarScanqr.setVisibility(View.INVISIBLE);
        if (getActivity()!=null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void transferMobile(String amount, String recieverId, String senderid) {
        showLoading();
        tranferViewModel.transferMobile(amount, recieverId, senderid).observe(this, walletTransfer -> {
            dismissLoading();
            Scanqr();
            if (!walletTransfer.error) {
               // Scanqr();
                SweetToast.success(getActivity(),walletTransfer.getMessage());
                AppUtils.openPopupPaymentStatus(getActivity(),true,walletTransfer.getMessage(),amount,recieverId);
                onResume();
            } else {
                SweetToast.error(getActivity(),walletTransfer.getMessage());
                AppUtils.openPopupPaymentStatus(getActivity(),false,walletTransfer.getMessage(),amount,recieverId);
                onResume();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.getFlash();
        Scanqr();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MY_PERMISSIONS_REQUEST_CAMERA&&resultCode==RESULT_OK){
            Scanqr();
        }
    }

    private boolean setValidation(String amount, String phone) {
        float balance= Float.parseFloat(SplashActivity.getPreferences(Constant.BALANCE, ""));
        if (TextUtils.isEmpty(amount)) {
            SweetToast.error(getActivity(),getResources().getString(R.string.enter_amount));
            return false;
        }
        if (Float.parseFloat(amount)<=0){
            SweetToast.error(getActivity(),getResources().getString(R.string.invalid_amount));
            return false;
        }
        if (Float.parseFloat(amount)>49900){
            SweetToast.error(getActivity(),getResources().getString(R.string.amount_should_499));
            return false;
        }
        if (Float.parseFloat(amount)>balance){
            SweetToast.error(getActivity(),getResources().getString(R.string.wallet_balance_low));
            return false;
        }
        return true;
    }

}
