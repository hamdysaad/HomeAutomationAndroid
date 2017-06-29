package Vendor.Validation;

import java.util.ArrayList;

import Vendor.Errors.Error;


/**
 * Created by Hamdy on 4/23/2017.
 */

public interface IValidationHandler {


    void onValidationSuccessfull();
    void onValidationFailed(ArrayList<Error> errors);
}
