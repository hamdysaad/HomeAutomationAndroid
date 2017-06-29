package Vendor.Validation;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hamdy on 4/23/2017.
 */



/*
* to use this validation rule you must insert
* strings in strings file
*     <string fName="validation_confirm_password">Confirm password not match</string>

*     <string fName="validation_confirm_password">كلمة المرور غير مطابقة</string>

* */

public class ConfirmPassword implements IValidationRule {


    String password;

    public ConfirmPassword(String password) {
        this.password = password;
    }

    @Override
    public String validate(String value, Context context) {

        String message = null;

        if(value != null){

            if(!value.equals(password))
                message = context.getString(context.getResources().getIdentifier("validation_confirm_password" , "string" , context.getPackageName()));

        }


        return message;
    }



    private static boolean validateEmail(String email) {

        // return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        Pattern pattern;
        Matcher matcher;

        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
