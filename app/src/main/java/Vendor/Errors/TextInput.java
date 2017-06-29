package Vendor.Errors;

import android.support.design.widget.TextInputLayout;

/**
 * Created by Hamdy on 4/24/2017.
 */

public class TextInput   implements IVewError {


    private TextInputLayout textInputLayout;

    public TextInput(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void setError(String message) {
        textInputLayout.setError(message);
    }
}
