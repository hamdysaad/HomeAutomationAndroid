package Vendor.Authentication;

import android.content.Context;

import org.json.JSONObject;

import Vendor.WebService.IResponseHandler;

/**
 * Created by Hamdy on 4/20/2017.
 */

public interface Authenticatable {

    void attempt(Context context, IAuthenticationHandler validationHandler, IResponseHandler responseHandler);
    void createSession(Context context);
    void parseData(String data);
    boolean isValid();


    int getAuthErrorMessageCode(Context context);


    JSONObject getUserJsonData();
}
