package com.webmobril.badoli.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.webmobril.badoli.R;
import com.webmobril.badoli.activities.HomePageActivites.HomePageActivity;
import com.webmobril.badoli.config.Configuration;
import com.webmobril.badoli.config.Constant;
import com.webmobril.badoli.config.PrefManager;
import com.webmobril.badoli.databinding.ProfileFragmentBinding;
import com.webmobril.badoli.model.UserData;
import com.webmobril.badoli.viewModels.ProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener{

    private static final int PICK_IMAGE_CAMERA = 100;
    private static final int PICK_IMAGE_GALLERY = 200;
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private UserData userData;
    private ProfileViewModel profileViewModel;
    private ProfileFragmentBinding profileFragmentBinding;
    private FragmentTransaction ft;
    private Fragment currentFragment;
    private int RequestPermissionCode=1;
    private File destination;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment,container,false);
        View view = profileFragmentBinding.getRoot();
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        userData= PrefManager.getInstance(getActivity()).getUserData();
        ((HomePageActivity)Objects.requireNonNull(getActivity())).hideHeader();
        try {
            if (getActivity()!=null) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
            Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }catch (Exception e){
            e.printStackTrace();
        }

        loadData();

        return  view;
    }

    private void loadData() {
        profileFragmentBinding.userCalender.setOnClickListener(this);
        profileFragmentBinding.imgbackProfile.setOnClickListener(this);
        profileFragmentBinding.imgUploadProfile.setOnClickListener(this);
        profileFragmentBinding.userName.setText(userData.getName());
        profileFragmentBinding.userEmail.setText(userData.getEmail());
        profileFragmentBinding.userMobile.setText(userData.getMobile());
        profileFragmentBinding.txtNameProfile.setText(userData.getName());
        if (!TextUtils.isEmpty(userData.getUser_image())){

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo);

            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(Constant.IMAGE_URL+userData.getUser_image())
                    .apply(options)
                    .fitCenter()
                    .into(profileFragmentBinding.profileImage);

            Log.e(TAG,"USER--->"+userData.getUser_image());

        }else {
            profileFragmentBinding.profileImage.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public void onClick(View v) {
        //Fragment f = Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.rootLayout);
        if (v==profileFragmentBinding.imgbackProfile){
            if (getActivity()!=null) {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                currentFragment = new HomeFragment();
                ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
                loadData();
            }
        }
        if (v==profileFragmentBinding.imgUploadProfile){
            EnableRuntimePermissionToAccessCamera();
            selectImage();
        }
        if (v==profileFragmentBinding.userCalender){
            Configuration.showcalendar(profileFragmentBinding.userCalender,getActivity());
        }
    }
    private void EnableRuntimePermissionToAccessCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                Manifest.permission.CAMERA)) {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {

        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
                //  Toast.makeText(UploadImage.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Permission Cancelled,allow to upload reciept", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void selectImage() {
        try {
            PackageManager pm = Objects.requireNonNull(getActivity()).getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent();
                        pickPhoto.setType("image/*");
                        pickPhoto.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(pickPhoto, "Select Image"), PICK_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgPath;
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK && data!=null) {
            try {
                bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

               // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination  = new File(Objects.requireNonNull(getActivity()).getCacheDir(), "profile_image.jpg");
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profileFragmentBinding.profileImage.setImageBitmap(bitmap);
                imgPath = destination.getAbsolutePath();
                destination=new File(imgPath);
                sendImage(Integer.valueOf(userData.getId()),destination);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data!=null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                imgPath = getRealPathFromURI(selectedImage);
                profileFragmentBinding.profileImage.setImageBitmap(bitmap);

                destination = new File(imgPath);
                sendImage(Integer.valueOf(userData.getId()),destination);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImage(int id, File file) {
        Log.e(TAG,"USER ID--->"+id);
        File compressedImgFile=null;
        try {
            compressedImgFile = new Compressor(Objects.requireNonNull(getActivity())).compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        //RequestBody fileReq = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id));

        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", file.getName(), fileReqBody);

        profileFragmentBinding.progreebarProfile.setVisibility(View.VISIBLE);

//        long length = file.length();
//        length = length/1024;
//        System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB"+"\n"+compressedImgFile.length()/1024);
        MultipartBody.Part partCompress = MultipartBody.Part.createFormData("profile_image", compressedImgFile.getName(), fileReqBody);

        profileViewModel.saveProfileImage(partCompress,id).observe(this, profileImageResponse -> {
            if (!profileImageResponse.error) {
                Log.e("Profile Image--->", new Gson().toJson(profileImageResponse.message));
                profileFragmentBinding.progreebarProfile.setVisibility(View.GONE);
                UserData userData = new UserData(
                        String.valueOf(profileImageResponse.result.getId()),
                        String.valueOf(profileImageResponse.result.getAuthToken()),
                        String.valueOf(profileImageResponse.result.getCountryCode()),
                        String.valueOf(profileImageResponse.result.getCountryId()),
                        profileImageResponse.result.getDeviceToken(),
                        profileImageResponse.result.getEmail(),
                        profileImageResponse.result.getMobile(),
                        profileImageResponse.result.getName(),
                        profileImageResponse.result.getWalletBalance(),
                        profileImageResponse.result.getUser_image(),
                        profileImageResponse.result.getQrcode_image());
                PrefManager.getInstance(getActivity()).userLogin(userData);
            } else {
                profileFragmentBinding.progreebarProfile.setVisibility(View.GONE);
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        @SuppressLint("Recycle") Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = 0;
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
        }
      //  assert cursor != null;
        return cursor.getString(column_index);
    }
}
