package Vendor.Validation;


import Vendor.Errors.IVewError;

/**
 * Created by Hamdy on 4/23/2017.
 */

public class ValidationField {


    public String value;
    public IVewError view;
    public IValidationRule[] validationRules;

    public ValidationField(String value, IVewError view, IValidationRule[] validationRules) {
        this.value = value;
        this.view = view;
        this.validationRules = validationRules;
    }
}
