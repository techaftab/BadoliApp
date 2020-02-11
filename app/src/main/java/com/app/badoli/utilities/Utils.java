package com.app.badoli.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

public class Utils {

    public   Context context;
    public Utils (Context context){
       this.context = context;
    }

    public  void setHeaderTitle(String title,Activity activity){
        activity.setTitle(title);
       // activity.getSu
    }

    public void hideKeyboard(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*public  boolean validateEmail(String email){
        if(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() && getPassword().length() > 8){
            return  true;
        }else {
            return false;
        }
    }*/
}
