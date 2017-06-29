package Vendor.Validation;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


/**
 * Created by Hamdy on 4/23/2017.
 */


/*
* to use this validation rule you must insert
* strings in strings file
 *          <string fName="validation_required">هذا الحقل مطلوب</string>
 *           <string fName="validation_required">This field is required</string>
* */


public class Required implements IValidationRule {
    private  final String TAG = "Required" ;

    @Override
    public String validate(String value, Context context) {

        Log.e(TAG , "value : " + value);

        String message = null;

        if(value != null){
            if(TextUtils.isEmpty(value))
                message = context.getString(context.getResources().getIdentifier("validation_required" , "string" , context.getPackageName()));
        }

        return message;
    }
}
