package com.webmobril.badoli.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.zxing.Result;
import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.databinding.FragmentQrPayBinding;

import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.app.Activity.RESULT_OK;

public class FragmentQrPay extends Fragment implements View.OnClickListener,ZXingScannerView.ResultHandler {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final String TAG = FragmentQrPay.class.getSimpleName();
    private FragmentQrPayBinding fragmentBinding;
    private ZXingScannerView zXingScannerView;
    private boolean isPermitted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_pay,container,false);
        View view   = fragmentBinding.getRoot();
        checkRunTimePermission();
        listener();

        return  view;
    }
    private void listener() {
        ((HomePageActivity) Objects.requireNonNull(getContext())).updateToolbar();
        zXingScannerView=new ZXingScannerView(getActivity());
        //zXingScannerView.setFlash(true);
        fragmentBinding.imgFlash.setOnClickListener(this);

    }
    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
            Scanqr();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
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

        dialog.setTitle("Permission Denied")
                .setInverseBackgroundForced(true)
                //.setIcon(R.drawable.ic_info_black_24dp)
                .setMessage("Without those permission the app is unable to scan. App needs external storage and also need to get profile image from camera or external storage.Are you sure you want to deny this permission?")

                .setNegativeButton("I'M SURE", (dialoginterface, i) -> dialoginterface.dismiss())

                .setPositiveButton("RE-TRY", (dialoginterface, i) -> {
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
        zXingScannerView=new ZXingScannerView(Objects.requireNonNull(getActivity()).getApplicationContext());
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

        if (true) {
            // zXingScannerView.removeAllViews(); //<- here remove all the views, it will make an Activity having no View
            zXingScannerView.stopCamera();
            Log.d(TAG,"Qrcode data:"+result.toString());
            Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();

         //   String walletAddress= String.valueOf(result);
          /*  String string=walletAddress.replace("bitcoin:","");
            walletAddressVerify(string);*/
        }
        else {
            Toast.makeText(getActivity(), "Invalide QR_Code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.getFlash();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MY_PERMISSIONS_REQUEST_CAMERA&&resultCode==RESULT_OK){
            Scanqr();
        }
    }
}
