package com.gp.smarthome.hamdy.smarthomegp.Controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gp.smarthome.hamdy.smarthomegp.Activities.LoginActivity;
import com.gp.smarthome.hamdy.smarthomegp.Activities.MainActivity;
import com.gp.smarthome.hamdy.smarthomegp.Admin;
import com.gp.smarthome.hamdy.smarthomegp.Models.LoginModel;
import com.gp.smarthome.hamdy.smarthomegp.R;
import com.gp.smarthome.hamdy.smarthomegp.SmartHomeApp;
import com.gp.smarthome.hamdy.smarthomegp.User;
import com.gp.smarthome.hamdy.smarthomegp.Utilities.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Vendor.Authentication.Authentication;
import Vendor.Authentication.IAuthenticationHandler;
import Vendor.Errors.Error;
import Vendor.Errors.TextInput;
import Vendor.Validation.Email;
import Vendor.Validation.IValidationRule;
import Vendor.Validation.Required;
import Vendor.Validation.Validation;

import static android.R.attr.id;

/**
 * Created by Hamdy on 4/18/2017.
 */

public class LoginController implements
        Vendor.Validation.IValidationHandler,
        IAuthenticationHandler {

    private  final String TAG = "LoginController";
    public final LoginActivity mLoginActivity;
    private  User user;
    private final LoginModel model;
    private Authentication auth;
    private Context context;

    public LoginController(LoginActivity loginActivity) {
        context = loginActivity.getApplicationContext();
        mLoginActivity = loginActivity;

        user = ((SmartHomeApp) mLoginActivity.getApplication()).user;

        model = new LoginModel();

    }


    public void actionListener() {

        mLoginActivity.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }


    private void doLogin() {

        String email =  mLoginActivity.emailET.getText().toString();
        String password = mLoginActivity.passwordET.getText().toString();


        mLoginActivity.progress(true);

        model.email = email;
        model.password = password;

        Validation validation = new Validation();

        //mobile
        validation.addValidationField(
                email ,
                new TextInput( mLoginActivity.emailTIL) ,
                new IValidationRule[]{
                        new Required(),
                        new Email()
                });

        //password
        validation.addValidationField(
                password ,
                new TextInput(mLoginActivity.passwordTIL ),
                new IValidationRule[]{
                        new Required()
                } );

        validation.validate(context , this);

    }

    @Override
    public void onValidationSuccessfull() {
        Log.e(TAG , "onValidationSuccessfull");
        auth =  new Authentication();
        auth.attemp(context , model, this);
    }

    @Override
    public void onValidationFailed(ArrayList<Error> errors) {
        Log.e(TAG , "onValidationFailed : " + errors.toString());
        mLoginActivity.displayValidationErrors(errors);

    }


    @Override
    public void onAuthSuccessfull(JSONObject data) {

        Log.e(TAG , "user : " + data.toString());
        //start home;

        try {
            user.parseUserJO(data);
            user.setUserData(context);
            user.getUserData(context);

            if(user.isAdmin()) {
                Admin admin = new Admin();
                admin.getUserData(context);
                user = admin;
            }


            MainActivity.start(mLoginActivity);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG , e.toString());
            Toast.makeText(mLoginActivity, mLoginActivity.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAuthFailed(int messageCode) {
        Log.e(TAG , "onAuthFailed : " + messageCode);

        String message = context.getString(R.string.invalid_username_or_password);


        mLoginActivity.displayLoginMessage(message);
    }

    @Override
    public void onAuthResponseFailed(VolleyError error) {

        Log.e(TAG , "onAuthResponseFailed : " + error.toString());

        String message = context.getString(context.getResources().getIdentifier("error_occured", "string", context.getPackageName()));


        mLoginActivity.displayLoginMessage(message);
    }
}
