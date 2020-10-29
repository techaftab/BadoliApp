package com.app.badoli.professionalFragment;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.AddMoney;
import com.app.badoli.activities.HomePageActivites.AgentActivity;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.activities.ProfessionalActivity;
import com.app.badoli.auth.otp.VerifyOtpActivity;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.PrefManager;
import com.app.badoli.databinding.FragmentProfHomeBinding;
import com.app.badoli.model.UserData;
import com.app.badoli.model.UserProfile;
import com.app.badoli.utilities.LoginPre;
import com.app.badoli.viewModels.AuthViewModel;
import com.app.badoli.viewModels.ProfessionalViewModel;
import com.app.badoli.viewModels.ProfileViewModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfessionalHomeFragment extends Fragment {

    private static final String TAG = ProfessionalHomeFragment.class.getSimpleName();
    private FragmentProfHomeBinding binding;
    private UserData userData;
    private ProfessionalViewModel professionalViewModel;
    private ProfileViewModel profileViewModel;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_prof_home,container,false);
        profileViewModel =new ViewModelProvider(this).get(ProfileViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userData = PrefManager.getInstance(requireActivity()).getUserData();
        professionalViewModel = new ViewModelProvider(this).get(ProfessionalViewModel.class);
        View view = binding.getRoot();
        try {
            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }catch (Exception e){e.printStackTrace();}
        init();
        return  view;
    }

    private void init() {
        ((ProfessionalActivity)getActivity()).homeData();

        binding.lnCountRefill.setOnClickListener(this::goCountRefill);
        binding.lnTransferAgent.setOnClickListener(this::goTransferAgent);
        binding.btnTransactions.setOnClickListener(this::goTransactions);

        if (AppUtils.hasNetworkConnection(requireActivity())){
            getProfile(userData.getId(),userData.getUserType());
        }else {
            AppUtils.openPopup(requireActivity(),R.style.Dialod_UpDown,"internetError",getResources().getString(R.string.no_internet));
        }
    }

    private void goTransactions(View view) {
        ((ProfessionalActivity)requireActivity()).setBottomBar(2);
    }

    private void goTransferAgent(View view) {
        Intent intent = new Intent(getActivity(), AgentActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void goCountRefill(View view) {
        Intent wallet = new Intent(getActivity(), AddMoney.class);
        startActivity(wallet);
        requireActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    private void getProfile(String id, String userType) {
        ((ProfessionalActivity)requireActivity()).showLoading(getResources().getString(R.string.plz_wait));
        profileViewModel.getProfile(id,userType)
                .observe(requireActivity(), loginResponse -> {
                    try {
                        ((ProfessionalActivity) requireActivity()).dismissLoading();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (loginResponse!=null&&!loginResponse.getError()) {
                        UserData userData = new UserData(
                                String.valueOf(loginResponse.getResult().getId()),
                                String.valueOf(loginResponse.getResult().getDevice_token()),
                                String.valueOf(loginResponse.getResult().getCountry_code()),
                                String.valueOf(loginResponse.getResult().getCountry_code()),
                                loginResponse.getResult().getDevice_token(),
                                loginResponse.getResult().getEmail(),
                                loginResponse.getResult().getMobile(),
                                loginResponse.getResult().getName(),
                                loginResponse.getResult().getWallet_balance(),
                                loginResponse.getResult().getUser_image(),
                                loginResponse.getResult().getQrcode_image(),
                                "4");
                        PrefManager.getInstance(requireActivity()).userLogin(userData);
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
        if (TextUtils.isEmpty(result.getQrcode_image())&&!TextUtils.isEmpty(result.getAirtel_merchant_id())){
            ((ProfessionalActivity)requireActivity()).showLoading(getResources().getString(R.string.plz_wait));
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    generateQrCode(result.getAirtel_merchant_id(), userData.getId(), this);
                }
            };
            mThread.start();
        }
    }

    private void generateQrCode(String mobile, String id, Thread thread) {

        int width = 500;
        int height = 500;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            bitMatrix = new MultiFormatWriter().encode(
                    new String(mobile.getBytes()),
                    BarcodeFormat.QR_CODE, width, height, hintMap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int bitMatrixWidth = 0;
        if (bitMatrix != null) {
            bitMatrixWidth = bitMatrix.getWidth();
        }
        int bitMatrixHeight = 0;
        if (bitMatrix != null) {
            bitMatrixHeight = bitMatrix.getHeight();
        }
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.bla_trans) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File file = new File(requireActivity().getCacheDir(), "temporary_file.jpg");
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        requireActivity().runOnUiThread(() -> sendImage(file, id));
        stopThread(thread);
    }

    private synchronized void stopThread(Thread theThread) {
        if (theThread != null) {
            // theThread = null;
            theThread.interrupt();
        }
    }

    private void sendImage(File file, String id) {
        Log.e(TAG, "USER ID--->" + id);
        RequestBody fileReqBody = RequestBody.create(file, Objects.requireNonNull(MediaType.parse("image/*")));
        MultipartBody.Part part = MultipartBody.Part.createFormData("qrcode_image", file.getName(), fileReqBody);
        RequestBody userId = RequestBody.create(id, MediaType.parse("multipart/form-data"));
        authViewModel.sendQrcode(part, userId).observe(this, verifyOtpResponse -> {
            ((ProfessionalActivity)requireActivity()).dismissLoading();
            if (verifyOtpResponse != null && !verifyOtpResponse.error) {
                Log.e(TAG,"IMAGE SAVED FOR-->"+id);
            } else {
                if (verifyOtpResponse != null && verifyOtpResponse.getMessage() != null) {
                    AppUtils.openPopup(requireActivity(), R.style.Dialod_UpDown, "error",verifyOtpResponse.getMessage());
                } else {
                    AppUtils.openPopup(requireActivity(), R.style.Dialod_UpDown, "error",getResources().getString(R.string.something_wrong));
                }
            }
        });
    }

}
