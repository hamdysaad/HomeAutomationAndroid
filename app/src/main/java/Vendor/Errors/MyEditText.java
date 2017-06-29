package Vendor.Errors;

import android.widget.EditText;


/**
 * Created by Hamdy on 4/24/2017.
 */

public class MyEditText implements IVewError {

    private EditText editText;

    public MyEditText(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void setError(String message) {
        editText.setError(message);
    }
}
