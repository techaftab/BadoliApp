package com.app.badoli.professionalFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.auth.otp.VerifyOtpActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.ProfessionalProfileFragmentBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.model.UserProfile;
import com.app.badoli.viewModels.ProfileViewModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class ProfessionalProfileFragment extends Fragment {

    private static final String TAG = ProfessionalProfileFragment.class.getSimpleName();
    private static final int PICK_IMAGE_CAMERA = 741;
    private static final int PICK_IMAGE_GALLERY = 745;
    private ProfessionalProfileFragmentBinding binding;
    private UserData userData;
    private FragmentTransaction ft;
    private Fragment currentFragment;
    private int RequestPermissionCode=123;
    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.professional_profile_fragment,container,false);
        profileViewModel =new ViewModelProvider(this).get(ProfileViewModel.class);
        View view = binding.getRoot();
        try {
            if (getActivity()!=null) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
            requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }catch (Exception e){
            e.printStackTrace();
        }
        userData= PrefManager.getInstance(getActivity()).getUserData();
        if (userData.getUserType().equalsIgnoreCase("3")) {
            ((HomePageActivity) requireActivity()).hideHeader();
        }else {
            ((ProfessionalActivity) requireActivity()).hideHeader();
        }
        init();
        return  view;
    }

    private void init() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarProfile);
        Objects.requireNonNull(Objects.requireNonNull((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(null);

        binding.toolbarProfile.setTitle("");
        binding.toolbarProfile.setNavigationOnClickListener(v -> {
            Log.d("tag", "onClick : navigating back to back activity ");
            //finish();
            if (getActivity()!=null) {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                currentFragment = new ProfessionalHomeFragment();
                ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                ft.replace(R.id.rootLayout, currentFragment);
                ft.commit();
            }
        });

        binding.btnView.setOnClickListener(this::viewQr);
        binding.imgUploadProfile.setOnClickListener(this::checkPermission);

        if (AppUtils.hasNetworkConnection(requireActivity())){
            getProfile(userData.getId(),userData.getUserType());
        }else {
            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"internetError",getResources().getString(R.string.no_internet));
        }
    }

    private void checkPermission(View view) {
        if (checkAndRequestPermissions()) {
            selectImage();
        }else {
            Toast.makeText(getActivity(),getResources().getString(R.string.enable_perm),Toast.LENGTH_SHORT).show();
        }
    }

    private void viewQr(View view) {
        Fragment currentFragment = new ProfQrViewFragment();
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.frameLayout, currentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private boolean checkAndRequestPermissions() {
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), RequestPermissionCode);
            return false;
        }
        return true;
    }

    private void EnableRuntimePermissionToAccessCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CAMERA)) {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(getActivity(),getResources().getString(R.string.camera_perm_allow),
                    Toast.LENGTH_LONG).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,},RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {
        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
                //Toast.makeText(UploadImage.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
            } else {
                EnableRuntimePermissionToAccessCamera();
                Toast.makeText(getActivity(), getResources().getString(R.string.perm_cacelled), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectImage() {
        try {
            PackageManager pm = requireActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                String take_photo=getResources().getString(R.string.take_photo);
                String select_gallery=getResources().getString(R.string.select_gallery);
                String cancel=getResources().getString(R.string.cancel);

                final CharSequence[] options = {take_photo,select_gallery, cancel};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.select_option));
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals(getResources().getString(R.string.take_photo))) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
                    } else if (options[item].equals(getResources().getString(R.string.select_gallery))) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent();
                        pickPhoto.setType("image/*");
                        pickPhoto.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(pickPhoto, getResources().getString(R.string.select_image)), PICK_IMAGE_GALLERY);
                    } else if (options[item].equals(getResources().getString(R.string.cancel))) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else
                Toast.makeText(getActivity(), getResources().getString(R.string.camera_perm_error), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgPath;
        File destination;
        Bitmap bitmap;
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK && data!=null) {
            try {
                bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                }

                Log.e("Activity", "Pick from Camera::>>> ");

                // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(requireActivity().getCacheDir(), "profile_image.jpg");
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                binding.profileImage.setImageBitmap(bitmap);
                imgPath = destination.getAbsolutePath();
                destination =new File(imgPath);
                sendImage(Integer.parseInt(userData.getId()), destination);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data!=null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                imgPath = getRealPathFromURI(selectedImage);
                binding.profileImage.setImageBitmap(bitmap);

                destination = new File(imgPath);
                sendImage(Integer.parseInt(userData.getId()), destination);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImage(int id, File file) {
        Log.e(TAG,"USER ID--->"+id);
        File compressedImgFile=null;
        try {
            compressedImgFile = new Compressor(requireActivity()).compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("image/*"));
        ((ProfessionalActivity)requireActivity()).showLoading(getResources().getString(R.string.updating_image));

        MultipartBody.Part partCompress = null;
        if (compressedImgFile != null) {
            partCompress = MultipartBody.Part.createFormData("profile_image", compressedImgFile.getName(), fileReqBody);
        }
        profileViewModel.saveProfileImage(partCompress,id).observe(this, profileImageResponse -> {
            ((ProfessionalActivity)requireActivity()).dismissLoading();
            if (!profileImageResponse.error) {
                Log.e("Profile Image--->", new Gson().toJson(profileImageResponse.message));
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
                        profileImageResponse.result.getQrcode_image(),
                        "3");
                PrefManager.getInstance(getActivity()).userLogin(userData);
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        @SuppressLint("Recycle") Cursor cursor = requireActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = 0;
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
        }
        //  assert cursor != null;
        assert cursor != null;
        return cursor.getString(column_index);
    }

    private void getProfile(String id, String userType) {
        ((ProfessionalActivity)requireActivity()).showLoading(getResources().getString(R.string.plz_wait));
        profileViewModel.getProfile(id,userType)
                .observe(requireActivity(), loginResponse -> {
                    ((ProfessionalActivity)requireActivity()).dismissLoading();
                    if (loginResponse!=null&&loginResponse.getError()) {
                       /* UserData userData = new UserData(
                                id,
                                loginResponse.getResult().getai(),
                                countryCode,
                                countryId,
                                verifyOtpResponse.result.getDeviceToken(),
                                verifyOtpResponse.result.getEmail(),
                                verifyOtpResponse.result.getMobile(),
                                verifyOtpResponse.result.getName(),
                                verifyOtpResponse.result.getWalletBalance(),
                                verifyOtpResponse.result.getUser_image(),
                                verifyOtpResponse.result.getQrcode_image(),
                                roleId);
                        PrefManager.getInstance(requireActivity()).userLogin(userData);*/
                        setDetails(loginResponse.getResult());
                    } else {
                        if (loginResponse!=null&&!TextUtils.isEmpty(loginResponse.getMessage())) {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",loginResponse.getMessage());
                        }else {
                            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"error",getResources().getString(R.string.something_went_wrong));
                        }
                    }
                });
    }

    private void setDetails(UserProfile.Result result) {
        binding.edittextName.setText(result.getCompany_name());
        binding.edittextCode.setText(result.getAirtel_merchant_id());
        binding.edittextMobile.setText(result.getMobile());
        binding.edittextAddress.setText(result.getCompany_address());

        Glide.with(this).load(result.getUser_image())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .thumbnail(0.06f)
                .into(binding.profileImage);
    }

}
