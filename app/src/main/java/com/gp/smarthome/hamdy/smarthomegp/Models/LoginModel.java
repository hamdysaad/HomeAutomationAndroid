package com.gp.smarthome.hamdy.smarthomegp.Models;

import android.content.Context;


import com.gp.smarthome.hamdy.smarthomegp.Utilities.Constant;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.ResponseParserUtils;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import Vendor.Authentication.Authenticatable;
import Vendor.Authentication.Authentication;
import Vendor.Authentication.IAuthenticationHandler;
import Vendor.WebService.IResponseHandler;
import Vendor.WebService.Request;

/**
 * Created by hamdy on 16/05/17.
 */

public class LoginModel implements Authenticatable {


    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final int TIMEOUT = 10000;
    public String email ;
    public String password ;
    private ResponseParserUtils parser;


    @Override
    public void attempt(Context context, IAuthenticationHandler validationHandler, IResponseHandler responseHandler) {

        String url = Constant.LOGIN_URL;
        Request.post(context, url)
                .addParam(EMAIL_KEY , email)
                .addParam(PASSWORD_KEY , password)
                .setResponseHandler(responseHandler)
                .timeout(TIMEOUT)
                .send();
    }

    @Override
    public void createSession(Context context) {

        SharedPref.putBoolean(context , Authentication.PREF_AUTH , true);
    }

    @Override
    public void parseData(String data) {

        try {
            parser = new ResponseParserUtils(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean isValid() {
        return parser.result;
    }

    @Override
    public int getAuthErrorMessageCode(Context context) {
        return 0;
    }

    @Override
    public JSONObject getUserJsonData() {
        return parser.data;
    }


}
