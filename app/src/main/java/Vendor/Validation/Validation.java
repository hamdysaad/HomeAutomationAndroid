package Vendor.Validation;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import Vendor.Errors.Error;
import Vendor.Errors.IVewError;


/**
 * Created by Hamdy on 4/23/2017.
 */

public class Validation {


    private  final String TAG = "Validation";
    private ArrayList<ValidationField> validationFields = new ArrayList<>();

    private ArrayList<Error> errors;

    public Validation addValidationField(String value , IVewError vewError , IValidationRule[] rules){

        validationFields.add( new ValidationField(value , vewError , rules));

        return this;
    }


    public void validate(Context context , IValidationHandler iValidationHandler){

        int errorNo = 0;
        errors = new ArrayList<>();

        for(int i = 0 ; i < validationFields.size() ; i++ ){

            ValidationField validationField =  validationFields.get(i);

            IValidationRule[] rules = validationField.validationRules;

            ArrayList<String> fieldError = new ArrayList<>();


            String error = "";
            for(int j = 0 ; j < rules.length ; j++){
                 String mess = rules[j].validate(validationField.value , context);
                Log.e(TAG , "error : " + mess);
                if(mess != null) {
                    errorNo++;
                    error += mess + "\n";
                }
            }

            if(!error.equals(""))
                errors.add(new Error(error , validationField.view));
            else
                errors.add(new Error(null , validationField.view));
        }


        if (errorNo > 0)
            iValidationHandler.onValidationFailed(errors);
        else {
            resetErrors();
            iValidationHandler.onValidationSuccessfull();
        }

    }

    private void resetErrors() {

        for(int i =0 ; i < errors.size() ; i++){
            errors.get(i).displayError();
        }
    }

}
