package Vendor.Errors;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Hamdy on 4/24/2017.
 */

public class MyTextView implements IVewError {

    private TextView textView;

    public MyTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void setError(String message) {
        textView.setText(message);
        textView.setTextColor(Color.RED);
    }
}
