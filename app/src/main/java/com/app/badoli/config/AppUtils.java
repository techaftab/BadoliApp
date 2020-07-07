package com.app.badoli.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.app.badoli.utilities.LoginPre;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aftab on 1/10/2018.
 */

public class AppUtils {

    public static boolean hasNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    public static void showcalendar(final TextView edtDob, Context context)
    {
        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n")
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    if ((monthOfYear+1)<10){
                        String mont="0"+ (monthOfYear + 1);
                        edtDob.setText(dayOfMonth + "/" + mont + "/" + year);
                    }else {
                        edtDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public static String encodePath(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        //Base64.de
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static void hideKeyboardFrom(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm!=null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void showCustomToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    public static void openPopupPaymentStatus(Context context, boolean status, String message, String amount, String phone) {

        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_status);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);

        TextView txtTitle=dialg.findViewById(R.id.txt_title_popup);
        TextView txtMessage=dialg.findViewById(R.id.txt_message);
        ImageView imgStatus=dialg.findViewById(R.id.img_status_recharge);
        Button btnOkay=dialg.findViewById(R.id.btn_okay);
        if (status){
            imgStatus.setImageResource(R.drawable.success_pay);
            txtMessage.setText("The amount of "+amount+" successfully transferred to "+phone);
        } else {
            imgStatus.setImageResource(R.drawable.failed_transaction);
            txtMessage.setText("The amount of "+amount+" does not transferred");
        }
        txtTitle.setText(message);

        btnOkay.setOnClickListener(v -> dialg.dismiss());

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }
    public static void openPopup(Context context, int animationSource, String error, String message) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.notice_info);
        Objects.requireNonNull(dialg.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.imgStatus);
        TextView txtMessage=dialg.findViewById(R.id.txtMessage);
        Button btnOk=dialg.findViewById(R.id.btnOkay);
        txtMessage.setText(message);
        if (error.equalsIgnoreCase("error")){
            imageView.setImageResource(R.drawable.ic_error);
        }else if (error.equalsIgnoreCase("internetError")){
            imageView.setImageResource(R.drawable.nointernet);
        }else if (error.equalsIgnoreCase("back")){
            imageView.setImageResource(R.drawable.ic_success);
            try {
                if (((FragmentActivity) context).getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
                }
            }catch (Exception e){e.printStackTrace();}
        }else if (error.equalsIgnoreCase("underdevelop")){
            imageView.setImageResource(R.drawable.maintenance);
        }else {
            imageView.setImageResource(R.drawable.success);
        }

        btnOk.setOnClickListener(v ->{
            if (error.equalsIgnoreCase("express")){
                dialg.dismiss();
                try {
                    if (((FragmentActivity) context).getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
                    }
                }catch (Exception e){e.printStackTrace();}
            }else {
                dialg.dismiss();
            }
        });

        Objects.requireNonNull(dialg.getWindow()).getAttributes().windowAnimations = animationSource;
        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }


    public static void openDialogUnderMaintenace(Context context){
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_status);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_status_recharge);
      //  TextView txtStatus=dialg.findViewById(R.id.txt_status);
        TextView txtTitle=dialg.findViewById(R.id.txt_title_popup);
      //  Button btnReciept=dialg.findViewById(R.id.btn_recipt);
        Button btnOk= dialg.findViewById(R.id.btn_okay);
        txtTitle.setVisibility(View.GONE);
      //  txtStatus.setText("Service Under Maintenance!!!\nComing Soon...");
        imageView.setImageResource(R.drawable.maintenance);
       // btnReciept.setVisibility(View.GONE);

        btnOk.setOnClickListener(v1 -> dialg.dismiss());

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static void showSnackbar(String message, View v) {
        Snackbar mSnackBar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        view.setLayoutParams(params);
        //view.setBackgroundColor(Color.RED);
        view.setBackgroundColor(Color.parseColor("#006E2C"));
        TextView mainTextView = (view).findViewById(com.google.android.material.R.id.snackbar_text);
        mainTextView.setTextColor(Color.WHITE);
        mainTextView.setPadding(0, 20, 0, 20);
        mSnackBar.show();
    }


}
