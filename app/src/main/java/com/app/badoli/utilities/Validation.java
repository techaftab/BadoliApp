package com.app.badoli.utilities;

import java.util.regex.Pattern;

public class Validation {


    /**************Email Validation method*****************/
    public static boolean isValidEmaillId(String email) {
        String PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,4}";
        String PATTERN1 = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{1,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|1[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|1[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|1[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        return Pattern.compile(PATTERN1).matcher(email).matches();
    }
    /*End*/
}
