package Vendor.Validation;

import android.content.Context;


/**
 * Created by Hamdy on 4/23/2017.
 */


/*
* to use this validation rule you must insert
* strings in strings file
 *        <string fName="validation_mobile">يجب ان يكون رقم الموبايل صحيحاً</string>
 *        <string fName="validation_mobile">Mobile must be valid mobile number</string>
* */

public class Mobile implements IValidationRule {



    @Override
    public String validate(String value, Context context) {

        String message = null;

        if(value != null){

            if(!validatePhoneNumber(value))
               message =   context.getString(context.getResources().getIdentifier("validation_mobile" , "string" , context.getPackageName()));
        }

        return message;
    }



   /* public static boolean isValidMobile(String phone) {

        String pettern = "(^([0-9]){10}$)";

        return Pattern.matches(pettern, phone);

    }*/


    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{11}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }
}
