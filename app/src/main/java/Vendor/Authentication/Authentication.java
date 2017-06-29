package Vendor.Authentication;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import Vendor.WebService.IResponseHandler;

/**
 * Created by Hamdy on 4/20/2017.
 */

/*
* to use this validation rule you must insert
* strings in strings file
*
*           <string fName="no_internet_connection">No internet connectiion</string>

*           <string fName="no_internet_connection">من فضلك تأكد من الاتصال بالأنترنت</string>
*
*
*          <string fName="error_occured">Sorry, an error occured please try again</string>

*          <string fName="error_occured">لقد حدث خطأ. من فضلك حاول مرة اخرى</string>

* */




public class Authentication{

    public static final String PREF_AUTH = "auth";
    private  final String TAG = "Authentication";


    public void attemp(final Context context, final Authenticatable authenticatable, final IAuthenticationHandler authenticationHandler ){

        authenticatable.attempt(context, authenticationHandler,
                new IResponseHandler() {
                    @Override
                    public void onResponseSuccess(String response) {
                        Log.e(TAG , response);

                        authenticatable.parseData(response);

                        if(authenticatable.isValid()){
                            authenticatable.createSession(context);
                            authenticationHandler.onAuthSuccessfull(authenticatable.getUserJsonData());
                        }
                        else{
                            int messageCode = authenticatable.getAuthErrorMessageCode(context);
                            authenticationHandler.onAuthFailed(messageCode);
                        }
                    }

                    @Override
                    public void onResponseFailed(VolleyError error) {
                        Log.e(TAG , error.toString());

//                        int messageCode ;
//                        if(error instanceof TimeoutError || error instanceof NoConnectionError)
//
//                            messageCode = ErrorCode.NO_INTERNET_CONNECTION;
//
//                        else
//                        messageCode = ErrorCode.ERROR_OCCURED;
//
                        authenticationHandler.onAuthResponseFailed(error);
                    }
                });
    }

    public static boolean isLogedin(Context context){
        return true;
    }
}
