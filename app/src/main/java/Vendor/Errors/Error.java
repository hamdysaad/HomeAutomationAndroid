package Vendor.Errors;

/**
 * Created by Hamdy on 4/24/2017.
 */

public class Error {


    String message;
    IVewError vewError;

    public Error(String message, IVewError vewError) {
        this.message = message;
        this.vewError = vewError;
    }

    public void displayError(){
        vewError.setError(message);
    }
}
