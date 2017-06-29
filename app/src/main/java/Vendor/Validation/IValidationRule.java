package Vendor.Validation;

import android.content.Context;

/**
 * Created by Hamdy on 4/23/2017.
 */

public interface IValidationRule {

    String validate(String value, Context context);
}
