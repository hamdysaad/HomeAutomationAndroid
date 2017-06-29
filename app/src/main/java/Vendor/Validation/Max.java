package Vendor.Validation;

import android.content.Context;


/**
 * Created by Hamdy on 4/23/2017.
 */


/*
* to use this validation rule you must insert
* strings in strings file
 *     <string fName="validation_max">The number of characters must be less than or equal to</string>
 *     <string fName="validation_max">عدد الحروف يجب ان تكون أقل من او تساوي</string>
* */

public class Max implements IValidationRule {



    private int max;

    public Max(int max) {
        this.max = max;
    }

    @Override
    public String validate(String value , Context context) {

        String message = null;
        if(value != null){

            if(value.length() > max)
                message = context.getString(context.getResources().getIdentifier("validation_max" , "string" , context.getPackageName())) + " " + max;
        }

        return message;
    }
}
